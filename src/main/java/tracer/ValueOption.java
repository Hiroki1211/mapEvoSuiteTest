package tracer;

public class ValueOption {

	private String id = "";
	private String type = "";
	private String value = "";
	
	public void display() {
		System.out.print("id:" + id + ", ");
		System.out.print("type:" + type + ", ");
		System.out.println("value:" + value);
	}
	
	public void setId(String input) {
		id = input;
	}
	
	public void setType(String input) {
		type = input;
	}
	
	public void setValue(String input) {
		value = input;
	}
	
	public String getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
	
}
