package eu.waldonia.labs.traffic.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Calendar;
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
public class JourneyProcessorTest {
    @InjectMocks
    private JourneyProcessor processor;
    
    @Mock
    private TrafficPersister mockPersister;
    
    private XMLEventReader reader;

    @Before
    public void setUp() throws Exception {
	processor = new JourneyProcessor();
	processor.setTrafficPersister(mockPersister);
	XMLInputFactory f = XMLInputFactory.newInstance();
	reader = f.createXMLEventReader(new FileInputStream(new File("./data/test-journey.xml")));
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
	    
	    GenericDomainObject o = new GenericDomainObject();
	    o.addKey("k_location_id", "Section11117");
	    o.addKey("publication_ts", "2014-03-22T17:37:57Z");
	    when(mockPersister.getLocation("Section11117")).thenReturn(o);
	    
	    processor.process();
	    
	    verify(mockPersister).store(o);
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
	    processor.setTrafficPersister(checker);
	    processor.process();
	}
	catch (Exception e) {
	    e.printStackTrace();
	    fail("Shouldn't have thrown an exception");
	}	
    }
    
    class VerifyDomainObjectPersister implements TrafficPersister {

	@Override
	public void store(GenericDomainObject objectToStore) {
	    
	    Map<String,Object> keys = new HashMap<String,Object>();
	    keys.put("k_location_id", "Section11117");
	    Calendar c = javax.xml.bind.DatatypeConverter.parseDateTime("2014-03-22T17:37:57Z");
	    	    
	    keys.put("k_publication_ts", c.getTime().getTime());	    
	    assertEquals(keys,objectToStore.getKeys());
	    
	    Map<String,Object> attrs = objectToStore.getAttributes();
	    
	    assertTrue(attrs.containsKey("travel_time"));
	    assertEquals(new BigDecimal("79.0"), attrs.get("travel_time"));
	    
	    assertTrue(attrs.containsKey("freeflow_time"));
	    assertEquals(new BigDecimal("83.0"), attrs.get("freeflow_time"));
	    
	    assertTrue(attrs.containsKey("normal_time"));
	    assertEquals(new BigDecimal("83.0"), attrs.get("normal_time"));	    
	    
	}

	@Override
	public GenericDomainObject getLocation(String locationId) {
	    GenericDomainObject o = new GenericDomainObject();
	    o.addKey("k_location_id", locationId);
	    o.addKey("publication_ts", "2014-03-22T17:37:57Z");
	    return o;
	}

	@Override
	public void setTableName(String tableName) {
	    // TODO Auto-generated method stub
	    
	}
	
    }

}
