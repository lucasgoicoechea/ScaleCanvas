package com.scalecanvas.scenario.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.scalecanvas.TestFixtures;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ScalingController.class)
class ScalingControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean com.scalecanvas.scenario.application.ScenarioService scenarioService;

    @Test
    void returnsScalingMatrix() throws Exception {
        when(scenarioService.getDeployment(any())).thenReturn(TestFixtures.scenarioRequest().deployment());

        mockMvc.perform(get("/api/v1/scenarios/" + UUID.randomUUID() + "/scaling-matrix"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serverType").value("CONTAINER"))
                .andExpect(jsonPath("$.cloudProvider").value("AWS"))
                .andExpect(jsonPath("$.services[0]").value("api"))
                .andExpect(jsonPath("$.matrix[0].serviceName").value("api"))
                .andExpect(jsonPath("$.matrix[0].utilizationPercent").isNumber());
    }
}
