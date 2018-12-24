package st;

public class SimpleTemplateEngine {
	private static final Character This = Character.valueOf('#');
	public static final Integer CASE_SENSITIVE = Integer.valueOf(1);
	public static final Integer WHOLE_WORLD_SEARCH = Integer.valueOf(2);
	public static final Integer DEFAULT_MATCH = Integer.valueOf(0);

	public String evaluate(String arg0, String arg1, String arg2, Integer arg3) {
		if (!This(arg0).booleanValue() | !This(arg2).booleanValue() | !This(arg1).booleanValue()) {
			return arg0;
		} else {
			if (!(arg3 == null
					? Boolean.FALSE
					: (arg3.intValue() < 0 ? Boolean.FALSE : (arg3.intValue() > 3 ? Boolean.FALSE : Boolean.TRUE)))
							.booleanValue()) {
				arg3 = DEFAULT_MATCH;
			}

			String arg4 = "";
			Integer arg5 = Integer.valueOf(0);
			Integer arg6 = Integer.valueOf(0);
			Boolean arg8 = Boolean.FALSE;
			Boolean arg9 = Boolean.FALSE;
			Integer arg10 = Integer.valueOf(0);
			Integer arg11 = Integer.valueOf(0);

			label137 : while (arg6.intValue() < arg1.length()) {
				Character arg7;
				if ((arg7 = Character.valueOf(arg1.charAt(arg6.intValue()))).equals(This) && arg8 != Boolean.TRUE) {
					arg8 = Boolean.TRUE;
					arg6 = Integer.valueOf(arg6.intValue() + 1);
				} else if (arg8 == Boolean.TRUE) {
					if (!arg7.equals(This)) {
						if (!Character.isDigit(arg7.charValue())) {
							return arg0;
						}

						arg10 = arg6;
						arg11 = Integer.valueOf(arg6.intValue() + 1);
						arg9 = Boolean.TRUE;
						arg8 = Boolean.FALSE;

						while (true) {
							if (arg11.intValue() >= arg1.length() || !Character
									.isDigit(Character.valueOf(arg1.charAt(arg11.intValue())).charValue())) {
								break label137;
							}

							arg11 = Integer.valueOf(arg11.intValue() + 1);
						}
					}

					arg8 = Boolean.FALSE;
					arg6 = Integer.valueOf(arg6.intValue() + 1);
					arg4 = arg4 + arg7;
				} else {
					arg6 = Integer.valueOf(arg6.intValue() + 1);
					arg4 = arg4 + arg7;
				}
			}

			if (!This(arg4).booleanValue()) {
				return arg0;
			} else if (arg8 == Boolean.TRUE) {
				return arg0;
			} else {
				if (arg9.booleanValue()) {
					arg5 = Integer.valueOf(arg1.substring(arg10.intValue(), arg11.intValue()));
				}

				if (!This(arg3).booleanValue()) {
					arg4 = arg4.toLowerCase();
				}

				arg6 = Integer.valueOf(0);
				Integer arg12 = Integer.valueOf(arg4.length());
				Integer arg13 = Integer.valueOf(0);

				while (true) {
					while (arg6.intValue() <= arg0.length() - arg12.intValue()) {
						String arg14 = arg0.substring(arg6.intValue(), arg6.intValue() + arg12.intValue());
						if (!This(arg3).booleanValue()) {
							arg14 = arg14.toLowerCase();
						}

						if (arg14.equals(arg4)
								&& (!((arg3.intValue() & WHOLE_WORLD_SEARCH.intValue()) == WHOLE_WORLD_SEARCH.intValue()
										? Boolean.TRUE
										: Boolean.FALSE).booleanValue()
										|| (arg6.intValue() + arg12.intValue() >= arg0.length() || !Character
												.isLetterOrDigit(arg0.charAt(arg6.intValue() + arg12.intValue())))
												&& (arg6.intValue() == 0 || !Character
														.isLetterOrDigit(arg0.charAt(arg6.intValue() - 1))))) {
							arg13 = Integer.valueOf(arg13.intValue() + 1);
							if (arg9.booleanValue() && arg13 == arg5 || !arg9.booleanValue()) {
								arg14 = arg0.substring(0, arg6.intValue());
								String arg15 = "";
								if (arg6.intValue() + arg12.intValue() < arg0.length()) {
									arg15 = arg0.substring(arg6.intValue() + arg12.intValue(), arg0.length());
								}

								arg0 = arg14 + arg2 + arg15;
								arg6 = Integer.valueOf(arg6.intValue() + arg2.length());
								continue;
							}
						}

						arg6 = Integer.valueOf(arg6.intValue() + 1);
					}

					return arg0;
				}
			}
		}
	}

	private static Boolean This(String arg) {
		return arg == null ? Boolean.FALSE : (arg.isEmpty() ? Boolean.FALSE : Boolean.TRUE);
	}

	private static Boolean This(Integer arg) {
		return (arg.intValue() & CASE_SENSITIVE.intValue()) == CASE_SENSITIVE.intValue() ? Boolean.TRUE : Boolean.FALSE;
	}
}