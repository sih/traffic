package eu.waldonia.labs.traffic.processors;

import eu.waldonia.labs.traffic.domain.GenericDomainObject;

/**
 * 
 * @author waldo
 *
 */
public interface TrafficPersister {
    
    /**
     * @param objectToStore
     */
    void store(GenericDomainObject objectToStore);
    
    GenericDomainObject getLocation(String locationId);
    
    void setTableName(String tableName);
    
}
