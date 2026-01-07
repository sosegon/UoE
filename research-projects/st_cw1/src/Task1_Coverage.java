import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runners.model.RunnerBuilder;

import st.EntryMap;
import st.SimpleTemplateEngine;
import st.TemplateEngine;

@RunWith(Task1_Coverage.class)
@SuiteClasses({Task1_Coverage.EntryMapStoreTest.class, Task1_Coverage.EntryMapDeleteTest.class,
	Task1_Coverage.EntryMapUpdateTest.class, Task1_Coverage.SimpleTemplateEngineTest.class,
	Task1_Coverage.TemplateEngineTest.class,
	Task1_Coverage.SimpleTemplateEngineCoverageTest.class})
public class Task1_Coverage extends Suite{
	
	public Task1_Coverage(Class<?> klass, RunnerBuilder builder) throws org.junit.runners.model.InitializationError {
		super(klass, builder);
		// TODO Auto-generated constructor stub
	}
	
	public static class SimpleTemplateEngineCoverageTest {
		
		private SimpleTemplateEngine simpleEngine;
		
		@Before
	    public void setUp() throws Exception {
			simpleEngine = new SimpleTemplateEngine();
	    }

		/*
		 * Invalid matching mode -1
		 */
		@Test
	    public void TestCase9_1() {
	    	String template = "ABC0abc0ABC0ABC";
	    	String pattern = "abc#2";
	    	String value = "XYZ";
	    	int matchingMode = -1;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	String comparison = "ABC0XYZ0ABC0ABC";
	    	assertEquals(result, comparison);
	    }
		
		/*
		 * Invalid matching mode null
		 */
		@Test
	    public void TestCase9_2() {
	    	String template = "ABC0abc0ABC0ABC";
	    	String pattern = "abc#2";
	    	String value = "XYZ";
	    	String result = simpleEngine.evaluate(template, pattern, value, null);
	    	String comparison = "ABC0XYZ0ABC0ABC";
	    	assertEquals(result, comparison);
	    }
		
		/*
		 * Invalid matching mode >3
		 */
		@Test
	    public void TestCase9_3() {
	    	String template = "ABC0abc0ABC0ABC";
	    	String pattern = "abc#2";
	    	String value = "XYZ";
	    	int matchingMode = 4;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	String comparison = "ABC0XYZ0ABC0ABC";
	    	assertEquals(result, comparison);
	    }
		/*
		 * Does not increase coverage, it just test the invalid mode 3, which is not covered in the code.
		 */
		@Test
	    public void TestCase9_4() {
	    	String template = "ABC0abc0ABC0ABC";
	    	String pattern = "abc";
	    	String value = "XYZ";
	    	int matchingMode = 3;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	assertEquals(result, template);
	    }
		
		/*
		 * Invalid suffix letter
		 */
		@Test
	    public void TestCase9_5() {
	    	String template = "ABC0abc0ABC0ABC";
	    	String pattern = "abc#a";
	    	String value = "XYZ";
	    	int matchingMode = 4;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	assertEquals(result, template);
	    }
		
		/*
		 * Invalid suffix number and letter
		 */
		@Test
	    public void TestCase9_6() {
	    	String template = "ABC0abc0ABC0ABC";
	    	String pattern = "abc#22a";
	    	String value = "XYZ";
	    	int matchingMode = 4;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	assertEquals(result, template);
	    }
		
		/*
		 * Invalid suffix no number
		 */
		@Test
	    public void TestCase9_7() {
	    	String template = "ABC0abc0ABC0ABC";
	    	String pattern = "abc#";
	    	String value = "XYZ";
	    	int matchingMode = 4;
	    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
	    	assertEquals(result, template);
	    }

	    /*
			Test cases of Antonios Valais
		 */
	    @Test
		public void TestCase_Coverage_10() {
			String originalText = "A"+Character.MIN_VALUE+"AA";
			String expected = "ABC"+Character.MIN_VALUE+"AA";
			String pattern = "A";
			String result = simpleEngine.evaluate(originalText, pattern, "ABC", 2);
			assertEquals(result, expected);
		}
	}

	public static class TemplateEngineCoverageTest {
		/*
			Test cases of Antonios Valais
		 */
		private TemplateEngine engine;
	    private EntryMap map;

	    @Before
	    public void setUp() throws Exception {
	        engine = new TemplateEngine();
	        map = new EntryMap();
	    }

