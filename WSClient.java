package client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.example.contract.doubleit.DoubleItPortType;
import org.example.contract.doubleit.DoubleItService;

public class WSClient {
	static int storeBalance;
    public static void main (String[] args) {
        DoubleItService service = new DoubleItService();
        DoubleItPortType port = service.getDoubleItPort();           

        doubleIt(port, 20);
        doubleIt(port, 1);
        doubleIt(port, -30);
        
        System.out.println("Your Username: ");
        Scanner sc = new Scanner(System.in);
        String username = sc.nextLine();
        System.out.println("Your Password: ");
        String password = sc.nextLine();
        
        verifyUser(port, username, password);

    } 
    
    private static void verifyUser(DoubleItPortType port, String username,
			String password) {
		// TODO Auto-generated method stub
		String userType = port.verifyUser(username, password);
		
		if(userType.compareTo("designer") == 0) {
			
			System.out.println("Welcome Designer");
			System.out.println("----------------");
			System.out.println("Designer Name: ");
			Scanner sc1 = new Scanner(System.in);
			String designerName = sc1.nextLine();
			displayDesignerPatternSale(port, designerName);
			System.out.println("New Pattern Name: ");
			String patternName = sc1.nextLine();
			System.out.println("New Pattern Price: ");
			int patternPrice = sc1.nextInt();
			sc1.close();
			
			uploadPattern(port, designerName, patternName, patternPrice);
		}
		else if (userType.compareTo("customer") == 0) {
			System.out.println("Welcome Customer");
			System.out.println("----------------");
			System.out.println("Store List");
			System.out.println("1-- Morris & Sons");
			System.out.println("2-- Clegs");
			System.out.println("3-- Dairing");
			System.out.println("4-- Wool Baa");
			System.out.println("5-- Onabee");
			System.out.println("6-- The Craft Circle");
			System.out.println("7-- Wondoflex Yarn Craft Centre");
			System.out.println("8-- Sunspun");
			System.out.println("9-- Knitting Yarn Shop");
			System.out.println("Which Store to Purchase: ");
			Scanner sc2 = new Scanner(System.in);
			int storeID = Integer.parseInt(sc2.nextLine());
			System.out.println("Pattern Name: ");
			String patternName = sc2.nextLine();
			System.out.println("What Yarn to Purchase: ");
			String yarn = sc2.nextLine();
			
			customerPurchase(port, storeID, patternName, yarn);
		}
		else if (userType.compareTo("store") == 0) {
			System.out.println("Welcome Store");
			System.out.println("----------------");
			System.out.println("Store List");
			System.out.println("1-- Morris & Sons");
			System.out.println("2-- Clegs");
			System.out.println("3-- Dairing");
			System.out.println("4-- Wool Baa");
			System.out.println("5-- Onabee");
			System.out.println("6-- The Craft Circle");
			System.out.println("7-- Wondoflex Yarn Craft Centre");
			System.out.println("8-- Sunspun");
			System.out.println("9-- Knitting Yarn Shop");
			System.out.println("Which Store you are: ");
			Scanner sc3 = new Scanner(System.in);
			int storeID = Integer.parseInt(sc3.nextLine());
			getStoreBalance(port, storeID);
			System.out.println("What Pattern to Purchase: ");
			String patternName = sc3.nextLine();
			
			storePurchasePattern(port, storeID, patternName, storeBalance);
		}
		else {
			System.out.println("You are not a valid user");
		}
	}

	private static void displayDesignerPatternSale(DoubleItPortType port,
			String designerName) {
		// TODO Auto-generated method stub
		String resp = port.displayPatternSale(designerName);
		System.out.println(resp);
	}

	private static void getStoreBalance(DoubleItPortType port, int storeID) {
		// TODO Auto-generated method stub
		int resp = port.getStoreBalance(storeID);
		System.out.println("Your Account Balance is: " + resp);
		storeBalance = resp;
	}

	private static void storePurchasePattern(DoubleItPortType port,
			int storeID, String patternName, int storeBalance) {
		// TODO Auto-generated method stub
		String resp = port.storePurchasePattern(storeID, patternName, storeBalance);
		System.out.println(resp);
	}

	private static void customerPurchase(DoubleItPortType port, int storeID,
			String patternName, String yarn) {
		// TODO Auto-generated method stub
		String resp = port.customerPurchase(storeID, patternName, yarn);
		System.out.println(resp);
	}

	private static void uploadPattern(DoubleItPortType port,
			String designerName, String patternName, int patternPrice) {
		// TODO Auto-generated method stub
		String resp = port.uploadPattern(designerName, patternName, patternPrice);
		System.out.println(resp);
	}

	public static void doubleIt(DoubleItPortType port, 
            int numToDouble) {
        int resp = port.doubleIt(numToDouble);
        System.out.println("The number " + numToDouble + " doubled is " 
            + resp);
    }
}
