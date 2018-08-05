package pl.sda.jdbcjpa;

import java.math.BigDecimal;
import java.sql.*;

public class JdbcMain {

    public static void main(String[] args) {
//        statements();
//        preparedStatements();
        callableStatment();
    }

    private static void callableStatment() {
        try (Connection connection = getConnection()) {
            String sql = "{call sdajdbc.getNameById(?,?)}";
            CallableStatement callableStatement = connection.prepareCall(sql);
            int empnr = 7499;
            callableStatement.setInt(1, empnr);
            callableStatement.registerOutParameter(2, Types.VARCHAR);
            callableStatement.execute();
            String empName = callableStatement.getString(2);
            System.out.println(empName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void preparedStatements() {
        selectDataFromEmployeesAboveSalary(3000);
    }

    private static void selectDataFromEmployeesAboveSalary(int salaryBoundary) {
        try (Connection connection = getConnection()) {
            System.out.println("Connection success");

            String query = "select ename, job, sal,  hiredate, comm, mgr" +
                    " from sdajdbc.employee" +
                    " where sal > ? ";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, salaryBoundary);
            ResultSet resultSet = preparedStatement.executeQuery();
            printResults(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void printResults(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String ename = resultSet.getString("ename");
            String job = resultSet.getString("job");
            BigDecimal sal = resultSet.getBigDecimal("sal");
            Date hiredate = resultSet.getDate("hiredate");
            BigDecimal comm = resultSet.getBigDecimal("comm");
            Integer mgr = resultSet.getInt("mgr");
            if (resultSet.wasNull()) {
                mgr = null;
            }
            System.out.println(ename + " " + job + " " + sal + " " + hiredate + " " + comm + " " + mgr);
        }
    }

    private static void statements() {
        selectAllDataFromEmployees("select ename, job, sal,  hiredate, comm, mgr from sdajdbc.employee");
        String query = "select ename, job, sal,  hiredate, comm, mgr from sdajdbc.employee";
        String whereFromUserInput = "where sal > 3000";
        String hackerWhereFromUserInput = "where sal > 3000 OR 1=1";
        selectAllDataFromEmployees(query + whereFromUserInput);
    }

    private static void selectAllDataFromEmployees(String query) {
        try (Connection connection = getConnection()) {
            System.out.println("Connection success");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            printResults(resultSet);

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
