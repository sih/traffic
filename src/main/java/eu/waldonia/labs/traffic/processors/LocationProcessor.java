/**
 * 
 */
package eu.waldonia.labs.traffic.processors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import eu.waldonia.labs.traffic.domain.SingleKeyDomainObject;

/**
 * @author sid
 * 
 */
/**
 * @author sid
 * 
 */
public class LocationProcessor {

    private String fileLocation;

    public LocationProcessor() {

    }

    public void setFileLocation(String fileLocation) {
	this.fileLocation = fileLocation;
    }

    /**
     * @return
     */
    public int process() {
	if (null == this.fileLocation)
	    throw new IllegalStateException("You need to supply a file location");
	int counter = 0;
	long time = System.currentTimeMillis();
	XMLInputFactory f = XMLInputFactory.newInstance();
	XMLEventReader r = null;
	try {
	    r = f.createXMLEventReader(new FileInputStream(new File(fileLocation)));
	    while (r.hasNext()) {
		XMLEvent event = r.nextEvent();		
		SingleKeyDomainObject d = parseEvent(event);
		counter++;
	    }
	    time = System.currentTimeMillis() - time;
	    System.out.println("Found " + counter + " locations in " + time
		    + " ms");

	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (XMLStreamException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    if (r != null)
		try {
		    r.close();
		} catch (XMLStreamException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	}

	return counter;
    }

    private SingleKeyDomainObject parseEvent(XMLEvent event) {
	SingleKeyDomainObject row = new SingleKeyDomainObject();
	if (event.isStartElement()) {
	    StartElement s = event.asStartElement();
	    QName n = s.getName();
	    if ("predefinedLocation".equals(n.getLocalPart())) {
		Attribute a = s.getAttributeByName(new QName("id"));
		if (a != null) {
		    row.setKey(a.getValue());
		}
	    }
	}
	return row;
    }

}
