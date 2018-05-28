package interpreter.utils.register;

import interpreter.Interpreter;
import interpreter.exception.SimlaException;
import interpreter.instructions.DataInstruction;
import interpreter.instructions.Instructions;

public class LoopRegister extends AbstractRegister
{
	private String expression;
	
	public LoopRegister(String source, String target, String expression) 
	{
		super(source, target);
		this.expression = expression;
	}

	@Override
	public void execute() throws SimlaException
	{	
		try
		{
			while((boolean) DataInstruction.evaluateExpression(expression).getValue())
			{
				Interpreter.read(this.codeRegistered, Instructions.currentLine);
			}	
		} catch(ClassCastException e)
		{
			/**
			 * Throw an exception if the expression cannot be evaluated
			 */
			throw new SimlaException(SimlaException.WRONG_EVALUATION);
		}
	}	
}