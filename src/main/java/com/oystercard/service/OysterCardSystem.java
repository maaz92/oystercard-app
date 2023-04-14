package com.citystoragesystems.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class OysterCardSystem {

    private static Logger LOG = LogManager.getLogger(OrderService.class);

    private Scanner scanner;

    private static final String SHUT_DOWN = "SHUT_DOWN";

    List<OysterCardEventProcessor> oysterCardEventProcessors;

    public OysterCardSystem(List<OysterCardEventProcessor> oysterCardEventProcessors) {
        this.oysterCardEventProcessors = oysterCardEventProcessors;
        this.scanner = new Scanner(System.in);
    }

    private void printEventTypes() {
        oysterCardEventProcessors.forEach(oysterCardEventProcessor -> oysterCardEventProcessor.printOysterCardEventType());
    }
    public void run() {
        System.out.println("Input the type of event or type SHUT_DOWN to shut down the app. Following are the supported types: ");
        printEventTypes();
        String eventType = scanner.nextLine();
        if(SHUT_DOWN.equals(eventType)) {
            return;
        }
        if(!oysterCardEventProcessors.stream().filter(oysterCardEventProcessor -> oysterCardEventProcessor.matches(eventType)).findFirst().isPresent()) {
            System.out.println("Invalid event type.");
        } else {
            oysterCardEventProcessors.stream().forEach(oysterCardEventProcessor -> {
                if(oysterCardEventProcessor.matches(eventType)) {
                    oysterCardEventProcessor.run(scanner);
                }
            });
        }
        run();
    }
}
