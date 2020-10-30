package ru.jenya705.component;

public class GlobalComponent {

	public static void count(ComponentMethodData data) {
		
		String exercise = data.getArgs()[0];
		String operators[] = exercise.split("[0-9]+");
		String operands[] = exercise.split("[+-/*%]");
		int result = Integer.parseInt(operands[0]);
		for (int i = 1; i < operands.length; ++i) {
			if (operators[i].equals("+")) result += Integer.parseInt(operands[i]);
			else if (operators[i].equals("-")) result -= Integer.parseInt(operands[i]);
			else if (operators[i].equals("*")) result *= Integer.parseInt(operands[i]);
			else if (operators[i].equals("/")) result /= Integer.parseInt(operands[i]);
			else result %= Integer.parseInt(operands[i]);
		}
		data.setResult(Integer.toString(result));
		
	}
	
}
