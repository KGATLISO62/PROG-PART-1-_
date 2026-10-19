/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplicationproject.pkg1;

import java.util.Scanner; 
import java.util.Random;  
/**
 *
 * @author kgatl
 */
public class messages {
    public static void main(String [] args){ 
        Scanner yana = new Scanner(System.in);
        int option;
        do{ 
            
            System.out.println("Welcome to Quickchat!");
            System.out.println("Option 1" + "Send Messages"); 
            System.out.println("Option 2" + "Coming soon"); 
            System.out.println("Option 3" + "Quit"); 
            option = yana.nextInt();  
            
            
            if (option == 1){ 
            } 
            else if (option == 2){
            System.out.println("Coming soon"); 
            }
            else if (option == 3) {
                System.out.println("Thank you for using Quickapp");
                break;
            }else { 
                System.out.println("Invaild option chosen");
            }
         
            while (option != 3); 
   
    // FOR LOOPS 
     
    
        
    public static void sendMessage() {  
    Scanner yana = new Scanner(System.in); 
    Random no = new Random();  
    System.out.print("Enter number of messages you want to send");
    int n = yana.nextInt();
    for (int i = 0; i < n; i++){ 
        int messageID = no.nextInt ();
    }
    System.out.print("Enter the recipients number");
    String number = yana.nextLine();
    if (number.startsWith("+27") && number.length () == 12) {
        System.out.println("Correct format");
        
        
        System.out.println("Write a message");
        String message = yana.nextLine();
        if (message.length () < 250) {
            
        }else { 
            
        } 
    } else { 
        System.out.println("Incorrect");
        System.out.println();
    }
   
}
       
}
