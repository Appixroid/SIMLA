package interpreter.instructions;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import interpreter.KEYWORDS;
import interpreter.exception.SimlaException;
import interpreter.managers.DataManagers;
import interpreter.managers.DataManagers.TYPES;
import interpreter.managers.FuncManager;
import interpreter.utils.Var;
import interpreter.utils.register.FunctionRegister;


public class DataInstruction
{	
	/**
	 * Evaluate the mathematical instruction
	 * @param expression : the expression to evaluate
	 * @return the result of the evaluation 
	 * @throws SimlaException
	 */
	public static Var<Object> evaluateExpression(String expression) throws SimlaException
	{
		ScriptEngineManager mgr = new ScriptEngineManager();
	    ScriptEngine engine = mgr.getEngineByName("JavaScript");
	    Object value = null;
	    Var<Object> result = null;

	    try 
	    {
	    	DataManagers.bindVars(engine);
	    	expression = DataManagers.replaceArray(expression);
	    	value = engine.eval(expression);
	    	result = new Var<Object>();
	    	
			result.setValue(value);
		} catch (ScriptException e) 
	    {
			/**
			 * Throw an exception if the expression cannot be parse
			 */
			throw new SimlaException(SimlaException.WRONG_EVALUATION);
		}
	    
	    /**
	     * Choose the type of the result 
	     */
	    if(value instanceof Boolean)
	    {
	    	result.setType(TYPES.Boolean);
	    }
	    else if(value instanceof String)
	    {
	    	result.setType(TYPES.String);
	    }
	    else if(value instanceof Double)
	    {
	    	result.setType(TYPES.Real);
	    }
	    else if(value instanceof Integer)
	    {
	    	result.setType(TYPES.Integer);
	    }
	    else
	    {
	    	result.setType(TYPES.Null);
	    }
	    
		return result;
	}
	
	/**
	 * Interpretate a "Var" instruction for var creation
	 * @param instruction : the instruction to interpretate
	 * @throws SimlaException
	 */
	public static void varInstruction(String instruction) throws SimlaException
	{
		String[] elements = instruction.split(KEYWORDS.ARGUMENT_SEPARATOR);
		
		if(elements.length <= 1)
		{
			/**
			 * Throw an exception if there are after the instruction label
			 */
			throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
		}
		
		if(elements[elements.length - 2].equals(KEYWORDS.TYPE_SET_KEYWORD) && elements[elements.length - 4].equals(KEYWORDS.ASSIGNMENT_KEYWORD))
		{
			TYPES type = TYPES.getTypes(elements[elements.length - 1]);
						
			if(type == TYPES.Null)
			{
				/**
				 * Throw an exception if the type doesn't exist
				 */
				throw new SimlaException(SimlaException.CREATE_NULL_VARIABLE);
			}
				
			String label = elements[elements.length - 3];
			Object value;
			
			if(elements[1].startsWith(KEYWORDS.STRING_START))
			{
				if(type == TYPES.String)
				{
					int i = 1;
					value = readString(elements, i)[0];
				}
				else
				{
					/**
					 * Throw exception if the type is not a String
					 */
					throw new SimlaException(SimlaException.WRONG_TYPE);
				}
			}
			else
			{
				value = elements[1];
			}
				
			/**
			 * Create a var and set its value
			 */
			DataManagers.newVar(label, TYPES.String);
			DataManagers.setVar(label, value);
			DataManagers.castVar(label, type);
		}
		else
		{
			/**
			 * Throw an exception if the instruction is malformed
			 */
			throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
		}
	}
	
