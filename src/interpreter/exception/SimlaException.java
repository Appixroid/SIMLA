package interpreter.exception;

import interpreter.instructions.Instructions;

public class SimlaException extends Exception 
{
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -9113037405628252201L;
	
	/**
	 * Exception message
	 */
	public static final String UNKNOW_FUNCTION = "Unknow Function Reference";
	public static final String EXISTING_FUNCTION = "Multiple Same Named Function";
	
	public static final String UNKNOW_VARIABLE = "Unknow Variable Reference";
	public static final String EXISTING_VARIABLE = "Attemp To Create Already Existing Variable";
	
	public static final String NULL_ELEMENT = "Null Element At Index";
	public static final String OUT_OF_BOUND = "Out Of Bound";
	public static final String NOT_A_SIZE = "Not An Array Size";
	public static final String NOT_A_INDEX = "Not A Valid Index";
	public static final String ARRAY_CAST = "Array Cannot Be Cast";
	
	public static final String CREATE_NULL_VARIABLE = "Cannot Create Null Variable";
	public static final String UNFINISHED_STRING = "Unfinished String";
	public static final String UNFINISHED_EXPRESSION = "Unfinished Expression";
	
	public static final String UNKNOW_INSTRUCTION = "Unknow Instruction Exception";
	public static final String MISSING_ARGUMENTS = "Missing Arguments";
	public static final String UNEXPECTED_ARGUMENTS = "Unexpected arguments";
	public static final String NOT_A_ARRAY = "Argument Is Not An Array";
	
	public static final String UNOPENED_BLOCK = "Unopened Block";
	public static final String MISSING_PREVIOUS_BLOCK = "Missing Previous Block";
	public static final String UNEXPECTED_RETURN = "Not In Function";

	public static final String WRONG_EVALUATION = "Error While Evaluating Expresion";
	
	public static final String WRONG_TYPE = "Wrong Type For Value";
	public static final String UNABLE_TO_CAST = "Unable To Cast";
	
	/**
	 * Create a SIMLA exception with the given message
	 * @param message : the message of the error
	 */
	public SimlaException(String message) 
	{
		super(message + " @" + Instructions.currentLine + " :: " + Instructions.currentInstruction);
	}

}
