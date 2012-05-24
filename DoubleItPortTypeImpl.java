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
		    	uploadResult = "New Pattern " + patternName + " with Price " + patternPrice + " has added to database"; 
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
    
    public String storePurchasePattern(int storeID, String patternName, int storeBalance) {
    	String storePurchaseResult = "Failed to purchase pattern";
    	
    	try {
			Class.forName(driver).newInstance();
		    con = DriverManager.getConnection(url,user,pass);
		    
		    Statement s1 = con.createStatement();
			s1.executeQuery("SELECT price FROM pattern WHERE pattern_name = '" + patternName + "'");
			rs = s1.getResultSet();
			rs.next();
			int patternPrice = rs.getInt("price");
			
		    
		    if(patternPrice > storeBalance) {
		    	storePurchaseResult = "Failed to purchase " + patternName + " Reason: Pattern Price is " 
		    			+ patternPrice + " Your Account Balance " + storeBalance; 
		    }
		    else {
		    	storeBalance = storeBalance - patternPrice;
		    	Statement s2 = con.createStatement();
				s2.executeUpdate("UPDATE store SET balance = '" + storeBalance + "' WHERE store_id = '" + storeID + "'");
		    	
		    	storePurchaseResult = "Your Store bought the Pattern " + patternName + " and the cost " 
		    			+ patternPrice + " has deducted from your account";
		    }
		}
		catch(ClassNotFoundException e) {
			storePurchaseResult = "Cannot Connected to Database";
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
    	
    	return storePurchaseResult;
    }
    
    public int getStoreBalance(int storeID) {
    	int storeBalance = 0;
    	
    	try {
			Class.forName(driver).newInstance();
		    con = DriverManager.getConnection(url,user,pass);
		    
		    s = con.createStatement();
			s.executeQuery("SELECT balance FROM store WHERE store_id = '" + storeID + "'");
			rs = s.getResultSet();
			
			if (rs == null) {
				storeBalance = 0;
			}
			else {
				rs.next();
				storeBalance = rs.getInt("balance");
			}
		}
		catch(ClassNotFoundException e) {
			storeBalance = 0;
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
    	
    	return storeBalance;
    }
    
    public String displayPatternSale(String designerName) {
    	String patternSaleResult = "Failed to display pattern sold";
    	int count = 0;
    	int totalPatternSale = 0;
    	
    	try {
			Class.forName(driver).newInstance();
		    con = DriverManager.getConnection(url,user,pass);
		    
		    Statement s1 = con.createStatement();
			s1.executeQuery("SELECT designerName FROM pattern, sale WHERE pattern.pattern_name = sale.pattern_name AND designer_name = '" 
								+ designerName + "'");
			rs = s1.getResultSet();
			
			if(rs == null) {
				patternSaleResult = "You don't have 5 pattern sales yet";
			}
			else {
				while(rs.next()) {
					++count;
				}
			}
			
		    if(count >= 5) {
		    	Statement s2 = con.createStatement();
				s2.executeQuery("SELECT price FROM pattern, sale WHERE pattern.pattern_name = sale.pattern_name AND pattern.designer_name = '" 
									+ designerName + "'");
				rs = s2.getResultSet();
				
				while(rs.next()) {
					int patternPrice = rs.getInt("price");
					totalPatternSale = totalPatternSale + patternPrice;
				}
				
				Statement s3 = con.createStatement();
				s3.executeQuery("SELECT store.name, sale.pattern_name, yarn.price FROM pattern, store, sale, yarn WHERE store.store_id = sale.store_id AND yarn.name = sale.yarn AND sale.pattern_name = pattern.pattern_name WHERE pattern.designer_name = '" 
									+ designerName + "'");
				
				while(rs.next()) {
					String storeName = rs.getString("store.name");
					String patternName = rs.getString("sale.pattern_name");
					int yarnPrice = rs.getInt("yarn.price");
					
					patternSaleResult = "Total Amount you have earned " + totalPatternSale + " Store Name " 
			    			+ storeName + " Pattern Name " + patternName + " $ Value of Yarn " + yarnPrice; 
				}
		    	
		    	
		    }
		    else {
		    	patternSaleResult = "You don't have 5 pattern sales yet";
		    }
		}
		catch(ClassNotFoundException e) {
			patternSaleResult = "Cannot Connected to Database";
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
    	
    	return patternSaleResult;
    }
}
