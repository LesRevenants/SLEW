package Relation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

public class WikiArticleDB {
	
private Connection connection;
	
	 public WikiArticleDB(String url, String login, String pwd){

		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			
			connection = DriverManager.getConnection(url,login,pwd);
			if(connection == null) {
				System.out.println("Connection Failed! Check output console");
				return;
			}
			System.out.println("[WIKI_DB SUCCESS] Connection to database");			
		}
		catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}		
	}
	 
	 public boolean exist(int article_id){
		 StringBuilder query=new StringBuilder( "SELECT article_id FROM Wiki_Article WHERE article_id = ?");
		 try {
				PreparedStatement pstmt = connection.prepareStatement(query.toString());
				pstmt.setInt(1, article_id);
				return pstmt.executeQuery().next();
				
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
		}		
	 }
	 
	 public String article_name_for(int article_id){
		 StringBuilder query=new StringBuilder( "SELECT article_id FROM Wiki_Article WHERE article_id = ?");
		 try {
				PreparedStatement pstmt = connection.prepareStatement(query.toString());
				pstmt.setInt(1, article_id);
				return pstmt.executeQuery().getString("article_name");
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
		}	
	 }
	 
	 public int article_id_for(String article_name){
		 StringBuilder query=new StringBuilder( "SELECT article_id FROM Wiki_Article WHERE article_name = ?");
		 try {
				PreparedStatement pstmt = connection.prepareStatement(query.toString());
				pstmt.setString(1, article_name);
				ResultSet rs=pstmt.executeQuery();
				if(rs.next()){
					return rs.getInt("article_id");
				}
				return -1;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return -1;
		}	
	 }
	 
	 public boolean exist(String article_name){
		 StringBuilder query=new StringBuilder("SELECT article_id FROM Wiki_Article WHERE article_name = ?");
		 try {
				PreparedStatement pstmt = connection.prepareStatement(query.toString());
				pstmt.setString(1, article_name);
				return pstmt.executeQuery().next();
				
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}			
	 }
	 
	  
	 public boolean add(String article_name){
		 StringBuilder query=new StringBuilder(
				 "INSERT INTO Wiki_Article (article_name,article_date) VALUES(?,NOW())"
				 );
		 try {
			PreparedStatement pstmt = connection.prepareStatement(query.toString());
			pstmt.setString(1, article_name);
			pstmt.executeUpdate();
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}		
	 }
	 

	 
}
