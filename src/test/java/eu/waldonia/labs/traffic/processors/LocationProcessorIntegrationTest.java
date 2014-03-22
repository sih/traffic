package eu.waldonia.labs.traffic.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.waldonia.labs.traffic.domain.CassandraProxy;
import eu.waldonia.labs.traffic.domain.GenericDomainObject;

public class LocationProcessorIntegrationTest {

    private LocationProcessor processor;
    private CassandraProxy proxy;
    
    private XMLEventReader reader;


    /*
     * 	Test table structure
     * 	k_location_id text, 
     *  name text, 
     *	direction text, 
     *	location_type text,
     *	to_latitude text,
     *	to_longitude text,
     *	to_first_loc text,
     *	to_second_loc text,
     *	from_latitude text, 
     *	from_longitude text, 
     *	from_first_loc text,
     *	from_second_loc text,
     *	primary key (location_id)
     */
    @Before
    public void setUp() throws Exception {
	processor = new LocationProcessor();
	
	proxy = new CassandraProxy();
	proxy.executeStatement("CREATE KEYSPACE IF NOT EXISTS testks  WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1}");
	proxy.executeStatement("CREATE TABLE testks.test_locations (k_location_id text, name text, direction text, location_type text, to_latitude text, to_longitude text, to_first_loc text, to_second_loc text, from_latitude text, from_longitude text, from_first_loc text, from_second_loc text, primary key (k_location_id))");
	proxy.setTableName("testks.test_locations");
	
	processor.setLocationPersister(proxy);
	XMLInputFactory f = XMLInputFactory.newInstance();
	
	reader = f.createXMLEventReader(new FileInputStream(new File("./data/test-location.xml")));
	processor.setReader(reader);
    }

    @After
    public void tearDown() {
	proxy.executeStatement("DROP TABLE IF EXISTS testks.test_locations");
	proxy.executeStatement("DROP KEYSPACE IF EXISTS testks");
    }
    
    @Test
    public void testProcessRow() {
	List<GenericDomainObject> results = proxy.executeQuery("SELECT * FROM testks.test_locations");
	assertTrue(results.isEmpty());
	int rowcount = processor.process();
	assertEquals(1,rowcount);
	results = proxy.executeQuery("SELECT * FROM testks.test_locations");
	assertTrue(results.size() == 1);
	
	GenericDomainObject o = results.get(0);
	String firstKey = o.getKeys().keySet().iterator().next();
	String firstKeyValue = o.getKeys().get(firstKey);
	assertEquals("Section11117",firstKeyValue);
	
    }
}
