import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReadingUsingFunctionalProg 
{
    public static void main(String[] args) 
    {
	
		try 
		{
//			//long count because .count() will return long -- always use long when working with stream counts
  
	        long count =
	                Files.readAllLines(Paths.get("D:/CHUBB Training/13Nov25/FileHandling/src/India.txt"))     // read all lines--returns List<String>
	                     .stream()             //convert to stream
	                     .flatMap(line ->      //break into words
	                             Arrays.stream(line.split(" "))
	                     )
	                     .filter(word -> word.equalsIgnoreCase("india"))
	                     .count();

	        System.out.println("The total count of the word India is: " + count);
	        
	        
	    } 
		catch (IOException e) 
		{
	        System.out.println("Error reading file: " + e.getMessage());
	    }

    }
}
