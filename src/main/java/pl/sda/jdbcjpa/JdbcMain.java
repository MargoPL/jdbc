package pl.sda.jdbcjpa;

import java.math.BigDecimal;
import java.sql.*;

public class JdbcMain {

    public static void main(String[] args) {
        selectAllDataFromEmployees();
    }

    private static void selectAllDataFromEmployees() {
        try (Connection connection = getConnection()) {
            System.out.println("Connection success");

            String query = "select ename, job, sal,  hiredate from sdajdbc.employee";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String ename = resultSet.getString("ename");
                String job = resultSet.getString("job");
                BigDecimal sal = resultSet.getBigDecimal("sal");
                Date hiredate = resultSet.getDate("hiredate");
                System.out.println(ename + " " + job + " " + sal + " " + hiredate);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() {
        try {
            return DriverManager
                    .getConnection("jdbc:mysql://127.0.0.1:3306/sdajdbc?useSSL=false&serverTimezone=UTC", "root", "MyNewPass");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
