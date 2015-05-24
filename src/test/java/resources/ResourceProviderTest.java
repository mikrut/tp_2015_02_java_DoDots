package resources;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceProviderTest {
    @Test
    public void testStringInit() {
        TestResource resource = (TestResource) ResourceProvider.getProvider().getResource("testresource.xml");
        assertEquals("Hello! This is a test string.", resource.getTestString());
    }

    @Test
    public void testIntInit() {
        TestResource resource = (TestResource) ResourceProvider.getProvider().getResource("testresource.xml");
        assertEquals((Integer) 42, resource.getTestInt());
    }
}