package keyValueBaseInterfaces;

public interface KeyValueBaseLog<K extends Key<K>, V extends Value>
{
	public void quiesce();
	public void resume();
}
