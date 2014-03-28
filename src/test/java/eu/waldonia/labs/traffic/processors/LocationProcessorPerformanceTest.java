package eu.waldonia.labs.traffic.processors;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import eu.waldonia.labs.traffic.domain.CassandraProxy;

@RunWith(MockitoJUnitRunner.class)
public class LocationProcessorPerformanceTest {
    
    @InjectMocks
    private LocationProcessor processor;
    
    @Mock
    private TrafficPersister mockPersister;
    
    private XMLEventReader reader;
    private CassandraProxy proxy;

    @Before
    public void setUp() throws Exception {
	XMLInputFactory f = XMLInputFactory.newInstance();
	reader = f.createXMLEventReader(new FileInputStream(new File("./data/predefined-location.xml")));
	
	proxy = new CassandraProxy();
	proxy.executeStatement("CREATE KEYSPACE IF NOT EXISTS testks  WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1}");
	String ddl = "CREATE TABLE testks.test_locations " +
		"(k_location_id text, publication_ts timestamp, name text, " +
		"direction text, location_type text, to_latitude text, " +
		"to_longitude text," +"to_first_loc text,to_second_loc text," +
		"from_latitude text, from_longitude text," +"from_first_loc text," +
		"from_second_loc text,primary key (k_location_id))";
	proxy.executeStatement(ddl);
	processor = new LocationProcessor();
	processor.setTableName("testks.test_locations");
	processor.setReader(reader);
	
    }

    @After
    public void tearDown() throws Exception {
	proxy.executeStatement("DROP TABLE IF EXISTS testks.test_locations");
	proxy.executeStatement("DROP KEYSPACE IF EXISTS testks");
    }
    
    /**
     * Test 28000 rows in under 7s (i.e. 4000 rows/sec)
     */
    @Ignore
    public void testParseTimingLarge() {
	long timer = System.currentTimeMillis();
	int rows = processor.process();
	// stop the timer
	timer = System.currentTimeMillis() - timer;
	assertTrue(rows > 28000);
	assertTrue(timer < 7000);
    }

    
    /**
     * Test 28000 rows in under 28s (i.e. 1000 rows/sec)
     */
    @Ignore
    public void testStorageTimingLarge() {
	processor.setTrafficPersister(proxy);
	long timer = System.currentTimeMillis();
	int rows = processor.process();
	// stop the timer
	timer = System.currentTimeMillis() - timer;
	assertTrue(rows > 28000);
	assertTrue(timer < 28000);
    }    
    

    /**
     * 
     */
    @Test
    public void testParseTiming() {
	long timer = System.currentTimeMillis();
	int rows = processor.process();
	// stop the timer
	timer = System.currentTimeMillis() - timer;
	assertTrue(rows > 800);
	assertTrue(timer < 2500);
    }

    
    /**
     * 
     */
    @Test
    public void testStorageTiming() {
	processor.setTrafficPersister(proxy);
	long timer = System.currentTimeMillis();
	int rows = processor.process();
	// stop the timer
	timer = System.currentTimeMillis() - timer;
	assertTrue(rows > 800);
	assertTrue(timer < 3500);
    }    
    
}
