package interpreter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class Interpreter
{
	/**
	 * Type constant
	 */
	public static final String INTEGER = "Integer", REAL = "Real", COMPLEX = "Complex", STRING = "String", BOOLEAN = "Boolean", UNSIGNED = "Unsigned";
	/**
	 * List of function to use 
	 * key -> func label
	 * value -> func code
	 */
	private Hashtable<String, String> funcMap = new Hashtable<String, String>();
	
	/**
	 * List of var to use
	 * key -> var label
	 * value -> Object[] ([0] String var type, [1]Int var index)
	 */
	private Hashtable<String, Object[]> varMap = new Hashtable<String, Object[]>();
	
	/**
	 * Int list
	 */
	private ArrayList<Integer> intList = new ArrayList<Integer>();
	/**
	 * Real list
	 */
	private ArrayList<Double> realList = new ArrayList<Double>();
	/**
	 * Complex list
	 */
	private ArrayList<int[]> complexList = new ArrayList<int[]>();
	/**
	 * String list
	 */
	private ArrayList<String> stringList = new ArrayList<String>();
	/**
	 * Boolean list
	 */
	private ArrayList<Boolean> booleanList = new ArrayList<Boolean>();
	/**
	 * Unsigned list
	 */
	private ArrayList<Integer> unsignedList = new ArrayList<Integer>();
	
	/**
	 * Scanner obj
	 */
	private Scanner sc = new Scanner(System.in);

	public Interpreter()
	{
		
	}	
	
	public void read(String code)
	{
		String[] linedCode = code.split("\n");
		
		for(int i = 0; i < linedCode.length; i ++)
		{
			readLine(linedCode[i]);
		}
	}
	
	public void readLine(String line)
	{
		if(line.startsWith("com "))
		{
			//Comentary
		}
		else if(line.startsWith("call "))
		{
			callInstruction(line.split("call ")[1]);
		}
		else if(line.startsWith("var "))
		{
			
		}
		else if(line.startsWith("calc "))
		{
			
		}
		else if(line.startsWith("if "))
		{
			
		}
		else if(line.startsWith("loop if "))
		{
			
		}
		else if(line.startsWith("for "))
		{
			
		}
		else if(line.startsWith("func "))
		{
			
		}
		else
		{
			//TODO Unknow line
		}
	}
	
	private Object[] getVar(String name)
	{
		try
		{
			return varMap.get(name);
		}catch(NullPointerException e){
			//TODO Unknow var
			return null;
		}
	}
	
	private void callInstruction(String call)
	{
		int index = call.indexOf(" ");
		
		if(index > -1)
		{
			String functionName = call.substring(0, index);
			String[] args = call.substring(index + 1, call.length()).split(",");
			
			switch(functionName)
			{
				case "output":
					String output = "";
					
					for(int i = 0; i < args.length; i++)
					{
						if(args[i].startsWith("\"") && args[i].endsWith("\""))
						{
							output += args[i];
						}
						else if(args[i].startsWith("\"") || args[i].endsWith("\""))
						{
							//TODO String error
						}
						else
						{
							Object[] temp;
							if((temp = getVar(args[i])) != null)
							{
								switch((String) temp[0])
								{
									case INTEGER:
										output += intList.get((int) temp[1]);
										break;
										
									case REAL:
										output += realList.get((int) temp[1]);
										break;
										
									case STRING:
										output += stringList.get((int) temp[1]);
										break;
										
									case BOOLEAN:
										output += booleanList.get((int) temp[1]);
										break;
										
									case COMPLEX:
										output += complexList.get((int) temp[1]);
										break;
										
									case UNSIGNED:
										output += unsignedList.get((int) temp[1]);
										break;
								}
							}
						}
					}
					
					inbuildOutput(output);
					break;
					
				case "input":
					break;
					
				default:
					
			}
		}
		else
		{
			switch(call)
			{
				case "output":
					inbuildOutput("\n");
					break;

				case "input":
					break;
					
				case "version":
					inbuildOutput("SIMLA version pre-alpha 0.1");
					break;
					
					default:
			}
		}
	}
	
	private void inbuildOutput(String output)
	{
		System.out.println(output);
	}
	
	private String inbuildInput(String message)
	{
		System.out.println(message);
		return sc.nextLine();
	}
}
