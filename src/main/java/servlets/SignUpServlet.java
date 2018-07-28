package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import com.google.gson.Gson;
import dbService.DBException;
import dbService.DBService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
        }

        PrintWriter out = response.getWriter();
        out.println("User registered: "+ login);
        out.close();
    }

}
