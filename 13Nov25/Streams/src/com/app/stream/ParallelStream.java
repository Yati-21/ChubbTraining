package com.app.stream;

import java.util.stream.LongStream;

public class ParallelStream 
{

	public static void main(String[] args) 
	{
		long time = System.currentTimeMillis();
		
		//parallel stream
		System.out.println(LongStream.range(0, 1000000000).parallel().sum());
		System.out.println(System.currentTimeMillis()-time);

		System.out.println();
		
		//sequential
		System.out.println(LongStream.range(0, 1000000000).sum());
		System.out.println(System.currentTimeMillis()-time);
	}

}
