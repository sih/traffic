package eu.waldonia.labs.traffic.processors;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class LocationProcessorTest {

    private LocationProcessor parser;

    @Before
    public void setUp() {
	parser = new LocationProcessor();
	parser.setFileLocation("./data/test-location.xml");
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
