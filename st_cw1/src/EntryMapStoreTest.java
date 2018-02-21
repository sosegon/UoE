import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import st.EntryMap;
import st.TemplateEngine;

public class EntryMapStoreTest {
	
	private EntryMap map;
	private TemplateEngine engine;
	
	@Before
    public void setUp() throws Exception {
        map = new EntryMap();
        engine = new TemplateEngine();
    }

	@Test(expected=RuntimeException.class)
	public void TestCase1() {
		map.store(null, "");		
	}
	
	@Test(expected=RuntimeException.class)
	public void TestCase2() {
		map.store("", "");
	}
	
	@Test(expected=RuntimeException.class)
	public void TestCase3() {
		map.store("key", null);
	}
	
	@Test
	public void TestCase4() {
		map.store("key", "@\nabc 123");
		map.store("key", "@\nabc 1234");
		Integer matchingMode = TemplateEngine.DEFAULT;
		String result = engine.evaluate("Value is ${key}", map, matchingMode);
		assertEquals("Value is @\nabc 123", result);
	}
	
	@Test
	public void TestCase5() {
		map.store("key1", "@\nabc 123");
		map.store("key2", "@\nabc 1234");
		Integer matchingMode = TemplateEngine.DEFAULT;
		String result = engine.evaluate("Value is ${key2}", map, matchingMode);
		assertEquals("Value is @\nabc 1234", result);
		
		//TODO check order
	}
}
