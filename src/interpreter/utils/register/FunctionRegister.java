package interpreter.utils.register;

import interpreter.exception.SimlaException;
import interpreter.instructions.Instructions;
import interpreter.managers.DataManagers.TYPES;
import interpreter.managers.FuncManager;
import interpreter.utils.Function;

public class FunctionRegister extends AbstractRegister
{	
	private Function function;
	
	public FunctionRegister(String source, String target, String label, String returnType, String[] args) 
	{
		super(source, target);
		
		this.function = new Function(label, TYPES.getTypes(returnType), TYPES.isArray(returnType), Instructions.currentLine, args);
	}

	@Override
	public void execute() throws SimlaException
	{				
		this.function.addCode(this.codeRegistered);
		FuncManager.newFunc(this.function.getLabel(), this.function);
	}
}
