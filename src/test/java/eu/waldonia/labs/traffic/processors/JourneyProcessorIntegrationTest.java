package eu.waldonia.labs.traffic.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.waldonia.labs.traffic.domain.CassandraProxy;
import eu.waldonia.labs.traffic.domain.GenericDomainObject;

public class JourneyProcessorIntegrationTest {

    private JourneyProcessor processor;
    private CassandraProxy proxy;
    
    private XMLEventReader reader;


    /*
     * 	Test table structure
     * 	k_location_id text, 
     * 	k_publication_ts timestamp,
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
     *	travel_time decimal,
     *	freeflow_time decimal,
     *	actual_time decimal,
     *	primary key (k_location_id, k_publication_ts)
     */
    @Before
    public void setUp() throws Exception {
	
	proxy = new CassandraProxy();
	proxy.executeStatement("CREATE KEYSPACE IF NOT EXISTS testks  WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1}");
	String ddl = "CREATE TABLE testks.test_locations " +
		"(k_location_id text, k_publication_ts timestamp,name text, " +
		"direction text, location_type text, to_latitude text, " +
		"to_longitude text," +"to_first_loc text,to_second_loc text," +
		"from_latitude text, from_longitude text," +"from_first_loc text," +
		"from_second_loc text,travel_time decimal,freeflow_time decimal," +
		"normal_time decimal,primary key (k_location_id, k_publication_ts))";
	proxy.executeStatement(ddl);
	proxy.setTableName("testks.test_locations");

	XMLInputFactory f = XMLInputFactory.newInstance();

	
	LocationProcessor lProcessor = new LocationProcessor();
	lProcessor.setLocationPersister(proxy);
	reader = f.createXMLEventReader(new FileInputStream(new File("./data/test-location.xml")));
	lProcessor.setReader(reader);
	lProcessor.process();
	
	
	processor = new JourneyProcessor();
	processor.setLocationPersister(proxy);
	reader = f.createXMLEventReader(new FileInputStream(new File("./data/test-journey.xml")));
	processor.setReader(reader);
    }

    @After
    public void tearDown() {
	proxy.executeStatement("DROP TABLE IF EXISTS testks.test_locations");
	proxy.executeStatement("DROP KEYSPACE IF EXISTS testks");
    }
    
    @Test
    public void testProcessRow() {
	// there will already be a row there
	List<GenericDomainObject> results = proxy.executeQuery("SELECT * FROM testks.test_locations");
	assertTrue(results.size() == 1);
	
	results = proxy.executeQuery("SELECT * FROM testks.test_locations");
	assertTrue(results.size() == 1);
	
	GenericDomainObject o = results.get(0);
	Calendar c = javax.xml.bind.DatatypeConverter.parseDateTime("2014-03-21T11:55:32Z");
	Date d = c.getTime();
	
	assertEquals("Section11117",o.getKeys().get("k_location_id"));
	assertNull(o.getAttributes().get("travel_time"));
	
	// now process the journey data
	processor.process();
	results = proxy.executeQuery("SELECT * FROM testks.test_locations");
	assertTrue(results.size() == 2);
	
	c = javax.xml.bind.DatatypeConverter.parseDateTime("2014-03-22T17:37:57Z");
	d = c.getTime();
	
	
	results = proxy.executeQuery("SELECT * FROM testks.test_locations WHERE k_location_id = 'Section11117' AND k_publication_ts = "+d.getTime());
	assertTrue(results.size() == 1);
	o = results.get(0);
	
	assertTrue(o.getAttributes().containsKey("travel_time"));
	assertEquals(new BigDecimal("79.0"), o.getAttributes().get("travel_time"));
	assertTrue(o.getAttributes().containsKey("freeflow_time"));
	assertEquals(new BigDecimal("83.0"), o.getAttributes().get("freeflow_time"));	
	assertTrue(o.getAttributes().containsKey("normal_time"));
	assertEquals(new BigDecimal("83.0"), o.getAttributes().get("normal_time"));
	
    }
}
