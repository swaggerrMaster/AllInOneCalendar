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

@WebServlet("/AddEventServlet")
public class AddEventServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        
        System.out.println("event body: " + requestBody);
        
        Event event = new Gson().fromJson(requestBody, Event.class);
        
        // get user ID
        int userID = DataBase.getUserIDByUsername(event.getUsername());
        
        // add userID to the event
        event.setUserID(userID);
        
        
        // add event to database
        int eventID = DataBase.addEvent(event);
        
        event.setEventID(eventID);
        
        event.displayEventDetails();
        
        if (eventID > 0) {
            out.println(new Gson().toJson(event));
        } else {
            out.println(new Gson().toJson("EventAddFail"));
        }
    }
}
