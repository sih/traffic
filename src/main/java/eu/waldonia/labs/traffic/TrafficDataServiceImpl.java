package eu.waldonia.labs.traffic;

import java.util.ArrayList;
import java.util.List;

import eu.waldonia.labs.traffic.domain.CassandraProxy;
import eu.waldonia.labs.traffic.domain.GenericDomainObject;

/**
 * API implementation to explore the location and journey data
 * 
 * @author waldo
 */
public class TrafficDataServiceImpl implements TrafficDataService {

    CassandraProxy proxy;
    String tableName;

    public void setTableName(String tableName) {
	this.tableName = tableName;
    }

    public TrafficDataServiceImpl() {
	proxy = new CassandraProxy();
    }

    @Override
    public List<String> getLocationsAsJSON() {
	List<String> locationJSON = new ArrayList<String>();

	proxy.setTableName(tableName);
	String cql = "SELECT name, from_longitude, from_latitude, to_longitude, to_latitude FROM "
		+ tableName;

	List<GenericDomainObject> results = proxy.executeQuery(cql);

	for (GenericDomainObject location : results) {
	    locationJSON.add(to_JSON(location));
	}

	return locationJSON;
    }

    private String to_JSON(GenericDomainObject location) {
	StringBuffer buffy = new StringBuffer();

	buffy.append("{");

	if (location != null) {
	    String name = (String) location.getAttributes().get("name");
	    String from_long = location.getAttributes().get("from_longitude")
		    .toString();
	    String from_lat = location.getAttributes().get("from_latitude")
		    .toString();
	    String to_long = location.getAttributes().get("to_longitude")
		    .toString();
	    String to_lat = location.getAttributes().get("to_latitude")
		    .toString();

	    buffy.append("\"name\":");
	    buffy.append("\"");
	    buffy.append(name);
	    buffy.append("\"");

	    buffy.append(",");

	    buffy.append("\"from_loc\":");
	    buffy.append("{");
	    buffy.append("\"long\":");
	    buffy.append("\"");
	    buffy.append(from_long);
	    buffy.append("\"");
	    buffy.append(",");
	    buffy.append("\"lat\":");
	    buffy.append("\"");
	    buffy.append(from_lat);
	    buffy.append("\"");
	    buffy.append("}");

	    buffy.append(",");

	    buffy.append("\"to_loc\":");
	    buffy.append("{");
	    buffy.append("\"long\":");
	    buffy.append("\"");
	    buffy.append(to_long);
	    buffy.append("\"");
	    buffy.append(",");
	    buffy.append("\"lat\":");
	    buffy.append("\"");
	    buffy.append(to_lat);
	    buffy.append("\"");
	    buffy.append("}");

	}

	buffy.append("}");

	return buffy.toString();
    }

    @Override
    public String getLocationAsJSON(String locationId) {
	String json = "{}";

	if (locationId != null) {
	    String cql = "SELECT name, from_longitude, from_latitude, to_longitude, to_latitude FROM "
		    + tableName + " WHERE k_location_id = '" + locationId + "'";

	    List<GenericDomainObject> results = proxy.executeQuery(cql);
	    
	    if (!results.isEmpty()) {
		json = to_JSON(results.get(0));	// should never be > 1 but get first anyways
	    }

	}

	return json;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	// TODO Auto-generated method stub

    }

}
