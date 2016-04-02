package com.coderdojo.dojoapp1;


public class Kid {
	
	private String _name;

	private Boolean _is_checked;
	
	public Kid(String name){
		_name = name;
		_is_checked = false;
	}
	
	public void setName(String name){
		_name = name;
	}
	
	public String getName(){
		return _name;
	}

	public String toString(){
		String output = _name;
		return output;
	}

	public boolean isChecked(){
		return _is_checked;
	}

	public void setChecked(boolean isChecked){
		_is_checked = isChecked;
	}

	public void fromString(String input){
		_name = input;
	}

}
