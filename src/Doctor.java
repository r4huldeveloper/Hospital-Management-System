import java.sql.*;


    public class Doctor {
        private final Connection connection;

        public Doctor(Connection connection){
            this.connection = connection;
        }

        public void viewDoctors() throws SQLException{
            String query = "SELECT * from doctors";

            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                System.out.printf("%-12s|%-20s|%-10s\n",id,name,specialization);
            }
        }

        public boolean getDoctorById(int id) throws SQLException{
            String query = "SELECT * FROM doctors where id=?";
            PreparedStatement psmt = connection.prepareStatement(query);
            psmt.setInt(1,id);
            ResultSet rs = psmt.executeQuery();
            return rs.next();
        }
}