	    @Test
	    public void TestCase_Coverage_1()
	    {
	    	map.store("name", "Antonios");
			
			Integer integer = new Integer(2);
			
			for (Object entry : map.getEntries()) {
				assertFalse(entry.equals(null));
				assertTrue(entry.equals(entry));
				assertFalse(entry.equals(integer));
			}
	    }

		@Test
	    public void TestTemplateStartsWithEntry() {
	        map.store("name", "Antonios");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("${name} is my name", map, matchingMode);
	        assertEquals("Antonios is my name", result);
	    }
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

	public static class TemplateEngineTest {
		/*
			Test cases of Antonios Valais
		 */
		private TemplateEngine engine;
	    private EntryMap map;

	    @Before
	    public void setUp() throws Exception {
	        engine = new TemplateEngine();
	        map = new EntryMap();
	    }

		@Test
	    public void TestNullTemplate() {
	        map.store("name", "Antonios");
	        map.store("surname", "Valais");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate(null, map, matchingMode);
	        assertNull(result);
	    }
	    
	    @Test
	    public void TestEmptyTemplate() {
	        map.store("name", "Antonios");
	        map.store("surname", "Valais");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("", map, matchingMode);
	        assertEquals("", result);
	    }
	    
	    @Test
	    public void TestTemplateWithOnlySpaces() {
	        map.store("name", "Antonios");
	        map.store("surname", "Valais");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("  ", map, matchingMode);
	        assertEquals("  ", result);
	    }
	    
	    @Test
	    public void TestNullEntryMap() {
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("My name is ${name}", null, matchingMode);
	        assertEquals("My name is ${name}", result);
	    }
	    
	    @Test
	    public void TestEmptyEntryMap() {
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("My name is ${name}", map, matchingMode);
	        assertEquals("My name is ${name}", result);
	    }
	    
	    @Test
	    public void TestNullMatchingMode() {
	        map.store("name", "Antonios");
	        Integer matchingMode = null;
	        String result = engine.evaluate("Hello my name is ${name}", map, matchingMode);
	        assertEquals("Hello my name is Antonios", result);
	    }
	    
	    @Test
	    public void TestOutOfBoundMatchingMode_NEGATIVE() {
	        map.store("name", "Antonios");
	        Integer matchingMode = -1;
	        String result = engine.evaluate("Hello my name is ${name}", map, matchingMode);
	        assertEquals("Hello my name is Antonios", result);
	    }
	    
	    @Test
	    public void TestOutOfBoundMatchingMode_MIN_NEGATIVE() {
	        map.store("name", "Antonios");
	        Integer matchingMode = Integer.MIN_VALUE;
	        String result = engine.evaluate("Hello my name is ${name}", map, matchingMode);
	        assertEquals("Hello my name is Antonios", result);
	    }
	    
	    @Test
	    public void TestOutOfBoundMatchingMode_GreaterThanSeven() {
	        map.store("name", "Antonios");
	        Integer matchingMode = 8;
	        String result = engine.evaluate("Hello my name is ${name}", map, matchingMode);
	        assertEquals("Hello my name is Antonios", result);
	    }
	    
	    @Test
	    public void TestOutOfBoundMatchingMode_MAX() {
	        map.store("name", "Antonios");
	        Integer matchingMode = Integer.MAX_VALUE;
	        String result = engine.evaluate("Hello my name is ${name}", map, matchingMode);
	        assertEquals("Hello my name is Antonios", result);
	    }
	    
	    @Test
	    public void TestOneWordTemplateWithOneEntryMatching_Default() {
	        map.store("name", "Antonios");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("Hello my name is ${name}", map, matchingMode);
	        assertEquals("Hello my name is Antonios", result);
	    }
	    
	    @Test
	    public void TestOneWordTemplateWithOneEntryMatching_CASESENSITIVE_NOT_MATCHING() {
	        map.store("Name", "Antonios");
	        Integer matchingMode = TemplateEngine.CASE_SENSITIVE;
	        String result = engine.evaluate("Hello my name is ${name}", map, matchingMode);
	        assertEquals("Hello my name is ${name}", result);
	    }
	    
