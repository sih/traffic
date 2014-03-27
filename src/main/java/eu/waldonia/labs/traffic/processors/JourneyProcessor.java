package eu.waldonia.labs.traffic.processors;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import eu.waldonia.labs.traffic.domain.CassandraProxy;
import eu.waldonia.labs.traffic.domain.GenericDomainObject;

/**
 * @author waldo
 * 
 */
public class JourneyProcessor extends AbstractProcessor {

    private String journeyTable;
    private String locationTable;
    
    
    
    public void setJourneyTable(String journeyTable) {
        this.journeyTable = journeyTable;
    }



    public void setLocationTable(String locationTable) {
        this.locationTable = locationTable;
    }



    public int process() {
	
	if (null == this.persister) {
	    this.setTrafficPersister(new CassandraProxy());
	}
	persister.setTableName(this.tableName);
	
	int counter = 0;
	if (null == this.xmlReader)
	    throw new IllegalStateException("You need to supply an XMLReader");
	long timer = System.currentTimeMillis();// timer
	try {

	    GenericDomainObject row = null; // will hold the data to persist

	    String publicationTimestamp = null;
	    
	    // loop through each event
	    while (xmlReader.hasNext()) {
		XMLEvent event = xmlReader.nextEvent();
		// pull out start elements and match on the ones we're
		// interested in
		if (event.isStartElement()) {
		    StartElement s = event.asStartElement();
		    QName n = s.getName();


		    // timestamp for this data extract
		    if ("publicationTime".equals(n.getLocalPart())) {
			event = xmlReader.nextEvent();
			if (event.isCharacters()) {
			    publicationTimestamp = event.asCharacters().getData();
			}
		    }

		    // start of a row (as long as this has an id)
		    else if ("predefinedLocationReference".equals(n
			    .getLocalPart())) {
			event = xmlReader.nextEvent();
			if (event.isCharacters()) {
			    String locationId = event.asCharacters().toString()
				    .trim();
			    persister.setTableName(locationTable);
			    row = persister.getLocation(locationId);
			    persister.setTableName(journeyTable);
			    row.addKey("location_id", locationId);
			    Calendar c = javax.xml.bind.DatatypeConverter.parseDateTime(publicationTimestamp);
			    row.addKey("publication_ts", c.getTime().getTime());
			}

		    }

		    // travel time
		    else if ("travelTime".equals(n.getLocalPart())) {
			event = xmlReader.nextEvent();
			if (event.isCharacters()) {
			    String time = event.asCharacters().toString()
				    .trim();
			    BigDecimal d = new BigDecimal(time);
			    row.addAttribute("travel_time", d);
			}

		    }

		    // travel time
		    else if ("freeFlowTravelTime".equals(n.getLocalPart())) {
			event = xmlReader.nextEvent();
			if (event.isCharacters()) {
			    String time = event.asCharacters().toString()
				    .trim();
			    BigDecimal d = new BigDecimal(time);
			    row.addAttribute("freeflow_time", d);
			}

		    }

		    // travel time
		    else if ("normallyExpectedTravelTime".equals(n
			    .getLocalPart())) {
			event = xmlReader.nextEvent();
			if (event.isCharacters()) {
			    String time = event.asCharacters().toString()
				    .trim();
			    BigDecimal d = new BigDecimal(time);
			    row.addAttribute("normal_time", d);
			}

		    }

		}
		
		// end of a row ... save it
		else if (event.isEndElement()) {
		    EndElement e = event.asEndElement();
		    QName n = e.getName();
		    // check we have a valid row and not just a furniture
		    // predefinedLocation
		    if (row != null
			    && "elaboratedData".equals(n.getLocalPart())) {

			try {
			    persister.setTableName(journeyTable);
			    persister.store(row);
			    counter++; // add one to the count
			}
			catch (Exception ce) {
			    System.out.println(row.insertCql());
			}
			row = null; // reset ready for next location

		    }

		}

	    }
	    // stop the timer
	    timer = System.currentTimeMillis() - timer;
	    System.out.println("Found " + counter + " locations in " + timer
		    + " ms");

	}
	catch (XMLStreamException e) {
	    e.printStackTrace();
	} finally {
	    if (xmlReader != null)
		try {
		    xmlReader.close();
		}
		catch (XMLStreamException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	}

	return counter;
    }

}
