package interpreter.utils.register;

import interpreter.Interpreter;
import interpreter.exception.SimlaException;
import interpreter.instructions.Instructions;

public class IfRegister extends AbstractRegister
{
	private boolean execute;
	
	public IfRegister(String source, String target, boolean execute) 
	{
		super(source, target);
		this.execute = execute;
	}

	@Override
	public void execute() throws SimlaException
	{				
		if(this.execute)
		{
			Interpreter.read(this.codeRegistered, Instructions.currentLine);
		}
		
		Instructions.lastCondition = this.execute;
	}
}
