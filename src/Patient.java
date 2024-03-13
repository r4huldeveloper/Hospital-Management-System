import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Scanner;

public class Patient {
    private final Connection connection;
    private final Scanner scanner;

    public Patient(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient(){
        System.out.print("Enter patient name: ");
        String name = scanner.next();
        System.out.print("Enter patient age: ");
        int age = scanner.nextInt();
        System.out.print("Enter patient gender: ");
        String gender = scanner.next();

        try {
            String query = "INSERT INTO patients(name,age,gender) VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0)
                System.out.println("Patient added Successfully!");
            else
                System.out.println("Operation failed");
        }
        catch (SQLException e){
            e.printStackTrace();;
        }
    }

    public void viewPatients() throws SQLException{
        String query = "SELECT * from patients";

        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet resultSet = ps.executeQuery();
        while(resultSet.next()){
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            String gender = resultSet.getString("gender");
            System.out.printf("%-12s|%-20s|%-10s|%-12s\n",id,name,age,gender);
        }
    }

    public boolean getPatientById(int id) throws SQLException{
        String query = "SELECT * FROM patients where id=?";
        PreparedStatement psmt = connection.prepareStatement(query);
        psmt.setInt(1,id);
        ResultSet rs = psmt.executeQuery();
        if(rs.next())
            return true;
        else
            return false;
    }
}
