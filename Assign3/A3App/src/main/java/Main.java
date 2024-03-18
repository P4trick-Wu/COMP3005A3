import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Main {

    public static String url = "jdbc:postgresql://localhost:5432/Assignment3";
    public static String user = "postgres";
    public static String password = "postgres";

    public static void main (String[] args) {

        Scanner scanner = new Scanner(System.in);
        int options = -1;

        while (options != 0) {

            try {
                //Get user input
                System.out.println("Options: 0 - Exit, 1 - List students, 2 - Add student, 3 - Update email, 4 - Delete student  \n Enter your choice: ");
                options = scanner.nextInt();

                //Perform actions based on use rinput
                switch (options) {
                    case 0:
                        System.out.println("Exiting program");
                        break;
                    case 1:
                        System.out.println("Listing students...");
                        getAllStudents();
                        break;
                    case 2:
                        //Get user input
                        scanner.nextLine();
                        System.out.println("Enter first name, last name, email and enrollment date (yyy-mm-dd) in that order: ");
                        String userInput = scanner.nextLine();

                        //Split input into seperate words for parameter

                        String[] words = userInput.split("\\s+");
                        addStudent(words[0], words[1], words[2], words[3]);

                        break;
                    case 3:
                        //get user input
                        scanner.nextLine();
                        System.out.println("Enter student id and new email in that order:  ");
                        String updateInput = scanner.nextLine();

                        //Split input into seperate words for parameters
                        String[] updateWords = updateInput.split("\\s+");
                        updateStudentEmail(Integer.parseInt(updateWords[0]), updateWords[1]);
                        break;
                    case 4:
                        //get user input
                        scanner.nextLine();
                        System.out.println("Enter student id to be deleted");
                        int input = Integer.parseInt(scanner.nextLine());

                        deleteStudent(input);
                        break;
                    default:
                        System.out.println("Try again.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        scanner.close();


    }

    //Retrieves and displays all records from the students table
    static void getAllStudents() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            //Check connection
            if(connection != null) {
                System.out.println("Connected");
            } else {
                System.out.println("Failed to establish connected");
            }

            //Get database data
            Statement stmt = connection.createStatement();
            String SQL = "SELECT * from students";
            ResultSet rs = stmt.executeQuery(SQL);

            System.out.println("Printing all student info");
            //iterate through all student data, printing all corresponding information
            while (rs.next()) {
                int id = rs.getInt("student_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                Date date = rs.getDate("enrollment_date");
                System.out.println("Student #" + id + ", " + firstName + " " + lastName + ", email: " + email + ", enrollment date: " + date.toString());
            }

            //Close resources
            rs.close();
            stmt.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //Inserts a new student record into the students table
    static void addStudent(String first_name, String last_name, String email, String enrollment_date) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            //Check connection
            if(connection != null) {
                System.out.println("Connected");
            } else {
                System.out.println("Failed to establish connected");
            }

            //Change date stored in string into date object
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = formatter.parse(enrollment_date);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            //Inserting prepared statement
            String insertSQL = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?,?,?,?)";
            try(PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
                pstmt.setString(1, first_name);
                pstmt.setString(2, last_name);
                pstmt.setString(3, email);
                pstmt.setDate(4, sqlDate);
                pstmt.executeUpdate();
                System.out.println("Data inserted using prepared statement");
            }


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //Updates email address for a student with the specified student_id
    static void updateStudentEmail(int student_id, String new_email) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            //Check connection
            if(connection != null) {
                System.out.println("Connected");
            } else {
                System.out.println("Failed to establish connected");
            }

            //update student email based on input
            Statement stmt = connection.createStatement();
            String insertSQL = "UPDATE students SET email = " + "'" + new_email + "'" + " WHERE student_id = " + student_id;
            stmt.executeUpdate(insertSQL);
            System.out.println("Student " + student_id + " email updated to " + new_email);

            stmt.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //Deletes the record of the student with the sepcified student_id
    static void deleteStudent(int student_id) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            //Check connection
            if(connection != null) {
                System.out.println("Connected");
            } else {
                System.out.println("Failed to establish connected");
            }

            //Delete student based on input
            Statement stmt = connection.createStatement();
            String insertSQL = "DELETE from students where student_id = " + student_id;
            stmt.executeUpdate(insertSQL);
            System.out.println("Student " + student_id + "deleted.");

            stmt.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
