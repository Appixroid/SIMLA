package interpreter.managers;

import java.util.HashMap;
import java.util.Map;

import interpreter.Interpreter;
import interpreter.KEYWORDS;
import interpreter.exception.SimlaException;
import interpreter.instructions.DataInstruction;
import interpreter.instructions.Instructions;
import interpreter.instructions.inBuild.GeneralFunction;
import interpreter.instructions.inBuild.IOFunction;
import interpreter.managers.DataManagers.TYPES;
import interpreter.utils.Function;

public class FuncManager 
{
	private static Map<String, Function> funcMap = new HashMap<String, Function>();
	
	/**
	 * Return if a function is created with the given label
	 * @param label : the label of the function
	 * @return true if this function exists
	 */
	public static boolean isFunc(String label)
	{
		return funcMap.containsKey(label);
	}
	
	/**
	 * Execute the existing function 
	 * @param label : label of the function
	 * @param args : arguments of the function
	 * @throws SimlaException
	 */
	public static void execFunc(String label, String[] args) throws SimlaException
	{
		/**
		 * If it's a in-build IO function
		 */
		if(IOFunction.isFunc(label))
		{
			IOFunction.execIOFunc(label, args);
		}
		/**
		 * If it's an in-build function
		 */
		else if(GeneralFunction.isFunc(label))
		{
			GeneralFunction.execGeneralFunc(label, args);
		}
		/**
		 * If it's a created function
		 */
		else if(isFunc(label))
		{
			execFunction(label, args);
		}
		else
		{
			/**
			 * Throw an exception if the exception doesn't exist
			 */
			throw new SimlaException(SimlaException.UNKNOW_FUNCTION);
		}
			
	}
	
