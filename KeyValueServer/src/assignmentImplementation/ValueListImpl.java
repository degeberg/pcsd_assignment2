package assignmentImplementation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import keyValueBaseInterfaces.ValueList;

@XmlRootElement
public class ValueListImpl implements ValueList<ValueImpl>{
    private static final long serialVersionUID = -2327808136525385315L;
    
    @XmlElement(name="elements")
    private ArrayList<ValueImpl> l;
    
    public ValueListImpl()
    {
    	l = new ArrayList<ValueImpl>();
    }
    
    @Override
    public void add(ValueImpl v) {
        l.add(v);
    }

    @Override
    public void remove(ValueImpl v) {
        l.remove(v);
    }

    @Override
    public void merge(ValueList<ValueImpl> vl) {
        l.addAll(vl.toList());
    }

    @Override
    public List<ValueImpl> toList() {
        return l;
    }
    
    @Override
    public Iterator<ValueImpl> iterator() {
        return l.iterator();
    }

}
