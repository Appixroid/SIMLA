package interpreter.instructions;

import interpreter.KEYWORDS;
import interpreter.exception.SimlaException;
import interpreter.utils.register.AbstractRegister;

public class Instructions 
{
	
	public static String currentInstruction = "";
	public static int currentLine = 0;
	
	public static boolean hasToRegister = false;
	public static AbstractRegister register = null;
	public static int targetExpected = 0;
	
	public static Boolean lastCondition = (Boolean) null;
	
	public static boolean inFunction = false;
	public static String returnLabel = null;

	/**
	 * Interprate the instruction according the start of the instruction
	 * @param instruction : the instruction line
	 * @param line : the line number
	 * @throws SimlaException
	 */
	public static void interpretate(String instruction, int line) throws SimlaException
	{		
		if(!hasToRegister)
		{
			currentInstruction = instruction;
			currentLine = line;
		}
		
		instruction = deleteBlank(instruction);
		
		if(hasToRegister)
		{			
			
			if(instruction.startsWith(register.getTarget()))
			{
				targetExpected--;
				if(targetExpected > 0)
				{
					register.addCodeRegistered(instruction);
				}
			}
			else if(instruction.startsWith(register.getSource()))
			{
				targetExpected++;
				register.addCodeRegistered(instruction);
			}
			else
			{
				register.addCodeRegistered(instruction);
			}
			
			if(targetExpected == 0)
			{
				hasToRegister = false;
				register.execute();
				register = null;
				targetExpected = 0;
			}
			else if(targetExpected < 0)
			{
				throw new SimlaException(SimlaException.UNOPENED_BLOCK);
			}
		}	
		else if(!instruction.replaceAll(" ", "").isEmpty())
		{			
			/**
			 * The instruction type
			 */
			String instructionType = instruction.split(KEYWORDS.ARGUMENT_SEPARATOR)[0];
			
			if(!instructionType.startsWith(KEYWORDS.COM_INSTRUCTION))
			{
				instruction = DataInstruction.parseParenthesis(instruction);
			}
			
			switch(instructionType)
			{
				case KEYWORDS.COM_INSTRUCTION:
					comInstruction(instruction);
					break;
					
				case KEYWORDS.VAR_INSTRUCTION:
					DataInstruction.varInstruction(instruction);
					lastCondition = null;
					break;
					
				case KEYWORDS.TAB_INSTRUCTION:
					DataInstruction.tabInstruction(instruction);
					lastCondition = null;
					break;
					
				case KEYWORDS.CALC_INSTRUCTION:
					DataInstruction.calcInstruction(instruction);
					lastCondition = null;
					break;
					
				case KEYWORDS.CALL_INSTRUCTION:
					DataInstruction.callInstruction(instruction);
					lastCondition = null;
					break;
					
				case KEYWORDS.IF_INSTRUCTION:
					ControlInstruction.ifInstruction(instruction);
					break;
					
				case KEYWORDS.ELSE_IF_INSTRUCTION:
					ControlInstruction.elseIfInstruction(instruction);
					break;
					
				case KEYWORDS.ELSE_INSTRUCTION:
					ControlInstruction.elseIntruction(instruction);
					break;
					
				case KEYWORDS.LOOP_INSTRUCTION:
					ControlInstruction.loopInstruction(instruction);
					lastCondition = null;
					break;
					
				case KEYWORDS.FOR_INSTRUCTION:
					ControlInstruction.forInstruction(instruction);
					lastCondition = null;
					break;
					
				case KEYWORDS.FUNC_INSTRUCTION:
					DataInstruction.funcInstruction(instruction);
					lastCondition = null;
					break;
					
				case KEYWORDS.FUNC_RETURN:
					DataInstruction.returnInstruction(instruction);
					break;
					
				case KEYWORDS.CAST_INSTRUCTION:
					DataInstruction.castInstruction(instruction);
					break;
					
				default:
					/**
					 * Exception if the instruction doesn't exist
					 */
					throw new SimlaException(SimlaException.UNKNOW_INSTRUCTION);
			}
		}
	}

	/**
	 * Use if the line is a commentary (Do nothing)
	 * @param line : the commentary line
	 */
	private static void comInstruction(String line) 
	{
		//System.err.println(line);
	}

	/**
	 * Delete starting and ending blank of a line
	 * @param instruction
	 * @return
	 */
	public static String deleteBlank(String instruction)
	{
		int i = 0;
		while(i < instruction.length() && !Character.isAlphabetic(instruction.charAt(i)))
		{
			instruction = instruction.substring(1, instruction.length());
			i++;
		}
		
		/*i = instruction.length() - 1;
		while(i >= 0 && !Character.isAlphabetic(instruction.charAt(i)))
		{
			instruction = instruction.substring(0, i);
			i--;
		}*/
		return instruction;
	}
}
