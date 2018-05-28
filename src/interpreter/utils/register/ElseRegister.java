package interpreter.utils.register;

import interpreter.Interpreter;
import interpreter.exception.SimlaException;
import interpreter.instructions.Instructions;

public class ElseRegister extends AbstractRegister
{	
	public ElseRegister(String source, String target) 
	{
		super(source, target);
	}

	@Override
	public void execute() throws SimlaException
	{						
		if(Instructions.lastCondition == null)
		{
			/*
			 * Throw an exception if there is no previous if
			 */
			throw new SimlaException(SimlaException.MISSING_PREVIOUS_BLOCK);
		}
		else if(Instructions.lastCondition == false)
		{
			Interpreter.read(this.codeRegistered, Instructions.currentLine);
			Instructions.lastCondition = null;
		}
	}
}
