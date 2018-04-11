package interpreter.utils.register;

import interpreter.KEYWORDS;
import interpreter.exception.SimlaException;

public abstract class AbstractRegister 
{
	protected String target;
	protected String source;
	protected String codeRegistered;
	
	public AbstractRegister(String source, String target) 
	{
		this.source = source;
		this.codeRegistered = "";
		this.target = target;
	}
	
	public abstract void execute() throws SimlaException;

	public String getCodeRegistered()
	{
		return codeRegistered;
	}
	
	public void addCodeRegistered(String append)
	{
		this.codeRegistered += append + KEYWORDS.INSTRUCTION_SEPARATOR;
	}
	
	public String getTarget()
	{
		return this.target;
	}
	
	public String getSource()
	{
		return this.source;
	}
}
