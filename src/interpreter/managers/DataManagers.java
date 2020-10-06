package interpreter.managers;

import java.util.HashMap;
import java.util.Iterator;

import javax.script.ScriptEngine;

import interpreter.KEYWORDS;
import interpreter.exception.SimlaException;
import interpreter.instructions.DataInstruction;
import interpreter.utils.Array;
import interpreter.utils.Var;

public class DataManagers 
{
	private static HashMap<String, Var<Object>> varMap = new HashMap<String, Var<Object>>();
	private static HashMap<String, Array<Object>> arrayMap = new HashMap<String, Array<Object>>();
	
	/**
	 * Return if the var exist
	 * @param label : label of the var
	 * @return true if var exist
	 */
	public static boolean existVar(String label)
	{
		return varMap.containsKey(label) || isArray(label);
	}
	
	/**
	 * Return if it is an array
	 * @param label : label of the array
	 * @return true if it's an array
	 */
	public static boolean isArray(String label)
	{
		if(label.contains(KEYWORDS.ARRAY_INDEX_START) && label.endsWith(KEYWORDS.ARRAY_INDEX_END))
		{
			return arrayMap.containsKey(label.split("\\" + KEYWORDS.ARRAY_INDEX_START)[0]);
		}
		else
		{
			return arrayMap.containsKey(label);
		}
	}
	
	/**
	 * Obtain a Var objectof the var with the given label
	 * @param label : label of the var
	 * @return A Var object with the value and the type of the var
	 * @throws SimlaException
	 */
	public static Var<Object> getVar(String label) throws SimlaException
	{
		if(existVar(label))
		{
			if(isArray(label))
			{
				String[] elements = label.split("\\" + KEYWORDS.ARRAY_INDEX_END)[0].split("\\" + KEYWORDS.ARRAY_INDEX_START);
				String name = elements[0]; 
				int index = 0;
				try
				{
					index = ((Double) Double.parseDouble(DataInstruction.evaluateExpression(elements[1]).getValue().toString())).intValue();
				} catch(Exception e) {
					/*
					 * Throw exception if the index is not an integer
					 */
					throw new SimlaException(SimlaException.NOT_A_INDEX);
				}
				return arrayMap.get(name).get(index);
			}
			else
			{
				return varMap.get(label);
			}
		}
		else
		{
			/**
			 * Throw an exception if the var doesn't exist
			 */
			throw new SimlaException(SimlaException.UNKNOW_VARIABLE);
		}
	}
	
	public static Array<Object> getArray(String label) throws SimlaException
	{
		if(isArray(label))
		{
			return arrayMap.get(label);
		}
		else
		{
			/*
			 * Throw exception if it's not an array
			 */
			throw new SimlaException(SimlaException.NOT_A_ARRAY);
		}
	}
	
	/**
	 * Change the value of the var with the given label
	 * @param label : label of the var
	 * @param value : new value of the var
	 * @throws SimlaException
	 */
	public static void setVar(String label, Object value) throws SimlaException
	{
		if(existVar(label))
		{
			if(isArray(label))
			{
				String[] elements = label.split("\\" + KEYWORDS.ARRAY_INDEX_END)[0].split("\\" + KEYWORDS.ARRAY_INDEX_START);
				String name = elements[0]; 
				int index = 0;
				try
				{
					index = ((Double) Double.parseDouble(DataInstruction.evaluateExpression(elements[1]).getValue().toString())).intValue();
				} catch(Exception e) {
					/*
					 * Throw exception if the index is not an integer
					 */
					throw new SimlaException(SimlaException.NOT_A_INDEX);
				}
				 arrayMap.get(name).get(index).setValue(value);
			}
			else
			{
				varMap.get(label).setValue(value);
			}
		}
		else
		{
			/**
			 * Throw an exception if the var with this label doesn't exist
			 */
			throw new SimlaException(SimlaException.UNKNOW_VARIABLE);
		}
	}
	
