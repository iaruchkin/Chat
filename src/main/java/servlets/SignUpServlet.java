package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import dbService.DBException;
import dbService.DBService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {
    @SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"}) //todo: remove after module 2 home work
    private final AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    //sign up
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String pass = request.getParameter("password");

        UserProfile profile = new UserProfile(login);
        accountService.addNewUser(profile);

        DBService dbService = new DBService();
        dbService.printConnectInfo();

        try {
            long userId;
            userId = dbService.addUser(login);
            System.out.println("Added student id: " + userId);
        } catch (DBException e) {
            e.printStackTrace();
        }

        if (login == null || pass == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            //return;
        } else {
//            Map<String, Object> pageVariables = createPageVariablesMap(request);
//
//            String message = request.getParameter("message");
//
//            if (message == null || message.isEmpty()) {
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            } else {
//                response.setStatus(HttpServletResponse.SC_OK);
//            }
//            pageVariables.put("user", message == null ? "" : message);
//
//            response.getWriter().println(PageGenerator.instance().getPage("chat.html", pageVariables));
//
//            response.setContentType("text/html;charset=utf-8");
//            response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
        out.println("User registered: "+ login);
        out.close();

        }
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        pageVariables.put("pathInfo", request.getPathInfo());
        pageVariables.put("sessionId", request.getSession().getId());
        pageVariables.put("parameters",request.getParameterMap().toString());
        return pageVariables;
    }
}