	/**
	 * Interpretate a "tab" instruction for tab creation
	 * @param instruction : the instruction to interpretate
	 * @throws SimlaException
	 */
	public static void tabInstruction(String instruction) throws SimlaException
	{
		String[] elements = instruction.split(KEYWORDS.ARGUMENT_SEPARATOR);
		
		if(elements.length <= 1)
		{
			/**
			 * Throw an exception if there are after the instruction label
			 */
			throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
		}
		
		if(elements[2].equals(KEYWORDS.ARRAY_SIZE_KEYWORD) && elements[4].equals(KEYWORDS.TYPE_SET_KEYWORD))
		{
			TYPES type = TYPES.getTypes(elements[elements.length - 1]);
						
			if(type == TYPES.Null)
			{
				/**
				 * Throw an exception if the type doesn't exist
				 */
				throw new SimlaException(SimlaException.CREATE_NULL_VARIABLE);
			}
			
			String label = elements[1];
			int size = 0;
			
			try
			{
				size = Integer.parseInt(elements[3]);
			} catch(Exception e) {
				/*
				 * throw exception if the size is not valid
				 */
				throw new SimlaException(SimlaException.NOT_A_SIZE);
			}
				
			/**
			 * Create a array and set its value
			 */
			DataManagers.newArray(label, TYPES.String, size);
		}
		else
		{
			/**
			 * Throw an exception if the instruction is malformed
			 */
			throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
		}
	}
	
	/**
	 * Interpretate a "calc" instruction for calculate an expression
	 * @param instruction : the instruction to interpretate
	 * @throws SimlaException
	 */
	public static void calcInstruction(String instruction) throws SimlaException
	{
		String[] elements = instruction.split(KEYWORDS.ARGUMENT_SEPARATOR);
		
		if(elements.length <= 1)
		{
			/**
			 * Throw an exception if there are after the instruction label
			 */
			throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
		}
		
		int i = 1;
		String expression = "";
		
		/**
		 * Separate the expression and the reception var
		 */
		while(i < elements.length && !elements[i].equals(KEYWORDS.ASSIGNMENT_KEYWORD))
		{
			expression += elements[i];
			i++;
		}
		
		if(i >= elements.length)
		{
			/**
			 * Throw an expression if there is not a "in" statement
			 */
			throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
		}
	    
		Var<Object> result = evaluateExpression(expression);
		
		if(result == null)
		{
			/**
			 * Throw an exception if the expression is null
			 */
			throw new SimlaException(SimlaException.WRONG_EVALUATION);
		}
		
		/**
		 * Update the var with the evaluated value
		 */
		if(DataManagers.existVar(elements[i + 1]))
		{
			DataManagers.setVar(elements[i + 1], result.getValue());
			DataManagers.changeType(elements[i+1], TYPES.String);
			
			DataManagers.castVar(elements[i + 1], result.getType());
		}
		else
		{
			/**
			 * Throw an exception if the reception var doesn't exist
			 */
			throw new SimlaException(SimlaException.UNKNOW_VARIABLE);
		}
	}
	
	/**
	 * Interpretate the "call" instruction to call an existing function
	 * @param instruction : the instruction to interpretate
	 * @throws SimlaException 
	 */
	public static void callInstruction(String instruction) throws SimlaException
	{
		String[] elements = instruction.split(KEYWORDS.ARGUMENT_SEPARATOR);
		if(elements.length <= 1)
		{
			/**
			 * Throw an exception if there are after the instruction label
			 */
			throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
		}
		
		String label = elements[1];
		String[] args = new String[elements.length - 2];
		
		for(int i = 2; i < elements.length; i++)
		{
			args[i - 2] = elements[i];
		}
		
		FuncManager.execFunc(label, args);
	}
	
	/**
	 * Interpretate the "func" instruction to create a new function
	 * @param instruction : the instruction to interpretate
	 * @throws SimlaException 
	 */
	public static void funcInstruction(String instruction) throws SimlaException
	{
		String[] elements = instruction.split(KEYWORDS.ARGUMENT_SEPARATOR);
		if(elements.length <= 1)
		{
			/**
			 * Throw an exception if there are after the instruction label
			 */
			throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
		}
		
		String[] args = new String[elements.length - 3];
		
		for(int i = 3; i < elements.length; i++)
		{
			args[i - 3] = elements[i];
		}
		
		Instructions.register = new FunctionRegister(KEYWORDS.FUNC_INSTRUCTION, KEYWORDS.FUNC_END, elements[2], (elements[1].equals(KEYWORDS.VOID_STRING) ? KEYWORDS.NULL_STRING : elements[1]), args);
		Instructions.hasToRegister = true;
		Instructions.targetExpected = 1;
	}
	
