package eu.waldonia.labs.traffic.processors;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

import org.junit.After;
import org.junit.Before;
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
    private LocationPersister mockPersister;
    
    private XMLEventReader reader;
    private CassandraProxy proxy;

    @Before
    public void setUp() throws Exception {
	processor = new LocationProcessor();
	
	processor.setLocationPersister(mockPersister);
	
	XMLInputFactory f = XMLInputFactory.newInstance();
	reader = f.createXMLEventReader(new FileInputStream(new File("./data/predefined-location.xml")));
	processor.setReader(reader);
	
	proxy = new CassandraProxy();
	proxy.executeStatement("CREATE KEYSPACE IF NOT EXISTS testks  WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1}");
	proxy.executeStatement("CREATE TABLE testks.test_locations (k_location_id text, name text, direction text, location_type text, to_latitude text, to_longitude text, to_first_loc text, to_second_loc text, from_latitude text, from_longitude text, from_first_loc text, from_second_loc text, primary key (k_location_id))");
	proxy.setTableName("testks.test_locations");
	
    }

    @After
    public void tearDown() throws Exception {
	proxy.executeStatement("DROP TABLE IF EXISTS testks.test_locations");
	proxy.executeStatement("DROP KEYSPACE IF EXISTS testks");
    }
    
    /**
     * Test 28000 rows in under 7s (i.e. 4000 rows/sec)
     */
    @Test
    public void testParseTiming() {
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
    @Test
    public void testStorageTiming() {
	processor.setLocationPersister(proxy);
	long timer = System.currentTimeMillis();
	int rows = processor.process();
	// stop the timer
	timer = System.currentTimeMillis() - timer;
	assertTrue(rows > 28000);
	assertTrue(timer < 28000);
    }    
    
    
}
