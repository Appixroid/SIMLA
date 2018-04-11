package interpreter.instructions.inBuild;

import java.util.Scanner;

import interpreter.KEYWORDS;
import interpreter.exception.SimlaException;
import interpreter.instructions.DataInstruction;
import interpreter.managers.DataManagers;
import interpreter.managers.DataManagers.TYPES;

public class IOFunction
{
	/**
	 * List of the IO functions
	 */
	private static String[] ioFuncs = {KEYWORDS.OUTPUT_FUNCTION, KEYWORDS.INPUT_FUNCTION};
	
	private static Scanner sc = new Scanner(System.in);

	
	public static boolean isFunc(String label)
	{
		boolean contains = false;
		
		for(int i = 0; i < ioFuncs.length && !contains; i++)
		{
			if(ioFuncs[i].equals(label))
			{
				contains = true;
			}
		}
		
		return contains;
	}
	
	/**
	 * Exec the IO function with the label and the given parameters
	 * @param label : label of the function
	 * @param params : list of the parameters of the function
	 * @throws SimlaException
	 */
	public static void execIOFunc(String label, String[] params) throws SimlaException
	{
		if(label.equals(ioFuncs[0]))
		{
			execOutputFunc(params);
		}
		else if(label.equals(ioFuncs[1]))
		{
			execInputFunc(params);
		}
		else 
		{
			/**
			 * Throw an exception if the function doesn't exist
			 */
			throw new SimlaException(SimlaException.UNKNOW_FUNCTION);
		}
	}

	/**
	 * Execute the Output func with paramaters
	 * @param params : the parameters of the function
	 * @throws SimlaException 
	 */
	private static void execOutputFunc(String[] params) throws SimlaException 
	{
		int i = 0;
		
		while( i < params.length)
		{
			String str = "";
			
			if(params[i].startsWith(KEYWORDS.STRING_START))
			{
				Object[] obj = DataInstruction.readString(params, i);
				i = (int) obj[1];
				str = (String) obj[0];
				
				str = str.startsWith("\"") ? str.substring(1, str.length()) : str;
			}
			else if(!Character.isDigit(params[i].charAt(0)))
			{
				str += DataManagers.getVar(params[i]).getValue();
			}
			else
			{
				str += params[i];
			}
			
			System.out.print(str + " ");
			
			i++;
		}
		
		System.out.print("\n");
	}
	
	/**
	 * Execute the Input func with paramaters
	 * @param params : the parameters of the function
	 * @throws SimlaException 
	 */
	private static void execInputFunc(String[] params) throws SimlaException 
	{
		String str = "";
		int i = 0;
		if(params[params.length - 2].equals(KEYWORDS.ASSIGNMENT_KEYWORD))
		{
			if(params[i].startsWith(KEYWORDS.STRING_START))
			{
				Object[] obj = DataInstruction.readString(params, i);
				i = (int) obj[1];
				str = (String) obj[0];
			}
			else
			{
				/**
				 * Throw an exception if there are no String start 
				 */
				throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
			}
			
			System.out.print(str);
			
			String input = sc.nextLine();
			
			TYPES targetType = DataManagers.getVar(params[params.length - 1]).getType();
			
			DataManagers.changeType(params[params.length - 1], TYPES.String);
			DataManagers.setVar(params[params.length - 1], input);
			DataManagers.castVar(params[params.length - 1], targetType);			
		}
		else
		{
			/**
			 * Throw an exception if there are no in statement 
			 */
			throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
		}
	}
}
