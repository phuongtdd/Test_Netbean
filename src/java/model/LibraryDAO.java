package model;

import model.Book;
import model.User;
import model.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryDAO {

    private Connection conn;

    public LibraryDAO() {
        this.conn = DBConnection.getConnection(); // Sử dụng DBConnection để lấy kết nối
        if (this.conn == null) {
            System.out.println("❌ Lỗi: Không thể kết nối database trong LibraryDAO!");
        } else {
            System.out.println("✅ LibraryDAO đã kết nối database thành công!");
        }
    }

    // Xử lý đăng nhập
    public User login(String email, String password) {
        String query = "SELECT * FROM Users WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                if (storedPassword.equals(password)) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"));
                } else {
                    return new User(-1, "", "wrong_password", ""); // Đánh dấu mật khẩu sai
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi trong login(): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Lấy danh sách sách
    public List<Book> getAvailableBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM Books WHERE quantity > 0";

        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn trong getAvailableBooks(): " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }

    // Mượn sách
    public boolean borrowBook(int bookId, int userId) {
        String updateQuery = "UPDATE Books SET quantity = quantity - 1 WHERE id = ? AND quantity > 0";
        String insertQuery = "INSERT INTO Loans (book_id, borrower_id, loan_date) VALUES (?, ?, GETDATE())";

        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setInt(1, bookId);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                try (PreparedStatement stmt2 = conn.prepareStatement(insertQuery)) {
                    stmt2.setInt(1, bookId);
                    stmt2.setInt(2, userId);
                    return stmt2.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi trong borrowBook(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    //Danh sách books đã mượn
    public List<Book> getBorrowedBooks(int userId) {
        List<Book> borrowedBooks = new ArrayList<>();
        String query = "SELECT b.id, b.title, b.author FROM Books b "
                + "JOIN Loans l ON b.id = l.book_id WHERE l.borrower_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                borrowedBooks.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        0 // Không cần hiển thị số lượng sách đã mượn
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowedBooks;
    }

    // Test kết nối & lấy danh sách sách từ database
    public static void main(String[] args) {
        LibraryDAO dao = new LibraryDAO();
        List<Book> books = dao.getAvailableBooks();

        if (books.isEmpty()) {
            System.out.println("Không có sách nào trong database.");
        } else {
            for (Book book : books) {
                System.out.println("ID: " + book.getId() + " | Title: " + book.getTitle()
                        + " | Author: " + book.getAuthor() + " | Quantity: " + book.getQuantity());
            }
        }
    }
}
