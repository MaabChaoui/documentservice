package com.example.document.security;

import lombok.Data;

import java.util.List;

@Data
public class UserContext {
    private String email;
    private String role;
    private List<Integer> departments;

    private static final ThreadLocal<UserContext> context = new ThreadLocal<>();

    public static void set(UserContext userContext) {
        context.set(userContext);
    }

    public static UserContext get() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}
