import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.stream.Stream;


/*
 * use java to read data line by line from a file and count how many words are "india" irrespective of the case.
 */

public class FileReadLineByLine 
{
	
	public static int traditionalApproach(String filename) 
	{
		int count=0;
		
		try(BufferedReader bufferedReader= new BufferedReader(new FileReader(filename))) 
		{
			
			String line; //line will iterate through the paragraph
			while((line=bufferedReader.readLine())!=null) 
			{				
				String[] words =line.split(" "); //split words from lines 
				// into string array 
				for(String word:words) {
					if(word.equalsIgnoreCase("india")) {
						count++;
					}
				}
				
			}
		}
		catch(IOException e) 
		{
			System.out.println(" error reading file: ");  //change to print stack trace
			return 0;
		}
		return count;
	}
	
	public static long functionalApproach(String filename) 
	{
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) 
		{
			Stream<String> linesStream =bufferedReader.lines();

	        long cnt =
	                linesStream
	                    .flatMap(line->Stream.of(line.split(" ")))    //can also use regex --split("\\W+")
	                    .filter(word->word.equalsIgnoreCase("india"))
	                    .count();   //return long  -- so cnt is long
	        return cnt;
        }
		catch (IOException e) 
		{
            System.out.println("Error reading file: " + e.getMessage());
            return 0;
        }
	}
	
 
	public static void main(String[] args) 
	{
		String filename="D:/CHUBB Training/13Nov25/FileHandling/src/India.txt";
		System.out.println("count of 'india' (traditional way) : "+ traditionalApproach(filename));
		System.out.println("count of 'india' using stream api : "+ functionalApproach(filename));
			

		
		
		
	}
}
