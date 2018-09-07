package pl.sda.jdbcjpa;

import java.math.BigDecimal;
import java.sql.*;

public class JdbcMain {

    public static void main(String[] args) {
//        statements();
//        preparedStatements();
        callableStatement();
    }

    private static void callableStatement() {
        try (Connection connection = getConnection()) {
            String sql = "{call sdajdbc.getNameById(?,?)}";
            CallableStatement callableStatement = connection.prepareCall(sql);
            int empnr = 7499;
            callableStatement.setInt(1,empnr);
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
            String query = "SELECT ename, job,comm, sal,hiredate,mgr " +
                    "FROM sdajdbc.employee " +
                    "WHERE sal > ?"; //tu jest parametr - różnica względem staement
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, salaryBoundary);
            ResultSet resultSet = preparedStatement.executeQuery();
            printResult(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printResult(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String ename = resultSet.getString("ename");
            String job = resultSet.getString("job");
            BigDecimal comm = resultSet.getBigDecimal("comm");
            BigDecimal sal = resultSet.getBigDecimal("sal");
            Date hiredate = resultSet.getDate("hiredate");
            Integer mgr = resultSet.getInt("mgr");
            if (resultSet.wasNull()) {
                mgr = null;
            }
            System.out.println(ename + " " + job + " " + sal + " " + hiredate + " " + comm + " " + mgr);
        }
    }

    private static void statements() {
        selectAllDataFromEmployees("SELECT ename, job,comm, sal,hiredate,mgr FROM sdajdbc.employee");
        String query = "SELECT ename, job,comm, sal,hiredate,mgr FROM sdajdbc.employee";
        String whereFromUserInput = " where sal > 3000";
        String hackerWhereFromUserInput = " where sal > 3000 OR 1=1";
        selectAllDataFromEmployees(query + hackerWhereFromUserInput);
    }


    private static void selectAllDataFromEmployees(String query) {
        try (Connection connection = getConnection()) {
            System.out.println("Connection success");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            printResult(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void selectAllDataFromEmployeesBadStyle() {
        Connection connection = null;
        try {
            connection = getConnection();

        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static Connection getConnection() {
        try {
            return DriverManager
                    .getConnection("jdbc:mysql://127.0.0.1:3306/sdajdbc?useSSL=false&serverTimezone=UTC",
                            "root", "MyNewPass");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
