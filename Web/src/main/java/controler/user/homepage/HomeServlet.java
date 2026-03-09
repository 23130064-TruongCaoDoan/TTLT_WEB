package controler.user.homepage;

import Service.BookService;
import Service.EventService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Book;
import model.Event;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "home", value = "/home")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        BookService bookService = new BookService();
        List<Book> booksListSale = bookService.getBooksDiscounted();
        List<Book> booksListNew = bookService.getBooksNew();
        List<Book> booksListFavourite = bookService.getFavouriteBook();

        EventService eventService = new EventService();
        eventService.updatBookPriceForEvent();

        List<Event> eventList= eventService.getActiveEventsOrderByStartDateAsc();
        Map<Integer, Boolean> eventHasBooks = new HashMap<>();

        for (Event e : eventList) {
            int count = bookService.countBooksByEvent(e.getId());
            eventHasBooks.put(e.getId(), count > 0);
        }

        request.setAttribute("events", eventList);
        request.setAttribute("eventHasBooks", eventHasBooks);




        request.setAttribute("booksListSale", booksListSale);
        request.setAttribute("booksListNew", booksListNew);
        request.setAttribute("booksListFavourite", booksListFavourite);

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            request.setAttribute("user", session.getAttribute("user"));
        }
        request.getRequestDispatcher("user/home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}