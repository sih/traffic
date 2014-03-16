package eu.waldonia.labs.traffic.domain;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class SingleKeyDomainObjectTest {

    private SingleKeyDomainObject domain;
    
    @Before
    public void setUp() {
	domain = new SingleKeyDomainObject();
    }
    
    @Test
    public void testNew() {
	assertNull(domain.getKey());
	assertNotNull(domain.getAttributes());
    }

    
    @Test
    public void testAddAttribute() {
	assertTrue(domain.getAttributes().isEmpty());
	domain.addAttribute("greeting", "Hello");
	assertFalse(domain.getAttributes().isEmpty());
	assertEquals(1,domain.getAttributes().size());
	assertTrue(domain.getAttributes().containsKey("greeting"));
	assertEquals("Hello", domain.getAttributes().get("greeting"));
    }
}
