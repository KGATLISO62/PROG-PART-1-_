/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplicationproject.pkg1;
  import java.util.Scanner;
/**
 *
 * @author kgatl
 */
public class JavaApplicationProject1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

          Scanner ista = new Scanner(System.in);

        // Registration
        System.out.println("=== REGISTRATION ===");

        System.out.print("Enter your first name: ");
        String firstName = ista.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = ista.nextLine();

        System.out.print("Enter your username: ");
        String username = ista.nextLine();

        System.out.print("Enter your password: ");
        String password = ista.nextLine();

        System.out.print("Enter your South African cell phone number: ");
        String cellPhone = ista.nextLine();

        // Create the user
        RegistrationLogin user = new RegistrationLogin(
                username,
                password,
                cellPhone,
                firstName,
                lastName
        );

        // Check registration
        System.out.println();
        System.out.println(user.registerUser());

        // Login
        System.out.println();
        System.out.println("=== LOGIN ===");

        System.out.print("Enter your username: ");
        String loginUsername = ista.nextLine();

        System.out.print("Enter your password: ");
        String loginPassword = ista.nextLine();

        // Check login details
        boolean loginStatus = user.loginUser(
                loginUsername,
                loginPassword
        );

        // Display login result
        System.out.println();
        System.out.println(user.returnLoginStatus(loginStatus));
    
    
}

 
  }
 
    



