package max.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import max.models.MenuItem;
import max.requests.MenuItem.CreateMenuItemRequest;
import max.utils.Http;

import javax.persistence.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;


public class MenuItemsServlet extends HttpServlet {

    private Gson gson = new Gson();
    private EntityManager em;

    /** Example: /menu-items?priceFrom=20&priceTo=50&isDiscount=1&weightTo=1000 */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        EntityManagerFactory emf = (EntityManagerFactory)getServletContext().getAttribute("emf");
        em = emf.createEntityManager();

        // Prepare query params
        Long priceFrom = req.getParameter("priceFrom") == null ? null : Long.parseLong(req.getParameter("priceFrom"));
        Long priceTo = req.getParameter("priceTo") == null ? null : Long.parseLong(req.getParameter("priceTo"));
        Integer weightTo = req.getParameter("weightTo") ==null ? null : Integer.parseInt(req.getParameter("weightTo"));
        Boolean isDiscount = null;
        if(req.getParameter("isDiscount") != null) {
            isDiscount = "1".equals(req.getParameter("isDiscount"));
        }

        // Prepare HQL string
        List<String> prepareQuery = new LinkedList<>();

        prepareQuery.add("SELECT mi FROM MenuItem mi");
        prepareQuery.add("WHERE mi.id > 0");

        if(priceFrom != null) {
            prepareQuery.add("AND mi.price >= :priceFrom");
        }

        if(priceTo != null) {
            prepareQuery.add("AND mi.price <= :priceTo");
        }

        if(isDiscount != null) {
            prepareQuery.add("AND mi.isDiscount = :isDiscount");
        }

        if(weightTo != null) {
            prepareQuery.add("AND mi.weight <= :weightTo");
            prepareQuery.add("ORDER BY mi.weight DESC");
        }

        String queryString = String.join(" ", prepareQuery);
 
        Query query = em.createQuery(queryString, MenuItem.class);

        // Prepare params to HQL string
        if(priceFrom != null) query.setParameter("priceFrom", priceFrom);
        if(priceTo != null) query.setParameter("priceTo", priceTo);
        if(isDiscount != null) query.setParameter("isDiscount", isDiscount);
        if(weightTo != null) query.setParameter("weightTo", weightTo);


        List<MenuItem> list = (List<MenuItem>) query.getResultList();
        List<MenuItem> balancedMenu = new ArrayList<>();

        // Balance our menu-items by total weight
        int totalWeight = 0;
        for (MenuItem mi:list) {
            int checkWeight = totalWeight + mi.getWeight();
            if(checkWeight <= weightTo) {
                balancedMenu.add(mi);
                totalWeight = checkWeight;
            }
        }

        Gson gson = new Gson();
        String str = gson.toJson(balancedMenu);

        Http.sendResponse(resp, HttpServletResponse.SC_OK, str);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        EntityManagerFactory emf = (EntityManagerFactory)getServletContext().getAttribute("emf");
        em = emf.createEntityManager();

        String body = Http.getBody(req);

        try {

            // Parse request json body into MenuItemRequest object
            CreateMenuItemRequest menuItemRequest = new Gson().fromJson(body, CreateMenuItemRequest.class);

            // Create new Menu Item from JsonRequest
            MenuItem mi = new MenuItem();
            mi.setName(menuItemRequest.getName());
            mi.setPrice(menuItemRequest.getPrice());
            mi.setDiscount(menuItemRequest.isDiscount());
            mi.setWeight(menuItemRequest.getWeight());

            em.getTransaction().begin();
            em.persist(mi);
            em.getTransaction().commit();

        } catch (PersistenceException | JsonSyntaxException e) {
            em.getTransaction().rollback();
            Http.sendResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } finally {
            em.close();
        }

        Http.sendResponse(resp, HttpServletResponse.SC_NO_CONTENT);
    }
}
