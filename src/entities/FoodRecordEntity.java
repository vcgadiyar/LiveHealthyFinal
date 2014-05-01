package entities;

public class FoodRecordEntity {
	
	String timestamp;
	int foodid;
	
	public FoodRecordEntity(int foodid, String timestamp)
	{
		this.foodid = foodid;
		this.timestamp = timestamp;
	}
	
	public void setFoodid(int foodid)
	{
		this.foodid = foodid;
	}
		
	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}
	
	public int getFoodid()
	{
		return this.foodid;
	}

	public String getTimestamp()
	{
		return this.timestamp;
	}
		
}
