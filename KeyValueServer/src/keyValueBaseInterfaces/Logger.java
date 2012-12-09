package keyValueBaseInterfaces;

import java.util.concurrent.Future;

public interface Logger extends Runnable{
	public Future<?> logRequest(LogRecord record);
}
