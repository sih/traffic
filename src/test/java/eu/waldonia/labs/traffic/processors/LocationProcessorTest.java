package eu.waldonia.labs.traffic.processors;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import eu.waldonia.labs.traffic.processors.LocationProcessor;

public class LocationProcessorTest {

    private LocationProcessor parser;

    @Before
    public void setUp() {
	parser = new LocationProcessor();
	parser.setFileLocation("/Users/sid/data/traffic/predefined-location.xml");
    }

    @Test
    public void testParseNoFile() {
	parser.setFileLocation(null);
	try {
	    parser.process();
	    fail("Should have thrown an exception");
	}
	catch (IllegalStateException ise) {
	    // all ok
	}
	catch (Exception e) {
	    fail("Shouldn't have thrown this exception");
	}
    }
    
    @Test
    public void testParse() {
	try {
	    parser.process();
	}
	catch (Exception e) {
	    e.printStackTrace();
	    fail("Shouldn't have thrown this exception");
	}	
    }
    
    
}
