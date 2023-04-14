package com.oystercard.service;

import com.oystercard.entity.OysterCardEventType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Service
public class OysterCardSystem {

    private static final String SHUT_DOWN = "SHUT_DOWN";
    List<OysterCardEventProcessor> oysterCardEventProcessors;
    private final Scanner scanner;

    public OysterCardSystem(List<OysterCardEventProcessor> oysterCardEventProcessors) {
        this.oysterCardEventProcessors = oysterCardEventProcessors;
        this.scanner = new Scanner(System.in);
    }

    private void printEventTypes() {
        oysterCardEventProcessors.forEach(oysterCardEventProcessor -> oysterCardEventProcessor.printOysterCardEventType());
    }

    public void run() {
        System.out.println("Input the type of event or type -1 to shut down the app. Following are the supported types: ");
        Arrays.stream(OysterCardEventType.values()).forEach(oysterCardEventType -> {
            System.out.println(oysterCardEventType.getId() + " for "+ oysterCardEventType.name());
        });
        String input = scanner.nextLine();
        int command = Integer.parseInt(input);
        if (command == -1) {
            return;
        }
        OysterCardEventType eventType = Arrays.stream(OysterCardEventType.values()).filter(e -> e.getId() == command).findFirst().orElse(null);
        if (eventType == null || !oysterCardEventProcessors.stream().filter(oysterCardEventProcessor -> oysterCardEventProcessor.matches(eventType)).findFirst().isPresent()) {
            System.out.println("Invalid event type.");
        } else {
            oysterCardEventProcessors.stream().forEach(oysterCardEventProcessor -> {
                if (oysterCardEventProcessor.matches(eventType)) {
                    try {
                        oysterCardEventProcessor.run(scanner);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Some error occurred during the execution");
                    }
                }
            });
        }
        run();
    }
}
