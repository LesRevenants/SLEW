package JDM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class JDM_DB {

    private Connection connection;

    /**
     *
     * @param url
     * @param login
     * @param pwd
     */

    public JDM_DB(String url, String login, String pwd){

        try {
            //DriverManager.registerDriver(new com.mysql.jdbc.Driver());

            connection = DriverManager.getConnection(url,login,pwd);
            if(connection == null) {
                System.out.println("Connection Failed! Check output console");
                return;
            }
            System.out.println("[JDM_DB SUCCESS] Connection to database");
        }
        catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
    }

    public void insert_node_type(int node_id, String node_name){
        StringBuilder query=new StringBuilder("INSERT INTO JDM_NODE_TYPE VALUES(?,?) ");
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query.toString());
            preparedStatement.setInt(1,node_id);
            preparedStatement.setString(2,node_name);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void insert_relation_type(int rid, String r_name, String r_nom_entendu, String r_info){
        StringBuilder query=new StringBuilder("INSERT INTO JDM_RELATION_TYPE VALUES(?,?,?,?) ");
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query.toString());
            preparedStatement.setInt(1,rid);
            preparedStatement.setString(2,r_name);
            preparedStatement.setString(3,r_nom_entendu);
            preparedStatement.setString(4,r_info);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void insert_relation(int r_id, int x, int y, int r_type, int r_weight){
        StringBuilder query=new StringBuilder("INSERT INTO JDM_RELATION VALUES(?,?,?,?,?) ");
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query.toString());
            preparedStatement.setInt(1,r_id);
            preparedStatement.setInt(2,x);
            preparedStatement.setInt(3,y);
            preparedStatement.setInt(4,r_type);
            preparedStatement.setInt(5,r_weight);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


    public void insert_node(int node_id, String name, int node_type, int weight) {
        StringBuilder query = new StringBuilder("INSERT INTO JDM_NODE VALUES(?,?,?,?) ");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
            preparedStatement.setInt(1, node_id);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, node_type);
            preparedStatement.setInt(4, weight);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean relation_exist(int r_type, String x, String y){
        StringBuilder query=new StringBuilder( "SELECT JDM_RELATION WHERE x=? AND y=? AND r_type=?");
        try {
            PreparedStatement pstmt = connection.prepareStatement(query.toString());
            pstmt.setString(1,x);
            pstmt.setString(2,y);
            pstmt.setInt(3,r_type);
            return pstmt.executeQuery().next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean relation_exist(String r_type, String x, String y){
        StringBuilder query=new StringBuilder(
                "SELECT r_id FROM JDM_RELATION R, JDM_RELATION_TYPE JT WHERE " +
                        "JT.r_type=? AND JT_id=R.id AND R.x=? AND R.y=?");
        try {
            PreparedStatement pstmt = connection.prepareStatement(query.toString());
            pstmt.setString(1,x);
            pstmt.setString(2,y);
            pstmt.setString(3,r_type);
            return pstmt.executeQuery().next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    public boolean node_exist(String name){
        StringBuilder query=new StringBuilder("SELECT id FROM JDM_NODE where name=?");
        try {
            PreparedStatement pstmt = connection.prepareStatement(query.toString());
            pstmt.setString(1,name);
            return pstmt.executeQuery().next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




}