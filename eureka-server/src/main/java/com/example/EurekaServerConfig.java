package com.example;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Setter
@Getter
@ConfigurationProperties
@RefreshScope
@ToString
public class EurekaServerConfig {
}
