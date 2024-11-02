package ex07;

public class Executer {

	public int run(String s) {
		Formula formula = new Formula(s);
		Calculator calculator = new Calculator();
		int result = calculator.run(formula.getA(), formula.getB(), formula.getOpe());
		return result;
	}
}
