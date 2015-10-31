package com.coderdojo.dojoapp1.dataSources;

import com.coderdojo.dojoapp1.Kid;

import java.util.ArrayList;

public abstract class KidsDataSource{
	abstract public void addKid(Kid newKid) throws Exception;
	abstract public void removeKid(String name) throws Exception;
	abstract public ArrayList<Kid> getKids();
	abstract public Kid getKid(String name);
	abstract public void clear() throws Exception;
	abstract public void load() throws Exception;
	abstract public void save() throws Exception;
	abstract public void close() throws Exception;
	abstract public String toString();
	abstract public void fromString(String  input)throws Exception ;

}
