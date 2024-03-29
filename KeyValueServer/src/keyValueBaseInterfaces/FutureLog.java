package keyValueBaseInterfaces;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureLog<T> implements Future<T> {
	
	private String lock = "";
	private T ret;
	private boolean done = false;

	@Override
	public T get(long arg0, TimeUnit arg1) throws InterruptedException,
			ExecutionException, TimeoutException {

		synchronized(this.lock) {
			this.lock.wait(arg0);
		}
		return ret;
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		synchronized(this.lock) {
			this.lock.wait();
		}
		return ret;
	}
	
	public void signalAll(T ret) {
		synchronized(this.lock) {
			this.ret = ret;
			this.lock.notifyAll();
			this.done = true;
		}
	}

	@Override
	public boolean cancel(boolean arg0) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		return this.done;
	}


}
