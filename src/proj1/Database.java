package proj1;



/*
 *  Project One: Revamp of TestDB
 *  BY: Wes Barr
 *  Instructor: Dr. Coffey
 *  Course: Advanced Programming COP4027
*/

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public final class Database 
{
   Log log;
   Vehicle v = new Vehicle();
   Class reflect = v.getClass();
   Class input;
   ArrayList<Vehicle> vehicles = new ArrayList<>();
   ArrayList<Field> fields;
   Field[] field; 
  
   Statement stat;
   ResultSet result;
   Connection conn;
   private String sqlStatement;
   private String tableName;
   private String paramName;
   private String paramType;
   private static int colCount;
   private boolean isBadVar;
  

   public Database() throws IOException
   {  
       this.log = new Log();
       this.sqlStatement = "";
       this.tableName = "";
       stat = null;
       conn = null;
       colCount = 0;
   }
   public Database(String tableName) throws IOException
   {  
       this.log = new Log();
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
       
       FileOutputStream fout = new FileOutputStream("Vehicles.dat");
       try (ObjectOutputStream out = new ObjectOutputStream(fout))
       {
           for(int i = 0; i < 10; i++)
           {
               vehicles.add(new Vehicle());
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
           log.closeFile();
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
       log.writeToFile(getSqlStatement());
      
       try
       {
           stat.execute(getSqlStatement());
       }
       catch (SQLException e) 
       {
           System.out.println("\nInitial drop failed\n");
           log.writeToFile("ERROR: Initial drop failed: " + getSqlStatement());
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
           else if(fields.get(i).toString().contains("double"))
           {
               paramName = fields.get(i).getName();
               paramType = "DECIMAL(7, 2)";
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
               log.writeToFile("ERROR: " + paramName + " of is of an invalid data type (" + paramType + ")");
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
                   log.writeToFile(getSqlStatement());  
               }
               catch (SQLException e)
               {
                   System.out.println("Error Adding Column");
                   log.writeToFile("ERROR: " + getSqlStatement());
               }
               finally
               {
                   colCount++;
               }  
           }
       }
   }
   public void insertItems() throws FileNotFoundException, IOException, ClassNotFoundException, NoSuchFieldException
   {
       try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("Vehicles.dat"))) 
       {
           int numOfEntries = 0;
           while(numOfEntries < 10)
           {
               try
               {
                   Vehicle veh = (Vehicle)in.readObject();
                   String s = veh.getMake();
                   String sty = veh.getStyle();
                   boolean bool = veh.getImport();
                   double dub = veh.getEngineSize();
                   double dubb = veh.getWeight();
                   String column = "";
                   String vals = "";
                   
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
                       
                       if(fields.get(i).toString().contains("make"))
                       {
                           if(i < (fields.size() - 1))
                           {
                               vals += "'" + s + "', ";
                           }
                           else
                           {
                               vals += "'" + s + "'";
                           }
                       }
                       else if(fields.get(i).toString().contains("style"))
                       {
                           if(i < (fields.size() - 1))
                           {
                               vals += "'" + sty + "', ";
                           }
                           else
                           {
                               vals += "'" + sty + "'";
                           }
                       }
                       else if(fields.get(i).toString().contains("boolean"))
                       {
                           if(i < (fields.size() - 1))
                           {
                               vals += "'" + bool + "', ";
                           }
                           else
                           {
                               vals += "'" + bool + "'";
                           }
                       }
                       else if(fields.get(i).toString().contains("weight"))
                       {
                           if(i < (fields.size() - 1))
                           {
                               vals += dubb + ", ";
                           }
                           else
                           {
                               vals += dubb;
                           }
                       }
                       else if(fields.get(i).toString().contains("engineSize"))
                       {
                           if(i < (fields.size() - 1))
                           {
                               vals += dub + ", ";
                           }
                           else
                           {
                               vals += dub;
                           }
                       }
                   }
                   setSqlStatement("INSERT INTO "+ getTableName() + " (" + column + ") VALUES (" + vals + ")");
                   try
                   {
                       stat.execute(getSqlStatement());
                       log.writeToFile(getSqlStatement());
                   }
                   catch (SQLException e)
                   {
                       System.out.println("Error Inserting Row");
                       log.writeToFile("ERROR: " + getSqlStatement());
                   }
                   numOfEntries++;
               }
               catch(IOException e){}
           }
       }
   }
   
   public void getResults() throws SQLException, IOException
   {
       setSqlStatement("SELECT * FROM " + getTableName());// + "WHERE make = 'Chevy' or make = 'Toyota'");
       log.writeToFile(getSqlStatement());
       try
       {
           result = stat.executeQuery(getSqlStatement());
       }
       catch (SQLException e)
       {
           System.out.println("Error getting result set");
           log.writeToFile("ERROR: " + getSqlStatement());
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
       log.writeToFile(getSqlStatement());
       try
       {
           result = stat.executeQuery(getSqlStatement());
       }
       catch (SQLException e)
       {
           System.out.println("Error getting result set");
           log.writeToFile("ERROR: " + getSqlStatement());
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
       log.writeToFile(getSqlStatement());
       try
       {
           result = stat.executeQuery(getSqlStatement());
       }
       catch (SQLException e)
       {
           System.out.println("Error getting result set");
           log.writeToFile("ERROR: " + getSqlStatement());
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

