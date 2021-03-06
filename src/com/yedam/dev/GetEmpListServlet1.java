package com.yedam.dev;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@WebServlet("/GetEmpListServlet1")
public class GetEmpListServlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GetEmpListServlet1() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		response.getWriter().append("Served at: ").append(request.getContextPath()).append("JHI");
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		EmpDAO dao = new EmpDAO();
		for (Employee e : dao.getEmpList()) {
			obj.put("empId", e.getEmployeeId());
			obj.put("firstName", e.getFirstName());
			obj.put("lastName", e.getLastName());
			ary.add(obj);
		}
		PrintWriter out = response.getWriter();
		out.print(ary.toString());

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
