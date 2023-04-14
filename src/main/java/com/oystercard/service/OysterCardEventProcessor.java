package com.citystoragesystems.service;

import com.citystoragesystems.entity.OysterCardEvent;

import java.util.Scanner;

public interface OysterCardEventProcessor {
    void process(OysterCardEvent oysterCardEvent);
    boolean matches(String eventType);

    void run(Scanner scanner);
    boolean validate(OysterCardEvent oysterCardEvent);
    void printOysterCardEventType();
}
