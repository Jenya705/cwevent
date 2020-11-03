package ru.jenya705.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		for (int i = 0; i < arr.length - 1; ++i) {
			stringBuilder.append(arr[i] + between);
		}
		stringBuilder.append(arr[arr.length-1]);
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
	
	public static int[] findAll(String string, char toFind) {
		
		int[] bufArr = new int[10];
		int currentLen = 0;
		for (int i = 0; i < string.length(); ++i) {
			if (string.charAt(i) == toFind) bufArr[currentLen] = i;
		}
		return Arrays.copyOf(bufArr, currentLen);
		
	}
	
	public static List<File> getFilesInDirectory(File directory) {
		List<File> files = new ArrayList<>();
		File[] directoryFiles = directory.listFiles();
		for (File file : directoryFiles) {
			if (file.isDirectory()) {
				List<File> directoryInDirectoryFiles = getFilesInDirectory(file);
				for (File inDirectoryFile : directoryInDirectoryFiles) files.add(inDirectoryFile);
			}
			else {
				files.add(file);
			}
		}
		return files;
	}
	
}
