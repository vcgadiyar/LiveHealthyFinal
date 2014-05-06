package entities;

public abstract class Entity implements EntityInterface{
	
	String name;
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
}
