package com.performance;

public class Worker extends Thread{
	int a,b;
	
	static int sum(int a, int b)
	{
		sum(1,2); 
		return a+b;
	}
	
	@Override
	public void run() 
	{
		sum(1,2);
	}

}
