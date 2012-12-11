package assignmentImplementation;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import keyValueBaseInterfaces.Key;

@XmlRootElement
public class KeyImpl implements Key<KeyImpl>, Serializable
{
    private static final long serialVersionUID = 2935599519255109033L;
    private Integer key;

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getKey() {
        return this.key;
    }

    public int compareTo(KeyImpl other) {
        return key.compareTo(other.key);
    }
    
    public String toString() {
        return key.toString();
    }

}