	/**
	 * Exec a function created by the user
	 * @param label : The label of the function
	 * @param args : The arguments for the function
	 * @throws SimlaException
	 */
	public static void execFunction(String label, String[] args) throws SimlaException
	{
		Function func = getFunc(label);
		
		String saveVar = null;
		String[] funcArgs = new String[0];
		String code = func.getCode();
		func.call();

		//Assigment processing
		if(String.join(" ", args).contains(KEYWORDS.ARGUMENT_SEPARATOR + KEYWORDS.ASSIGNMENT_KEYWORD + KEYWORDS.ARGUMENT_SEPARATOR) && args[args.length - 2].equals(KEYWORDS.ASSIGNMENT_KEYWORD))
		{
			saveVar = args[args.length - 1];
			DataManagers.changeType(saveVar, TYPES.Null);
			
			funcArgs = new String[args.length - 2];
			for(int i = 0; i < args.length - 2; i++)
			{				
				String str = "";
				if(args[i].startsWith(KEYWORDS.STRING_START))
				{
					Object[] obj = DataInstruction.readString(args, i);
					i = (int) obj[1];
					str = (String) obj[0];
					
					str = str.startsWith("\"") ? str.substring(1, str.length()) : str;
				}
				else if(!Character.isDigit(args[i].charAt(0)))
				{
					str += DataManagers.getVar(args[i]).getValue();
				}
				else
				{
					str += args[i];
				}
				
				funcArgs[i] = str;

			}
		}
		else if(String.join(" ", args).contains(KEYWORDS.ARGUMENT_SEPARATOR + KEYWORDS.ASSIGNMENT_KEYWORD + KEYWORDS.ARGUMENT_SEPARATOR) && !args[args.length - 2].equals(KEYWORDS.ASSIGNMENT_KEYWORD))
		{
			func.uncall();
			
			/*
			 * Exception if the assignement statement is not at the end
			 */
			throw new SimlaException(SimlaException.UNEXPECTED_ARGUMENTS);
		}
		else if(!String.join(" ", args).contains(KEYWORDS.ARGUMENT_SEPARATOR + KEYWORDS.ASSIGNMENT_KEYWORD + KEYWORDS.ARGUMENT_SEPARATOR) && args.length > 0)
		{
			funcArgs = new String[args.length];
			for(int i = 0; i < args.length; i++)
			{				
				String str = "";
				if(args[i].startsWith(KEYWORDS.STRING_START))
				{
					Object[] obj = DataInstruction.readString(args, i);
					i = (int) obj[1];
					str = (String) obj[0];
					
					str = str.startsWith("\"") ? str.substring(1, str.length()) : str;
				}
				else if(!Character.isDigit(args[i].charAt(0)))
				{
					str += DataManagers.getVar(args[i]).getValue();
				}
				else
				{
					str += args[i];
				}
				funcArgs[i] = str;

			}
		}
		
		//Args Processing
		if(funcArgs.length == func.getArgs().length)
		{
			for(int i = 0; i < funcArgs.length; i++)
			{
				DataManagers.newVar(func.getArgs()[i] + "__arg__" + i + "__function__" + func.getCurrentCall(), TYPES.getType(funcArgs[i]));
				DataManagers.setVar(func.getArgs()[i] + "__arg__" + i + "__function__" + func.getCurrentCall(), funcArgs[i]);
				code = code.replaceAll(" " + func.getArgs()[i] + "\n", " " + func.getArgs()[i] + "__arg__" + i + "__function__" + func.getCurrentCall() + "\n");
				code = code.replaceAll(" " + func.getArgs()[i] + "\t", " " + func.getArgs()[i] + "__arg__" + i + "__function__" + func.getCurrentCall() + "\t");
				code = code.replaceAll(" " + func.getArgs()[i] + " ", " " + func.getArgs()[i] + "__arg__" + i + "__function__" + func.getCurrentCall() + " ");
			}	
		}
		else if(funcArgs.length < func.getArgs().length)
		{
			func.uncall();
			
			/*
			 * Exception if argument are missing
			 */
			throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
			
		}
		else if(funcArgs.length > func.getArgs().length)
		{
			func.uncall();
			
			/*
			 * Exception if argument are unexpected
			 */
			throw new SimlaException(SimlaException.UNEXPECTED_ARGUMENTS);
		}
		
		//Return Processing
		if(func.returnArray())
		{
			Instructions.returnLabel = func.getLabel() + "__return__function__array__" + func.getCurrentCall();
			DataManagers.newArray(Instructions.returnLabel, func.getReturnType(), 0);
			DataManagers.setArray(Instructions.returnLabel, null);
		}
		if(func.getReturnType() != TYPES.Null)
		{
			Instructions.returnLabel = func.getLabel() + "__return__function__" + func.getCurrentCall();
			DataManagers.newVar(Instructions.returnLabel, func.getReturnType());
			DataManagers.setVar(Instructions.returnLabel, 0);
		}
		
		Instructions.inFunction = true;
		int beginLine = Instructions.currentLine;

		Interpreter.read(code, func.getOffset());
		
		Instructions.currentLine = beginLine;
		Instructions.inFunction = false;
		
		if(func.getReturnType() == TYPES.Null)
		{
			if(saveVar != null)
			{
				DataManagers.setVar(saveVar, "NULL");
			}
		}
		else if(func.returnArray())
		{
			if(saveVar != null)
			{
				DataManagers.setArray(saveVar, Instructions.returnLabel);
			}
		}
		else
		{
			if(saveVar != null)
			{
				DataManagers.setVar(saveVar, DataManagers.getVar(Instructions.returnLabel).getValue());
			}
			
			DataManagers.delVar(Instructions.returnLabel);
		}
		
		for(int i = 0; i < funcArgs.length; i++)
		{
			DataManagers.delVar(func.getArgs()[i] + "__arg__" + i + "__function__" + func.getCurrentCall());
		}	
		func.uncall();
		Instructions.returnLabel = null;
	}
	
	/**
	 * Obtain the body of the function with this label
	 * @param label : label of the function
	 * @return the body of this function
	 * @throws SimlaException
	 */
	private static Function getFunc(String label) throws SimlaException
	{
		if(funcMap.containsKey(label))
		{
			return funcMap.get(label);
		}
		else 
		{
			/**
			 * Throw an exception if the function doesn(t exist
			 */
			throw new SimlaException(SimlaException.UNKNOW_FUNCTION);
		}
	}
	
	/**
	 * Create a new function with the given label and body
	 * @param label : label of the function
	 * @param func : body of the function
	 * @throws SimlaException
	 */
	public static void newFunc(String label, Function func) throws SimlaException
	{
		if(!funcMap.containsKey(label))
		{
			funcMap.put(label, func);
		}
		else
		{
			/**
			 * Throw an exception if a function with the same label already exist
			 */
			throw new SimlaException(SimlaException.EXISTING_FUNCTION);
		}
	}
}
