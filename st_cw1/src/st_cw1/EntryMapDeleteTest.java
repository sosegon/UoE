package st_cw1;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import st.EntryMap;

public class EntryMapDeleteTest {

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
