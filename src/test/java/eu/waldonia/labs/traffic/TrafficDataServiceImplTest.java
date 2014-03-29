package eu.waldonia.labs.traffic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import eu.waldonia.labs.traffic.domain.CassandraProxy;
import eu.waldonia.labs.traffic.domain.GenericDomainObject;

@RunWith(MockitoJUnitRunner.class)
public class TrafficDataServiceImplTest {

    @InjectMocks
    private TrafficDataServiceImpl tds;
    
    @Mock
    private CassandraProxy mockProxy;
    
    private static final String GET_ALL = "SELECT name, from_longitude, from_latitude, to_longitude, to_latitude FROM testks.test_locations";
    private static final String GET_ID = "SELECT name, from_longitude, from_latitude, to_longitude, to_latitude FROM testks.test_locations WHERE k_location_id = 'Section10101'";

    private static final String EXPECTED_ONE = "{\"name\":\"M6 20 to 21a\",\"from_loc\":{\"long\":\"-2.498387\",\"lat\":\"53.34795\"},\"to_loc\":{\"long\":\"-2.547838\",\"lat\":\"53.42038\"}}";
    
    private List<GenericDomainObject> oneResult;
    
    @Before
    public void setUp() {
	tds.setTableName("testks.test_locations");
	
	oneResult = new ArrayList<GenericDomainObject>();
	GenericDomainObject gdo = new GenericDomainObject();
	gdo.addAttribute("name", "M6 20 to 21a");
	gdo.addAttribute("from_longitude", new BigDecimal("-2.498387"));
	gdo.addAttribute("from_latitude", new BigDecimal("53.34795"));
	gdo.addAttribute("to_longitude", new BigDecimal("-2.547838"));
	gdo.addAttribute("to_latitude", new BigDecimal("53.42038"));
	oneResult.add(gdo);
	
    }
    
    @Test
    public void testGetLocationsAsJSONNoResults() {

	when(mockProxy.executeQuery(GET_ALL)).thenReturn(new ArrayList<GenericDomainObject>());
	
	List<String> json = tds.getLocationsAsJSON();
	assertTrue(json.isEmpty());
    }
    
    
    @Test
    public void testGetLocationsAsJSONResults() {

	when(mockProxy.executeQuery(GET_ALL)).thenReturn(oneResult);
	
	List<String> json = tds.getLocationsAsJSON();
	assertEquals(1,json.size());
	assertEquals(EXPECTED_ONE, json.get(0));
    }    


    @Test
    public void testGetLocationAsJSONNullLocationId() {
	String locationId = null;
	
	String json = tds.getLocationAsJSON(locationId);
	assertEquals("{}",json);
    }
    
    @Test
    public void testGetLocationAsJSONNoResults() {

	when(mockProxy.executeQuery(GET_ID)).thenReturn(new ArrayList<GenericDomainObject>());

	String locationId = "Section10101";
	
	String json = tds.getLocationAsJSON(locationId);
	assertEquals("{}",json);
    }
    
    
    @Test
    public void testGetLocationAsJSONResults() {

	when(mockProxy.executeQuery(GET_ID)).thenReturn(oneResult);

	String locationId = "Section10101";
	
	String json = tds.getLocationAsJSON(locationId);
	assertEquals(EXPECTED_ONE, json);
    }  
    
}
