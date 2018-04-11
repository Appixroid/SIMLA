package interpreter.utils.register;

import interpreter.Interpreter;
import interpreter.exception.SimlaException;
import interpreter.instructions.DataInstruction;
import interpreter.managers.DataManagers;

public class ForRegister extends AbstractRegister
{
	private String init;
	private String expression;
	private String step;
	private String varToDelete;
	
	public ForRegister(String source, String target, String init, String expression, String step, String varToDelete) 
	{
		super(source, target);
		this.expression = expression;
		this.init = init;
		this.step = step;
		this.varToDelete = varToDelete;
	}

	@Override
	public void execute() throws SimlaException
	{	
		Interpreter.read(init);
		
		this.codeRegistered += step;
		try
		{
			while((boolean) DataInstruction.evaluateExpression(expression).getValue())
			{
				Interpreter.read(this.codeRegistered);
			}	
		} catch(ClassCastException e)
		{
			/**
			 * Throw an exception if the expression cannot be evaluated
			 */
			throw new SimlaException(SimlaException.WRONG_EVALUATION);
		}
		
		DataManagers.delVar(this.varToDelete);
	}	
}