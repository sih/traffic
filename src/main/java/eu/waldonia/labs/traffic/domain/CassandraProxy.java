package eu.waldonia.labs.traffic.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eu.waldonia.labs.traffic.processors.LocationPersister;

/**
 * Connect to an manipulate the Cassandra data store
 * 
 * @author waldo
 * 
 */
public class CassandraProxy implements LocationPersister {

    private Session session;

    public CassandraProxy() {
	this("localhost");
    }

    public CassandraProxy(String host) {
	Cluster c = Cluster.builder().addContactPoint(host).build();
	session = c.connect();
    }

    /**
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
    public List<Map<String, String>> executeQuery(String query) {
	List<Map<String, String>> results = new ArrayList<Map<String, String>>();
	ResultSet rs = session.execute(query);
	for (Row row : rs) {
	    ColumnDefinitions colDefs = rs.getColumnDefinitions();
	    Map<String, String> columns = new LinkedHashMap<String, String>();
	    for (Definition colDef : colDefs) {
		columns.put(colDef.getName(), row.getString(colDef.getName()));
	    }
	    results.add(columns);
	}
	return results;
    }

    @Override
    public void store(GenericDomainObject objectToStore) {
	// TODO Auto-generated method stub
	
    }

}
