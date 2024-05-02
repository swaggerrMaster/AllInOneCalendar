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
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

@WebServlet("/FetchEventsServlet")
public class FetchEventsServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        String userIDsJson = request.getParameter("users");
        if (userIDsJson == null) {
            out.println("[]");
            return;
        }

        // put into list of strings
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> userIDs = gson.fromJson(userIDsJson, listType);

        // Convert each string in the list to an integer
        List<Integer> ids = new ArrayList<>();
        try {
            for (String userID : userIDs) {
                ids.add(Integer.parseInt(userID));
            }
            System.out.println("IDS chnaged to ints");
            for (String userID : userIDs) {
                System.out.println(userID = " ");
                System.out.println();
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing user IDs: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user IDs");
            return;
        }

        List<Event> events = DataBase.getEventsByUserIDs(ids);
        String eventsJson = new Gson().toJson(events);
        System.out.println("Events sending: " + eventsJson);
        out.print(eventsJson);
        out.flush();
    }
}