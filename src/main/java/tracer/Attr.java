package tracer;

public class Attr {	
	
	private String owner;
	private String name;
	private String desc;	
	private String type;
	private String methodtype;
	private int index;
	private int varindex;
	private String opcode;
	private String jumpto;
	private int amount;
	private String location;
	private int created;
	private String blocktype;
	private String start;
	private String end;

	//	private String objecttype;
	//	private String id;
	//	private String str;

	public void displayContent() {
		System.out.println(
				" " +
				"owner:" + owner + ", " +
				"name:" + name + ", " +
				"desc:" + desc + ", " +
				"type:" + type + ", " +
				"methodtype:" + methodtype + ", " +
				"index:" + index + ", " +
				"varindex:" + varindex + ", " +
				"opcode:" + opcode + ", " +
				"jumpto:" + jumpto + ", " +
				"amount:" + amount + ", " +
				"location:" + location + ", " +
				"created:" + created + ", " +
				"blocktype" + blocktype + ", " +
				"start:" + start + ", " +
				"end:" + end  
				);
	}
	
	public void setOwner(String input) {
		owner = input;
	}
	
	public void setName(String input) {
		name = input;
	}
	
	public void setDesc(String input) {
		desc = input;
	}
	
	public void setType(String input) {
		type = input;
	}
	
	public void setMethodtype(String input) {
		methodtype = input;
	}
	
	public void setIndex(int input) {
		index = input;
	}
	
	public void setVarindex(int input) {
		varindex = input;
	}
	
	public void setOpcode(String input) {
		opcode = input;
	}

	public void setAmount(int input) {
		amount = input;
	}
	
	public void setCreated(int input) {
		created = input;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String getType() {
		return type;
	}
	
	public String getMethodtype() {
		return methodtype;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getVarindex() {
		return varindex;
	}
	
	public String getOpcode() {
		return opcode;
	}
	
	public void getJumpto() {
		
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void getLocation() {
		
	}
	
	public int getCreated() {
		return created;
	}

	public void getBlocktype() {
		
	}
	
	public void getStart() {
		
	}
	
	public void getEnd() {
		
	}
}
