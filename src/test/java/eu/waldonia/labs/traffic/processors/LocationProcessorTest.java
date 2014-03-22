package eu.waldonia.labs.traffic.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import eu.waldonia.labs.traffic.domain.GenericDomainObject;

@RunWith(MockitoJUnitRunner.class)
public class LocationProcessorTest {
    
    @InjectMocks
    private LocationProcessor processor;
    
    @Mock
    private LocationPersister mockPersister;
    
    private XMLEventReader reader;

    @Before
    public void setUp() throws Exception {
	processor = new LocationProcessor();
	processor.setLocationPersister(mockPersister);
	XMLInputFactory f = XMLInputFactory.newInstance();
	reader = f.createXMLEventReader(new FileInputStream(new File("./data/test-location.xml")));
	processor.setReader(reader);
    }

    @Test
    public void testProcessNoReader() {
	processor.setReader(null);
	try {
	    processor.process();
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
    public void testProcess() {
	try {
	    processor.process();
	    GenericDomainObject objectToStore = new GenericDomainObject();
	    objectToStore.addKey("k_location_id", "Section11117");
	    verify(mockPersister).store(objectToStore);
	}
	catch (Exception e) {
	    e.printStackTrace();
	    fail("Shouldn't have thrown this exception");
	}
    }
    
    @Test
    public void testProcessAllAttributes() {
	try {
	    VerifyDomainObjectPersister checker = new VerifyDomainObjectPersister();
	    processor.setLocationPersister(checker);
	    processor.process();
	}
	catch (Exception e) {
	    fail("Should have thrown an exception");
	}	
    }
    
    class VerifyDomainObjectPersister implements LocationPersister {

	@Override
	public void store(GenericDomainObject objectToStore) {
	    
	    Map<String,String> keys = new HashMap<String,String>();
	    keys.put("k_location_id", "Section11117");
	    assertEquals(keys,objectToStore.getKeys());
	    
	    Map<String,Object> attrs = objectToStore.getAttributes();
	    
	    assertTrue(attrs.containsKey("name"));
	    assertEquals("Journey Time Section for M53 southbound from J2 (A551) to J5 (A41)", attrs.get("name"));
	    
	    assertTrue(attrs.containsKey("direction"));
	    assertEquals("southBound", attrs.get("direction"));
	    
	    assertTrue(attrs.containsKey("location_type"));
	    assertEquals("segment", attrs.get("location_type"));
	    
	    assertTrue(attrs.containsKey("to_latitude"));
	    assertEquals("53.303505", attrs.get("to_latitude"));	    	    
	    
	    assertTrue(attrs.containsKey("to_longitude"));	    
	    assertEquals("-2.964967", attrs.get("to_longitude"));	    
	    
	    assertTrue(attrs.containsKey("to_first_loc"));	    
	    assertEquals("M53", attrs.get("to_first_loc"));	    	    

	    assertTrue(attrs.containsKey("to_second_loc"));	    
	    assertEquals("A41", attrs.get("to_second_loc"));	
	    	    
	    
	    assertTrue(attrs.containsKey("from_latitude"));
	    assertEquals("53.40252", attrs.get("from_latitude"));	    	    
	    
	    assertTrue(attrs.containsKey("from_longitude"));	    
	    assertEquals("-3.088529", attrs.get("from_longitude"));	    
	    
	    assertTrue(attrs.containsKey("from_first_loc"));	    
	    assertEquals("M53", attrs.get("from_first_loc"));	    	    

	    assertTrue(attrs.containsKey("from_second_loc"));	    
	    assertEquals("A551", attrs.get("from_second_loc"));	
	    
	}
	
    }

}
