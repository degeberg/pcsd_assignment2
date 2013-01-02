package assignmentImplementation;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import keyValueBaseExceptions.BeginGreaterThanEndException;
import keyValueBaseExceptions.KeyAlreadyPresentException;
import keyValueBaseExceptions.KeyNotFoundException;
import keyValueBaseExceptions.ServiceAlreadyInitializedException;
import keyValueBaseExceptions.ServiceInitializingException;
import keyValueBaseExceptions.ServiceNotInitializedException;
import keyValueBaseInterfaces.KeyValueBase;
import keyValueBaseInterfaces.KeyValueBaseLog;
import keyValueBaseInterfaces.LogRecord;
import keyValueBaseInterfaces.Pair;
import keyValueBaseInterfaces.Predicate;

public class KeyValueBaseImpl implements KeyValueBase<KeyImpl, ValueListImpl>, KeyValueBaseLog<KeyImpl, ValueListImpl> {
    private static final String LOG_PATH = "/tmp/pcsd_log";
    
    private boolean initialized;
    private IndexImpl index;
    private HashMap<KeyImpl, ReentrantReadWriteLock> lockTable;
    private Lock globalLock;
    private int lockCount;
    private MyLogger logger;
    private Thread checkpointer;

    public KeyValueBaseImpl(IndexImpl index) throws Exception {
        initialized = false;
        this.index = index;
        lockTable = new HashMap<>();
        globalLock = new ReentrantLock();
        lockCount = 0;
        
        File logFile = new File(LOG_PATH);
        boolean restore = logFile.exists();
        
        RandomAccessFile logRaf = new RandomAccessFile(logFile, "rws");
        
        logger = new MyLogger(logRaf);
        logger.start();
        
        if (restore) {
            restore(logRaf);
        }
        
        logger.enable();
        
        checkpointer = new MyCheckpointer(this, index, logger);
        checkpointer.start();
    }

