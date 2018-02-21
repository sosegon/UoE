package st_cw1_p1;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import st.SimpleTemplateEngine;

public class SimpleTemplateEngineTest {
	
    private SimpleTemplateEngine simpleEngine;
    
    @Before
    public void setUp() throws Exception {
        simpleEngine = new SimpleTemplateEngine();
    }
       
    @Test
    public void TestCase1() {
    	String template = null;
    	int mode = SimpleTemplateEngine.DEFAULT_MATCH;
    	String result = simpleEngine.evaluate(template, "", "", mode);
    	assertEquals(result, null);
    }
    
    @Test
    public void TestCase2() {
    	String template = "";
    	int mode = SimpleTemplateEngine.DEFAULT_MATCH;
    	String result = simpleEngine.evaluate(template, "", "", mode);
    	assertEquals(result, "");
    }
    
    @Test
    public void TestCase3() {
    	String pattern = null;
    	int mode = SimpleTemplateEngine.DEFAULT_MATCH;
    	String result = simpleEngine.evaluate("123", pattern, "", mode);
    	assertEquals(result, "123");
    }
    
    @Test
    public void TestCase4() {
    	String pattern = "";
    	int mode = SimpleTemplateEngine.DEFAULT_MATCH;
    	String result = simpleEngine.evaluate("123", pattern, "", mode);
    	assertEquals(result, "123");
    }
    
    @Test
    public void TestCase5() {
    	String template = "value(0) value#(1) value##(2) value###(3) value####(4) value#####(5) value######(6)";
    	String pattern = "value####";
    	String value = "new";
    	int mode = SimpleTemplateEngine.DEFAULT_MATCH;
    	String result = simpleEngine.evaluate(template, pattern, value, mode);
    	String comparison = "value(0) value#(1) new(2) new#(3) new##(4) new###(5) new####(6)";
    	assertEquals(result, comparison);
    }
    
    @Test
    public void TestCase6() {
    	String template = "value(0) value#(1) value##(2) value###(3) value####(4) value#####(5) value######(6)";
    	String pattern = "value###7";
    	String value = "new";
    	int mode = SimpleTemplateEngine.DEFAULT_MATCH;
    	String result = simpleEngine.evaluate(template, pattern, value, mode);
    	assertEquals(result, template);
    }
    
    @Test
    public void TestCase7() {
    	String template = "123";
    	String pattern = "123";
    	String value = null;
    	int mode = SimpleTemplateEngine.DEFAULT_MATCH;
    	String result = simpleEngine.evaluate(template, pattern, value, mode);
    	assertEquals(result, template);
    }
    
    @Test
    public void TestCase8() {
    	String template = "123";
    	String pattern = "123";
    	String value = "";
    	int mode = SimpleTemplateEngine.DEFAULT_MATCH;
    	String result = simpleEngine.evaluate(template, pattern, value, mode);
    	assertEquals(result, template);
    }
    
    @Test
    public void TestCase9() {
    	String template = "ABC0abc0ABC0ABC";
    	String pattern = "abc#2";
    	String value = "XYZ";
    	int mode = SimpleTemplateEngine.DEFAULT_MATCH;
    	String result = simpleEngine.evaluate(template, pattern, value, mode);
    	String comparison = "ABC0XYZ0ABC0ABC";
    	assertEquals(result, comparison);
    }
    
    @Test
    public void TestCase10() {
    	String template = "ABC0abc0ABC0ABC";
    	String pattern = "abc";
    	String value = "XYZ";
    	int mode = SimpleTemplateEngine.CASE_SENSITIVE;
    	String result = simpleEngine.evaluate(template, pattern, value, mode);
    	String comparison = "ABC0XYZ0ABC0ABC";
    	assertEquals(result, comparison);
    }
    
    @Test
    public void TestCase11() {
    	String template = "ABC0abc0ABC0ABC";
    	String pattern = "abc";
    	String value = "XYZ";
    	int mode = SimpleTemplateEngine.DEFAULT_MATCH;
    	String result = simpleEngine.evaluate(template, pattern, value, mode);
    	String comparison = "XYZ0XYZ0XYZ0XYZ";
    	assertEquals(result, comparison);
    }
    
    @Test
    public void TestCase12() {
    	String template = "ABC abc_ABC-ABC";
    	String pattern = "abc#4";
    	String value = "XYZ";
    	int mode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH;
    	String result = simpleEngine.evaluate(template, pattern, value, mode);
    	String comparison = "ABC abc_ABC-XYZ";
    	assertEquals(result, comparison);
    }
    
    @Test
    public void TestCase13() {
    	String template = "ABC abc_ABC-ABC";
    	String pattern = "ABC#2";
    	String value = "XYZ";
    	int mode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH | SimpleTemplateEngine.CASE_SENSITIVE;
    	String result = simpleEngine.evaluate(template, pattern, value, mode);
    	String comparison = "ABC abc_XYZ-ABC";
    	assertEquals(result, comparison);
    }
    
    @Test
    public void TestCase14() {
    	String template = "ABC abc_ABC-ABC";
    	String pattern = "abc";
    	String value = "XYZ";
    	int mode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH;
    	String result = simpleEngine.evaluate(template, pattern, value, mode);
    	String comparison = "XYZ XYZ_XYZ-XYZ";
    	assertEquals(result, comparison);
    }
    
    @Test
    public void TestCase15() {
    	String template = "ABC abc_ABC-ABC";
    	String pattern = "ABC";
    	String value = "XYZ";
    	int mode = SimpleTemplateEngine.CASE_SENSITIVE;
    	String result = simpleEngine.evaluate(template, pattern, value, mode);
    	String comparison = "XYZ abc_XYZ-XYZ";
    	assertEquals(result, comparison);
    }
    
    @Test
    public void TestCase16() {
    	String template = "ABC abc_ABC-ABC";
    	String pattern = "ABC";
    	String value = "XYZ";
    	int mode = SimpleTemplateEngine.DEFAULT_MATCH;
    	String result = simpleEngine.evaluate(template, pattern, value, mode);
    	String comparison = "XYZ XYZ_XYZ-XYZ";
    	assertEquals(result, comparison);
    }
    
    @Test
    public void TestCase17() {
    	String template = "ABC abc_ABC-ABC";
    	String pattern = "ABC";
    	String value = "XYZ";
    	int mode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH | SimpleTemplateEngine.CASE_SENSITIVE;
    	String result = simpleEngine.evaluate(template, pattern, value, mode);
    	String comparison = "XYZ abc_XYZ-XYZ";
    	assertEquals(result, comparison);
    }
	    

}
