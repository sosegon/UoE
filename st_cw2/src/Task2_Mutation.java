import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import st.EntryMap;
import st.SimpleTemplateEngine;
import st.TemplateEngine;

public class Task2_Mutation {
	private SimpleTemplateEngine simpleEngine;
	private TemplateEngine engine;
    private EntryMap map;
    
    @Before
    public void setUp() throws Exception {
        simpleEngine = new SimpleTemplateEngine();
        engine = new TemplateEngine();
        map = new EntryMap();
    }
    /*
     * ****************************************************************
     * Mutation 02
     * ****************************************************************
     */
    /*
     * ****************************************************************
     * Mutation 04
     * ****************************************************************
     */
    /*
     * ****************************************************************
     * Mutation 06
     * ****************************************************************
     */
    @Test
    public void TestEmptyTemplateBoundary_DeleteUnmatched() {
        map.store("name", "Antonios");
        map.store("surname", "Valais");
        Integer matchingMode = TemplateEngine.DEFAULT | TemplateEngine.DELETE_UNMATCHED;
        String result = engine.evaluate("Hello my name ${}is ${name} ${surname}", map, matchingMode);
        assertEquals("Hello my name is Antonios Valais", result);
    }
    /*
     * ****************************************************************
     * Mutation 08
     * ****************************************************************
     */
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
     * The pattern to replace is case sensitive in a string with spaces
     * Fails for Mutation 10 as well
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
     * The pattern to replace looks for whole words and is case sensitive for all matches
     * Fails for Mutation 10 as well
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
    /*
     * ****************************************************************
     * Mutation 10
     * ****************************************************************
     */
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
}
