package eu.waldonia.labs.traffic.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class HttpConnectorIntegrationTest {

    HttpConnector connector;
    
    @Before
    public void setUp() {
	connector = new HttpConnector();
    }
    
    
    @Test
    public void testGetNullUrl() {
	String url = null;
	Map<Integer,String> results = connector.get(url);
	assertTrue(results.isEmpty());
    }
    
    
    @Test
    public void testGetValidUrl() {
	String url = "http://hatrafficinfo.dft.gov.uk/feeds/datex/England/PredefinedLocationJourneyTimeSections/content.xml";
	Map<Integer,String> results = connector.get(url);
	assertEquals(1,results.size());
	Integer status = results.keySet().iterator().next();
	assertEquals(new Integer(200),status);
	assertTrue(results.get(200).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"));
    }

}
