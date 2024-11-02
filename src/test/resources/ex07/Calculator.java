package ex07;

public class Calculator {

	public int run(int a, int b, String ope) {
		int result = 0;
		
		if(ope.equals("+")) {
			result = a + b;
		}else if(ope.equals("-")) {
			result = a - b;
		}else if(ope.equals("*")) {
			result = a * b;
		}else if(ope.equals("/")) {
			if(b == 0) {
				result = 0;
			}else {
				result = a / b;
			}
		}
		
		return result;
	}
	
}
