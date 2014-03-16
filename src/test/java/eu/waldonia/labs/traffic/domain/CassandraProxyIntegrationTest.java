package eu.waldonia.labs.traffic.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.waldonia.labs.traffic.domain.CassandraProxy;

public class CassandraProxyIntegrationTest {

    private CassandraProxy proxy;

    @Before
    public void setUp() {
	proxy = new CassandraProxy();
	proxy.executeStatement("CREATE KEYSPACE IF NOT EXISTS testks  WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1}");
    }

    @After
    public void tearDown() {
	proxy.executeStatement("DROP TABLE IF EXISTS testks.test_locations");
	proxy.executeStatement("DROP KEYSPACE IF EXISTS testks");
    }

    @Test
    public void testExecuteStatementDDL() {
	try {
	    proxy.executeStatement("CREATE TABLE testks.test_locations (location_id text, name text, direction text, to_latitude text, to_longitude text, from_latitude text, from_longitude text, primary key (location_id))");
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Shouldn't have thrown an exception");
	}
    }

}
