package entities;

public class ExerciseRecordEntity {
	
	long timestamp;
	int tot_calories;
	String workoutid;
	int duration;
	
	public ExerciseRecordEntity(String workoutid, int duration, int tot_calories, long timestamp)
	{
		this.workoutid = workoutid;
		this.duration = duration;
		this.tot_calories = tot_calories;
		this.timestamp = timestamp;
	}
	
	public void setWorkoutid(String workoutid)
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
		
	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}
	
	public String getWorkoutName()
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
		
	public long getTimestamp()
	{
		return this.timestamp;
	}
		
}
