package proj1;



/*
 *  Project One: Revamp of TestDB
 *  BY: Wes Barr
 *  Instructor: Dr. Coffey
 *  Course: Advanced Programming COP4027
*/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Log 
{
   private final File fname;
   private final BufferedWriter file;
   
   public Log() throws IOException
   {
        fname = new File("dbOperations.log");

        if(fname == null)
        {
           System.out.println("Fail");
        }
        file = new BufferedWriter(new FileWriter(fname));
   }
   public void writeToFile(String dbStatement) throws IOException
   {
        file.write(dbStatement);
        file.newLine();
   }
   public void closeFile() throws IOException
   {
        file.close();
   }
}
