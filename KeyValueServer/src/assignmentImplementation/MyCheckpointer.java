package assignmentImplementation;

import keyValueBaseInterfaces.Checkpointer;

public class MyCheckpointer extends Thread implements Checkpointer {
    
    private static final int CHECKPOINT_INTERVAL = 2000;

    private KeyValueBaseImpl kv;
    private IndexImpl index;
    
    public MyCheckpointer(KeyValueBaseImpl kv, IndexImpl index) {
        this.kv = kv;
        this.index = index;
    }
    
	@Override
	public void run() {
	    while (true) {
	        try {
                Thread.sleep(CHECKPOINT_INTERVAL);
            } catch (InterruptedException e) {
                // doesn't matter
            }
	        kv.quiesce();
	        index.flush();
	        kv.resume();
	    }
	}

}