    @Override
    public void init(String serverFilename)
            throws ServiceAlreadyInitializedException,
            ServiceInitializingException {
        if (initialized) {
            throw new ServiceAlreadyInitializedException();
        }

        Scanner s = null;
        BufferedReader b = null;

        try {
            b = new BufferedReader(new FileReader(serverFilename));
            String str;
            Integer prevKey = null;
            ValueListImpl vl = new ValueListImpl();

            while ((str = b.readLine()) != null) {
                s = new Scanner(str);
                Integer key = s.nextInt();
                if (prevKey != null && !prevKey.equals(key)) {
                    KeyImpl k = new KeyImpl();
                    k.setKey(prevKey);
                    index.insert(k, vl);
                    vl = new ValueListImpl();
                }
                while (s.hasNextInt()) {
                    Integer x = s.nextInt();
                    ValueImpl v = new ValueImpl();
                    v.setValue(x);
                    vl.add(v);
                }
                s.close();
                prevKey = key;
            }
            if (prevKey != null) {
                KeyImpl k = new KeyImpl();
                k.setKey(prevKey);
                index.insert(k, vl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (s != null) {
                s.close();
            }
            if (b != null) {
                try {
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            index.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        initialized = true;
    }

    @Override
    public ValueListImpl read(KeyImpl k) throws KeyNotFoundException,
            IOException, ServiceNotInitializedException {
        if (!initialized) {
            throw new ServiceNotInitializedException();
        }

        lockRead(k);
        try {
            return index.get(k);
        } finally {
            unlock(k);
        }
    }

    @Override
    public void insert(KeyImpl k, ValueListImpl v)
            throws KeyAlreadyPresentException, IOException,
            ServiceNotInitializedException {
        if (!initialized) {
            throw new ServiceNotInitializedException();
        }

        lockWrite(k);
        try {
            LogRecord r = new LogRecord(index.getClass(), "insert", new Object[]{k,v});
            index.insert(k, v);
            logger.logRequest(r).get();
        } catch (InterruptedException | ExecutionException e) {
            // TODO What else to do?
            throw new RuntimeException(e);
        } finally {
            unlock(k);
        }
    }

    @Override
    public void update(KeyImpl k, ValueListImpl newV)
            throws KeyNotFoundException, IOException,
            ServiceNotInitializedException {
        if (!initialized) {
            throw new ServiceNotInitializedException();
        }

        lockWrite(k);
        try {
            LogRecord r = new LogRecord(index.getClass(), "update", new Object[]{k,newV});
            index.update(k, newV);
            logger.logRequest(r).get();
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            unlock(k);
        }
    }

    @Override
    public void delete(KeyImpl k) throws KeyNotFoundException,
            ServiceNotInitializedException {
        if (!initialized) {
            throw new ServiceNotInitializedException();
        }

        lockWrite(k);
        try {
            LogRecord r = new LogRecord(index.getClass(), "delete", new Object[]{k});
            index.remove(k);
            logger.logRequest(r).get();
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            unlock(k);
        }
    }

    @Override
    public List<ValueListImpl> scan(KeyImpl begin, KeyImpl end,
            Predicate<ValueListImpl> p) throws IOException,
            BeginGreaterThanEndException, ServiceNotInitializedException {
        if (!initialized) {
            throw new ServiceNotInitializedException();
        }

        ArrayList<ValueListImpl> list = new ArrayList<ValueListImpl>();
        for (ValueListImpl v : index.scan(begin, end)) {
            if (p.evaluate(v)) {
                System.out.println("predicate matched");
                list.add(v);
            }
            else
                System.out.println("predicate failed");
        }
        return list;
    }

    @Override
    synchronized public List<ValueListImpl> atomicScan(KeyImpl begin, KeyImpl end,
            Predicate<ValueListImpl> p) throws IOException,
            BeginGreaterThanEndException, ServiceNotInitializedException {
        if (!initialized) {
            throw new ServiceNotInitializedException();
        }

        List<KeyImpl> ks = KeyImpl.getInterval(begin, end);
        
        while (!lockReadMany(ks)) {
            try {
                wait();
            } catch (InterruptedException e) {
                // Doesn't matter...
            }
        }
        
        try {
            return scan(begin, end, p);
        } finally {
            unlock(ks);
        }
    }

    @Override
    public void bulkPut(List<Pair<KeyImpl, ValueListImpl>> mappings)
            throws IOException, ServiceNotInitializedException {
        if (!initialized) {
            throw new ServiceNotInitializedException();
        }

        ArrayList<KeyImpl> ks = new ArrayList<>();
        for (Pair<KeyImpl, ValueListImpl> p : mappings) {
            ks.add(p.getKey());
        }

        while (!lockWriteMany(ks)) {
            try {
                wait();
            } catch (InterruptedException e) {
                // Doesn't matter...
            }
        }

        try {
            LogRecord r = new LogRecord(index.getClass(), "bulkPut", new Object[]{mappings});
            index.bulkPut(mappings);
            logger.logRequest(r).get();
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            unlock(ks);
        }
    }
    
	@Override
	public void quiesce() {
	    globalLock.lock();
	    while (lockCount > 0) {
	        try {
                wait();
            } catch (InterruptedException e) {
                // Doesn't matter...
            }
	    }
	}

	@Override
	public void resume() {
		globalLock.unlock();
	}
	
	private void restore(RandomAccessFile logFile) throws Exception {
	    index.restore();
	    while (true) {
	        int size;
	        try {
    	        size = logFile.readInt();
	        } catch (EOFException e) {
	            break;
	        }
	        
	        byte[] b = new byte[size];
	        while (size > 0) {
	            size -= logFile.read(b);
	        }
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));
            try {
                LogRecord r = (LogRecord) in.readObject();
                r.invoke(index);
            } finally {
                in.close();
            }
	    }
	    
	    initialized = true;
	}

    synchronized private void lockRead(KeyImpl k) {
        globalLock.lock();
        ReentrantReadWriteLock l = getLock(k);
        l.readLock().lock();
        lockCount++;
        globalLock.unlock();
    }
    
    private boolean lockReadMany(List<KeyImpl> ks) {
        globalLock.lock();
        ArrayList<KeyImpl> locked = new ArrayList<>();
        for (KeyImpl k : ks) {
            if (!getLock(k).readLock().tryLock()) {
                unlock(locked);
                return false;
            } else {
                locked.add(k);
            }
        }
        globalLock.unlock();
        return true;
    }
    
    synchronized private void lockWrite(KeyImpl k) {
        globalLock.lock();
        ReentrantReadWriteLock l = getLock(k);
        l.writeLock().lock();
        lockCount++;
        globalLock.unlock();
    }
    
    private boolean lockWriteMany(List<KeyImpl> ks) {
        globalLock.lock();
        ArrayList<KeyImpl> locked = new ArrayList<>();
        for (KeyImpl k : ks) {
            if (!getLock(k).writeLock().tryLock()) {
                unlock(locked);
                return false;
            } else {
                locked.add(k);
            }
        }
        globalLock.unlock();
        return true;
    }
    
    synchronized private void unlock(KeyImpl k) {
        ReentrantReadWriteLock l = getLock(k);
        if (l.isWriteLocked()) {
            l.writeLock().unlock();
        } else {
            l.readLock().unlock();
        }
        lockCount--;
        notifyAll();
    }
    
    private void unlock(List<KeyImpl> ks) {
        for (KeyImpl k : ks) {
            unlock(k);
        }
    }

    synchronized private ReentrantReadWriteLock getLock(KeyImpl k) {
        ReentrantReadWriteLock l;
        l = lockTable.get(k);
        if (l == null) {
            l = new ReentrantReadWriteLock();
            lockTable.put(k, l);
        }
        return l;
    }

}
