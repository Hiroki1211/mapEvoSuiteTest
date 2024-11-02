package ex07;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formula {
	
	private int a = 0;
	private int b = 0;
	private String ope = "";

	public Formula(String s) {
		String[] split = s.split(" ");
		if(split.length == 3) {
			if(this.isNumber(split[0]) && this.isNumber(split[2])) {
				a = Integer.valueOf(split[0]);
				b = Integer.valueOf(split[2]);
				ope = split[1];
			}
		}
	}
	
	public int getA() {
		return a;
	}
	
	public int getB() {
		return b;
	}
	
	public String getOpe() {
		return ope;
	}
	
	private boolean isNumber(String val) {
		String regex = "\\A[-]?[0-9]+\\z";
		Pattern p = Pattern.compile(regex);
		Matcher m1 = p.matcher(val);
		return m1.find();
	}
}
