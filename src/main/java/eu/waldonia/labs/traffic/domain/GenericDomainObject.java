package eu.waldonia.labs.traffic.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author waldo
 *
 */
public class GenericDomainObject {

    private Map<String,String> keys;
    private Map<String,Object> attributes;

    /**
     * Make sure the attributes map is non-null
     */
    public GenericDomainObject() {
	attributes = new HashMap<String,Object>();
    }
    
    /**
     * @param name The attribute name to add
     * @param value The attribute value to add
     */
    public void addAttribute(String name, Object value) {
	attributes.put(name, value);
    }
    
    /**
     * @return A CQL insert statement for this row 
     */
    public String toCql() {
	StringBuffer buffy = new StringBuffer();
	// TODO Implement me!
	return buffy.toString();
    }
    
    public Map<String,String> getKeys() {
        return keys;
    }
    public void setKeys(Map<String,String> keys) {
        this.keys = keys;
    }
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((keys == null) ? 0 : keys.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	GenericDomainObject other = (GenericDomainObject) obj;
	if (keys == null) {
	    if (other.keys != null)
		return false;
	}
	else if (!keys.equals(other.keys))
	    return false;
	return true;
    }

    
    
    
    
    
    
}
