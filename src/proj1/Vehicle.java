package proj1;


/*
 *  Project One: Revamp of TestDB
 *  BY: Wes Barr
 *  Instructor: Dr. Coffey
 *  Course: Advanced Programming COP4027
*/

import java.io.Serializable;
import java.util.Random;


public final class Vehicle implements Serializable
{
   private String make;
   private String style;
   private double weight;
   private double engineSize;
   private boolean importYorN;
   
   public Vehicle() //Populates a Vehicle Make, Style, Weight, EngSize, & Import Status
   {
       this.make = popMake();
       style = popStyle();
       this.weight = popWeight();
       this.engineSize = popEngSize();
       importYorN = popImportStatus();
   }
      
   public String popMake()
   {
       Random rand = new Random();
       int randInt = rand.nextInt(5) + 1;

       switch(randInt)
       {
           case 1:
               this.make = "Chevy";
               break;
           case 2:
               this.make = "Ford";
               break;
           case 3:
               this.make = "Toyota";
               break;
           case 4:
               this.make = "Nissan";
               break;
           case 5:
               this.make = "Hyundai";
               break;
           default:
               System.out.println("\nERROR\n");
               break;  
       }
      
       return this.make;
   }
   
   public String popStyle()
   {
       Random rand = new Random();
       int randInt = rand.nextInt(3) + 1;
       switch(randInt)
       {
           case 1:
               this.style = "compact";
               break;
           case 2:
               this.style = "intermediate";
               break;
           case 3:
               this.style = "fullSized";
               break;
           default:
               System.out.println("\nERROR\n");
               break;  
       }
      
       return this.style;
   }
   
   public double popWeight()
   {
       double min = 0;
       double max = 0;
      
       switch(getStyle()) 
       {
           case "compact":
               min = 1500;
               max = 2000;
               break;
           case "intermediate":
               min = 2000;
               max = 2500;
               break;
           case "fullSized":
               min = 2500;
               max = 4000;
               break;
           default:
               System.out.println("ERROR: Could Not Generate Weight For: " + this.style);
               break;
       }
      
       Random rand = new Random();
       double randDouble = (rand.nextDouble() * (max - min));
       this.weight = randDouble + min;
       return this.weight;
   }
   
   public double popEngSize()
   {
       Random rand = new Random();
       double randDouble = rand.nextDouble() * (6 - 1);
       this.engineSize = randDouble + 1;
       return this.engineSize;
   }
   
   public boolean popImportStatus()
   {
       return getMake().equals("Toyota") || getMake().equals("Nissan") || getMake().equals("Hyundai");
   }
   
   public String getMake()
   {
       return this.make;
   }
   
   public void setMake(String make)
   {
       this.make = make;
   }
   
   public String getStyle()
   {
       return this.style;
   }
   
   public void setStyle(String style)
   {
       this.style = style;
   }
   
   public double getWeight()
   {
       return this.weight;
   }
   
   public void setWeight(double weight)
   {
       this.weight = weight;
   }
   
   public double getEngineSize()
   {
       return this.engineSize;
   }
   
   public void setEngineSize(double engSize)
   {
       this.engineSize = engSize;
   }
   
   public boolean getImport()
   {
       return this.importYorN;
   }
   
   public void setImport(boolean imp)
   {
       this.importYorN = imp;
   }
   
}
