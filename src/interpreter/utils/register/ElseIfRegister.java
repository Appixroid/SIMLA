package interpreter.utils.register;

import interpreter.Interpreter;
import interpreter.exception.SimlaException;
import interpreter.instructions.Instructions;

public class ElseIfRegister extends AbstractRegister
{	
	private boolean execute;

	public ElseIfRegister(String source, String target, boolean execute) 
	{
		super(source, target);
		this.execute = execute;
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
		else if(Instructions.lastCondition == false && this.execute)
		{
			Interpreter.read(this.codeRegistered, Instructions.currentLine);
			Instructions.lastCondition = this.execute;
		}
	}
}
