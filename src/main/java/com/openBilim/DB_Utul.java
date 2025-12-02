package com.openBilim;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.openBilim.Session_Handling.Test;

public class DB_Utul {
    public static String db_adress = "jdbc:postgresql://localhost:5432/openBilim";

        private static Connection connection = null;
        private static  Statement statement = null;
    public static void connect() {


        try {
            // Step 1 & 2: Load the driver (implicitly handled by DriverManager in modern
            // Java)
            // Class.forName("org.postgresql.Driver");

            // Step 3: Establish the connection
            String url = db_adress;
            String user = "postgres";
            String password = "123456";
            connection = DriverManager.getConnection(url, user, password);
            LOGGER.info("Connected to PostgreSQL database!");

            // Step 4: Create a statement and execute a query


        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } 
    }

        public static String ecranize(String str){
            return str.replace("'", "''"); 
        }

    public static Test getTestById(String id){


        String sql = "select * from get_test_by_id(" + DB_Utul.ecranize(id)+")";
        
        try{
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);
            
            if (!results.next()) return null;
        Test newTest = null;
        if (results.getString("name") != null)
        {
        newTest = new Test(id, results.getString("subject"), results.getString("name"));
        
        }
        return newTest;
        }
        catch (SQLException e) {
                LOGGER.warning("Error closing resources: " + e.getMessage());
                return null;
            }
        finally{
           // Step 5: Close resources
            try {
                if (statement != null)
                    statement.close();

            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}