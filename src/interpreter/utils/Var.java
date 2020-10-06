package interpreter.utils;

import interpreter.managers.DataManagers.TYPES;

public class Var<T>
{
	/**
	 * The Type of the var
	 */
	private TYPES type;
	
	/**
	 * The value of the var
	 */
	private T value;
	
	public Var()
	{
		this(TYPES.Null);
	}
	
	public Var(TYPES type)
	{
		this(type, null);
	}
	
	public Var(TYPES type, T value)
	{
		this.type = type;
		this.value = value;
	}
	
	public T getValue()
	{
		return this.value;
	}
	
	public TYPES getType()
	{
		return this.type;
	}

	public void setType(TYPES type) 
	{
		this.type = type;
	}

	public void setValue(T value) 
	{
		this.value = value;
	}
}