	    @Test
	    public void TestOneWordTemplateWithOneEntryMatching_CASESENSITIVE_MATCHING() {
	        map.store("Name", "Antonios");
	        Integer matchingMode = TemplateEngine.CASE_SENSITIVE;
	        String result = engine.evaluate("Hello my name is ${Name}", map, matchingMode);
	        assertEquals("Hello my name is Antonios", result);
	    }
	    
	    
	    @Test
	    public void TestTwoWordTemplateWithOneEntryMatching_DEFAULT() {
	        map.store("My Name", "Antonios");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("Hello my name is ${My Name}", map, matchingMode);
	        assertEquals("Hello my name is Antonios", result);
	    }
	    
	    @Test
	    public void TestTwoWordTemplateWithOneEntryMatching_CASESENSITIVE_NOT_MATCHING() {
	        map.store("My name", "Antonios");
	        Integer matchingMode = TemplateEngine.CASE_SENSITIVE;
	        String result = engine.evaluate("Hello my name is ${My Name}", map, matchingMode);
	        assertEquals("Hello my name is ${My Name}", result);
	    }
	    
	    @Test
	    public void TestTwoWordTemplateWithOneEntryMatching_CASESENSITIVE_MATCHING() {
	        map.store("My Name", "Antonios");
	        Integer matchingMode = TemplateEngine.CASE_SENSITIVE;
	        String result = engine.evaluate("Hello my name is ${My Name}", map, matchingMode);
	        assertEquals("Hello my name is Antonios", result);
	    }
	    
	    @Test
	    public void TestTwoWordTemplateWithOneEntryMatching_CASESENSITIVE_DELETEUNMATCHED_NOT_MATCHING() {
	        map.store("My name", "Antonios");
	        Integer matchingMode = TemplateEngine.CASE_SENSITIVE | TemplateEngine.DELETE_UNMATCHED ;
	        String result = engine.evaluate("Hello my name is ${My Name}", map, matchingMode);
	        assertEquals("Hello my name is ", result);
	    }
	    
	    @Test
	    public void TestTwoWordTemplateWithOneEntryMatching_CASESENSITIVE_DELETEUNMATCHED_MATCHING() {
	        map.store("My Name", "Antonios");
	        Integer matchingMode = TemplateEngine.CASE_SENSITIVE | TemplateEngine.DELETE_UNMATCHED ;
	        String result = engine.evaluate("Hello my name is ${My Name}", map, matchingMode);
	        assertEquals("Hello my name is Antonios", result);
	    }
	    
	    @Test
	    public void TestTwoWordTemplateWithOneEntryMatching_BLUR_CASESENSITIVE_NOT_MATCHING() {
	        map.store("Myname", "Antonios");
	        Integer matchingMode = TemplateEngine.BLUR_SEARCH | TemplateEngine.CASE_SENSITIVE;
	        String result = engine.evaluate("Hello my name is ${My Name}", map, matchingMode);
	        assertEquals("Hello my name is ${My Name}", result);
	    }
	    
	    @Test
	    public void TestTwoWordTemplateWithOneEntryMatching_BLUR_CASESENSITIVE_MATCHING() {
	        map.store("MyName", "Antonios");
	        Integer matchingMode = TemplateEngine.BLUR_SEARCH | TemplateEngine.CASE_SENSITIVE;
	        String result = engine.evaluate("Hello my name is ${My Name}", map, matchingMode);
	        assertEquals("Hello my name is Antonios", result);
	    }
	    
	    @Test
	    public void TestTwoWordTemplateWithOneEntryMatching_BLUR_CASESENSITIVE_DELETEUNMATCHED_NOT_MATCHING() {
	        map.store("Myname", "Antonios");
	        Integer matchingMode = TemplateEngine.BLUR_SEARCH | TemplateEngine.CASE_SENSITIVE | TemplateEngine.DELETE_UNMATCHED ;
	        String result = engine.evaluate("Hello my name is ${My Name}", map, matchingMode);
	        assertEquals("Hello my name is ", result);
	    }
	    
	    @Test
	    public void TestTwoWordTemplateWithOneEntryMatching_BLUR_CASESENSITIVE_DELETEUNMATCHED_MATCHING() {
	        map.store("MyName", "Antonios");
	        Integer matchingMode = TemplateEngine.BLUR_SEARCH | TemplateEngine.CASE_SENSITIVE | TemplateEngine.DELETE_UNMATCHED ;
	        String result = engine.evaluate("Hello my name is ${My Name}", map, matchingMode);
	        assertEquals("Hello my name is Antonios", result);
	    }
	    
