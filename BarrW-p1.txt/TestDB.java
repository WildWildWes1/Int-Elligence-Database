
/*
 *  Project One: Revamp of TestDB
 *  BY: Wes Barr
 *  Instructor: Dr. Coffey
 *  Course: Advanced Programming COP4027
*/

public class TestDB 
{
   public static void main(String[] args) throws Exception
   {
       Database database = new Database("Vehicle");
       database.openDataBase();
       
       try
       {
           database.createTable();
           database.buildTable();
           database.insertItems();
           System.out.println("Items have been inserted into Table\n");
           System.out.println("-------------ALL------------");
           database.getResults();
           System.out.println("-------Chevy & Toyota-------");
           database.getChevyAndToyota();
           System.out.println("-------Heavy Hitters--------");
           database.getTheHeavies();
       }
       finally
       {
           database.dropTable();
           database.closeDB();
       }
   }
}
