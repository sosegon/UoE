import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.InitializationError;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runners.model.RunnerBuilder;

import st.EntryMap;
import st.SimpleTemplateEngine;
import st.TemplateEngine;

@RunWith(Task1_Functional.class)
@SuiteClasses({Task1_Functional.EntryMapStoreTest.class, Task1_Functional.EntryMapDeleteTest.class,
	Task1_Functional.EntryMapUpdateTest.class, Task1_Functional.SimpleTemplateEngineTest.class})
public class Task1_Functional extends Suite{
	
	public Task1_Functional(Class<?> klass, RunnerBuilder builder) throws org.junit.runners.model.InitializationError {
		super(klass, builder);
		// TODO Auto-generated constructor stub
	}

	public static class EntryMapStoreTest {
		
		private EntryMap map;
		private TemplateEngine engine;
		
		@Before
	    public void setUp() throws Exception {
	        map = new EntryMap();
	        engine = new TemplateEngine();
	    }

		/*
		 * Template is null
		 */
		@Test(expected=RuntimeException.class)
		public void TestCase1() {
			map.store(null, "");		
		}
		
		/*
		 * Template is empty
		 */
		@Test(expected=RuntimeException.class)
		public void TestCase2() {
			map.store("", "");
		}
		
		/*
		 * Value is null
		 */
		@Test(expected=RuntimeException.class)
		public void TestCase3() {
			map.store("key", null);
		}
		
		/*
		 * An existing template is not stored again
		 */
		@Test
		public void TestCase4() {
			map.store("key", "@\nabc 123");
			map.store("key", "@\nabc 1234");
			Integer matchingMode = TemplateEngine.DEFAULT;
			String result = engine.evaluate("Value is ${key}", map, matchingMode);
			assertEquals("Value is @\nabc 123", result);
		}
		
		/*
		 * A new template is stored
		 */
		@Test
		public void TestCase5() {
			map.store("key1", "@\nabc 123");
			map.store("key2", "@\nabc 1234");
			Integer matchingMode = TemplateEngine.DEFAULT;
			String result = engine.evaluate("Value is ${key2}", map, matchingMode);
			assertEquals("Value is @\nabc 1234", result);
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

		/*
		 * Template is null
		 */
		@Test(expected=RuntimeException.class)
		public void TestCase1() {
			map.delete(null);
		}
		
		/*
		 * Template is empty
		 */
		@Test(expected=RuntimeException.class)
		public void TestCase2() {
			map.delete("");
		}
		
		/*
		 * An existing entry is deleted
		 */
		@Test
		public void TestCase3() {
			map.delete("key2");		
			assertEquals(map.getEntries().size(), 2);
		}
		
		/*
		 * A non-existing entry is not deleted
		 */
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
		
		/*
		 * Template is null
		 */
		@Test(expected=RuntimeException.class)
		public void TestCase1() {
			map.update(null, "");
		}
		
		/*
		 * Template is empty
		 */
		@Test(expected=RuntimeException.class)
		public void TestCase2() {
			map.update("", "");
		}
		
		/*
		 * Value is null
		 */
		@Test(expected=RuntimeException.class)
		public void TestCase3() {
			map.update("key", null);
		}
		
		/*
		 * An existing entry is updated
		 */
		@Test
		public void TestCase4() {
			map.update("key1", "nValue1");
			
			Integer matchingMode = TemplateEngine.DEFAULT;
			String result = engine.evaluate("Value is ${key1}", map, matchingMode);
			assertEquals("Value is nValue1", result);
		}
		
		/*
		 * A non-existing entry is not updated
		 */
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
	       
	    /*
	     * Template is null
	     */
	    @Test
	    public void TestCase1() {
	    	String template = null;
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate(template, "", "", matchingMode);
	    	assertEquals(result, null);
	    }
	    
	    /*
	     * Template is empty
	     */
	    @Test
	    public void TestCase2() {
	    	String template = "";
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate(template, "", "", matchingMode);
	    	assertEquals(result, "");
	    }
	    
	    /*
	     * Pattern is null
	     */
	    @Test
	    public void TestCase3() {
	    	String pattern = null;
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate("123", pattern, "", matchingMode);
	    	assertEquals(result, "123");
	    }
	    
	    /*
	     * Pattern is empty
	     */
	    @Test
	    public void TestCase4() {
	    	String pattern = "";
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate("123", pattern, "", matchingMode);
	    	assertEquals(result, "123");
	    }
	    
	    /*
	     * Pattern has even number of #
	     */
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
	    
	    /*
	     * The pattern to replace has a fewer number of occurrences than the specified
	     */
	    @Test
	    public void TestCase6() {
	    	String template = "value(0) value#(1) value##(2) value###(3) value####(4) value#####(5) value######(6)";
	    	String pattern = "value###7";
	    	String value = "new";
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	assertEquals(result, template);
	    }
	    
	    /*
	     * Value is null
	     */
	    @Test
	    public void TestCase7() {
	    	String template = "123";
	    	String pattern = "123";
	    	String value = null;
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	assertEquals(result, template);
	    }
	    
	    /*
	     * Value is empty
	     */
	    @Test
	    public void TestCase8() {
	    	String template = "123";
	    	String pattern = "123";
	    	String value = "";
	    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	assertEquals(result, template);
	    }
	    
	    /*
	     * The pattern to replace has a higher number of occurrences than the specified
	     */
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
	    
	    /*
	     * The pattern to replace is case sensitive
	     */
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
	    
	    /*
	     * The pattern to replace is not case sensitive
	     */
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
	    
	    /*
	     * The pattern to replace looks for whole words
	     */
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
	    
	    /*
	     * The pattern to replace looks for whole words and is case sensitive for a single match
	     */
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
	    
	    /*
	     * The pattern to replace looks for whole words and is not case sensitive
	     */
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
	    
	    /*
	     * The pattern to replace is case sensitive in a string with spaces
	     */
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
	    
	    /*
	     * The pattern to replace is not case sensitive in a string with spaces
	     */
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
	    
	    /*
	     * The pattern to replace looks for whole words and is case sensitive for all matches
	     */
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
