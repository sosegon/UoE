import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import st.EntryMap;
import st.TemplateEngine;

public class Task2_TDD_1 {
	private TemplateEngine engine;
    private EntryMap map;

    @Before
    public void setUp() throws Exception {
        engine = new TemplateEngine();
        map = new EntryMap();
    }
    
    /*
     * The value of the entry map follows the pattern "in X years"
     */
	@Test
	public void TDD01() {
		map.store("year", "in 3 years");
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);
        
        int year = Calendar.getInstance().get(Calendar.YEAR) + 3;
        assertEquals(String.valueOf(year), result);
	}
	
	/*
     * If the number of years is 0, then use the current year
     */
	@Test
	public void TDD02() {
		map.store("year", "in 0 years");
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);
        
        int year = Calendar.getInstance().get(Calendar.YEAR);
        assertEquals(String.valueOf(year), result);
	}
	
	/*
     * If the number of years is 0 (with several digits)
     */
	@Test
	public void TDD03() {
		map.store("year", "in 0000000000000000000000000000000 years");
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);
        
        int year = Calendar.getInstance().get(Calendar.YEAR);
        assertEquals(String.valueOf(year), result);
	}
	
	/*
     * If the number of years has initial zeros
     */
	@Test
	public void TDD04() {
		map.store("year", "in 0000000000000000000000000000003 years");
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);
        
        int year = Calendar.getInstance().get(Calendar.YEAR) + 3;
        assertEquals(String.valueOf(year), result);
	}
	
	/*
     * If the number of years has initial and final zeros
     */
	@Test
	public void TDD05() {
		map.store("year", "in 000000000000000000000000000000300 years");
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);
        
        int year = Calendar.getInstance().get(Calendar.YEAR) + 300;
        assertEquals(String.valueOf(year), result);
	}
	
	/*
     * If the number of years has initial and final zeros
     */
	@Test
	public void TDD06() {
		map.store("year", "in 0000300 years");
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);
        
        int year = Calendar.getInstance().get(Calendar.YEAR) + 300;
        assertEquals(String.valueOf(year), result);
	}
	
	/*
     * If the number of years is negative.
     */
	@Test
	public void TDD07() {
		map.store("year", "in -3 years");
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);

        assertEquals("in -3 years", result);
	}
	
	/*
     * If the number of years is not a number.
     */
	@Test
	public void TDD08() {
		map.store("year", "in -3x years");
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);
        
        assertEquals("in -3x years", result);
	}
	
	
	/*
     * The value of the entry map does not exactly follow the pattern "in X years"
     */
	@Test
	public void TDD09() {
		map.store("year", "in  3  years");
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);

        assertEquals("in  3  years", result);
	}
	
	/*
     * If the year in entry is greater than the maximum integer number in java,
     * then the year is the current year.
     * This is not part of the specification, but it's for exception handling.
     */
	@Test
	public void TDD10() {
		map.store("year", "in 30000000000000000000000000000 years");
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        assertEquals(String.valueOf(year), result);
	}
	
	/*
     * If the year in entry is equal to the maximum integer number in java,
     * then the year is the current year.
     * This is not part of the specification, but it's to handle unexpected values.
     */
	@Test
	public void TDD11() {
		String max = String.valueOf(Integer.MAX_VALUE);
		map.store("year", "in " + max + " years");
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        assertEquals(String.valueOf(year), result);
	}
	
	/*
     * If the sum of the year in the entry and the current year 
     * is equal to the maximum integer number in java
     */
	@Test
	public void TDD12() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String max = String.valueOf(Integer.MAX_VALUE - year);
		map.store("year", "in " + max + " years");
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);
        assertEquals(String.valueOf(Integer.MAX_VALUE), result);
	}
	
	/*
     * If the sum of the year in the entry and the current year 
     * is greater than the maximum integer number in java,
     * then the year is the current year.
     * This is not part of the specification, but it's to handle unexpected values.
     */
	@Test
	public void TDD13() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String max = String.valueOf(Integer.MAX_VALUE - year + 1);
		map.store("year", "in " + max + " years");
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);
        assertEquals(String.valueOf(year), result);
	}
	
	/*
	 * If there is a base_year entry with no number
	 */
	@Test
	public void TDD14() {
		map.store("year", "in 3 years");
		map.store("base_year", "year");
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);
        int year = Calendar.getInstance().get(Calendar.YEAR) + 3;
        assertEquals(String.valueOf(year), result);
	}
	
	/*
	 * If there is a base_year entry with negative number
	 */
	@Test
	public void TDD15() {
		map.store("year", "in 3 years");
		map.store("base_year", "-10");
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);
        int year = Calendar.getInstance().get(Calendar.YEAR) + 3;
        assertEquals(String.valueOf(year), result);
	}
	
	/*
	 * If there is a base_year entry with number
	 */
	@Test
	public void TDD16() {
		map.store("year", "in 3 years");
		map.store("base_year", "2000");
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);
        int year = 2000 + 3;
        assertEquals(String.valueOf(year), result);
	}
	
	/*
	 * If the base_year value is higher than the max integer,
	 * then the year is the current year.
	 * This is not part of the specification, but it's to handle exceptions.
	 */
	@Test
	public void TDD17() {
		map.store("year", "in 3 years");
		map.store("base_year", "30000000000000000000000000000");
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);
        int year = Calendar.getInstance().get(Calendar.YEAR) + 3;
        assertEquals(String.valueOf(year), result);
	}
	
	/*
	 * If the base_year added to the number of years is greater than max integer,
	 * then the year is the current year.
	 * This is not part of the specification, but it's to handle unexpected values.
	 */
	@Test
	public void TDD18() {
		map.store("year", "in 3 years");
		map.store("base_year", String.valueOf(Integer.MAX_VALUE));
		
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);
        int year = Calendar.getInstance().get(Calendar.YEAR) + 3;
        assertEquals(String.valueOf(year), result);
	}
	
	/*
	 * If the base_year added to the number of years is equal to max integer.
	 */
	@Test
	public void TDD19() {
		map.store("year", "in 0 years");
		map.store("base_year", String.valueOf(Integer.MAX_VALUE));
		
		Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${year}", map, matchingMode);
        int year = Integer.MAX_VALUE;
        assertEquals(String.valueOf(year), result);
	}

}
