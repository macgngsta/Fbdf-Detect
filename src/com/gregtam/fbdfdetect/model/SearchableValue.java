package com.gregtam.fbdfdetect.model;

/**
 * This class will be in a MAP with the id as key
 * 
 * @author gtam
 * 
 */
public class SearchableValue implements Comparable<Object>
{
	private long fbId;

	private String fullName;

	private boolean touched;

	public SearchableValue(Long key, String name)
	{
		this.fbId = key;
		this.fullName = name;
		this.touched = false;
	}

	public long getKey()
	{
		return fbId;
	}

	public void setKey(long key)
	{
		this.fbId = key;
	}

	public String getFullName()
	{
		return fullName;
	}

	public boolean isTouched()
	{
		return touched;
	}

	public void setFullName(String name)
	{
		this.fullName = name;
	}

	public void setTouched(boolean touched)
	{
		this.touched = touched;
	}

	@Override
	public String toString()
	{
		return "SearchableValue [name=" + fullName + ", touched=" + touched
				+ "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + (touched ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchableValue other = (SearchableValue) obj;
		if (fullName == null)
		{
			if (other.fullName != null)
				return false;
		}
		else if (!fullName.equals(other.fullName))
			return false;
		if (touched != other.touched)
			return false;
		return true;
	}

	@Override
	public int compareTo(Object input) throws ClassCastException
	{
		if (!(input instanceof SearchableValue))
		{
			throw new ClassCastException("not of Searchable class");
		}

		return this.fullName.compareTo(((SearchableValue) input).getFullName());
	}
}
