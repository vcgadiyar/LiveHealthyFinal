package entities;

public class ExerciseRecordEntity {
	
	String timestamp;
	int tot_calories;
	int workoutid;
	int duration;
	
	public ExerciseRecordEntity(int workoutid, int duration, int tot_calories, String timestamp)
	{
		this.workoutid = workoutid;
		this.duration = duration;
		this.tot_calories = tot_calories;
		this.timestamp = timestamp;
	}
	
	public void setWorkoutid(int workoutid)
	{
		this.workoutid = workoutid;
	}

	public void setDuration(int duration)
	{
		this.duration = duration;
	}
	
	public void setTotalCalories(int tot_calories)
	{
		this.tot_calories = tot_calories;
	}
		
	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}
	
	public int getWorkoutid()
	{
		return this.workoutid;
	}

	public int getDuration()
	{
		return this.duration;
	}
	
	public int getTotalCalories()
	{
		return this.tot_calories;
	}
		
	public String getTimestamp()
	{
		return this.timestamp;
	}
		
}
