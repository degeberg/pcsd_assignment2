package assignmentImplementation;

import javax.xml.bind.annotation.XmlRootElement;

import keyValueBaseInterfaces.Key;

@XmlRootElement
public class KeyImpl implements Key<KeyImpl>
{
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
