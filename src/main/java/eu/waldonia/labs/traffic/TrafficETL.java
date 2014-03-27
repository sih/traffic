package eu.waldonia.labs.traffic;

import javax.xml.stream.XMLInputFactory;

import eu.waldonia.labs.traffic.domain.CassandraProxy;
import eu.waldonia.labs.traffic.processors.HttpConnector;
import eu.waldonia.labs.traffic.processors.JourneyProcessor;
import eu.waldonia.labs.traffic.processors.LocationProcessor;
import eu.waldonia.labs.traffic.processors.TrafficPersister;


/**
 * @author waldo
 *
 */
public class TrafficETL {

    /**
     * @param args
     */
    public static void main(String[] args) {

	HttpConnector connector = new HttpConnector();
	
	String locationUrl = "http://hatrafficinfo.dft.gov.uk/feeds/datex/England/PredefinedLocationJourneyTimeSections/content.xml";
	String journeyUrl = "http://hatrafficinfo.dft.gov.uk/feeds/datex/England/JourneyTimeData/content.xml";
	
	XMLInputFactory f = XMLInputFactory.newInstance();
	
	
	LocationProcessor lp = new LocationProcessor();
	lp.setTableName("traffic.locations");
	
	
	JourneyProcessor jp = new JourneyProcessor();
	jp.setLocationTable("traffic.locations");
	jp.setJourneyTable("traffic.journeys");
	
	try {
	    
	    lp.setReader(f.createXMLEventReader(connector.getAsStream(locationUrl)));
	    lp.process();
	    
	    jp.setReader(f.createXMLEventReader(connector.getAsStream(journeyUrl)));
	    jp.process();
	    
	    connector.close();
	}
	catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	
    }

}
