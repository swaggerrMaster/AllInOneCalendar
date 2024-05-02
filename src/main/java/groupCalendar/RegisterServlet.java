package groupCalendar;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;


@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		System.out.println("body: " + requestBody);
		User user = new Gson().fromJson(requestBody, User.class);
		
		String username = user.getUsername();
		String name = user.getName();
		String password = user.getPassword();
		String email = user.getEmail();
		
		Gson gson = new Gson();
		
		if(username == null || username.isBlank() || name == null || name.isBlank() || password == null || password.isBlank() || email == null || email.isBlank()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String error = "User info missing";
				pw.write(gson.toJson(error));
				pw.flush();
		}
		
		int userID = DataBase.registerUser(username, name, email, password);
		
		if(userID == -1) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "Taken";
			System.out.println(error);
			pw.write(gson.toJson(error));
			pw.flush();
		} 
		else if(userID == -2) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "Taken";
			System.out.println(error);
			pw.write(gson.toJson(error));
			pw.flush();
		}
		else {
			response.setStatus(HttpServletResponse.SC_OK);
			//pw.write(gson.toJson(userID));
			pw.write(gson.toJson("Successful"));
			pw.flush();
		}
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}
}
