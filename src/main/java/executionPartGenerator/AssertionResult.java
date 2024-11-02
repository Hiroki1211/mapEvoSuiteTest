package executionPartGenerator;

public class AssertionResult {

	private String type;
	private boolean booleanResult;
	private char charResult;
	private byte byteResult;
	private int intResult;
	private long longResult;
	private float floatResult;
	private double doubleResult;
	private String stringResult;
	
	public AssertionResult(boolean booleanResult) {
		this.type = "boolean";
		this.booleanResult = booleanResult;
	}
	
	public AssertionResult(char charResult) {
		this.type = "char";
		this.charResult = charResult;
	}
	
	public AssertionResult(byte byteResult) {
		this.type = "byte";
		this.byteResult = byteResult;
	}
	
	public AssertionResult(int intResult) {
		this.type = "int";
		this.intResult = intResult;
	}
	
	public AssertionResult(long longResult) {
		this.type = "long";
		this.longResult = longResult;
	}
	
	public AssertionResult(float floatResult) {
		this.type = "float";
		this.floatResult = floatResult;
	}
	
	public AssertionResult(double doubleResult) {
		this.type = "double";
		this.doubleResult = doubleResult;
	}
	
	public AssertionResult(String stringResult) {
		this.type = "String";
		this.stringResult = stringResult;
	}
	
	public String getType() {
		return type;
	}
	
	public boolean getBooleanResult() {
		return booleanResult;
	}
	
	public char getCharResult() {
		return charResult;
	}
	
	public byte getByteResult() {
		return byteResult;
	}
	
	public int getIntResult() {
		return intResult;
	}
	
	public long getLongResult() {
		return longResult;
	}
	
	public float getFloatResult() {
		return floatResult;
	}
	
	public double getDoubleResult() {
		return doubleResult;
	}
	
	public String getStringResult() {
		return stringResult;
	}
}
