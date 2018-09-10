package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import dbService.DBException;
import dbService.DBService;
import dbService.dataSets.UsersDataSet;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/signin")
public class SignInServlet extends HttpServlet {
    @SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"}) //todo: remove after module 2 home work
    private final AccountService accountService;
//    private final DBService databaseService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
//        this.databaseService = databaseService;
    }

    //sign up
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        DBService dbService = new DBService();
        dbService.printConnectInfo();

        String login = request.getParameter("login");
        String pass = request.getParameter("password");


        if (login == null || pass == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            return;
        }
        else {
            //ищем в базе пользователя по имени, возвращаем его айди,
            // потом по айди создаем датасэт ???зачем??? - чтобы узнать пароль,
            //TODO а если пользователя нет БД вернет ошибку или еще чего?
            UsersDataSet dataSet = null;
            try {
                long id = dbService.getUserByLogin(login);
                dataSet = dbService.getUser(id);
            } catch (DBException e) {
                e.printStackTrace();
            }

            accountService.addNewUser(new UserProfile(dataSet.getName()));
            UserProfile profile = accountService.getUserByLogin(login);

            if (profile == null || !profile.getPass().equals(pass)) {
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }else {
                response.setStatus(HttpServletResponse.SC_OK);
            }
            Map<String, Object> pageVariables = createPageVariablesMap(request);

            String message = request.getParameter("login");
            pageVariables.put("login", message == null ? "" : message);

            response.getWriter().println(PageGenerator.instance().getPage("chat.html", pageVariables));

            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);

//        PrintWriter out = response.getWriter();
//        out.println("Authorized: "+ login);
//        out.close();
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
