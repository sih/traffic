package eu.waldonia.labs.traffic.processors;

import eu.waldonia.labs.traffic.domain.GenericDomainObject;

/**
 * 
 * @author waldo
 *
 */
public interface LocationPersister {
    
    /**
     * @param objectToStore
     */
    void store(GenericDomainObject objectToStore);
    
    GenericDomainObject getLocation(String locationId);
    
}
