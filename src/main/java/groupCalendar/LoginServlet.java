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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println(requestBody);
        User user = new Gson().fromJson(requestBody, User.class);
        
        String name = DataBase.getNameByUsername(user.getUsername());
        
        user.setName(name);
        
        System.out.println("authetitcaton: " + user.getName() + user.getUsername() + user.getPassword());
        boolean isAuthenticated = authenticateUser(user.getUsername(), user.getPassword());

        if (isAuthenticated) {
        	System.out.println("Name: " + user.getName());
            pw.write(new Gson().toJson(user));
        } else {
            pw.write(new Gson().toJson("Invalid"));
        }
        pw.flush();
    }

    private boolean authenticateUser(String username, String password) {
        return DataBase.checkLogin(username, password);
    }

}

