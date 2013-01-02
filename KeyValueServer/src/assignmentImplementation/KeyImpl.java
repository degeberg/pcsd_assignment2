package assignmentImplementation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof KeyImpl)) {
            return false;
        }
        
        return ((KeyImpl) other).key.equals(this.key);
    }
    
    @Override
    public int hashCode() {
        return this.key.hashCode();
    }
    
    public static List<KeyImpl> getInterval(KeyImpl start, KeyImpl end) {
        ArrayList<KeyImpl> ks = new ArrayList<>();
        for (int k = start.key; k <= end.key; k++) {
            KeyImpl ki = new KeyImpl();
            ki.setKey(k);
            ks.add(ki);
        }
        return ks;
    }
}
