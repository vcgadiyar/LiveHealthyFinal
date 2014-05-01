package entities;

public class UserEntity {
	
	String fname;
	String lname;
	int age;
	int hft;
	int hinch;
	int weight;
	
	public UserEntity(String fname, String lname, int age, int hft, int hinch, int weight)
	{
		this.fname = fname;
		this.lname = lname;
		this.age = age;
		this.hft = hft;
		this.hinch = hinch;
		this.weight = weight;
	}
	
	public void setFname(String name)
	{
		this.fname = name;
	}
	
	public void setLname(String name)
	{
		this.lname = name;
	}

	
	public void setAge(int age)
	{
		this.age = age;
	}
	
	public void setHeight(int hft, int hinch)
	{
		this.hft = hft;
		this.hinch = hinch;
	}

	public void setWeight(int weight)
	{
		this.weight = weight;
	}

	public String getFname()
	{
		return this.fname;
	}
	
	public String getLname()
	{
		return this.lname;
	}

	
	public int getAge()
	{
		return this.age;
	}
	
	public int getHft()
	{
		return this.hft;
	}
	
	public int getHinch()
	{
		return this.hinch;
	}

	public int getWeight()
	{
		return this.weight;
	}
	
}
