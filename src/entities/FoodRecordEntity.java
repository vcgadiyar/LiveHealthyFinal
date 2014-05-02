package entities;

public class FoodRecordEntity {
	
	long timestamp;
	String foodname;
	int calories;
	
	public FoodRecordEntity(String foodid, long timestamp, int calories)
	{
		this.foodname = foodid;
		this.timestamp = timestamp;
		this.calories = calories;
	}
	
	public void setFoodName(String foodname)
	{
		this.foodname = foodname;
	}
	
	public void setCalories(int calories)
	{
		this.calories = calories;
	}
		
	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}
	
	public String getFoodName()
	{
		return this.foodname;
	}

	public long getTimestamp()
	{
		return this.timestamp;
	}
	
	public int getCalories()
	{
		return this.calories;
	}
		
}
