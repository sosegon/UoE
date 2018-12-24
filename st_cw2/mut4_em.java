package st;

import java.util.ArrayList;
import java.util.HashSet;
import st.EntryMap.Entry;

public class EntryMap {
	private ArrayList This = new ArrayList();
	private HashSet thing = new HashSet();

	public void store(String arg0, String arg1) {
		Entry arg2;
		if (!This(arg2 = new Entry(this, arg0, arg1)).booleanValue()) {
			throw new RuntimeException();
		} else {
			if (this.thing(arg2).booleanValue()) {
				this.This.add(arg2);
				this.thing.add(arg2);
			}

		}
	}

	private static Boolean This(Entry arg) {
		return arg.getPattern() == null
				? Boolean.FALSE
				: (arg.getPattern().isEmpty()
						? Boolean.FALSE
						: (arg.getValue() == null ? Boolean.FALSE : Boolean.TRUE));
	}

	private Boolean thing(Entry arg0) {
		return Boolean.valueOf(!this.thing.contains(arg0));
	}

	public ArrayList getEntries() {
		return this.This;
	}

	public void delete(String arg0) {
		if (arg0 == null) {
			throw new RuntimeException();
		} else if (arg0.isEmpty()) {
			throw new RuntimeException();
		} else {
			Entry arg2 = new Entry(this, arg0, (String) null);
			if (this.thing.contains(arg2)) {
				this.thing.remove(arg2);
				this.This.remove(arg2);
			}

		}
	}

	public void update(String arg0, String arg1) {
		Entry arg2 = new Entry(this, arg0, (String) null);
		Entry arg3;
		if (!This(arg3 = new Entry(this, arg0, arg1)).booleanValue()) {
			throw new RuntimeException();
		} else {
			if (!this.thing(arg2).booleanValue()) {
				this.thing.remove(arg2);
				this.thing.add(arg3);
				this.This.set(this.This.indexOf(arg2), arg3);
			}

		}
	}
}