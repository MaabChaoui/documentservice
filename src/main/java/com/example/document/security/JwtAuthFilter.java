package com.example.document.security;

import com.example.document.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class JwtAuthFilter implements Filter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        System.out.println("🚨 JwtAuthFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();

        System.out.println("🧪 JwtAuthFilter doFilter triggered");
        System.out.println("➡️ Incoming request: " + method + " " + uri);

        // List all headers
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        System.out.println("📦 Headers:");
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            System.out.println("   → " + header + ": " + httpRequest.getHeader(header));
        }

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Missing or invalid Authorization header");
            System.out.println("❌ Missing or invalid Authorization header");
            return;
        }

        try {
            String token = authHeader.substring(7);
            System.out.println("🔐 Token received: " + token);

            Claims claims = jwtUtil.extractAllClaims(token);
            System.out.println("✅ Claims extracted:");
            System.out.println("   - Subject: " + claims.getSubject());
            System.out.println("   - Role: " + claims.get("role"));
            System.out.println("   - Departments: " + claims.get("departments"));

            UserContext ctx = new UserContext();
            ctx.setEmail(claims.getSubject());
            ctx.setRole((String) claims.get("role"));

            List<?> rawDepartments = (List<?>) claims.get("departments");
            List<Integer> departmentIds = new ArrayList<>();
            for (Object obj : rawDepartments) {
                try {
                    departmentIds.add(Integer.parseInt(obj.toString()));
                } catch (Exception ex) {
                    System.out.println("⚠️ Could not parse department ID: " + obj);
                }
            }
            ctx.setDepartments(departmentIds);

            UserContext.set(ctx);

            System.out.println("✅ JWT context set: " + ctx.getEmail() + ", Depts=" + departmentIds);

        } catch (Exception e) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Invalid or expired token: " + e.getMessage());
            System.out.println("❌ Token validation error: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        try {
            System.out.println("✅ Passing request to next filter/controller...");
            chain.doFilter(request, response);
        } finally {
            UserContext.clear();
            System.out.println("🧼 UserContext cleared");
        }
    }
}
