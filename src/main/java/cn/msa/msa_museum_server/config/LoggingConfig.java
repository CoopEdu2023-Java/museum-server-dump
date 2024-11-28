package cn.msa.msa_museum_server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {
    private static final Logger log = LoggerFactory.getLogger(LoggingConfig.class);

    public LoggingConfig() {
        log.info("Logging configuration initialized."); // Use 'log' instead of 'logger'
    }
}
