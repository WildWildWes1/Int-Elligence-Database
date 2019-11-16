/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proj1;

import java.io.Serializable;
import java.util.Random;


public class ParkingLot implements Serializable
{
    private String name = returnName();
    private int size = popNum();
    private int currCapacity = popCapacity();
    private boolean isFull = popStatus();
    private char[] alphabet = "ABCDEFGHIJ".toCharArray();
    
    public String returnName()
    {
        Random rand = new Random();
        int randInt = rand.nextInt(10) + 1;

        switch(randInt)
        {
            case 1:
                this.name = "Parking Lot A";
                break;
            case 2:
                this.name = "Parking Lot B";
                break;
            case 3:
                this.name = "Parking Lot C";
                break;
            case 4:
                this.name = "Parking Lot D";
                break;
            case 5:
                this.name = "Parking Lot E";
                break;
             case 6:
                this.name = "Parking Lot F";
                break;
            case 7:
                this.name = "Parking Lot G";
                break;
            case 8:
                this.name = "Parking Lot H";
                break;
            case 9:
                this.name = "Parking Lot I";
                break;
            case 10:
                this.name = "Parking Lot J";
                break;
            default:
                System.out.println("\nERROR\n");
                break;  
        }

        return this.name;
    }
   
   public int popNum()
   {
       Random rand = new Random();
       this.size = rand.nextInt(40);
      
       return this.size;
   }
   
   public int popCapacity()
   {
       
       if("Parking Lot A".equals(this.name))
       {
           this.currCapacity = 25;
       } 
       if("Parking Lot B".equals(this.name))
       {
           this.currCapacity = 27;
       } 
       if("Parking Lot C".equals(this.name))
       {
           this.currCapacity = 39;
       } 
       if("Parking Lot D".equals(this.name))
       {
           this.currCapacity = 34;
       } 
       if("Parking Lot E".equals(this.name))
       {
           this.currCapacity = 32;
       } 
       if("Parking Lot F".equals(this.name))
       {
           this.currCapacity = 33;
       } 
       if("Parking Lot G".equals(this.name))
       {
           this.currCapacity = 20;
       } 
       if("Parking Lot H".equals(this.name))
       {
           this.currCapacity = 26;
       } 
       if("Parking Lot I".equals(this.name))
       {
           this.currCapacity = 35;
       } 
       if("Parking Lot J".equals(this.name))
       {
           this.currCapacity = 30;
       } 
       return this.currCapacity;
   }
   
   public boolean popStatus()
   {
       this.isFull = this.size > this.currCapacity;
       
       return this.isFull;
   }
   public int getCapacity()
   {
       return this.currCapacity;
   }
   public String getName()
   {
       return this.name;
   }
   public boolean getIsFull()
   {
       return this.isFull;
   }
   public int getSize()
   {
       return this.size;
   }
}
