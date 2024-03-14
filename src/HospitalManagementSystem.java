import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/hospital";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "rahul12345";

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        Scanner scanner = new Scanner(System.in);
        Patient patient = new Patient(connection,scanner);
        Doctor doctor = new Doctor(connection);
        while (true){
            System.out.println("Hospital Management System");
            System.out.println("1. Add Patient");
            System.out.println("2. View Patients");
            System.out.println("3. View Doctors");
            System.out.println("4. Book Appointment");
            System.out.println("5. Exit");
            System.out.println("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice){
                case 1:
                    //Add patient
                    patient.addPatient();
                    System.out.println();
                    break;
                case 2:
                    //view patient
                    patient.viewPatients();
                    System.out.println();
                    break;
                case 3:
                    //view doctors
                    doctor.viewDoctors();
                    System.out.println();
                    break;
                case 4:
                    //book appointment
                    bookAppointment(patient,doctor,connection,scanner);
                    System.out.println();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) throws SQLException{
        System.out.print("Enter patient id: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor id: ");
        int doctorId = scanner.nextInt();
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();

        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
            if (checkDoctorAvailability(doctorId, appointmentDate, connection)){
                String query = "INSERT INTO appointment(patient_id, doctor_id, appointment_date) VALUES(?,?,?)";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1,patientId);
                ps.setInt(2,doctorId);
                ps.setString(3,appointmentDate);
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0)
                    System.out.println("Appointment Booked!!");
                else
                    System.out.println("Failed to book appointment");
            }
            else
                System.out.println("Doctor is not available!!");
        }
    }

    private static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) throws SQLException{
        String query = "SELECT COUNT(*) FROM appointment WHERE doctor_id = ? AND appointment_date = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1,doctorId);
        ps.setString(2,appointmentDate);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            int count = rs.getInt(1);
            return count == 0;
        }
        return false;
    }
}
