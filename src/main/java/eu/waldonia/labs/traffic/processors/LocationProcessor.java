package eu.waldonia.labs.traffic.processors;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import eu.waldonia.labs.traffic.domain.GenericDomainObject;

/**
 * @author sid
 * 
 */
public class LocationProcessor {

    private XMLEventReader xmlReader;
    private LocationPersister persister;

    public void setLocationPersister(LocationPersister persister) {
	this.persister = persister;
    }

    public void setReader(XMLEventReader reader) {
	this.xmlReader = reader;
    }

    /**
     * This loops through event in the XML stream 
     * @return The number of traffic location rows found and stored
     */
    public int process() {
	if (null == this.xmlReader)
	    throw new IllegalStateException(
		    "You need to supply an XMLReader");
	int counter = 0;
	long time = System.currentTimeMillis();
	try {

	    GenericDomainObject row = null;
	    boolean processingToElement = true;
	    // loop through each event
	    while (xmlReader.hasNext()) {
		XMLEvent event = xmlReader.nextEvent();
		if (event.isStartElement()) {
		    StartElement s = event.asStartElement();
		    QName n = s.getName();
		    if ("predefinedLocation".equals(n.getLocalPart())) {
			Attribute a = s.getAttributeByName(new QName("id"));
			// don't do anything for other predefinedLocations only
			// the pk
			if (a != null) {
			    row = new GenericDomainObject();
			    Map<String, String> keys = new HashMap<String, String>();
			    keys.put("location_id", a.getValue());
			    row.setKeys(keys);
			}
		    }
		    else if ("predefinedLocationName".equals(n.getLocalPart())) {
			while ((event = xmlReader.nextEvent()) != null) {
			    if (event.isStartElement()) {
				s = event.asStartElement();
				if ("value".equals(s.getName().getLocalPart())) {
				    break;
				}
			    }
			}
			event = xmlReader.nextEvent();
			if (event.isCharacters()) {
			    row.addAttribute("name", event.asCharacters()
				    .toString().trim());
			}

		    }
		    else if ("tpeglinearLocation".equals(n.getLocalPart())) {
			// direction
			while ((event = xmlReader.nextEvent()) != null) {
			    if (event.isStartElement()) {
				s = event.asStartElement();
				if ("tpegDirection".equals(s.getName()
					.getLocalPart())) {
				    break;
				}
			    }
			}
			event = xmlReader.nextEvent();
			if (event.isCharacters()) {
			    row.addAttribute("direction", event.asCharacters()
				    .toString().trim());
			}

			// location type
			while ((event = xmlReader.nextEvent()) != null) {
			    if (event.isStartElement()) {
				s = event.asStartElement();
				if ("tpegLocationType".equals(s.getName()
					.getLocalPart())) {
				    break;
				}
			    }
			}
			event = xmlReader.nextEvent();
			if (event.isCharacters()) {
			    row.addAttribute("location_type", event
				    .asCharacters().toString().trim());
			}
		    }

		    else if ("to".equals(n.getLocalPart())) {
			processingToElement = true;
		    }
		    else if ("from".equals(n.getLocalPart())) {
			processingToElement = false;
		    }
		    // latitude (will process both to and from location)
		    else if ("latitude".equals(n.getLocalPart())) {
			String lat = null;
			// next event will be the string value of the lat
			event = xmlReader.nextEvent();
			if (event.isCharacters()) {
			    lat = event.asCharacters().getData();
			}
			if (processingToElement)
			    row.addAttribute("to_latitude", lat);
			else
			    row.addAttribute("from_latitude", lat);
		    }
		    // longitude (will process both to and from location)
		    else if ("longitude".equals(n.getLocalPart())) {
			String lng = null;
			// next event will be the string value of the lat
			event = xmlReader.nextEvent();
			if (event.isCharacters()) {
			    lng = event.asCharacters().getData();
			}
			if (processingToElement)
			    row.addAttribute("to_longitude", lng);
			else
			    row.addAttribute("from_longitude", lng);
		    }

		    // location type and names of to and from locations
		    else if ("ilc".equals(n.getLocalPart())) {
			// location type
			while ((event = xmlReader.nextEvent()) != null) {
			    if (event.isStartElement()) {
				s = event.asStartElement();
				if ("value".equals(s.getName().getLocalPart())) {
				    break;
				}
			    }
			}
			// get the location values out
			event = xmlReader.nextEvent();
			if (event.isCharacters()) {
			    String value = event.asCharacters().toString()
				    .trim();
			    String prefix = null;
			    if (processingToElement)
				prefix = "to_";
			    else
				prefix = "from_";
			    if (row.getAttributes().containsKey(
				    prefix + "first_loc")) {
				row.addAttribute(prefix + "second_loc", value);
			    }
			    else {
				row.addAttribute(prefix + "first_loc", value);
			    }
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
			    && "predefinedLocation".equals(n.getLocalPart())) {
			persister.store(row);
			row = null; // reset ready for next location
			counter++; // add one to the count
		    }

		}

	    }
	    time = System.currentTimeMillis() - time;
	    System.out.println("Found " + counter + " locations in " + time
		    + " ms");

	}
	catch (XMLStreamException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} 
	finally {
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