	/**
	 * Set the default value for the whole array
	 * @param label : the label of the array
	 * @param defaultValue : the default value to set
	 * @throws SimlaException 
	 */
	public static void setArray(String label, Object defaultValue) throws SimlaException
	{
		if(isArray(label))
		{
			for(int i = 0; i <arrayMap.get(label).getSize(); i++)
			{
				arrayMap.get(label).set(i, defaultValue);
			}
		}
		else
		{
			throw new SimlaException(SimlaException.UNKNOW_VARIABLE);
		}
	}
	
	/**
	 * Copy an array in another
	 * @param label : the target array
	 * @param arrayToCopy : the array to copy
	 * @throws SimlaException 
	 */
	public static void setArray(String label, String arrayToCopy) throws SimlaException
	{
		if(isArray(label) && isArray(arrayToCopy))
		{
			arrayMap.get(label).setSize(arrayMap.get(arrayToCopy).getSize());
			
			for(int i = 0; i < arrayMap.get(label).getSize(); i++)
			{
				arrayMap.get(label).set(i, arrayMap.get(arrayToCopy).get(i));
			}
		}
		else
		{
			throw new SimlaException(SimlaException.UNKNOW_VARIABLE);
		}
	}
	
	/**
	 * Change the type of the var with the given label
	 * @param label : label of the var
	 * @param type : new type of the var
	 * @throws SimlaException
	 */
	public static void changeType(String label, TYPES type) throws SimlaException
	{
		if(existVar(label))
		{
			if(isArray(label))
			{
				String[] elements = label.split("\\" + KEYWORDS.ARRAY_INDEX_END)[0].split("\\" + KEYWORDS.ARRAY_INDEX_START);
				String name = elements[0]; 
				int index = 0;
				try
				{
					index = ((Double) Double.parseDouble(DataInstruction.evaluateExpression(elements[1]).getValue().toString())).intValue();
				} catch(Exception e) {
					/*
					 * Throw exception if the index is not an integer
					 */
					throw new SimlaException(SimlaException.NOT_A_INDEX);
				}
				 arrayMap.get(name).get(index).setType(type);
			}
			else
			{
				varMap.get(label).setType(type);
			}
		}
		else
		{
			/**
			 * Throw an exception if the var with this label doesn't exist
			 */
			throw new SimlaException(SimlaException.UNKNOW_VARIABLE);
		}
	}
	
	/**
	 * Create a new var with label and type
	 * @param label : label of the new var
	 * @param type : Type of the new var
	 * @throws SimlaException
	 */
	public static void newVar(String label, TYPES type) throws SimlaException
	{
		if(!existVar(label))
		{
			Var<Object> var = new Var<Object>(type);
			varMap.put(label, var);
		}
		else
		{
			throw new SimlaException(SimlaException.EXISTING_VARIABLE);
		}
	}
	
	/**
	 * Create a new array with label and type
	 * @param label : label of the new array
	 * @param type : Type of the new array
	 * @param size : Size of the new array
	 * @throws SimlaException
	 */
	public static void newArray(String label, TYPES type, int size) throws SimlaException
	{
		if(!existVar(label))
		{
			Array<Object> array = new Array<Object>(size, type);
			arrayMap.put(label, array);
		}
		else
		{
			throw new SimlaException(SimlaException.EXISTING_VARIABLE);
		}
	}
	
	/**
	 * Delete a var with label
	 * @param label : the label of the deleted var
	 * @return 
	 * @throws SimlaException 
	 */
	public static void delVar(String label) throws SimlaException
	{
		if(varMap.containsKey(label))
		{
			varMap.remove(label);
		}
		else if(isArray(label))
		{
			arrayMap.remove(label);
		}
	}
	
