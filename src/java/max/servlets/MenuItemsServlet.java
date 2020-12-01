package max.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

public class MenuItemsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        HttpServletRequestWrapper reqWrapper = new HttpServletRequestWrapper(req);
//
//        for (Map.Entry<String,String[]> set: reqWrapper.getParameterMap().entrySet()) {
//            System.out.println(set.getKey());
//            System.out.println(Arrays.toString(set.getValue()));
//        }

        resp.setContentType("application/json");
        resp.setStatus(200);

        OutputStream os = resp.getOutputStream();
        byte[] buf = "Test2".getBytes(StandardCharsets.UTF_8);
        os.write(buf);
    }
}
