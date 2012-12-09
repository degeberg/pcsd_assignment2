package assignmentImplementation;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import keyValueBaseExceptions.BeginGreaterThanEndException;
import keyValueBaseExceptions.KeyAlreadyPresentException;
import keyValueBaseExceptions.KeyNotFoundException;
import keyValueBaseExceptions.ServiceAlreadyInitializedException;
import keyValueBaseExceptions.ServiceInitializingException;
import keyValueBaseExceptions.ServiceNotInitializedException;
import keyValueBaseInterfaces.Pair;
import keyValueBaseInterfaces.Predicate;

@WebService
@SOAPBinding(style = Style.RPC)
public class KeyValueBaseService {
	private KeyValueBaseImpl lort;
	private IndexImpl idx;

	@WebMethod
	public void dummy(LengthPredicate p) { }
	
    public KeyValueBaseService() throws IndexOutOfBoundsException, IOException {
    	idx = new IndexImpl();
    	lort = new KeyValueBaseImpl(idx);
    }

    @WebMethod
    public void init(String serverFilename)
            throws ServiceAlreadyInitializedException,
            ServiceInitializingException, FileNotFoundException {
    	lort.init(serverFilename);
    }

    @WebMethod
    public ValueListImpl read(KeyImpl k) throws KeyNotFoundException,
            IOException, ServiceNotInitializedException {
    	return lort.read(k);
    }

    
    @WebMethod
    public void insert(KeyImpl k, ValueListImpl v)
            throws KeyAlreadyPresentException, IOException,
            ServiceNotInitializedException {
    	lort.insert(k, v);
    }

    
    @WebMethod
    public void update(KeyImpl k, ValueListImpl newV)
            throws KeyNotFoundException, IOException,
            ServiceNotInitializedException {
    	lort.update(k, newV);
    }

    
    @WebMethod
    public void delete(KeyImpl k) throws KeyNotFoundException,
            ServiceNotInitializedException {
    	lort.delete(k);
    }

    
    @WebMethod
    public ValueListImpl[] scan(KeyImpl begin, KeyImpl end,
            Predicate<ValueListImpl> p) throws IOException,
            BeginGreaterThanEndException, ServiceNotInitializedException {
    	return lort.scan(begin, end, p).toArray(new ValueListImpl[]{});
    }

    
    @WebMethod
    public ValueListImpl[] atomicScan(KeyImpl begin, KeyImpl end,
            Predicate<ValueListImpl> p) throws IOException,
            BeginGreaterThanEndException, ServiceNotInitializedException {
    	return lort.atomicScan(begin, end, p).toArray(new ValueListImpl[] { });
    }

    
    @WebMethod
    public void bulkPut(BulkList bl)
            throws IOException, ServiceNotInitializedException
    {
        ArrayList<Pair<KeyImpl, ValueListImpl>> al = new ArrayList<Pair<KeyImpl, ValueListImpl>>();
        List<KeyImpl> ks = bl.getKeys();
        List<ValueListImpl> vls = bl.getValues();
        int n = Math.min(ks.size(), vls.size());
        for (int i = 0; i < n; ++i)
            al.add(new Pair<KeyImpl, ValueListImpl>(ks.get(i), vls.get(i)));
        
    	lort.bulkPut(al);
    }
}
