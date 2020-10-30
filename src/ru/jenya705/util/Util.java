package ru.jenya705.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Util {

	public static String[] cutStringArray(String[] arr, int start, int end) {
		
		String[] result = new String[end - start];
		for (int i = start; i < end; ++i) {
			result[i - start] = arr[i];
		}
		return result;
		
	}
	
	public static String connectStringsFromArray(String[] arr, String between) {
		
		StringBuilder stringBuilder = new StringBuilder();
		for (String str : arr) {
			stringBuilder.append(str + between);
		}
		return stringBuilder.toString();
			
		
	}
	
	public static String connectStringsFromArrayWithCut(String[] arr, int start, int end, String between) {
		
		return connectStringsFromArray(cutStringArray(arr, start, end), between);
		
	}
	
	public static void writeTextInFile(String text, File file) throws IOException{
		
		try(FileWriter fileWriter = new FileWriter(file)){
			fileWriter.write(text);
		}
		
	}
	
	public static String readTextFromFile(File file) throws IOException{
		
		try(FileReader fileReader = new FileReader(file)){
			String result = "";
			int i;
			while((i = fileReader.read()) != -1) result += (char) i;
			return result;
		}
		
	}
	
}
