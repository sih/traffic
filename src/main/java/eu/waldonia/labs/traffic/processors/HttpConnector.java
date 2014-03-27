package eu.waldonia.labs.traffic.processors;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 * @author waldo
 *
 */
public class HttpConnector {
    
    private CloseableHttpClient client;
    
    /**
     * 
     */
    public HttpConnector() {
	client = HttpClients.createDefault();
    }
    
    
    /**
     * @param url
     * @return {status => body}
     */
    public Map<Integer,String> get(final String url) {
	Map<Integer,String> results = new HashMap<Integer,String>();
	
	if (null == url) return results;
	
	HttpGet get = new HttpGet(url);
	CloseableHttpResponse response = null;
	try {
	    response = client.execute(get);
	    Integer status = response.getStatusLine().getStatusCode();
	    HttpEntity entity = response.getEntity();
	    results.put(status, EntityUtils.toString(entity));
	}
	catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	finally {
	    try {
		if (response != null) response.close();		
	    }
	    catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	
	return results;
    }
    
    public InputStream getAsStream(String url) {

	if (null == url) return null;
	
	InputStream stream = null;
	
	HttpGet get = new HttpGet(url);
	
	CloseableHttpResponse response = null;
	try {
	    response = client.execute(get);
	    HttpEntity entity = response.getEntity();
	    stream = entity.getContent();
	}
	catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	return stream;
	
    }
    
    public void close() {
	try {
	    client.close();
	}
	catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    
}
