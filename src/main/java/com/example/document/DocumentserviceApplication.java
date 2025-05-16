package com.example.document;

import com.example.document.model.Category;
import com.example.document.model.Department;
import com.example.document.repository.CategoryRepository;
import com.example.document.repository.DepartmentRepository;
import com.example.document.security.JwtAuthFilter;
import com.example.document.util.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DocumentserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocumentserviceApplication.class, args);
    }

    // ✅ Declare the filter instance manually
    @Bean
    public JwtAuthFilter jwtAuthFilter(JwtUtil jwtUtil) {
        return new JwtAuthFilter(jwtUtil);
    }

    // ✅ Register filter with /api/* pattern
    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtFilter(JwtAuthFilter filter) {
        FilterRegistrationBean<JwtAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1); // Ensure it runs early
		System.out.println("✅ JwtAuthFilter registered for /api/*");
        return registrationBean;
    }

    // ✅ Seed initial data
    @Bean
    public org.springframework.boot.CommandLineRunner seedData(
            DepartmentRepository departmentRepository,
            CategoryRepository categoryRepository) {

        return args -> {
           if (departmentRepository.count() == 0) {
    departmentRepository.save(Department.builder().name("IT").build());
    departmentRepository.save(Department.builder().name("HR").build());
}


            if (categoryRepository.count() == 0) {
                categoryRepository.save(Category.builder().name("Invoices").build());
                categoryRepository.save(Category.builder().name("Reports").build());

            }
        };
    }
}
