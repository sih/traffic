package eu.waldonia.labs.traffic.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eu.waldonia.labs.traffic.processors.TrafficPersister;

/**
 * Connect to an manipulate the Cassandra data store
 * 
 * @author waldo
 * 
 */
public class CassandraProxy implements TrafficPersister {

    private Session session;
    private String tableName;
    
    public CassandraProxy() {
	this("localhost");
    }

    public CassandraProxy(String host) {
	Cluster c = Cluster.builder().addContactPoint(host).build();
	session = c.connect();
    }

    /**
     * 
     * @param statement
     */
    public void executeStatement(String statement) {
	session.execute(statement);
    }
    
    
    /**
     * 
     * @param query
     * @return
     */
    public List<GenericDomainObject> executeQuery(String query) {
	List<GenericDomainObject> results = new ArrayList<GenericDomainObject>();
	ResultSet rs = session.execute(query);
	for (Row row : rs) {
	    GenericDomainObject o = new GenericDomainObject();
	    ColumnDefinitions colDefs = rs.getColumnDefinitions();
	    for (Definition colDef : colDefs) {
		String colName = colDef.getName();
		if (colName.startsWith("k_")) {
		    if (colDef.getType().equals(DataType.varchar())) {
			o.addKey(colName, row.getString(colName));			
		    }
		    else if (colDef.getType().equals(DataType.timestamp())) {
			o.addKey(colName, row.getDate(colName));		
		    }

		}
		else {
		    if (colDef.getType().equals(DataType.varchar())) {
			o.addAttribute(colDef.getName(), row.getString(colName));
		    }
		    else if (colDef.getType().equals(DataType.decimal())) {
			o.addAttribute(colDef.getName(),row.getDecimal(colName));
		    }
		    else if (colDef.getType().equals(DataType.timestamp())) {
			o.addAttribute(colName, row.getDate(colName));		
		    }
		}
		// TODO support more types
	    }
	    results.add(o);
	}
	return results;
    }

    @Override
    public void store(GenericDomainObject objectToStore) {
	objectToStore.setTableName(tableName);
	session.execute(objectToStore.insertCql());
    }

    public void setTableName(final String tableName) {
	this.tableName = tableName;
    }

    @Override
    public GenericDomainObject getLocation(String locationId) {
	
	String cql = "SELECT * FROM "+tableName+" WHERE k_location_id = '"+locationId+"' LIMIT 1";
	List<GenericDomainObject> results = this.executeQuery(cql);
	return results.get(0);
	
    }
    
}