	    @Test
	    public void TestTwoWordTemplateWithSpacesOneEntryMatching_CASESENSITIVE_MATCHING() {
	        map.store("My   Name", "Antonios");
	        Integer matchingMode = TemplateEngine.CASE_SENSITIVE;
	        String result = engine.evaluate("Hello my name is ${My   Name}", map, matchingMode);
	        assertEquals("Hello my name is Antonios", result);
	    }
	    
	    @Test
	    public void TestTwoWordTemplateWithSpacesOneEntryMatching_BLUR_MATCHING() {
	        map.store("My   name", "Antonios");
	        Integer matchingMode = TemplateEngine.BLUR_SEARCH;
	        String result = engine.evaluate("Hello my name is ${MyName}", map, matchingMode);
	        assertEquals("Hello my name is Antonios", result);
	    }
	    
	    @Test
	    public void TestTwoWordTemplateWithSpacesOneEntryMatching_BLUR_CASESENSITIVE_MATCHING() {
	        map.store("My   Name", "Antonios");
	        Integer matchingMode = TemplateEngine.BLUR_SEARCH | TemplateEngine.CASE_SENSITIVE;
	        String result = engine.evaluate("Hello my name is ${MyName}", map, matchingMode);
	        assertEquals("Hello my name is Antonios", result);
	    }
	    
	    @Test
	    public void TestTwoWordTemplateWithSpacesOneEntryMatching_BLUR_CASESENSITIVE_NOT_MATCHING() {
	        map.store("My   name", "Antonios");
	        Integer matchingMode = TemplateEngine.BLUR_SEARCH | TemplateEngine.CASE_SENSITIVE;
	        String result = engine.evaluate("Hello my name is ${MyName}", map, matchingMode);
	        assertEquals("Hello my name is ${MyName}", result);
	    }
	    
	    @Test
	    public void TestNestedTemplate() {
	        map.store("name Valais", "Antonios Valais");
	        map.store("surname", "Valais");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("Hello my name is ${name ${Surname}}", map, matchingMode);
	        assertEquals("Hello my name is Antonios Valais", result);
	    }
	    
	    @Test
	    public void TestLeftBrokenTemplateBoundary() {
	        map.store("name", "Antonios");
	        map.store("surname", "Valais");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("Hello my name ${ is ${name} ${surname}", map, matchingMode);
	        assertEquals("Hello my name ${ is Antonios Valais", result);
	    }
	    
	    @Test
	    public void TestLeftBrokenTemplateBoundary_OnlyDollar() {
	        map.store("name", "Antonios");
	        map.store("surname", "Valais");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("Hello my name $ is ${name} ${surname}", map, matchingMode);
	        assertEquals("Hello my name $ is Antonios Valais", result);
	    }
	    
	    @Test
	    public void TestLeftBrokenTemplateBoundary_OnlyBracket() {
	        map.store("name", "Antonios");
	        map.store("surname", "Valais");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("Hello my name { is ${name} ${surname}", map, matchingMode);
	        assertEquals("Hello my name { is Antonios Valais", result);
	    }
	    
	    @Test
	    public void TestRightBrokenTemplateBoundary() {
	        map.store("name", "Antonios");
	        map.store("surname", "Valais");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("Hello my name } is ${name} ${surname}", map, matchingMode);
	        assertEquals("Hello my name } is Antonios Valais", result);
	    }
	    
	    @Test
	    public void TestEmptyTemplateBoundary_KeepUnmatched() {
	        map.store("name", "Antonios");
	        map.store("surname", "Valais");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("Hello my name ${} is ${name} ${surname}", map, matchingMode);
	        assertEquals("Hello my name ${} is Antonios Valais", result);
	    }
	    
	    @Test
	    public void TestEmptyTemplateBoundary_DeleteUnmatched() {
	        map.store("name", "Antonios");
	        map.store("surname", "Valais");
	        Integer matchingMode = TemplateEngine.DEFAULT | TemplateEngine.DELETE_UNMATCHED;
	        String result = engine.evaluate("Hello my name ${}is ${name} ${surname}", map, matchingMode);
	        assertEquals("Hello my name is Antonios Valais", result);
	    }
	    
	    @Test
	    public void TestDoubleTemplateBoundary() {
	        map.store("name", "Antonios");
	        map.store("surname", "Valais");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("Hello my name is ${${name}} ${surname}", map, matchingMode);
	        assertEquals("Hello my name is ${Antonios} Valais", result);
	    }
	    
