package com.scalecanvas.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MutationApiKeyConfig implements WebMvcConfigurer {
    static final String HEADER = "X-ScaleCanvas-Key";
    private final String mutationApiKey;

    public MutationApiKeyConfig(@Value("${app.security.mutation-api-key:}") String mutationApiKey) {
        this.mutationApiKey = mutationApiKey == null ? "" : mutationApiKey;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MutationApiKeyInterceptor(mutationApiKey))
                .addPathPatterns("/api/**");
    }

    static final class MutationApiKeyInterceptor implements HandlerInterceptor {
        private final byte[] expected;

        MutationApiKeyInterceptor(String mutationApiKey) {
            this.expected = mutationApiKey.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public boolean preHandle(
                HttpServletRequest request,
                HttpServletResponse response,
                Object handler) {
            if (expected.length == 0
                    || HttpMethod.GET.matches(request.getMethod())
                    || HttpMethod.HEAD.matches(request.getMethod())
                    || HttpMethod.OPTIONS.matches(request.getMethod())) {
                return true;
            }
            String provided = request.getHeader(HEADER);
            boolean matches = provided != null && MessageDigest.isEqual(
                    expected,
                    provided.getBytes(StandardCharsets.UTF_8));
            if (matches) {
                return true;
            }
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/problem+json");
            return false;
        }
    }
}
