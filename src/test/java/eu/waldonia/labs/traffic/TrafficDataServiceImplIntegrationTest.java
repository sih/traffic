package eu.waldonia.labs.traffic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import eu.waldonia.labs.traffic.domain.CassandraProxy;
import eu.waldonia.labs.traffic.domain.GenericDomainObject;

public class TrafficDataServiceImplIntegrationTest {

    private TrafficDataServiceImpl tds;
    
    private CassandraProxy proxy;
    
    private static final String GET_ALL = "SELECT name, from_longitude, from_latitude, to_longitude, to_latitude FROM testks.test_locations";
    private static final String GET_ID = "SELECT name, from_longitude, from_latitude, to_longitude, to_latitude FROM testks.test_locations WHERE k_location_id = 'Section10101'";

    private static final String EXPECTED_ONE = "{\"name\":\"M6 20 to 21a\",\"from_loc\":{\"long\":\"-2.498387\",\"lat\":\"53.34795\"},\"to_loc\":{\"long\":\"-2.547838\",\"lat\":\"53.42038\"}}";
    
    private List<GenericDomainObject> oneResult;

    @Before
    public void setUp() {
	// set up the test data service
	tds = new TrafficDataServiceImpl();
	tds.setTableName("testks.test_locations");
	
	// set up the test table and a row
	proxy = new CassandraProxy();
	proxy.executeStatement("CREATE KEYSPACE IF NOT EXISTS testks  WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1}");
	String ddl = "CREATE TABLE testks.test_locations " +
		"(k_location_id text, publication_ts timestamp, name text, " +
		"direction text, location_type text, to_latitude text, " +
		"to_longitude text," +"to_first_loc text,to_second_loc text," +
		"from_latitude text, from_longitude text," +"from_first_loc text," +
		"from_second_loc text,primary key (k_location_id))";
	proxy.executeStatement(ddl);	
	proxy.executeStatement("INSERT INTO testks.test_locations (k_location_id, name, from_longitude, from_latitude, to_longitude, to_latitude) VALUES('Section10101','M6 20 to 21a','-2.498387','53.34795','-2.547838','53.42038')");
	
	// set up the object we expect to get back from the query
	oneResult = new ArrayList<GenericDomainObject>();
	GenericDomainObject gdo = new GenericDomainObject();
	gdo.addAttribute("name", "M6 20 to 21a");
	gdo.addAttribute("from_longitude", new BigDecimal("-2.498387"));
	gdo.addAttribute("from_latitude", new BigDecimal("53.34795"));
	gdo.addAttribute("to_longitude", new BigDecimal("-2.547838"));
	gdo.addAttribute("to_latitude", new BigDecimal("53.42038"));
	oneResult.add(gdo);
	
    }

    @After
    public void tearDown() {
	proxy.executeStatement("DROP TABLE IF EXISTS testks.test_locations");
	proxy.executeStatement("DROP KEYSPACE IF EXISTS testks");
    }    
    
    
    
    @Test
    public void testGetLocationsAsJSONResults() {
	List<String> json = tds.getLocationsAsJSON();
	assertEquals(1,json.size());
	assertEquals(EXPECTED_ONE, json.get(0));
    }    

    
    @Test
    public void testGetLocationAsJSONNoResults() {
	String locationId = "Section10102";
	
	String json = tds.getLocationAsJSON(locationId);
	assertEquals("{}",json);
    }
    
    
    @Test
    public void testGetLocationAsJSONResults() {
	String locationId = "Section10101";
	
	String json = tds.getLocationAsJSON(locationId);
	assertEquals(EXPECTED_ONE, json);
    }  
    
}
