package eu.waldonia.labs.traffic.domain;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author waldo
 *
 */
public class GenericDomainObject {

    private Map<String,String> keys;
    private Map<String,Object> attributes;
    private String tableName;

    /**
     * Make sure the attributes map is non-null
     */
    public GenericDomainObject() {
	keys = new LinkedHashMap<String,String>();
	attributes = new LinkedHashMap<String,Object>();
    }

    /**
     * @param name The key name to add
     * @param value The key value to add
     */
    public void addKey(String name, String value) {
	keys.put(name, value);
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
    public String insert() {
	StringBuffer buffy = new StringBuffer();
	buffy.append("INSERT INTO ");
	buffy.append(tableName);
	buffy.append(" (");
	buffy.append(")");
	
	
	// TODO Implement me!
	return buffy.toString();
    }
    
    public Map<String,String> getKeys() {
        return keys;
    }
    
    
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    
    public void setTableName(String tableName) {
	this.tableName = tableName;
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
