package com.uniquedeveloper.registration;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Servlet implementation class Course
 */

@WebServlet("/course")
public class Course extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String course = request.getParameter("course");
		String rollno = request.getParameter("rollno");
		int sem = Integer.parseInt(request.getParameter("semester"));
		int yr = Integer.parseInt(request.getParameter("year"));
		Connection con = null;
		RequestDispatcher dispatcher = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursedb", "root", "gokul@123");
			PreparedStatement stmt = con.prepareStatement("insert into Courses(Username,rollno,year,course,semester) values(?,?,?,?,?)");
			stmt.setString(1, username);
			stmt.setString(2, rollno);
			stmt.setInt(3, yr);
			stmt.setString(4, course);
			stmt.setInt(5, sem);
			int rowCount = stmt.executeUpdate();
			dispatcher = request.getRequestDispatcher("index.jsp");
			if(rowCount > 0) {
				request.setAttribute("status", "success");
				
			}else {
				request.setAttribute("status", "failed");
			}
			
			dispatcher.forward(request, response);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
