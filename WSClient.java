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
    public static void main (String[] args) {
        DoubleItService service = new DoubleItService();
        DoubleItPortType port = service.getDoubleItPort();           

        doubleIt(port, 20);
        doubleIt(port, 1);
        doubleIt(port, -30);
        
//        Connection con;
//    	ResultSet rs;
//    	String returnString = "Not Valid";
//        String url = "jdbc:mysql://localhost:3306/ravelry_db";
//        String driver = "com.mysql.jdbc.Driver";
//        String user = "root"; 
//        String pass = "root";
//        
//        String designerName = "Gary";
//        String patternName = "Test2";
//        int patternPrice = 100;
       
//        String uploadResult = "Failed to add pattern";
//    	
//    	try {
//			Class.forName(driver).newInstance();
//		    con = DriverManager.getConnection(url,user,pass);
//		    
//		    Statement s1 = con.createStatement();
//			s1.executeQuery("SELECT pattern_id FROM pattern WHERE pattern_id = (SELECT MAX(pattern_id) FROM pattern)");
//			rs = s1.getResultSet();
//			rs.next();
//			int max_id = rs.getInt("pattern_id");
//			int id = max_id + 1;
//		    
//		    PreparedStatement findID = con.prepareStatement("");
//		    PreparedStatement pstmt = con.prepareStatement("INSERT INTO pattern VALUES(?, ?, ?, ?)");
//		    pstmt.setInt(1, id);
//		    pstmt.setString(2, designerName);
//		    pstmt.setString(3, patternName);
//		    pstmt.setInt(4, patternPrice);
//		    
//		    int s = pstmt.executeUpdate();
//		    
//		    if(s > 0) {
//		    	System.out.println("Pattern " + patternName + " with Price " + patternPrice + " has added to database"); 
//		    }
//		    else {
//		    	System.out.println("Pattern didn't add");
//		    }
//		}
//		catch(ClassNotFoundException e) {
//			System.out.println("Cannot Connected to Database");
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
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
			System.out.println("Store List: ");
			System.out.println("1-- Morris & Sons: ");
			System.out.println("2-- Clegs: ");
			System.out.println("3-- Dairing: ");
			System.out.println("4-- Wool Baa: ");
			System.out.println("5-- Onabee: ");
			System.out.println("6-- The Craft Circle: ");
			System.out.println("7-- Wondoflex Yarn Craft Centre: ");
			System.out.println("8-- Sunspun: ");
			System.out.println("9-- TKnitting Yarn Shop: ");
			System.out.println("Which Store to Purchase: ");
			Scanner sc2 = new Scanner(System.in);
			int storeID = Integer.parseInt(sc2.nextLine());
			System.out.println("Pattern Name: ");
			String patternName = sc2.nextLine();
			System.out.println("What Yarn to Purchase: ");
			String yarn = sc2.nextLine();
			
			customerPurchase(port, storeID, patternName, yarn);
		}
		else {
			System.out.println("You are not a valid user");
		}
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
