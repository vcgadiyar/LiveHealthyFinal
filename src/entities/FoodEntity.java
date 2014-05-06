package entities;

public class FoodEntity extends Entity {
	
//	String name;
	int calories;
	String barcode;
	
	public FoodEntity(String name, int calories, String barcode)
	{
		this.name = name;
		this.calories = calories;
		this.barcode = barcode;
	}
	
//	public void setName(String name)
//	{
//		this.name = name;
//	}
	
	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
	}

	public void setCalories(int calories)
	{
		this.calories = calories;
	}
	
//	public String getName()
//	{
//		return this.name;
//	}
	
	public int getCalories()
	{
		return this.calories;
	}
	
	public String getBarcode()
	{
		return this.barcode;
	}
	
}
