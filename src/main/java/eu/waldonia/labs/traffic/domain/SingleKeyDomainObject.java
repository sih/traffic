package eu.waldonia.labs.traffic.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author waldo
 *
 */
public class SingleKeyDomainObject {

    private String key;
    private Map<String,Object> attributes;
    
    public SingleKeyDomainObject() {
	attributes = new HashMap<String,Object>();
    }
    
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
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
	result = prime * result + ((key == null) ? 0 : key.hashCode());
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
	SingleKeyDomainObject other = (SingleKeyDomainObject) obj;
	if (key == null) {
	    if (other.key != null)
		return false;
	} else if (!key.equals(other.key))
	    return false;
	return true;
    }
    
    
    
    
}
