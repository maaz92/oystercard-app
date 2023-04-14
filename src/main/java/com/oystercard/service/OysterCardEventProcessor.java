package com.oystercard.service;

import com.oystercard.entity.OysterCardEvent;
import com.oystercard.entity.OysterCardEventType;

import java.util.Scanner;

public interface OysterCardEventProcessor {
    boolean matches(OysterCardEventType oysterCardEventType);

    void run(Scanner scanner);
    void printOysterCardEventType();
}
