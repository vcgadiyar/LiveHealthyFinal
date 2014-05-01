package entities;

public class ExerciseEntity {
	
	String name;
	int caloriespermin;
	
	public ExerciseEntity(String name, int caloriespermin)
	{
		this.name = name;
		this.caloriespermin = caloriespermin;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public void setCalories(int caloriespermin)
	{
		this.caloriespermin = caloriespermin;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getCaloriesPerMin()
	{
		return this.caloriespermin;
	}
	
}
