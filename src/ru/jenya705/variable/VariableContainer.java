package ru.jenya705.variable;

import java.util.HashMap;
import java.util.Map;

import ru.jenya705.component.ComponentMethodData;

public class VariableContainer {

	private static Map<String, String> variables = new HashMap<>();

	public static void getVarComponentFunction(ComponentMethodData data) {
		Object variable = getVariable(data.getArgs()[0]);
		if (variable != null) data.setResult(variable.toString());
		else data.setResult("null");
	}
	
	public static Map<String, String> getVariables() {
		return variables;
	}
	public static Object getVariable(String name) {
		return variables.getOrDefault(name, null);
	}
	public static void removeVariable(String name) {
		variables.remove(name);
	}
	public static void setVariables(Map<String, String> variables) {
		VariableContainer.variables = variables;
	}
	public static void setVariable(String name, String variable) {
		variables.put(name, variable);
	}
	
}
