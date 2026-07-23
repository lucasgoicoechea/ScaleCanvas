package com.scalecanvas.scenario.application;

import com.scalecanvas.TestFixtures;
import com.scalecanvas.scenario.infrastructure.ScenarioVersionEntity;
import com.scalecanvas.scenario.infrastructure.ScenarioVersionJpaRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ScenarioVersionServiceTest {
    @Test
    void createsAndListsVersions() {
        var repository = mock(ScenarioVersionJpaRepository.class);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(repository.findByScenarioIdOrderByCreatedAtDesc(any())).thenReturn(List.of());
        var service = new ScenarioVersionService(repository, new com.fasterxml.jackson.databind.ObjectMapper());

        var version = service.createVersion(UUID.randomUUID(), "v1", TestFixtures.scenarioRequest());

        assertThat(version.getVersionLabel()).isEqualTo("v1");
        assertThat(version.getPayloadJson()).contains("Portfolio SaaS");
        ArgumentCaptor<ScenarioVersionEntity> captor = ArgumentCaptor.forClass(ScenarioVersionEntity.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getScenarioId()).isEqualTo(version.getScenarioId());
    }
}
