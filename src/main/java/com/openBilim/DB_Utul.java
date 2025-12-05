package com.openBilim;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.openBilim.Session_Handling.Test;
import com.openBilim.Tasks.MultipleChoiceTask;
import com.openBilim.Tasks.SingleChoiceTask;
import com.openBilim.Tasks.Task;
import com.openBilim.Tasks.TextTask;
import com.openBilim.Users.User;

public class DB_Utul {
    public static String db_adress;

    private static Connection connection = null;
    private static Statement statement = null;

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

    public static String ecranize(String str) {
        return str.replace("'", "''");
    }


    public static int getLastSession(){
         String sql = "select * from get_last_session()";

        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            if (!results.next())
                return -1;
            return results.getInt("id");
        } catch (SQLException e) {
            LOGGER.warning("Error closing resources: " + e.getMessage());
            return -1;
        } finally {
            // Step 5: Close resources
            try {
                if (statement != null)
                    statement.close();

            } catch (SQLException e) {
                LOGGER.warning("Error closing resources: " + e.getMessage());
            }
        }
    }
//IN p_session_id integer, IN p_task_id integer, IN p_user_answer text, IN p_is_correct boolean, IN p_points_earned double precision)
    public static void register_answer(int session_id,int task_id, String user_answer, Boolean isCorrect, float earned_points) {

        String sql = String.format("call register_answer( %d, %d, '%s', '%s', %f)", session_id, task_id, ecranize(user_answer), isCorrect? "true" : "false", earned_points);

        try {
            statement = connection.createStatement();
            statement.execute(sql);

        } catch (SQLException e) {
            LOGGER.warning("Error closing resources: " + e.getMessage());
        } finally {
            // Step 5: Close resources
            try {
                if (statement != null)
                    statement.close();

            } catch (SQLException e) {
                LOGGER.warning("Error closing resources: " + e.getMessage());
            }
        }
    }

    public static void finish_session(int id, float score) {

        String sql = String.format("call finish_session( %d, %f)", id, score);

        try {
            statement = connection.createStatement();
            statement.execute(sql);

        } catch (SQLException e) {
            LOGGER.warning("Error closing resources: " + e.getMessage());
        } finally {
            // Step 5: Close resources
            try {
                if (statement != null)
                    statement.close();

            } catch (SQLException e) {
                LOGGER.warning("Error closing resources: " + e.getMessage());
            }
        }
    }

    public static void register_session(int id, int test_id, int user_id, float total_points) {

        String sql = String.format("call register_session( %d, %d, %d, %f)", id, test_id, user_id, total_points);

        try {
            statement = connection.createStatement();
            statement.execute(sql);

        } catch (SQLException e) {
            LOGGER.warning("Error closing resources: " + e.getMessage());
        } finally {
            // Step 5: Close resources
            try {
                if (statement != null)
                    statement.close();

            } catch (SQLException e) {
                LOGGER.warning("Error closing resources: " + e.getMessage());
            }
        }
    }

    public static Test getTestById(String id) {

        String sql = "select * from get_test_by_id(" + DB_Utul.ecranize(id) + ")";

        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            if (!results.next())
                return null;
            Test newTest = null;
            if (results.getString("name") != null) {
                newTest = new Test(id, results.getString("subject"), results.getString("name"));

            }
            return newTest;
        } catch (SQLException e) {
            LOGGER.warning("Error closing resources: " + e.getMessage());
            return null;
        } finally {
            // Step 5: Close resources
            try {
                if (statement != null)
                    statement.close();

            } catch (SQLException e) {
                LOGGER.warning("Error closing resources: " + e.getMessage());
            }
        }
    }

    public static ArrayList<Task> getTestTasks(String test_id) {
        String sql = "select * from get_test_tasks(" + DB_Utul.ecranize(test_id) + ")";
        try {

            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            ArrayList<Task> tasksList = new ArrayList<>();

            while (results.next()) {
                String taskId = String.valueOf(results.getInt("id"));
                if (results.getString("type").equals("SINGLE")) {

                    tasksList.add(new SingleChoiceTask(taskId, results.getString("question"),
                            results.getArray("options"), Integer.parseInt(results.getString("correct_text")),
                            results.getDouble("points")));

                } else if (results.getString("type").equals("MULTIPLE")) {

                    tasksList.add(new MultipleChoiceTask(taskId, results.getString("question"),
                            results.getArray("options"), results.getArray("correct_indicies"),
                            results.getDouble("points")));
                } else if (results.getString("type").equals("TEXT")) {
                    tasksList.add(new TextTask(taskId, results.getString("question"),
                            results.getString("correct_text"), results.getDouble("points")));

                }
            }

            return tasksList;
        } catch (SQLException e) {
            LOGGER.warning("Error closing resources: " + e.getMessage());
            return null;
        } finally {
            // Step 5: Close resources
            try {
                if (statement != null)
                    statement.close();

            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public static List<Test> getAvailableTests(String user_id) {
        String sql = "select * from get_available_tests('" + DB_Utul.ecranize(user_id) + "')";

        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            List<Test> testsList = new ArrayList<>();

            while (results.next()) {

                testsList.add(new Test(results.getString("test_id"), results.getString("test_subject"),
                        results.getString("test_name")));

                testsList.getLast().setTasksList(getTestTasks(testsList.getLast().getId()));

            }

            return testsList;
        } catch (SQLException e) {
            LOGGER.warning("Error closing resources: " + e.getMessage());
            return null;
        } finally {
            // Step 5: Close resources
            try {
                if (statement != null)
                    statement.close();

            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

    }

    public static User getUserByEmail(String email) {

        String sql = "select * from get_user_by_email('" + DB_Utul.ecranize(email) + "')";

        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            if (!results.next())
                return null;
            User newUser = null;

            newUser = new User(results.getString("user_id"), results.getString("fio"), results.getString("group_name"),
                    results.getString("email"), results.getString("password_hash"), results.getString("password_salt"),
                    results.getString("role"));

            return newUser;
        } catch (SQLException e) {
            LOGGER.warning("Error closing resources: " + e.getMessage());
            return null;
        } finally {
            // Step 5: Close resources
            try {
                if (statement != null)
                    statement.close();

            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public static User getUserById(String id) {

        String sql = "select * from get_user_by_id(" + DB_Utul.ecranize(id) + ")";

        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            if (!results.next())
                return null;
            User newUser = null;

            newUser = new User(results.getString("user_id"), results.getString("fio"), results.getString("group_name"),
                    results.getString("email"), results.getString("password_hash"), results.getString("password_salt"),
                    results.getString("role"));

            return newUser;
        } catch (SQLException e) {
            LOGGER.warning("Error closing resources: " + e.getMessage());
            return null;
        } finally {
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