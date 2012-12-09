package keyValueBaseInterfaces;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public abstract class Predicate<T> {
    public abstract boolean evaluate(T input);
}
