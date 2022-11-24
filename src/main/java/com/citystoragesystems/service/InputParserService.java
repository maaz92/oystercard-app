package com.citystoragesystems.service;

import com.citystoragesystems.entity.Order;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Reads the Orders from the input file
 * and sends them to OrderService for processing
 *
 * @see     Order
 * @see     OrderService
 * @author  Mohammad Maaz Khan
 */
@Service
public class InputParserService {

    private static Logger LOG = LogManager.getLogger(InputParserService.class);

    @Autowired
    private  OrderService orderService;

    /**
     * Constructs an instance of InputParserService
     *
     *
     * @param   orderService  The kitchen service
     * @see                   KitchenService
     * @author                Mohammad Maaz Khan
     */
    public InputParserService(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Read the arguments and parse the input file
     *
     *
     * @param   args  The command line arguments Ingestion Rate and Orders File respectfully
     * @author        Mohammad Maaz Khan
     */
    public void parse(String[] args) throws IOException {
        if(args.length != 2) {
            LOG.error("Expected 2 arguments. Found {} argument(s)", args.length);
            throw new IllegalArgumentException("Expected 2 arguments. Found " + args.length + " argument(s)");
        }
        Double ingestionRate;
        try {
            ingestionRate = Double.parseDouble(args[0]);
        } catch (NumberFormatException numberFormatException) {
            LOG.error("Expected 1st argument to be double. Given {}", args[0]);
            throw new IllegalArgumentException("Expected 1st argument to be double. Given " + args[0]);
        }
        if(ingestionRate <= 0.0 ) {
            LOG.error("Ingestion rate cannot be zero or negative. Given {}", args[0]);
            throw new IllegalArgumentException("Ingestion rate cannot be zero or negative. Given "+ args[0]);
        }
        String filePath = args[1];
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException ioException) {
            LOG.error("Failed to read file {}", filePath);
            throw new IOException("Failed to read file " + filePath);
        }
        String json = contentBuilder.toString();
        List<Order> orders;
        try {
            Gson gson = new Gson();
            orders = gson.fromJson(json, new TypeToken<ArrayList<Order>>(){}.getType());
            if(orders == null) {
                throw new IOException();
            }
        } catch (JsonSyntaxException | IOException exception) {
            LOG.error("File {} does not contain a valid json representing orders", filePath);
            throw new IOException("File " + filePath + " does not contain a valid json representing orders");
        }
        LOG.info("Processing Orders from file {} with Order Ingestion Rate = {}", filePath, ingestionRate);
        List<Order> goodOrders = orders.stream().filter(order -> isGood(order)).collect(Collectors.toList());
        orderService.processOrders(goodOrders, ingestionRate);
    }

    private boolean isGood(Order order) {
        return !(order.getId() == null
                || order.getShelfLife() == null
                || order.getTemp() == null
                || order.getName() == null
                || order.getDecayRate() == null);
    }
}
