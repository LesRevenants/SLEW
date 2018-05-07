package Relation;

import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;


public class RelationDB {
	private Connection connection;
	private WikiArticleDB wikiDB;
	
	public RelationDB(String url, String login, String pwd){

		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			
			connection = DriverManager.getConnection(url,login,pwd);
			if(connection == null) {
				System.out.println("Connection Failed! Check output console");
				return;
			}
			System.out.println("[EXTRACTED_RELATION_DB SUCCESS] Connection to database");	
			wikiDB=new WikiArticleDB(url, login, pwd);
		}
		catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}		
	}

	public Collection<ExtractedRelation> getRelationsFromArticle(HashMap<String,RelationPattern> relationsFactory, String article_name, boolean is_human_found){
		HashSet<ExtractedRelation> relations = new HashSet<>();
		if(! wikiDB.exist(article_name)){
			System.err.println("[ERROR] no article : "+article_name);
			return relations;
		}
		try {
			int article_id = wikiDB.article_id_for(article_name);
			StringBuilder query = new StringBuilder("SELECT relation_type, predicate,x,y FROM Extracted_Relation" +
					"WHERE article_id=? AND is_human_found=");
			PreparedStatement pstmt= connection.prepareStatement(query.toString());
			pstmt.setInt(1,article_id);
			pstmt.setBoolean(2,is_human_found);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				relations.add(new ExtractedRelation(
						relationsFactory.get(rs.getString("relation_type")),
						rs.getString("predicate"),
						rs.getString("x"),
						rs.getString("y")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return relations;
	}

	public void add(ExtractedRelation relation, String article_name, boolean is_human_found){
		
		if(! wikiDB.exist(article_name)){
			System.err.println("[ERROR] no article : "+article_name);
			return;
		}
		
		StringBuilder query=new StringBuilder(
				 "INSERT INTO Extracted_Relation(relation_type,predicate,x,y,is_human_found,article_id) VALUES(?,?,?,?,?,?) "
				 );
		
		try {
			PreparedStatement pstmt= connection.prepareStatement(query.toString());
			pstmt.setString(1, relation.getRelation_type().getRelation_name());
			pstmt.setString(2,relation.getLinguisticPattern());
			pstmt.setString(3,relation.getY());
			pstmt.setString(4,relation.getX());
			pstmt.setBoolean(5, is_human_found);
			int id=wikiDB.article_id_for(article_name);
			if(id != -1){
				pstmt.setInt(6,id);
				pstmt.executeUpdate();	
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}


