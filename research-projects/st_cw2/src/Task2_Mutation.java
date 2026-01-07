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
	 * An existing entry is deleted
	 */
	@Test
	public void TestMutation02() {
		map.store("key1", "value1");
        map.store("key2", "value2");
        map.store("key3", "value3");
        
		map.delete("key2");		
		assertEquals(map.getEntries().size(), 2);
		
		map.store("key2", "nvalue2");
		
		Integer matchingMode = TemplateEngine.DEFAULT;
		String result = engine.evaluate("Value is ${key2}", map, matchingMode);
		assertEquals("Value is nvalue2", result);
	}
    /*
     * ****************************************************************
     * Mutation 04
     * ****************************************************************
     */
	/*
	 * An entry map is found
	 */
	@Test
	public void TestMutation04() {
		map.store("AABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890", "1");
		map.store("BABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890", "2");
		map.store("CABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890", "3");
		
		String template = "Value is ${AABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		String result = engine.evaluate(template, map, matchingMode);
		assertEquals("Value is 1", result);
	}
    /*
     * ****************************************************************
     * Mutation 06
     * ****************************************************************
     */
	/*
	 * An empty template is deleted
	 */
    @Test
    public void TestMutation06() {
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
    public void TestMutation08() {
    	String template = "ABC0abc0ABC0ABC";
    	String pattern = "abc";
    	String value = "XYZ";
    	int matchingMode = SimpleTemplateEngine.CASE_SENSITIVE;
    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
    	String comparison = "ABC0XYZ0ABC0ABC";
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
    public void TestMutation10() {
    	String template = "ABC0abc0ABC0ABC";
    	String pattern = "abc";
    	String value = "XYZ";
    	int matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
    	String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
    	String comparison = "XYZ0XYZ0XYZ0XYZ";
    	assertEquals(result, comparison);
    }
}
