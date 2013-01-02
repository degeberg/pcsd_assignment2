package assignmentImplementation;

import java.io.IOException;

import keyValueBaseInterfaces.Checkpointer;

public class MyCheckpointer extends Thread implements Checkpointer {
    
    private static final int CHECKPOINT_INTERVAL = 2000;

    private KeyValueBaseImpl kv;
    private IndexImpl index;
    private MyLogger logger;
    
    public MyCheckpointer(KeyValueBaseImpl kv, IndexImpl index, MyLogger logger) {
        this.kv = kv;
        this.index = index;
        this.logger = logger;
    }
    
	@Override
	public void run() {
	    while (true) {
	        try {
                Thread.sleep(CHECKPOINT_INTERVAL);
            } catch (InterruptedException e) {
                // doesn't matter
            }
	        //System.out.println("RUNNING CHECKPOINTER");
	        kv.quiesce();
	        try {
    	        index.flush();
    	        logger.truncate();
	        } catch (IOException e) {
	            // fuck...
	            e.printStackTrace();
	        }
	        kv.resume();
	    }
	}

}
