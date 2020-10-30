package ru.jenya705.component;

import ru.jenya705.parsing.DataParser.ParseData;

public class ComponentMethodData extends ParseData{

	private String result;
	
	public ComponentMethodData(int start, String[] args) {
		super(start, args);
		setResult("");
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	

}
