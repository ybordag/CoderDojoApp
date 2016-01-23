package com.coderdojo.dojoapp1;


public class Kid {
	
	private String _name;

	public Boolean _is_checked;
	
	public Kid(String name){
		_name = name;
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

	public void fromString(String input){
		_name = input;
	}

}
