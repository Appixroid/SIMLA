package interpreter.managers;

import java.util.HashMap;
import java.util.Map;

import interpreter.Interpreter;
import interpreter.KEYWORDS;
import interpreter.exception.SimlaException;
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
	
	public static void execFunction(String label, String[] args) throws SimlaException
	{
		Function func = getFunc(label);
		
		String saveVar = null;
		String[] funcArgs = args;
		
		if(String.join(" ", args).contains(KEYWORDS.ARGUMENT_SEPARATOR + KEYWORDS.ASSIGNMENT_KEYWORD + KEYWORDS.ARGUMENT_SEPARATOR) && args[args.length - 2].equals(KEYWORDS.ASSIGNMENT_KEYWORD))
		{
			saveVar = args[args.length - 1];
			DataManagers.changeType(saveVar, TYPES.Null);
			
			funcArgs = new String[args.length - 2];
			
			for(int i = 0; i < args.length - 2; i++)
			{
				funcArgs[i] = args[i];
			}
		}
		else if(String.join(" ", args).contains(KEYWORDS.ARGUMENT_SEPARATOR + KEYWORDS.ASSIGNMENT_KEYWORD + KEYWORDS.ARGUMENT_SEPARATOR) && !args[args.length - 2].equals(KEYWORDS.ASSIGNMENT_KEYWORD))
		{
			/*
			 * Exception if the assignement statement is not at the end
			 */
			throw new SimlaException(SimlaException.UNEXPECTED_ARGUMENTS);
		}
		
		if(funcArgs.length == func.getArgs().length)
		{
			for(int i = 0; i < funcArgs.length; i++)
			{
				DataManagers.newVar(func.getArgs()[i], TYPES.getType(funcArgs[i]));
				DataManagers.setVar(func.getArgs()[i], funcArgs[i]);
			}	
		}
		else if(funcArgs.length < func.getArgs().length)
		{
			/*
			 * Exception if argument are missing
			 */
			throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
			
		}
		else if(funcArgs.length > func.getArgs().length)
		{
			/*
			 * Exception if argument are unexpected
			 */
			throw new SimlaException(SimlaException.UNEXPECTED_ARGUMENTS);
		}
		
		if(func.getReturnType() != TYPES.Null)
		{
			func.call();
			DataManagers.newVar(func.getLabel() + func.getCurrentCall(), func.getReturnType());
			DataManagers.setVar(func.getLabel() + func.getCurrentCall(), 0);
			Instructions.returnLabel = func.getLabel() + func.getCurrentCall();
		}
		
		Instructions.inFunction = true;
		Interpreter.read(func.getCode());
		Instructions.inFunction = false;
		
		if(func.getReturnType() == TYPES.Null)
		{
			if(saveVar != null)
			{
				DataManagers.setVar(saveVar, "NULL");
			}
		}
		else
		{
			if(saveVar != null)
			{
				DataManagers.setVar(saveVar, DataManagers.getVar(func.getLabel() + func.getCurrentCall()).getValue());
			}
			
			DataManagers.delVar(func.getLabel() + func.getCurrentCall());
			func.uncall();
			Instructions.returnLabel = null;
		}
		
		for(int i = 0; i < funcArgs.length; i++)
		{
			DataManagers.delVar(func.getArgs()[i]);
		}
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
