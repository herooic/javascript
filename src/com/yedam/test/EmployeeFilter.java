package com.yedam.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class EmployeeFilter {
   public static void main(String[] args) throws IOException {
      // DB 연결
      Connection conn = null;
      String user = "hr";
      String pass = "hr";
      String url = "jdbc:oracle:thin:@localhost:1521:xe";
      try {
         Class.forName("oracle.jdbc.driver.OracleDriver");
         conn = DriverManager.getConnection(url, user, pass);
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      } catch (SQLException e) {
         e.printStackTrace();
      } 
     
      System.out.println("=========이름/오름차순/정렬=============");
      List<Employee> list = new ArrayList<>();
      try {
         PreparedStatement pstmt = conn.prepareStatement("SELECT first_name, salary FROM Employees ORDER BY 1");
         ResultSet rs = pstmt.executeQuery();
         while (rs.next()) {
            list.add(new Employee(rs.getString("first_name"), rs.getInt("salary")));
         }
      } catch (SQLException e) {
         System.out.println();
      }
      Stream<Employee> stream = list.stream();
      stream.filter(s -> s.getSalary() >= 1000)
            .forEach((s) -> System.out.println(s.getFirstName() + ", " + s.getSalary()));
  
      File file = new File("src/com/yedam/test/employees.txt");
      FileOutputStream fs = new FileOutputStream(file);
      for (Employee emp : list) {
         fs.write(emp.getSalary());
      }
      fs.flush();
      fs.close();
      System.out.println("==================================");
   }  		
}