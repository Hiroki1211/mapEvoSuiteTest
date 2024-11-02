package tracer;

import java.util.ArrayList;

public class Value {

	private ArrayList<ValueOption> valueOptionLists = new ArrayList<ValueOption>();
	
	public void displayContent() {
		System.out.print(" " + "values:");
		for(int i = 0; i < valueOptionLists.size(); i++) {
			ValueOption valueOption = valueOptionLists.get(i);
			if(valueOption.getId().equals("")) {
				System.out.print(valueOption.getValue() + ", ");
			}else {
				System.out.print("id:" + valueOption.getId() + ", type:" + valueOption.getType() + ", ");
			}
		}
	}
	
	public ArrayList<ValueOption> getValueOptionLists(){
		return valueOptionLists;
	}
	
	public void addValue(String input) {
		ValueOption valueOption = new ValueOption();
		valueOption.setValue(input);
		valueOptionLists.add(valueOption);
	}
	
	public void addID(String input) {
		ValueOption valueOption = new ValueOption();
		valueOption.setId(input);
		valueOptionLists.add(valueOption);
	}
	
	public void addType(String input) {
		ValueOption valueOption = valueOptionLists.get(valueOptionLists.size()-1);
		valueOption.setType(input);
	}
	
	public void addStr(String input) {
		ValueOption valueOption = valueOptionLists.get(valueOptionLists.size()-1);
		valueOption.setValue(input);
	}
}
