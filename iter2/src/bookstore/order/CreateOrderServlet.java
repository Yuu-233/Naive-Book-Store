package bookstore.order;

import bookstore.Helper;
import bookstore.database.DatabaseAccess;
import bookstore.database.OrderEntity;
import bookstore.database.UserEntity;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tang on 2017/4/11.
 */
@WebServlet("/submit_new_order")
public class CreateOrderServlet extends HttpServlet {
    public void init() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String orderDate = request.getParameter("orderDate");
        String totalPrice = request.getParameter("totalPrice");
        String deliverTo = request.getParameter("deliverTo");

        Map<String, Boolean> json_obj = new HashMap<>();
        Session hibernate_session = DatabaseAccess.getSession();
        try {
            System.out.println("made it here");
            hibernate_session.beginTransaction();
            OrderEntity order = new OrderEntity();
            order.setUser(hibernate_session.load(UserEntity.class, Integer.parseInt(userId)));
            order.setOrderDate(Date.valueOf(orderDate));
            order.setTotalPrice(BigDecimal.valueOf(Float.parseFloat(totalPrice)));
            order.setDeliverTo(deliverTo);
            hibernate_session.save(order);
            hibernate_session.getTransaction().commit();
            json_obj.put("status", true);
        } catch (Exception e) {
            Helper.goError(response);
            json_obj.put("status", false);
        } finally {
            hibernate_session.close();
        }

        Helper.responseJson(json_obj, response);
    }
}
