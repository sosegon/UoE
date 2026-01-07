package st;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import st.EntryMap;
import st.EntryMap.Entry;
import st.TemplateEngine.Result;
import st.TemplateEngine.Template;

public class TemplateEngine {
	public static final Integer DELETE_UNMATCHED = Integer.valueOf(1);
	public static final Integer KEEP_UNMATCHED = Integer.valueOf(0);
	public static final Integer CASE_SENSITIVE = Integer.valueOf(2);
	public static final Integer CASE_INSENSITIVE = Integer.valueOf(0);
	public static final Integer BLUR_SEARCH = Integer.valueOf(4);
	public static final Integer ACCURATE_SEARCH = Integer.valueOf(0);
	public static final Integer DEFAULT = Integer.valueOf(0);
	private static final Character This = Character.valueOf('$');
	private static final Character thing = Character.valueOf('{');
	private static final Character of = Character.valueOf('}');

	public String evaluate(String arg0, EntryMap arg1, Integer arg2) {
		if (!(arg0 == null
				? Boolean.FALSE
				: (arg0.isEmpty() ? Boolean.FALSE : (arg1 == null ? Boolean.FALSE : Boolean.TRUE))).booleanValue()) {
			return arg0;
		} else {
			if (!(arg2 == null
					? Boolean.FALSE
					: (arg2.intValue() < 0 ? Boolean.FALSE : (arg2.intValue() > 7 ? Boolean.FALSE : Boolean.TRUE)))
							.booleanValue()) {
				arg2 = Integer.valueOf(0);
			}

			String arg4 = arg0;
			TemplateEngine arg3 = this;
			HashSet arg5 = new HashSet();
			Stack arg6 = new Stack();
			Integer arg7 = Integer.valueOf(0);
			Boolean arg8 = Boolean.FALSE;

			while (arg7.intValue() < arg4.length()) {
				if (Character.compare(arg4.charAt(arg7.intValue()), This.charValue()) == 0) {
					arg8 = Boolean.TRUE;
					arg7 = Integer.valueOf(arg7.intValue() + 1);
				} else if (Character.compare(arg4.charAt(arg7.intValue()), thing.charValue()) == 0) {
					if (arg8.booleanValue()) {
						arg6.add(arg7);
					}

					arg8 = Boolean.FALSE;
					arg7 = Integer.valueOf(arg7.intValue() + 1);
				} else if (Character.compare(arg4.charAt(arg7.intValue()), of.charValue()) == 0) {
					if (!arg6.isEmpty()) {
						Integer arg10;
						Template arg11;
						if ((arg10 = (Integer) arg6.pop()).intValue() + 1 == arg7.intValue()) {
							arg11 = new Template(arg3, arg10, arg7, "");
						} else {
							arg11 = new Template(arg3, arg10, arg7,
									arg4.substring(arg10.intValue() + 1, arg7.intValue()));
						}

						arg5.add(arg11);
					}

					arg8 = Boolean.FALSE;
					arg7 = Integer.valueOf(arg7.intValue() + 1);
				} else {
					arg8 = Boolean.FALSE;
					arg7 = Integer.valueOf(arg7.intValue() + 1);
				}
			}

			ArrayList arg9 = This(arg5);
			return this.This(arg0, arg9, arg1.getEntries(), arg2).This;
		}
	}

	private static ArrayList This(HashSet arg) {
		ArrayList arg0 = new ArrayList();

		while (!arg.isEmpty()) {
			Template arg1 = null;
			Integer arg2 = Integer.valueOf(Integer.MAX_VALUE);
			Integer arg3 = Integer.valueOf(Integer.MAX_VALUE);
			Iterator arg5 = arg.iterator();

			while (arg5.hasNext()) {
				Template arg4;
				if ((arg4 = (Template) arg5.next()).getContent().length() < arg2.intValue()) {
					arg1 = arg4;
					arg2 = Integer.valueOf(arg4.getContent().length());
					arg3 = arg4.getStartIndex();
				} else if (arg4.getContent().length() == arg2.intValue()
						&& arg4.getStartIndex().intValue() < arg3.intValue()) {
					arg1 = arg4;
					arg2 = Integer.valueOf(arg4.getContent().length());
					arg3 = arg4.getStartIndex();
				}
			}

			if (arg1 == null) {
				throw new RuntimeException();
			}

			arg.remove(arg1);
			arg0.add(arg1);
		}

		return arg0;
	}

