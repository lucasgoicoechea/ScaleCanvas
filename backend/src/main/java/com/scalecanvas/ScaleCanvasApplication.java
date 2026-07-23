package com.scalecanvas;

import com.scalecanvas.observation.IntegrationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(IntegrationProperties.class)
public class ScaleCanvasApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScaleCanvasApplication.class, args);
    }
}
