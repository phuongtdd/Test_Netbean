<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Login</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
                font-family: Arial, sans-serif;
            }

            body {
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                background-color: #f4f4f4;
            }

            .container {
                width: 350px;
                background: white;
                padding: 30px;
                box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
                border-radius: 8px;
                text-align: center;
            }

            h2 {
                margin-bottom: 20px;
                color: #333;
            }

            .error {
                color: red;
                background: #ffdddd;
                padding: 10px;
                margin-bottom: 15px;
                border: 1px solid red;
                border-radius: 5px;
            }

            input {
                width: 100%;
                padding: 10px;
                margin: 10px 0;
                border: 1px solid #ccc;
                border-radius: 5px;
            }

            button {
                width: 100%;
                padding: 10px;
                background: #007bff;
                border: none;
                color: white;
                font-size: 16px;
                border-radius: 5px;
                cursor: pointer;
                transition: 0.3s;
            }

            button:hover {
                background: #0056b3;
            }
        </style>
    </head>
    <body>

        <div class="container">
            <h2>Login</h2>

            <c:if test="${not empty requestScope.error}">
                <p class="error">${requestScope.error}</p>
            </c:if>

            <form action="library" method="post">
                <input type="hidden" name="action" value="login">
                <input type="email" name="email" placeholder="Email" required>
                <input type="password" name="password" placeholder="Password" required>
                <button type="submit">Log-in</button>

            </form>
        </div>

    </body>
</html>
