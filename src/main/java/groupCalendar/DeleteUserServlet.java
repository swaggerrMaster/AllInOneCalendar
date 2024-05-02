package groupCalendar;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        

        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println("deleting body: " + requestBody);
        
        User user = new Gson().fromJson(requestBody, User.class);
        
        // delte users events from events table and thier entery in users table
        boolean delete = DataBase.deleteUser(user.getUsername());
        
        
        Gson gson = new Gson();
        
        if (delete) {
        	pw.write(gson.toJson("Successful"));
			pw.flush();
        }
        else {
        	pw.write(gson.toJson("Failed"));
			pw.flush();
        }
    }

}