	/**
	 * Cast the value of the var and change its type
	 * @param string : The label of the var
	 * @param types : the new type of the var
	 * @throws SimlaException 
	 */
	public static void castVar(String label, TYPES type) throws SimlaException
	{
		TYPES currentType;
		Object currentValue;
		
		if(isArray(label))
		{
			String[] elements = label.split("\\" + KEYWORDS.ARRAY_INDEX_END)[0].split("\\" + KEYWORDS.ARRAY_INDEX_START);
			String name = elements[0]; 
			int index = 0;
			try
			{
				index = ((Double) Double.parseDouble(DataInstruction.evaluateExpression(elements[1]).getValue().toString())).intValue();
			} catch(Exception e) {
				/*
				 * Throw exception if the index is not an integer
				 */
				throw new SimlaException(SimlaException.NOT_A_INDEX);
			}
			
			currentType = arrayMap.get(name).get(index).getType();
			currentValue = arrayMap.get(name).get(index).getValue();
		}
		else
		{
			currentType = getVar(label).getType();
			currentValue = getVar(label).getValue();
		}
		
		Object newValue = null;
			
		if(type == TYPES.String)
		{
			newValue = String.valueOf(currentValue);
		}
		else if(currentType == TYPES.String)
		{
			switch(type)
			{
				case Integer:
					if((String.valueOf(currentValue)).contains("."))
					{
						currentValue = String.valueOf(Double.valueOf(String.valueOf(currentValue)).intValue());
					}
					newValue = Integer.valueOf(String.valueOf(currentValue));
					break;
				
				case Real:
					newValue = Double.valueOf(String.valueOf(currentValue));
					break;
					
				case Boolean:
					newValue = Boolean.valueOf(String.valueOf(currentValue));
					break;
					
				default:
					/*
					 * Throw an exception if the cast is impossible
					 */
					throw new SimlaException(SimlaException.UNABLE_TO_CAST);
			}
		}
		else
		{
			try
			{
				newValue = Class.forName(type.toJavaClass()).cast(Class.forName(currentType.toJavaClass()).cast(currentValue));
			} catch(ClassCastException | ClassNotFoundException e) {
				if(e instanceof ClassNotFoundException)
				{
					/*
					 * Throw an exception if the cast is impossible
					 */
					throw new SimlaException(SimlaException.WRONG_TYPE);
				}
				else
				{
					e.printStackTrace();
				}
			}
		}
			
		
		changeType(label, type);
		setVar(label, newValue);
	}
	
	/**
	 * bind all label to all var in the engine for evaluation
	 * @param engine
	 * @throws SimlaException
	 */
	public static void bindVars(ScriptEngine engine) throws SimlaException
	{
		Iterator<String> it = varMap.keySet().iterator();
		
		while(it.hasNext())
		{
			String cle = it.next();
			Var<Object> var = varMap.get(cle);
			
			if((TYPES) var.getType() == TYPES.Integer)
			{
				engine.put(cle, Integer.valueOf(String.valueOf(var.getValue())));
			}
			else if((TYPES) var.getType() == TYPES.Real)
			{
				engine.put(cle, Double.valueOf(String.valueOf(var.getValue())));
				
			}
			else if((TYPES) var.getType() == TYPES.String)
			{
				engine.put(cle, String.valueOf(var.getValue()));
			}
			else if((TYPES) var.getType() == TYPES.Boolean)
			{
				engine.put(cle, Boolean.valueOf(String.valueOf(var.getValue())));
			}
		}
	}
	
	public static String replaceArray(String expression) throws SimlaException
	{
		Iterator<String> it = arrayMap.keySet().iterator(); 
		
		while(it.hasNext())
		{
			String cle = it.next();
			Array<Object> array = arrayMap.get(cle);
			
			for(int i = 0; i < array.getSize(); i++)
			{
				String label = cle + KEYWORDS.ARRAY_INDEX_START + i + KEYWORDS.ARRAY_INDEX_END;
				Var<Object> var = array.get(i);
				
				while(expression.contains(label))
				{
					
					expression = expression.replace(label, String.valueOf(var.getValue()));
				}
			}
		}
		
		return expression;
	}
	
