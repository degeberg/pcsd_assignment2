package assignmentImplementation;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class BulkList implements Serializable
{
    private static final long serialVersionUID = 6667176602210201393L;
    
    @XmlElement(name="keys")
    private ArrayList<KeyImpl> k;
    @XmlElement(name="vals")
    private ArrayList<ValueListImpl> v;

    public BulkList() {
        k = new ArrayList<KeyImpl>();
        v = new ArrayList<ValueListImpl>();
    }
    
    public void add(KeyImpl key, ValueListImpl vl) {
        k.add(key);
        v.add(vl);
    }
    
    public List<KeyImpl> getKeys() {
        return this.k;
    }
    
    public List<ValueListImpl> getValues() {
        return this.v;
    }
}
