import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.InitializationError;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import st.EntryMap;
import st.SimpleTemplateEngine;
import st.TemplateEngine;

@RunWith(Task1_Functional.class)
@SuiteClasses({EntryMapStoreTest.class, EntryMapDeleteTest.class, 
	EntryMapUpdateTest.class, SimpleTemplateEngineTest.class})
public class Task1_Functional extends Suite{

	public Task1_Functional(Class<?> klass) throws InitializationError {
        super(klass);
    }
	
	public static class EntryMapStoreTest {
		
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
	
	public static class EntryMapDeleteTest {

		private EntryMap map;
		
		@Before
	    public void setUp() throws Exception {
	        map = new EntryMap();        
	        map.store("key1", "value1");
	        map.store("key2", "value2");
	        map.store("key3", "value3");
	    }

		@Test(expected=RuntimeException.class)
		public void TestCase1() {
			map.delete(null);
		}
		
		@Test(expected=RuntimeException.class)
		public void TestCase2() {
			map.delete("");
		}
		
		@Test
		public void TestCase3() {
			map.delete("key2");		
			assertEquals(map.getEntries().size(), 2);
			// TODO check order
		}
		
		@Test
		public void TestCase5() {
			map.delete("key4");		
			assertEquals(map.getEntries().size(), 3);
		}

	}
	
	public static class EntryMapUpdateTest {

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
	
	public static class SimpleTemplateEngineTest {
		
	    private SimpleTemplateEngine simpleEngine;
	    
	    @Before
	    public void setUp() throws Exception {
	        simpleEngine = new SimpleTemplateEngine();
	    }
	       
	    @Test
	    public void TestCase1() {
	    	String template = null;
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate(template, "", "", matchingMode);
	    	assertEquals(result, null);
	    }
	    
	    @Test
	    public void TestCase2() {
	    	String template = "";
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate(template, "", "", matchingMode);
	    	assertEquals(result, "");
	    }
	    
	    @Test
	    public void TestCase3() {
	    	String pattern = null;
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate("123", pattern, "", matchingMode);
	    	assertEquals(result, "123");
	    }
	    
	    @Test
	    public void TestCase4() {
	    	String pattern = "";
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate("123", pattern, "", matchingMode);
	    	assertEquals(result, "123");
	    }
	    
	    @Test
	    public void TestCase5() {
	    	String template = "value(0) value#(1) value##(2) value###(3) value####(4) value#####(5) value######(6)";
	    	String pattern = "value####";
	    	String value = "new";
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	String comparison = "value(0) value#(1) new(2) new#(3) new##(4) new###(5) new####(6)";
	    	assertEquals(result, comparison);
	    }
	    
	    @Test
	    public void TestCase6() {
	    	String template = "value(0) value#(1) value##(2) value###(3) value####(4) value#####(5) value######(6)";
	    	String pattern = "value###7";
	    	String value = "new";
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	assertEquals(result, template);
	    }
	    
	    @Test
	    public void TestCase7() {
	    	String template = "123";
	    	String pattern = "123";
	    	String value = null;
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	assertEquals(result, template);
	    }
	    
	    @Test
	    public void TestCase8() {
	    	String template = "123";
	    	String pattern = "123";
	    	String value = "";
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	assertEquals(result, template);
	    }
	    
	    @Test
	    public void TestCase9() {
	    	String template = "ABC0abc0ABC0ABC";
	    	String pattern = "abc#2";
	    	String value = "XYZ";
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	String comparison = "ABC0XYZ0ABC0ABC";
	    	assertEquals(result, comparison);
	    }
	    
	    @Test
	    public void TestCase10() {
	    	String template = "ABC0abc0ABC0ABC";
	    	String pattern = "abc";
	    	String value = "XYZ";
	    	int matchingMode = SimpleTemplateEngine.CASE_SENSITIVE;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	String comparison = "ABC0XYZ0ABC0ABC";
	    	assertEquals(result, comparison);
	    }
	    
	    @Test
	    public void TestCase11() {
	    	String template = "ABC0abc0ABC0ABC";
	    	String pattern = "abc";
	    	String value = "XYZ";
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	String comparison = "XYZ0XYZ0XYZ0XYZ";
	    	assertEquals(result, comparison);
	    }
	    
	    @Test
	    public void TestCase12() {
	    	String template = "ABC abc_ABC-ABC";
	    	String pattern = "abc#4";
	    	String value = "XYZ";
	    	int matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	String comparison = "ABC abc_ABC-XYZ";
	    	assertEquals(result, comparison);
	    }
	    
	    @Test
	    public void TestCase13() {
	    	String template = "ABC abc_ABC-ABC";
	    	String pattern = "ABC#2";
	    	String value = "XYZ";
	    	int matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH | SimpleTemplateEngine.CASE_SENSITIVE;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	String comparison = "ABC abc_XYZ-ABC";
	    	assertEquals(result, comparison);
	    }
	    
	    @Test
	    public void TestCase14() {
	    	String template = "ABC abc_ABC-ABC";
	    	String pattern = "abc";
	    	String value = "XYZ";
	    	int matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	String comparison = "XYZ XYZ_XYZ-XYZ";
	    	assertEquals(result, comparison);
	    }
	    
	    @Test
	    public void TestCase15() {
	    	String template = "ABC abc_ABC-ABC";
	    	String pattern = "ABC";
	    	String value = "XYZ";
	    	int matchingMode = SimpleTemplateEngine.CASE_SENSITIVE;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	String comparison = "XYZ abc_XYZ-XYZ";
	    	assertEquals(result, comparison);
	    }
	    
	    @Test
	    public void TestCase16() {
	    	String template = "ABC abc_ABC-ABC";
	    	String pattern = "ABC";
	    	String value = "XYZ";
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	String comparison = "XYZ XYZ_XYZ-XYZ";
	    	assertEquals(result, comparison);
	    }
	    
	    @Test
	    public void TestCase17() {
	    	String template = "ABC abc_ABC-ABC";
	    	String pattern = "ABC";
	    	String value = "XYZ";
	    	int matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH | SimpleTemplateEngine.CASE_SENSITIVE;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	String comparison = "XYZ abc_XYZ-XYZ";
	    	assertEquals(result, comparison);
	    }
	}
}