	/**
	 * Test if the whole String is a digit
	 * @param str : the string to test
	 * @return true if the string is a number
	 */
	public static boolean isDigit(String str)
	{
		boolean isDigit = true;
		int i = 0;
		
		while(isDigit && i < str.length())
		{
			isDigit = Character.isDigit(str.charAt(i)) || str.charAt(i) == '.';
			i++;
		}
		
		return isDigit;
	}
	
	/**
	 * Enum of the existing var types
	 *
	 */
	public enum TYPES
	{
		Integer,
		Real,
		String,
		Boolean,
		Null;
		
		public static TYPES getTypes(String type)
		{
			if (type.equals(KEYWORDS.INTEGER_STRING) || (type.equals(KEYWORDS.INTEGER_STRING) && type.contains(KEYWORDS.ARRAY_INDEX_START) && type.endsWith(KEYWORDS.ARRAY_INDEX_END)))
			{
				return Integer;
			}				
			else if ((type.equals(KEYWORDS.REAL_STRING)) || (type.equals(KEYWORDS.REAL_STRING) && type.contains(KEYWORDS.ARRAY_INDEX_START) && type.endsWith(KEYWORDS.ARRAY_INDEX_END)))
			{
				return Real;
			}				
			else if ((type.equals(KEYWORDS.STRING_STRING)) || (type.equals(KEYWORDS.STRING_STRING) && type.contains(KEYWORDS.ARRAY_INDEX_START) && type.endsWith(KEYWORDS.ARRAY_INDEX_END)))
			{
				return String;
			}				
			else if ((type.equals(KEYWORDS.BOOLEAN_STRING)) || (type.equals(KEYWORDS.BOOLEAN_STRING) && type.contains(KEYWORDS.ARRAY_INDEX_START) && type.endsWith(KEYWORDS.ARRAY_INDEX_END)))
			{
				return Boolean;
			}
			else
			{
				return Null;
			}
		}
		
		public static boolean isArray(String type)
		{
			return type.endsWith(KEYWORDS.ARRAY_INDEX_START + KEYWORDS.ARRAY_INDEX_END);
		}
		
		public String toString()
		{
			if (this == Integer)
			{
				return KEYWORDS.INTEGER_STRING;
			}				
			else if (this == Real)
			{
				return KEYWORDS.REAL_STRING;
			}				
			else if (this == String)
			{
				return KEYWORDS.STRING_STRING;
			}				
			else if (this == Boolean)
			{
				return KEYWORDS.BOOLEAN_STRING;
			}
			else
			{
				return KEYWORDS.NULL_STRING;
			}
		}
		
		public String toJavaClass()
		{
			if (this == Integer)
			{
				return KEYWORDS.INTEGER_CLASS;
			}				
			else if (this == Real)
			{
				return KEYWORDS.REAL_CLASS;
			}				
			else if (this == String)
			{
				return KEYWORDS.STRING_CLASS;
			}				
			else if (this == Boolean)
			{
				return KEYWORDS.BOOLEAN_CLASS;
			}
			else
			{
				return KEYWORDS.NULL_STRING;
			}
		}
		
		public static TYPES getType(Object o)
		{
			if(o instanceof Integer || (o instanceof String && DataManagers.isDigit((java.lang.String) o) && !((String) o).contains(".")))
			{
				return Integer;
			}
			else if(o instanceof Double || (o instanceof String && DataManagers.isDigit((java.lang.String) o) && ((String) o).contains(".")))
			{
				return Real;
			}
			else if(o instanceof Boolean || (o instanceof String && ((String) o).equals(KEYWORDS.TRUE_VALUE) && ((String) o).equals(KEYWORDS.FALSE_VALUE)))
			{
				return Boolean;
			}
			else if(o instanceof String)
			{
				return String;
			}
			else
			{
				return Null;
			}
		}
	}
}
