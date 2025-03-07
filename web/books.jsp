<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Book List</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: Arial, sans-serif; }
        body { background-color: #f4f4f4; text-align: center; padding: 20px; }
        .container { width: 80%; margin: auto; background: white; padding: 20px; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); border-radius: 8px; }
        h2 { color: #333; margin-bottom: 20px; }
        .message { padding: 10px; margin-bottom: 15px; border-radius: 5px; text-align: center; font-weight: bold; }
        .success { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .error { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .book-list { display: flex; flex-wrap: wrap; justify-content: center; gap: 20px; margin-top: 20px; }
        .book-card { background: white; border: 1px solid #ddd; padding: 15px; border-radius: 8px; width: 250px; box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1); text-align: left; }
        .book-title { font-size: 18px; font-weight: bold; color: #007bff; }
        .book-author { font-size: 16px; color: #555; margin-top: 5px; }
        .book-quantity { font-size: 14px; color: #333; margin-top: 5px; }
        .borrow-btn { display: block; width: 100%; padding: 10px; margin-top: 10px; background: #007bff; color: white; text-align: center; border-radius: 5px; text-decoration: none; font-size: 14px; transition: 0.3s; border: none; cursor: pointer; }
        .borrow-btn:hover { background: #0056b3; }
        .logout-btn { display: inline-block; margin-top: 20px; padding: 10px 20px; background: #dc3545; color: white; text-decoration: none; border-radius: 5px; font-size: 16px; transition: 0.3s; }
        .logout-btn:hover { background: #c82333; }
    </style>
</head>
<body>

    <div class="container">
        <h2>Available Books</h2>

        <c:if test="${not empty param.success}">
            <p class="message success">${param.success}</p>
        </c:if>
        <c:if test="${not empty param.error}">
            <p class="message error">${param.error}</p>
        </c:if>

        <div class="book-list">
            <c:forEach var="book" items="${sessionScope.books}">
                <div class="book-card">
                    <p class="book-title">${book.title}</p>
                    <p class="book-author">Author: ${book.author}</p>
                    <p class="book-quantity">Available: ${book.quantity}</p>
                    <form action="library" method="post">
                        <input type="hidden" name="action" value="borrow">
                        <input type="hidden" name="bookId" value="${book.id}">
                        <button type="submit" class="borrow-btn">Borrow</button>
                    </form>
                </div>
            </c:forEach>
        </div>

        <h2>Your Borrowed Books</h2>
        <div class="book-list">
            <c:choose>
                <c:when test="${not empty sessionScope.borrowedBooks}">
                    <c:forEach var="book" items="${sessionScope.borrowedBooks}">
                        <div class="book-card">
                            <p class="book-title">${book.title}</p>
                            <p class="book-author">Author: ${book.author}</p>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p>You haven't borrowed any books yet.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <a href="library?action=logout" class="logout-btn">Logout</a>
    </div>

</body>
</html>
