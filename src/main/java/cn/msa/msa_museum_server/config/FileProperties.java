package cn.msa.msa_museum_server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "file")
@Getter
@Setter
public class FileProperties {
    private String storageLocation;
    private int maxUploadSize = 1024 * 1024 * 10;
}