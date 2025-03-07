package controller;

import model.LibraryDAO;
import model.Book;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class LibraryServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("login".equals(action)) {
            handleLogin(request, response);
        } else if ("borrow".equals(action)) {
            handleBorrowBook(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("logout".equals(action)) {
            handleLogout(request, response);
        } else if ("viewBooks".equals(action)) {
            handleViewBooks(request, response);
        } else if ("home".equals(action)) {
            handleHomePage(request, response);
        }
    }

    // ==================== XỬ LÝ TRANG HOME ====================
    private void handleHomePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LibraryDAO dao = new LibraryDAO();
        List<Book> books = dao.getAvailableBooks();

        // Kiểm tra danh sách sách có dữ liệu không
        if (books == null || books.isEmpty()) {
            System.out.println("❌ Không có sách nào trong database.");
        } else {
            System.out.println("✅ Lấy danh sách sách thành công! Số lượng: " + books.size());
        }

        request.setAttribute("books", books);
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }

    // ==================== XỬ LÝ ĐĂNG NHẬP ====================
    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        LibraryDAO dao = new LibraryDAO();
        User user = dao.login(email, password);

        if (user == null) {
            request.setAttribute("error", "Email not found!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else if ("wrong_password".equals(user.getPassword())) {
            request.setAttribute("error", "Incorrect password!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Lấy danh sách sách sau khi đăng nhập
            List<Book> books = dao.getAvailableBooks();
            session.setAttribute("books", books);

            response.sendRedirect("books.jsp");
        }
    }

    // ==================== XỬ LÝ XEM DANH SÁCH SÁCH ====================
    private void handleViewBooks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LibraryDAO dao = new LibraryDAO();
        List<Book> books = dao.getAvailableBooks();
        request.setAttribute("books", books);
        request.getRequestDispatcher("books.jsp").forward(request, response);
    }

    // ==================== XỬ LÝ MƯỢN SÁCH ====================
    private void handleBorrowBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int bookId = Integer.parseInt(request.getParameter("bookId"));
        LibraryDAO dao = new LibraryDAO();

        boolean success = dao.borrowBook(bookId, user.getId());

        if (success) {
            // Cập nhật danh sách sách có sẵn sau khi mượn
            List<Book> books = dao.getAvailableBooks();
            session.setAttribute("books", books);

            // Cập nhật danh sách sách đã mượn của người dùng
            List<Book> borrowedBooks = dao.getBorrowedBooks(user.getId());
            session.setAttribute("borrowedBooks", borrowedBooks);

            response.sendRedirect("books.jsp?success=Borrowed successfully");
        } else {
            request.setAttribute("error", "Borrowing failed! Try again.");
            request.getRequestDispatcher("books.jsp").forward(request, response);
        }
    }

    // ==================== XỬ LÝ ĐĂNG XUẤT ====================
private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    if (session != null) {
        session.invalidate(); // Hủy session
    }

    System.out.println("🔴 Người dùng đã logout!");

    // Gọi lại trang home để load danh sách sách
    handleHomePage(request, response);
}

}
