package com.scalecanvas.scenario.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalecanvas.TestFixtures;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DeploymentController.class)
class DeploymentControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean com.scalecanvas.scenario.application.ScenarioService scenarioService;

    @Test
    void returnsDeployment() throws Exception {
        var deployment = TestFixtures.scenarioRequest().deployment();
        when(scenarioService.getDeployment(any())).thenReturn(deployment);

        mockMvc.perform(get("/api/v1/scenarios/" + UUID.randomUUID() + "/deployment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serverType").value("CONTAINER"))
                .andExpect(jsonPath("$.cloudProvider").value("AWS"))
                .andExpect(jsonPath("$.minimumUnitMemoryMb").value(4096));
    }

    @Test
    void updatesDeployment() throws Exception {
        var deployment = TestFixtures.scenarioRequest().deployment();
        when(scenarioService.updateDeployment(any(), any())).thenReturn(deployment);

        mockMvc.perform(put("/api/v1/scenarios/" + UUID.randomUUID() + "/deployment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deployment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deploymentService").value("EKS"));
    }
}