	private Result This(String arg0, ArrayList arg1, ArrayList arg2, Integer arg3) {
		Integer arg4 = Integer.valueOf(0);

		for (Integer arg8 = Integer.valueOf(0); arg8.intValue() < arg1.size(); arg8 = Integer
				.valueOf(arg8.intValue() + 1)) {
			Template arg6 = (Template) arg1.get(arg8.intValue());
			Boolean arg5 = Boolean.FALSE;

			for (Integer arg9 = Integer.valueOf(0); arg9.intValue() < arg2.size(); arg9 = Integer
					.valueOf(arg9.intValue() + 1)) {
				Entry arg7 = (Entry) arg2.get(arg9.intValue());
				String arg10;
				String arg11;
				if (!((arg3.intValue() & BLUR_SEARCH.intValue()) == BLUR_SEARCH.intValue()
						? Boolean.FALSE
						: Boolean.TRUE).booleanValue()) {
					arg10 = arg6.getContent().replaceAll("\\s", "");
					arg11 = arg7.getPattern().replaceAll("\\s", "");
				} else {
					arg10 = arg6.getContent();
					arg11 = arg7.getPattern();
				}

				if ((((arg3.intValue() & CASE_SENSITIVE.intValue()) == CASE_SENSITIVE.intValue()
						? Boolean.FALSE
						: Boolean.TRUE).booleanValue()
								? Boolean.valueOf(arg10.toLowerCase().equals(arg11.toLowerCase()))
								: Boolean.valueOf(arg10.equals(arg11))).booleanValue()) {
					arg0 = This(arg0, arg6, arg8, arg7.getValue(), arg1);
					arg5 = Boolean.TRUE;
					break;
				}
			}

			if (arg5.booleanValue()) {
				arg4 = Integer.valueOf(arg4.intValue() + 1);
			} else if (!((arg3.intValue() & DELETE_UNMATCHED.intValue()) == DELETE_UNMATCHED.intValue()
					? Boolean.FALSE
					: Boolean.TRUE).booleanValue()) {
				arg0 = This(arg0, arg6, arg8, "", arg1);
			}
		}

		return new Result(this, arg0, arg4);
	}

	private static String This(String arg, Template arg0, Integer arg1, String arg2, ArrayList arg3) {
		Integer arg4 = Integer.valueOf(3 + arg0.getContent().length() - arg2.length());
		String arg5;
		if (arg0.getStartIndex().intValue() == 1) {
			arg5 = "";
		} else {
			arg5 = arg.substring(0, arg0.getStartIndex().intValue() - 1);
		}

		if (arg0.getEndIndex().intValue() == arg.length()) {
			arg = "";
		} else {
			arg = arg.substring(arg0.getEndIndex().intValue() + 1);
		}

		StringBuilder arg6;
		(arg6 = new StringBuilder()).append(arg5);
		arg6.append(arg2);
		arg6.append(arg);
		arg = arg6.toString();

		for (int arg8 = arg1.intValue() + 1; arg8 < arg3.size(); ++arg8) {
			Template arg7;
			if ((arg7 = (Template) arg3.get(arg8)).getStartIndex().intValue() < arg0.getStartIndex().intValue()
					&& arg7.getEndIndex().intValue() > arg0.getEndIndex().intValue()) {
				((Template) arg3.get(arg8))
						.setEndIndex(Integer.valueOf(arg7.getEndIndex().intValue() - arg4.intValue()));
				((Template) arg3.get(arg8))
						.setContent(arg.substring(((Template) arg3.get(arg8)).getStartIndex().intValue() + 1,
								((Template) arg3.get(arg8)).getEndIndex().intValue()));
			} else if (arg7.getStartIndex().intValue() > arg0.getEndIndex().intValue()) {
				((Template) arg3.get(arg8))
						.setStartIndex(Integer.valueOf(arg7.getStartIndex().intValue() - arg4.intValue()));
				((Template) arg3.get(arg8))
						.setEndIndex(Integer.valueOf(arg7.getEndIndex().intValue() - arg4.intValue()));
			}
		}

		return arg;
	}
}