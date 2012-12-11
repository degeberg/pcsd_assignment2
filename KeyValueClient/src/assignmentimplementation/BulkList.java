
package assignmentimplementation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for bulkList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bulkList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="keys" type="{http://assignmentImplementation/}keyImpl" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="vals" type="{http://assignmentImplementation/}valueListImpl" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bulkList", propOrder = {
    "keys",
    "vals"
})
public class BulkList {

    protected List<KeyImpl> keys;
    protected List<ValueListImpl> vals;

    /**
     * Gets the value of the keys property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the keys property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKeys().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KeyImpl }
     * 
     * 
     */
    public List<KeyImpl> getKeys() {
        if (keys == null) {
            keys = new ArrayList<KeyImpl>();
        }
        return this.keys;
    }

    /**
     * Gets the value of the vals property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vals property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVals().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValueListImpl }
     * 
     * 
     */
    public List<ValueListImpl> getVals() {
        if (vals == null) {
            vals = new ArrayList<ValueListImpl>();
        }
        return this.vals;
    }

}
