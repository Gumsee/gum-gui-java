package com.gumse.basics;

import com.gumse.tools.Debug;
import com.gumse.tools.FPS;

public class SmoothFloat
{
	private float speed = 5.0f;
	private float target = 0.0f;
	private float actual = 0.0f;
	private float min = Float.MIN_VALUE;
	private float max = Float.MAX_VALUE;
	
	public SmoothFloat() {};
	public void cleanup() {};

	public SmoothFloat(float target, float speed, float initialValue) 
	{
		this.target = target;
		this.actual = initialValue;
		this.speed = speed;
	}

	public boolean update() {
		float offset = target - actual;
		float change = offset * FPS.getFrametime() * speed;
		actual += change;

        boolean ret = actual != target;
		
		boolean reachedEnd = offset < 0.01 && offset > -0.01;
		if(reachedEnd)
			actual = target;

        return ret;
	}
	
	public void setTarget(float target) 			
	{ 
		this.target = target; 
		if(this.target < min) { this.target = min; }
		if(this.target > max) { this.target = max; }
	}


	public void increaseTarget(float target) 	{ setTarget(this.target + target); }
	public void instantIncrease(float increase) { this.actual += increase; }
	public void set(float val) 				    { this.actual = val; }
	public void setMin(float min) 				{ this.min = min; }
	public void setMax(float max) 				{ this.max = max; }
	public void reset() 						{ this.actual = 0; }
	public void setSpeed(float speed) 			{ this.speed = speed; }

    public float getPercentage()                { return this.actual / this.max; }
	public float getMax() 						{ return this.max; }
	public float get() 							{ return this.actual; }
	public float getTarget() 					{ return this.target; }
	public float getMin() 						{ return this.min; }
};