package interpreter.utils;

import interpreter.exception.SimlaException;
import interpreter.managers.DataManagers.TYPES;

public class Array<T>
{
	private Var<T>[] tab;
	
	@SuppressWarnings("unchecked")
	public Array(int size, TYPES type)
	{
		this.tab = (Var<T>[]) new Var[size];
		
		for(int i = 0; i < this.tab.length; i++)
		{
			this.tab[i] = new Var<T>(type);
		}
	}
	
	public Var<T> get(int index) throws SimlaException
	{
		if(index >= 0 && index < this.tab.length)
		{
			return this.tab[index];
		}
		else
		{
			/*
			 * throw an exception if the index is out of bound
			 */
			throw new SimlaException(SimlaException.OUT_OF_BOUND);
		}
	}
	
	public void set(int index, T value) throws SimlaException
	{
		if(index >= 0 && index < this.tab.length)
		{
			if(this.tab[index].getValue() != null)
			{
				this.tab[index].setValue(value);
			}
			else
			{
				/*
				 * Throw an exception if the element at this index contains nothing
				 */
				throw new SimlaException(SimlaException.NULL_ELEMENT);
			}
		}
		else
		{
			/*
			 * throw an exception if the index is out of bound
			 */
			throw new SimlaException(SimlaException.OUT_OF_BOUND);
		}
	}
	
	public int indexOf(Object value)
	{
		boolean trouve = false;
		int i = 0;
		
		while(i < tab.length && !trouve)
		{
			if(tab[i].getValue().equals(value))
			{
				trouve = true;
			}
			else
			{
				i++;
			}
		}
		
		return i < tab.length ? i : -1;
	}
	
	public int getSize()
	{
		return this.tab.length;
	}
}
