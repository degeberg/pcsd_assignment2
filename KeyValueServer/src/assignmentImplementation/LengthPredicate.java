package assignmentImplementation;

import javax.xml.bind.annotation.XmlRootElement;

import keyValueBaseInterfaces.Predicate;

@XmlRootElement
public class LengthPredicate extends Predicate<ValueListImpl>
{
    private int length;
    
    public void setLength(int length)
    {
        this.length = length;
    }
    
    public int getLength()
    {
        return this.length;
    }
    
    @Override
    public boolean evaluate(ValueListImpl vl)
    {
        return (vl.toList().size() < length);
    }
}
