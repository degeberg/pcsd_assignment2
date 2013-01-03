package test;

import assignmentimplementation.KeyImpl;
import assignmentimplementation.KeyValueBaseService;
import assignmentimplementation.KeyValueBaseServiceService;
import assignmentimplementation.ServiceAlreadyInitializedException_Exception;
import assignmentimplementation.ValueImpl;
import assignmentimplementation.ValueListImpl;

public class Main {
	
	static private KeyValueBaseService kv;

	public static void main(String[] args) throws Exception {
		KeyValueBaseServiceService s = new KeyValueBaseServiceService();
        kv = s.getKeyValueBaseServicePort();
        
        try {
            kv.init("/tmp/data.txt");
            System.out.println("Initialized.");
        } catch (ServiceAlreadyInitializedException_Exception e) {
            System.out.println("Already initialized.");
        }
        
		/*ValueListImpl vl = buildList(new int[]{1, 2, 3});
		KeyImpl key = new KeyImpl();
		key.setKey(42);
		kv.insert(key, vl);*/
		
		//printKey(42);
		//printKey(24);
		
	
        Client2 threads[] = new Client2[500];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Client2(kv);
        }
        long startTime = System.nanoTime();
        for (int i = 0; i < threads.length; ++i) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; ++i) {
            threads[i].join();
            //System.out.println("Joined " + i);
        }
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println("Elapsed time to process all threads: " + duration + " nanoseconds.");
        long avgDelay = 0;
        int totSize = 0;
        for (int i = 0; i < threads.length; ++i) {
            avgDelay += threads[i].getDuration();
            totSize += threads[i].getSize();
        }
        avgDelay /= threads.length;
        System.out.println("Transferred a total of " + totSize + " values with average request time of " + avgDelay + " ns.");
	}
	
	static public void printKey(int k) throws Exception {
		System.out.println("Key: " + k);
		KeyImpl key = new KeyImpl();
		key.setKey(k);
		
		ValueListImpl vl = kv.read(key);
		for (ValueImpl v : vl.getElements()) {
			System.out.println(v.getValue());
		}
		System.out.println();
	}

	static public ValueListImpl buildList(int[] e) {
		ValueListImpl vl = new ValueListImpl();
		for (Integer a : e) {
			ValueImpl v = new ValueImpl();
			v.setValue(a);
			vl.getElements().add(v);
		}
		return vl;
	}

}
