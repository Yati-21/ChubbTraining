package com.demo;

public class Main 
{
	public static void main(String[] args) throws InterruptedException
	{
		Thread virtualThread = Thread.ofVirtual().name("MyVirtualThread").start(()->{
			System.out.println("virtual thread running: "+Thread.currentThread().getName());
			try
			{
				Thread.sleep(1000); //simulate work
			}
			catch(InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
		});
		virtualThread.join(); //wait for thread for finish
		System.out.println("virtual thread finished");
	}

}
