package com.scalecanvas.scenario.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.scalecanvas.TestFixtures;
import com.scalecanvas.scenario.application.ScenarioVersionService;
import com.scalecanvas.scenario.infrastructure.ScenarioVersionEntity;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ScenarioVersionController.class)
class ScenarioVersionControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean ScenarioVersionService service;

    @Test
    void listsVersions() throws Exception {
        when(service.listVersions(any())).thenReturn(List.of(
                new ScenarioVersionEntity(UUID.randomUUID(), UUID.randomUUID(), "v1", "{}", java.time.Instant.now())));

        mockMvc.perform(get("/api/v1/scenario-versions/" + UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].versionLabel").value("v1"));
    }

    @Test
    void createsVersion() throws Exception {
        when(service.createVersion(any(), any(), any())).thenAnswer(inv -> {
            var scenarioId = inv.getArgument(0, UUID.class);
            var request = inv.getArgument(2, com.scalecanvas.scenario.api.dto.ScenarioRequest.class);
            return new ScenarioVersionEntity(UUID.randomUUID(), scenarioId, inv.getArgument(1), "{}", java.time.Instant.now());
        });

        mockMvc.perform(post("/api/v1/scenario-versions/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(TestFixtures.scenarioRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.versionLabel").value("v1"));
    }
}
