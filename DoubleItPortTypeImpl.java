package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.jws.WebService;
import org.example.contract.doubleit.DoubleItPortType;


@WebService(targetNamespace = "http://www.example.org/contract/DoubleIt", 
            portName="DoubleItPort",
            serviceName="DoubleItService", 
            endpointInterface="org.example.contract.doubleit.DoubleItPortType")
public class DoubleItPortTypeImpl implements DoubleItPortType {

	Connection con;
	Statement s;
	ResultSet rs;
	String returnString = "Not Valid";
    String url = "jdbc:mysql://localhost:3306/ravelry_db";
    String driver = "com.mysql.jdbc.Driver";
    String user = "root"; 
    String pass = "root";
	
    public int doubleIt(int numberToDouble) {
        return numberToDouble * 2;
    }
    
    public String verifyUser(String username, String password) {
 
		try {
			Class.forName(driver).newInstance();
		    con = DriverManager.getConnection(url,user,pass);
		    
		    s = con.createStatement();
			s.executeQuery("SELECT username FROM users WHERE username = '" + username + "' AND password = '" 
					+ password + "'");
			rs = s.getResultSet();
			
			if (rs == null) {
				returnString = "Not a valid user";
			}
			else {
				rs.next();
				String userType = rs.getString ("username");
				returnString = userType;
			}
		}
		catch(ClassNotFoundException e) {
			returnString = "Cannot Connected to Database";
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return returnString;
    }
    
    public String uploadPattern(String designerName, String patternName, int patternPrice) {
    	String uploadResult = "Failed to add pattern";
    	
    	try {
			Class.forName(driver).newInstance();
		    con = DriverManager.getConnection(url,user,pass);
		    
		    Statement s1 = con.createStatement();
			s1.executeQuery("SELECT pattern_id FROM pattern WHERE pattern_id = (SELECT MAX(pattern_id) FROM pattern)");
			rs = s1.getResultSet();
			rs.next();
			int max_id = rs.getInt("pattern_id");
			int id;
			if(max_id == 0) {
				id = 1;
			}
			else {
				id = max_id + 1;
			}
			
		    PreparedStatement pstmt = con.prepareStatement("INSERT INTO pattern VALUES(?, ?, ?, ?)");
		    pstmt.setInt(1, id);
		    pstmt.setString(2, designerName);
		    pstmt.setString(3, patternName);
		    pstmt.setInt(4, patternPrice);
		    
		    int s = pstmt.executeUpdate();
		    
		    if(s > 0) {
		    	uploadResult = "Pattern " + patternName + " with Price " + patternPrice + " has added to database"; 
		    }
		    else {
		    	uploadResult = "Pattern didn't add";
		    }
		}
		catch(ClassNotFoundException e) {
			uploadResult = "Cannot Connected to Database";
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return uploadResult;
    }
    
    public String customerPurchase(int storeID, String patternName, String yarn) {
		String purchaseResult = "Failed to purchase";
    	
		try {
			Class.forName(driver).newInstance();
		    con = DriverManager.getConnection(url,user,pass);
		    
		    Statement s1 = con.createStatement();
			s1.executeQuery("SELECT sale_id FROM sale WHERE sale_id = (SELECT MAX(sale_id) FROM sale)");
			rs = s1.getResultSet();
			rs.next();
			int max_id = rs.getInt("sale_id");
			int id;
			if(max_id == 0) {
				id = 1;
			}
			else {
				id = max_id + 1;
			}
			
			Statement s2 = con.createStatement();
			s2.executeQuery("SELECT name FROM store WHERE store_id = '" + storeID + "'");
			rs = s2.getResultSet();
			rs.next();
			String storeName = rs.getString("name");
			
		    PreparedStatement pstmt = con.prepareStatement("INSERT INTO sale VALUES(?, ?, ?, ?)");
		    pstmt.setInt(1, id);
		    pstmt.setInt(2, storeID);
		    pstmt.setString(3, patternName);
		    pstmt.setString(4, yarn);
		    
		    int s = pstmt.executeUpdate();
		    
		    if(s > 0) {
		    	purchaseResult = "You bought " + patternName + " with Yarn " + yarn + " from " + storeName; 
		    }
		    else {
		    	purchaseResult = "Purchase Failed";
		    }
		}
		catch(ClassNotFoundException e) {
			purchaseResult = "Cannot Connected to Database";
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return purchaseResult;
    }
}
