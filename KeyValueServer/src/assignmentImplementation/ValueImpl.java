package assignmentImplementation;

import javax.xml.bind.annotation.XmlRootElement;

import keyValueBaseInterfaces.Value;

@XmlRootElement
public class ValueImpl implements Value
{
    private static final long serialVersionUID = 4117889269011986973L;
    
    private Integer value;
    
    public Integer getValue() {
        return this.value;
    }
    
    public void setValue(Integer value) {
        this.value = value;
    }

    public String toString() {
        return value.toString();
    }
    
}

