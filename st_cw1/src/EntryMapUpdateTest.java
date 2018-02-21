import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import st.EntryMap;
import st.TemplateEngine;

public class EntryMapUpdateTest {

	private EntryMap map;
	private TemplateEngine engine;
	
	@Before
    public void setUp() throws Exception {
        map = new EntryMap();        
        map.store("key1", "value1");
        map.store("key2", "value2");
        map.store("key3", "value3");
        
        engine = new TemplateEngine();
    }
	
	@Test(expected=RuntimeException.class)
	public void TestCase1() {
		map.update(null, "");
	}
	
	@Test(expected=RuntimeException.class)
	public void TestCase2() {
		map.update("", "");
	}
	
	@Test(expected=RuntimeException.class)
	public void TestCase3() {
		map.update("key", null);
	}
	
	@Test
	public void TestCase4() {
		map.update("key1", "nValue1");
		
		Integer matchingMode = TemplateEngine.DEFAULT;
		String result = engine.evaluate("Value is ${key1}", map, matchingMode);
		assertEquals("Value is nValue1", result);
		
		//TODO check order
	}
	
	@Test
	public void TestCase5() {
		map.update("key4", "nValue4");
		Integer matchingMode = TemplateEngine.DEFAULT;
		String result = engine.evaluate("Value is ${key4}", map, matchingMode);
		assertEquals("Value is ${key4}", result);		
	}


}
