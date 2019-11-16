/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proj1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author hammond13
 */
public final class PDatabase 
{
   pLog plog = new pLog();
   ParkingLot v = new ParkingLot();
   Class reflect = v.getClass();
   Class input;
   ArrayList<ParkingLot> vehicles = new ArrayList<>();
   ArrayList<Field> fields;
   Field[] field; 
  
   log log = new log();
   Statement stat;
   ResultSet result;
   Connection conn;
   private String sqlStatement;
   private String tableName;
   private String paramName;
   private String paramType;
   private static int colCount;
   private boolean isBadVar;
   ArrayList<String> logs;
   ArrayList<String> cap;
   ArrayList<String> max;

   public PDatabase() throws IOException
   {  
       this.plog = new pLog();
       this.sqlStatement = "";
       this.tableName = "";
       stat = null;
       conn = null;
       colCount = 0;
   }
   public PDatabase(String tableName) throws IOException
   {  
       this.plog = new pLog();
       this.sqlStatement = "";
       setTableName(tableName);
       stat = null;
       conn = null;
       colCount = 0;
       writeDatFile();
   }
   
   public void writeDatFile() throws IOException
   {
       field = reflect.getDeclaredFields();
       fields = new ArrayList<>(Arrays.asList(field));
       
       FileOutputStream fout = new FileOutputStream("Parking.dat");
       try (ObjectOutputStream out = new ObjectOutputStream(fout))
       {
           for(int i = 0; i < 10; i++)
           {
               vehicles.add(new ParkingLot());
               out.writeObject(vehicles.get(i));
           }
           out.flush();
       }
       
   }
   
   public Statement openDataBase()
   {
       try
       {
           SimpleDataSource.init("database.properties");
           conn = SimpleDataSource.getConnection();
           stat = conn.createStatement();  
       }
       catch(IOException | ClassNotFoundException | SQLException e)
       {
           System.out.println("Could not open Database");
       }
      
       return stat;
   }
   
   public void closeDB() throws IOException
   {
       try
       {
           conn.close();
           plog.closeFile();
       }
       catch (SQLException e)
       {
           System.out.println("Error closing Database");
       }
   }
  
