package ru.jenya705.component;

public class GlobalComponent {

	public static void count(ComponentMethodData data) {
		
		String exercise = data.getArgs()[0];
		String operators[] = exercise.split("[0-9]+");
		String operands[] = exercise.split("[+-/*%]");
		
		float result = Float.parseFloat(operands[0]);
		for (int i = 1; i < operands.length; ++i) {
			if (operators[i].equals("+")) result += Float.parseFloat(operands[i]);
			else if (operators[i].equals("-")) result -= Float.parseFloat(operands[i]);
			else if (operators[i].equals("*")) result *= Float.parseFloat(operands[i]);
			else if (operators[i].equals("/")) result /= Float.parseFloat(operands[i]);
			else result %= (int) Float.parseFloat(operands[i]);
		}
		data.setResult(Float.toString(result));
		
	}
	
}
