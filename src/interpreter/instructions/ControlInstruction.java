package interpreter.instructions;

import interpreter.KEYWORDS;
import interpreter.exception.SimlaException;
import interpreter.utils.register.ElseIfRegister;
import interpreter.utils.register.ElseRegister;
import interpreter.utils.register.ForRegister;
import interpreter.utils.register.IfRegister;
import interpreter.utils.register.LoopRegister;

public class ControlInstruction 
{
	/**
	 * Interpretate the "if" instruction for condition
	 * @param instruction : the if intruction
	 * @throws SimlaException
	 */
	public static void ifInstruction(String instruction) throws SimlaException
	{
		String expression = "";
		String[] elements = instruction.split(" ");
		
		for(int i = 1; i < elements.length; i++)
		{
			expression += elements[i] + " ";
		}
		
		boolean condition = (boolean) DataInstruction.evaluateExpression(expression).getValue();
		
		Instructions.register = new IfRegister(KEYWORDS.IF_INSTRUCTION, KEYWORDS.IF_END, condition);
		Instructions.hasToRegister = true;
		Instructions.targetExpected = 1;
	}
	
	/**
	 * Interpretate the "else if" instruction to continue a condition
	 * @param instruction : the else if condition
	 * @throws SimlaException
	 */
	public static void elseIfInstruction(String instruction) throws SimlaException
	{
		String expression = "";
		String[] elements = instruction.split(" ");
		
		for(int i = 1; i < elements.length; i++)
		{
			expression += elements[i] + " ";
		}
		
		boolean condition = (boolean) DataInstruction.evaluateExpression(expression).getValue();
		
		Instructions.register = new ElseIfRegister(KEYWORDS.ELSE_IF_INSTRUCTION, KEYWORDS.ELSE_IF_END, condition);
		Instructions.hasToRegister = true;
		Instructions.targetExpected = 1;
	}
	
	/**
	 * Interpretate the "else" instruction to end a condition
	 * @param instruction : the else instruction
	 */
	public static void elseIntruction(String instruction)
	{
		Instructions.register = new ElseRegister(KEYWORDS.ELSE_INSTRUCTION, KEYWORDS.ELSE_END);
		Instructions.hasToRegister = true;
		Instructions.targetExpected = 1;
	}
	
	/**
	 * Interpretate a "loop" instruction to make a while loop
	 * @param instruction : the loop instruction
	 * @throws SimlaException
	 */
	public static void loopInstruction(String instruction) throws SimlaException
	{
		String expression = "";
		String[] elements = instruction.split(" ");
		
		for(int i = 1; i < elements.length; i++)
		{
			expression += elements[i] + " ";
		}
		
		Instructions.register = new LoopRegister(KEYWORDS.LOOP_INSTRUCTION, KEYWORDS.LOOP_END, expression);
		Instructions.hasToRegister = true;
		Instructions.targetExpected = 1;
	}
	
	/**
	 * Interpretate a "for" instruction to make a for loop
	 * @param instruction : the for instruction
	 * @throws SimlaException
	 */
	public static void forInstruction(String instruction) throws SimlaException
	{
		String[] elements = instruction.split(" ");
		
		if(elements[2].equals(KEYWORDS.ASSIGNMENT_KEYWORD))
		{
			String[] values = elements[1].split(KEYWORDS.FOR_SEPARATOR);
			if(values.length == 3)
			{
				String init = "var " + values[0] + " in " + elements[3] + " typed Integer\n";
				String step = "calc " + elements[3] + " + " + values[1] + " in " + elements[3] + "\n";
				String expression = elements[3] + " < " + values[2];
				
				Instructions.register = new ForRegister(KEYWORDS.FOR_INSTRUCTION, KEYWORDS.FOR_END, init, expression, step, elements[3]);
				Instructions.hasToRegister = true;
				Instructions.targetExpected = 1;
			}
			else
			{
				/*
				 * Throw an exception if the instruction is malformed
				 */
				throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
			}
		}
		else
		{
			/*
			 * Throw an Exception if the expression is malformed
			 */
			throw new SimlaException(SimlaException.MISSING_ARGUMENTS);
		}
	}
}