   public void createTable()
   {
       setSqlStatement("CREATE TABLE " + getTableName());
   }
   public void dropTable() throws IOException
   {
       setSqlStatement("DROP TABLE " + getTableName());
       plog.writeToFile(getSqlStatement());
      
       try
       {
           stat.execute(getSqlStatement());
       }
       catch (SQLException e) 
       {
           System.out.println("\nInitial drop failed\n");
           plog.writeToFile("ERROR: Initial drop failed: " + getSqlStatement());
       }
   }
   public void buildTable() throws IOException
   {
       for(int i = 0; i < fields.size(); i++)
       {
           fields.get(i).setAccessible(true);
           isBadVar = false;
           if(fields.get(i).toString().contains("String"))
           {
               paramName = fields.get(i).getName();
               paramType = "CHAR(20)";
           }
           else if(fields.get(i).toString().contains("int"))
           {
               paramName = fields.get(i).getName();
               paramType = "VARCHAR(7)";
           }
           else if(fields.get(i).toString().contains("boolean"))
           {
               paramName = fields.get(i).getName();
               paramType = "CHAR(10)";
           }
           else
           {
               paramName = fields.get(i).getName();
               paramType = fields.get(i).toString();
               fields.remove(i);
               isBadVar = true;
               i--;
               plog.writeToFile("ERROR: " + paramName + " of is of an invalid data type (" + paramType + ")");
           }
          
           if(!isBadVar)
           {
               if(colCount == 0)
               {
                   setSqlStatement(getSqlStatement() + " (" + paramName + " " + paramType + ")");
               }
               else
               {
                   setSqlStatement("ALTER TABLE " + getTableName() + " ADD " + paramName + " " + paramType);
               }
              
               try
               {
                   stat.execute(getSqlStatement());
                   plog.writeToFile(getSqlStatement());  
               }
               catch (SQLException e)
               {
                   System.out.println("Error Adding Column");
                   plog.writeToFile("ERROR: " + getSqlStatement());
               }
               finally
               {
                   colCount++;
               }  
           }
       }
   }
   public void PinsertItems() throws FileNotFoundException, IOException, ClassNotFoundException, NoSuchFieldException, ClassCastException
   {
                          String output = "";
       try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("Parking.dat"))) 
       {
           int numOfEntries = 0;
           int k = 1;
           while(numOfEntries < 10)
           {
               try
               {
                   ParkingLot veh = (ParkingLot) in.readObject();
                   String s = veh.getName();
                   int sty = veh.getSize();
                   int cap = veh.getCapacity();
                   boolean bool = veh.getIsFull();
                   String column = "";
                   String vals = "";
                   String pName ="{\"LOT_NAME\": \"";
                   String curr_amount = "\"curr_capacity\": \"";
                   String max_capacity = "\"max_capacity\": \"";
                   
                   for(int i = 0; i < fields.size(); i++)
                   {
                       paramName = fields.get(i).getName();
                       
                       if(i < (fields.size() - 1))
                       {
                           column += paramName + ", ";
                       }
                       else
                       {
                           column += paramName;
                       }
                       
                       if(fields.get(i).toString().contains("name"))
                       {
                           if(i < (fields.size() - 1))
                           {
                               vals = vals + "'" + s + "', ";
                               output = output + pName + s + "\",";
                           }
                           else
                           {
                               vals = vals + "'" + s + "'";
                               output = output + pName + s + "\",";
                           }
                       }
                       else if(fields.get(i).toString().contains("size"))
                       {
                           if(i < (fields.size() - 1))
                           {
                               vals = vals + "'" + sty + "', ";
                               output = output + curr_amount + sty + "\",";
                           }
                           else
                           {
                               vals = vals + "'" + sty + "'";
                               output = output + curr_amount + sty + "\",";
                           }
                       }
                       else if(fields.get(i).toString().contains("Capacity"))
                       {
                           if(i < (fields.size() - 1))
                           {
                               vals = vals + "'" + cap + "', ";
                               output = output + max_capacity + cap + "\"}";
                           }
                           else
                           {
                               vals = vals + "'" + cap + "'";
                               output = output + max_capacity + cap + "\"}";
                           }
                       }
                       else if(fields.get(i).toString().contains("isFull"))
                       {
                           if(i < (fields.size() - 1))
                           {
                               vals = vals + "'" + bool + "', ";
                           }
                           else
                           {
                               vals = vals + "'" + bool + "'";
                           }
                       }
                   }
                   setSqlStatement("INSERT INTO "+ getTableName() + " (" + column + ") VALUES (" + vals + ")");
                            
                   try
                   {
                       stat.execute(getSqlStatement());
                       plog.writeToFile(getSqlStatement());
                   }
                   catch (SQLException e)
                   {
                       System.out.println("Error Inserting Row");
                       plog.writeToFile("ERROR: " + getSqlStatement());
                   }
                   numOfEntries++;
               }catch(IOException e){}
               
           }
       }
       File fout = new File("ParkingLots.txt");
	FileOutputStream fos = new FileOutputStream(fout);
 
	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(output);
        bw.close();
      
    }
    
   public void getResults() throws SQLException, IOException
   {
       setSqlStatement("SELECT * FROM " + getTableName());// + "WHERE make = 'Chevy' or make = 'Toyota'");
       plog.writeToFile(getSqlStatement());
       try
       {
           result = stat.executeQuery(getSqlStatement());
       }
       catch (SQLException e)
       {
           System.out.println("Error getting result set");
           plog.writeToFile("ERROR: " + getSqlStatement());
       }
      
       ResultSetMetaData rsm = result.getMetaData();
      
       int cols = rsm.getColumnCount();
      
       while(result.next())
       {
           for(int i = 1; i <= cols; i++)
           {
               System.out.print(result.getString(i)+" ");
           }  
          
           System.out.println("");    
       }
   }
   
   public void getTheHeavies() throws SQLException, IOException
   {
       setSqlStatement("SELECT * FROM " + getTableName() + " WHERE weight > 2500");
       plog.writeToFile(getSqlStatement());
       try
       {
           result = stat.executeQuery(getSqlStatement());
       }
       catch (SQLException e)
       {
           System.out.println("Error getting result set");
           plog.writeToFile("ERROR: " + getSqlStatement());
       }
      
       ResultSetMetaData rsm = result.getMetaData();
      
       int cols = rsm.getColumnCount();
      
       while(result.next())
       {
           for(int i = 1; i <= cols; i++)
           {
               System.out.print(result.getString(i)+" ");
           }  
          
           System.out.println("");    
       }
   }
   
   public void getChevyAndToyota() throws SQLException, IOException
   {
       setSqlStatement("SELECT * FROM " + getTableName() + " WHERE make = 'Chevy' or make = 'Toyota'");
       plog.writeToFile(getSqlStatement());
       try
       {
           result = stat.executeQuery(getSqlStatement());
       }
       catch (SQLException e)
       {
           System.out.println("Error getting result set");
           plog.writeToFile("ERROR: " + getSqlStatement());
       }
      
       ResultSetMetaData rsm = result.getMetaData();
      
       int cols = rsm.getColumnCount();
      
       while(result.next())
       {
           for(int i = 1; i <= cols; i++)
           {
               System.out.print(result.getString(i)+" ");
           }  
          
           System.out.println("");    
       }
   }
   
   public String getSqlStatement()
   {
       return sqlStatement;
   }
   public void setSqlStatement(String sqlStatement)
   {
       this.sqlStatement = sqlStatement;
   }
   public String getTableName()
   {
       return tableName;
   }
   public void setTableName(String tableName)
   {
       this.tableName = tableName;
   }
}
