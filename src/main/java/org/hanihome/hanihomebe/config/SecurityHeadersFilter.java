package org.hanihome.hanihomebe.config;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityHeadersFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // COOP/COEP 제거
        httpResponse.setHeader("Cross-Origin-Opener-Policy", "unsafe-none");
        httpResponse.setHeader("Cross-Origin-Embedder-Policy", "unsafe-none");

        chain.doFilter(request, response);

    }
}
