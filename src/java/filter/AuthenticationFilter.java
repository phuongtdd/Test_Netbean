package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*") // Áp dụng filter cho tất cả request
public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false); // Lấy session hiện tại (không tạo mới)
        String path = req.getRequestURI();

        // 🛑 Nếu chưa login mà vào books.jsp → Chặn lại
        if ((session == null || session.getAttribute("user") == null) && path.endsWith("books.jsp")) {
            res.sendRedirect("login.jsp");
            return;
        }

        // 🛑 Nếu đã login mà vào login.jsp → Chuyển hướng sang books.jsp
        if (session != null && session.getAttribute("user") != null && path.endsWith("login.jsp")) {
            res.sendRedirect("books.jsp");
            return;
        }

        // ✅ Nếu không bị chặn, tiếp tục xử lý request
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
