/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proj1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author hammond13
 */
public class pLog 
{
   private final File pname;
   private final BufferedWriter file;
   
   public pLog() throws IOException
   {
        pname = new File("dbOperations2.log");

        if(pname == null)
        {
           System.out.println("Fail");
        }
        file = new BufferedWriter(new FileWriter(pname));
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

