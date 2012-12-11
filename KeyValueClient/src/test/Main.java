package test;

import assignmentimplementation.*;

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
        
		ValueListImpl vl = buildList(new int[]{1, 2, 3});
		KeyImpl key = new KeyImpl();
		key.setKey(42);
		kv.insert(key, vl);
		
		printKey(42);
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

	static private ValueListImpl buildList(int[] e) {
		ValueListImpl vl = new ValueListImpl();
		for (Integer a : e) {
			ValueImpl v = new ValueImpl();
			v.setValue(a);
			vl.getElements().add(v);
		}
		return vl;
	}

}
