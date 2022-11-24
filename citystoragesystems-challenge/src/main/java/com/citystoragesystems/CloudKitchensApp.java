package com.citystoragesystems;

import com.citystoragesystems.service.InputParserService;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.logging.log4j.Logger;

import java.io.IOException;

@SpringBootApplication
public class CloudKitchensApp implements CommandLineRunner {

    @Autowired
    private InputParserService inputParserService;

    private static Logger LOG = LogManager.getLogger(CloudKitchensApp.class);

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(CloudKitchensApp.class, args);
    }

    @Override
    public void run(String[] args) throws IOException {
        inputParserService.parse(args);
    }
}