	    @Test
	    public void TestTemplateThatCreatesItsOwnTemplate() {
	        map.store("name", "${name}");
	        map.store("surname", "Valais");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("Hello my name is ${name} ${surname}", map, matchingMode);
	        assertEquals("Hello my name is ${name} Valais", result);
	    }
	    
	    @Test
	    public void TestTemplatesLenghtArePlacedInAscedingOrder_testA() {
	        map.store("name", "Antonios");
	        map.store("surname", "Valais");
	        map.store("age", "29");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("Hello my name is ${surname} ${name}, I am ${age} years old", map, matchingMode);
	        assertEquals("Hello my name is Valais Antonios, I am 29 years old", result);
	    }
	    
	    @Test
	    public void TestTemplatesLenghtArePlacedInAscedingOrder_testB_DEFAULT() {
	        map.store("name", "Antonios");
	        map.store("surname", "Valais");
	        map.store("name ${surname}", "Captain America");
	        map.store("name Valais", "Batman");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("Hello my name is ${name ${surname}}", map, matchingMode);
	        assertEquals("Hello my name is Batman", result);
	    }
	    
	    @Test
	    public void TestTemplatesLenghtArePlacedInAscedingOrder_testB_DELETE_UNMATCHED() {
	        map.store("name", "Antonios");
	        map.store("surname", "Valais");
	        map.store("name ${surname}", "Captain America");
	        map.store("name Valais", "Batman");
	        Integer matchingMode = TemplateEngine.DELETE_UNMATCHED;
	        String result = engine.evaluate("Hello my name is ${name ${delete}${surname}}", map, matchingMode);
	        assertEquals("Hello my name is Batman", result);
	    }
	    
	    @Test
	    public void TestTemplatesLenghtArePlacedInAscedingOrder_testB_DELETE_UNMATCHED_CASE_SENSITIVE() {
	        map.store("name", "Antonios");
	        map.store("surname", "Valais");
	        map.store("name ${surname}", "Captain America");
	        map.store("name VaLais", "Batman");
	        Integer matchingMode = TemplateEngine.DELETE_UNMATCHED | TemplateEngine.CASE_SENSITIVE;
	        String result = engine.evaluate("Hello my name is ${name ${delete}${surname}}", map, matchingMode);
	        assertEquals("Hello my name is ", result);
	    }
	    
	    @Test
	    public void TestTemplatesLenghtArePlacedInAscedingOrder_testB_DELETE_UNMATCHED_CASE_SENSITIVE_BLUR_SEARCH() {
	        map.store("name", "Antonios");
	        map.store("surname", "Valais");
	        map.store("name ${surname}", "Captain America");
	        map.store("name VaLais", "Batman");
	        Integer matchingMode = TemplateEngine.DELETE_UNMATCHED | TemplateEngine.CASE_SENSITIVE | TemplateEngine.BLUR_SEARCH;
	        String result = engine.evaluate("Hello my name is ${name${delete}${surname}}", map, matchingMode);
	        assertEquals("Hello my name is ", result);
	    }
	    
	    @Test
	    public void TestTemplatesLenghtArePlacedInAscedingOrder_TwoWithSameLength() {
	        map.store("name", "Antonios");
	        map.store("surname", "Valais");
	        map.store("age", "29");
	        map.store("eat", "Bananas");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("Hello my name is ${surname} ${name}, I am ${age} years old and I like ${eat}", map, matchingMode);
	        assertEquals("Hello my name is Valais Antonios, I am 29 years old and I like Bananas", result);
	    }
	    
	    @Test
	    public void TestTemplateWithATemplateOccurTwice() {
	        map.store("name", "Antonios");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("Hello my name is ${name}, ${name} is my name", map, matchingMode);
	        assertEquals("Hello my name is Antonios, Antonios is my name", result);
	    }
	    
	    @Test
	    public void TestTemplateWithATemplateOccurTwice_ExtraTemplateBetween() {
	        map.store("name", "Antonios");
	        map.store("surname", "Valais");
	        Integer matchingMode = TemplateEngine.DEFAULT;
	        String result = engine.evaluate("Hello my name is ${name} ${surname}, ${name} is my name", map, matchingMode);
	        assertEquals("Hello my name is Antonios Valais, Antonios is my name", result);
	    }
	}
}