	/**
	 * Interpretate the return instrucion of a function
	 * @param instruction : the return instruction
	 * @throws SimlaException 
	 */
	public static void returnInstruction(String instruction) throws SimlaException
	{
		if(Instructions.inFunction)
		{
			String[] elements = instruction.split(KEYWORDS.ARGUMENT_SEPARATOR);
			if(elements.length <= 1)
			{
				/**
				 * Throw an exception if there are after the instruction label
				 */
				throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
			}
			
			String[] expTab = new String[elements.length - 1];
			
			for(int i = 1; i < elements.length; i++)
			{
				expTab[i - 1] = elements[i];
			}
			
			DataManagers.setVar(Instructions.returnLabel, DataInstruction.evaluateExpression(String.join(" ", expTab)).getValue());
		}
		else
		{
			/**
			 * Exception if it's not in function block
			 */
			throw new SimlaException(SimlaException.UNKNOW_INSTRUCTION);
		}
	}

	public static void castInstruction(String instruction) throws SimlaException 
	{
		String[] elements = instruction.split(KEYWORDS.ARGUMENT_SEPARATOR);
		if(elements.length <= 1)
		{
			/**
			 * Throw an exception if there are after the instruction label
			 */
			throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
		}
		
		if(elements[2].equals(KEYWORDS.TYPE_SET_KEYWORD))
		{
			if(DataManagers.isArray(elements[1]))
			{
				/*
				 * Throw exception if the var to cast is an array
				 */
				throw new SimlaException(SimlaException.ARRAY_CAST);
			}
			else
			{
				DataManagers.castVar(elements[1], TYPES.getTypes(elements[3]));
			}
		}
		else
		{
			/*
			 * Throw Exception if the instruction is malformed
			 */
			throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
		}
	}
	
	/**
	 * Replace the parenthesis with the value of the expression in
	 * @param string : the line containing the parenthesis
	 * @return the string with the parenthesis replace by their value
	 * @throws SimlaException 
	 */
	public static String parseParenthesis(String string) throws SimlaException
	{
		String line = String.valueOf(string);
		int i = 0;
		while(i < line.length())
		{
			if(line.charAt(i) == KEYWORDS.EXPRESSION_START.charAt(0))
			{
				int start = i;
				int count = 0;
				String expression = "";

				do
				{
					if(i >= string.length())
					{
						/**
						 * Throw an exception if the expression is unclosed
						 */
						throw new SimlaException(SimlaException.UNFINISHED_EXPRESSION);
					}
					
					if(line.charAt(i) == KEYWORDS.EXPRESSION_START.charAt(0))
					{
						count++;
					}
					else if(line.charAt(i) == KEYWORDS.EXPRESSION_END.charAt(0))
					{
						count--;
					}
					
					expression += line.charAt(i);
					i++;
				}while(count > 0);
				
				String result = String.valueOf(evaluateExpression(expression).getValue());
				line = line.replace(expression, result);
				i = start;
			}
			
			i++;
		}
		
		return line;
	}
	
	/**
	 * Read a String in the SIMLA Tab
	 * @param tab : the arguments with the String
	 * @return the String
	 * @throws SimlaException
	 */
	public static Object[] readString(String[] tab, int i) throws SimlaException
	{
		String str = "";
		while(tab[i].endsWith(KEYWORDS.STRING_ESCAPE + KEYWORDS.STRING_END) || !tab[i].endsWith(KEYWORDS.STRING_END))
		{
			str += (tab[i].startsWith(KEYWORDS.STRING_START) ? tab[i].substring(1, tab[i].length()): tab[i]);
			if(str.endsWith(KEYWORDS.STRING_ESCAPE + KEYWORDS.STRING_END))
			{
				str = str.substring(0, str.length() - 2) + KEYWORDS.STRING_END;
			}
			str += " ";
			i++;
			
			if(i >= tab.length)
			{
				/**
				 * Throw an exception if a string haven't a closing quote
				 */
				throw new SimlaException(SimlaException.UNFINISHED_STRING);
			}
		}
		
		str += tab[i].substring(0, tab[i].length() - 1);
		
		return new Object[] {str, i};
	}
	
	/**
	 * Read a String in the SIMLA String
	 * @param tab : the SIMLA String with the String within
	 * @return the String
	 * @throws SimlaException
	 */
	public static Object[] readString(String string, int i) throws SimlaException
	{
		return readString(string.split(KEYWORDS.ARGUMENT_SEPARATOR), i);
	}
}
