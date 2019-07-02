/**
 * 
 */
package edu.iastate.cs228.hw5;

/**
 * I'm going to use this simulate creating splay trees, so that I don't have to do the more difficult
 * String comparison, I'll just use integers instead
 * @author Aaron
 *
 */
public class NumericData implements Comparable<NumericData> {
	
	public int data;
	
	public NumericData(int data)
	{
		this.data = data;
	}

	@Override
	public int compareTo(NumericData other)
	{
		return (data > other.data) ? 1 : (data < other.data) ? -1 : 0;  
	}
	
	@Override
	public String toString()
	{
		return "" + data;
	}

}
