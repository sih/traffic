package eu.waldonia.labs.traffic.processors;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocationProcessorPerformanceTest {
    
    @InjectMocks
    private LocationProcessor parser;
    
    @Mock
    private LocationPersister mockPersister;
    
    private XMLEventReader reader;

    @Before
    public void setUp() throws Exception {
	parser = new LocationProcessor();
	parser.setLocationPersister(mockPersister);
	XMLInputFactory f = XMLInputFactory.newInstance();
	reader = f.createXMLEventReader(new FileInputStream(new File("./data/predefined-location.xml")));
	parser.setReader(reader);
    }

    /**
     * Test 28000 rows in under 7s (i.e. 4000 rows/sec)
     */
    @Test
    public void testTiming() {
	long timer = System.currentTimeMillis();
	int rows = parser.process();
	// stop the timer
	timer = System.currentTimeMillis() - timer;
	assertTrue(rows > 28000);
	assertTrue(timer < 7000);
    }

    
}
