package interpreter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import interpreter.exception.SimlaException;
import interpreter.instructions.Instructions;

public class Interpreter 
{
	/**
	 * Version of the SIMLA Language
	 */
	public static final String VERSION = "SIMLA Version 1.0.0";

	/**
	 * Parse the String by line and read each line
	 * @param code : the String to read
	 * @throws SimlaException
	 */
	public static void read(String code) throws SimlaException
	{
		String[] lines = code.split(KEYWORDS.INSTRUCTION_SEPARATOR);
		int lineCount = 0;
		
		for(String line : lines)
		{
			lineCount++;
			readLine(line, lineCount);
		}
	}
	
	/**
	 * Interpretate the given String who is the line-th line 
	 * @param instruction : the line to read
	 * @param line : the line number
	 * @throws SimlaException
	 */
	public static void readLine(String instruction, int line) throws SimlaException
	{
		Instructions.interpretate(instruction, line);
	}
	
	public static void main(String[] args)
	{
		if(args.length == 0)
		{
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader("Exemple.sml"));
				
				String code = "";
				String line;
				while((line = reader.readLine()) != null)
				{
					code += line + "\n";
				}
				
				reader.close();
								
				try
				{
					read(code);
				} catch (SimlaException e) 
				{
					System.err.println(e.getMessage());
				}
			} catch (IOException e) 
			{
				System.err.println("File Not Found Or Cannot Be Read");
			}
		}
		else if(args.length == 1)
		{
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(args[0]));
				
				String code = "";
				String line;
				while((line = reader.readLine()) != null)
				{
					code += line + "\n";
				}
				
				reader.close();
				
				try
				{
					read(code);
				} catch (SimlaException e) 
				{
					System.err.println(e.getMessage());
				}
			} catch (IOException e) 
			{
				System.err.println("File Not Found Or Cannot Be Read");
			}
		}
		else
		{
			System.err.println("To Much Arguments");
		}
	}
}
