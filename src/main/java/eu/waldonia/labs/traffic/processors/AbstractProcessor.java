package eu.waldonia.labs.traffic.processors;

import javax.xml.stream.XMLEventReader;

/**
 * 
 * @author waldo
 *
 */
public abstract class AbstractProcessor {

    protected XMLEventReader xmlReader;
    protected TrafficPersister persister;
    protected String tableName;

    public abstract int process();
    
    public void setLocationPersister(TrafficPersister persister) {
	this.persister = persister;
    }

    public void setReader(XMLEventReader reader) {
	this.xmlReader = reader;
    }

    public void setTableName(String tableName) {
	this.tableName = tableName;
    }
    
}
