package com.scalecanvas.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class MutationApiKeyConfigTest {

    @Test
    void allowsReadRequestsWithoutKey() {
        var interceptor = new MutationApiKeyConfig.MutationApiKeyInterceptor("secret");
        var request = new MockHttpServletRequest("GET", "/api/v1/scenarios");
        var response = new MockHttpServletResponse();

        assertThat(interceptor.preHandle(request, response, new Object())).isTrue();
    }

    @Test
    void rejectsMutationWithoutConfiguredKey() {
        var interceptor = new MutationApiKeyConfig.MutationApiKeyInterceptor("secret");
        var request = new MockHttpServletRequest("POST", "/api/v1/evaluations");
        var response = new MockHttpServletResponse();

        assertThat(interceptor.preHandle(request, response, new Object())).isFalse();
        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    void allowsMutationWithMatchingKey() {
        var interceptor = new MutationApiKeyConfig.MutationApiKeyInterceptor("secret");
        var request = new MockHttpServletRequest("POST", "/api/v1/evaluations");
        request.addHeader(MutationApiKeyConfig.HEADER, "secret");

        assertThat(interceptor.preHandle(request, new MockHttpServletResponse(), new Object())).isTrue();
    }
}
