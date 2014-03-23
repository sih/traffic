package eu.waldonia.labs.traffic.processors;

import javax.xml.stream.XMLEventReader;

/**
 * 
 * @author waldo
 *
 */
public abstract class AbstractProcessor {

    protected XMLEventReader xmlReader;
    protected LocationPersister persister;

    public abstract int process();
    
    public void setLocationPersister(LocationPersister persister) {
	this.persister = persister;
    }

    public void setReader(XMLEventReader reader) {
	this.xmlReader = reader;
    }

}
