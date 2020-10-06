package interpreter.instructions.inBuild;

import java.util.Random;

import interpreter.Interpreter;
import interpreter.KEYWORDS;
import interpreter.exception.SimlaException;
import interpreter.managers.DataManagers;
import interpreter.managers.DataManagers.TYPES;

public class GeneralFunction 
{
	/**
	 * List of the general function
	 */
	private static String[] generalFuncs = {KEYWORDS.TYPE_FUNCTION, KEYWORDS.VERSION_FUNCTION, KEYWORDS.RANDOM_FUNCTION, KEYWORDS.SIZE_FUNCTION};
	
	/**
	 * Return if a general function with this label exist
	 * @param label : the label of the function
	 * @return true if the function exist
	 */
	public static boolean isFunc(String label)
	{
		boolean contains = false;
		
		for(int i = 0; i < generalFuncs.length && !contains; i++)
		{
			if(generalFuncs[i].equals(label))
			{
				contains = true;
			}
		}
		
		return contains;
	}
	
	/**
	 * Exec the function with this label and the given parameters
	 * @param label : the label of the function
	 * @param params : the tab of arguments
	 * @throws SimlaException
	 */
	public static void execGeneralFunc(String label, String[] params) throws SimlaException
	{
		if(label.equals(generalFuncs[0]))
		{
			execTypeFunc(params);
		}
		else if(label.equals(generalFuncs[1]))
		{
			execVersionFunc(params);
		}
		else if(label.equals(generalFuncs[2]))
		{
			execRandomFunc(params);
		}
		else if(label.equals(generalFuncs[3]))
		{
			execSizeFunc(params);
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
	 * Execute the Type func with paramaters
	 * @param params : the parameters of the function
	 * @throws SimlaException 
	 */
	private static void execTypeFunc(String[] params) throws SimlaException 
	{
		String type = DataManagers.getVar(params[0]).getType().toString();
		
		if(params.length == 1)
		{
			System.out.println(type);
		}
		else if(params.length == 3 && params[1].equals(KEYWORDS.ASSIGNMENT_KEYWORD))
		{
			DataManagers.changeType(params[2], TYPES.String);
			DataManagers.setVar(params[2], type);
		}
		else
		{
			/**
			 * Throw an exception if there are unexpected parameters
			 */
			throw new SimlaException(SimlaException.UNEXPECTED_ARGUMENTS);
		}
	}
	
	/**
	 * Execute the size func with paramaters
	 * @param params : the parameters of the function
	 * @throws SimlaException 
	 */
	private static void execSizeFunc(String[] params) throws SimlaException 
	{
		if(DataManagers.isArray(params[0]))
		{
			int size = DataManagers.getArray(params[0]).getSize();
			
			if(params.length == 1)
			{
				System.out.println(size);
			}
			else if(params.length == 3 && params[1].equals(KEYWORDS.ASSIGNMENT_KEYWORD))
			{
				TYPES type = DataManagers.getVar(params[2]).getType();
				DataManagers.changeType(params[2], TYPES.String);
				DataManagers.setVar(params[2], size);
				DataManagers.castVar(params[2], type);
			}
			else
			{
				/**
				 * Throw an exception if there are unexpected parameters
				 */
				throw new SimlaException(SimlaException.UNEXPECTED_ARGUMENTS);
			}
		}
		else
		{
			/*
			 * Throw an exception if the var is not an array
			 */
			throw new SimlaException(SimlaException.NOT_A_ARRAY);
		}
	}
	
	/**
	 * Execute the Version func with paramaters
	 * @param params : the parameters of the function
	 * @throws SimlaException 
	 */
	private static void execVersionFunc(String[] params) throws SimlaException 
	{
		if(params.length == 0)
		{
			System.out.println(Interpreter.VERSION);
		}
		else if(params.length == 3 && params[1].equals(KEYWORDS.ASSIGNMENT_KEYWORD))
		{
			DataManagers.changeType(params[2], TYPES.String);
			DataManagers.setVar(params[2], Interpreter.VERSION);
		}
		else
		{
			/**
			 * Throw an exception if there are unexpected parameters
			 */
			throw new SimlaException(SimlaException.UNEXPECTED_ARGUMENTS);
		}
	}
	
	/**
	 * Execute the Random func with paramaters
	 * @param params : the parameters of the function
	 * @throws SimlaException 
	 */
	private static void execRandomFunc(String[] params) throws SimlaException 
	{
		if(params.length == 2 && params[0].equals(KEYWORDS.ASSIGNMENT_KEYWORD))
		{
			String label = params[1];
			Random rand = new Random();
			
			DataManagers.changeType(label, TYPES.Real);
			DataManagers.setVar(label, rand.nextDouble());
		}
		else if(params.length == 4 && params[2].equals(KEYWORDS.ASSIGNMENT_KEYWORD))
		{
			String label = params[3];
			int min = Integer.valueOf(params[0]);
			int max = Integer.valueOf(params[1]);
			
			Random rand = new Random();
			
			DataManagers.changeType(label, TYPES.Integer);
			DataManagers.setVar(label, (int) (((rand.nextInt(max) * (max-min)) / max) + min));
		}
		else if(params.length > 4)
		{
			/**
			 * Throw an exception if there are unexpected parameters
			 */
			throw new SimlaException(SimlaException.UNEXPECTED_ARGUMENTS);
		}
		else
		{
			/**
			 * Throw an exception if there are missing parameters
			 */
			throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
		}
	}
}
