package eu.waldonia.labs.traffic;

import java.util.List;

/**
 * API to explore the location and journey data
 * @author waldo
 */
public interface TrafficDataService {
    
    /**
     * @return A List of JSON objects or an empty List if no objects found   
     */
    public List<String> getLocationsAsJSON();

    /**
     * @param locationId The unique identifier for a location
     * @return A JSON representation of that location or an empty JSON object if not found
     */
    public String getLocationAsJSON(String locationId);

    
    
}
