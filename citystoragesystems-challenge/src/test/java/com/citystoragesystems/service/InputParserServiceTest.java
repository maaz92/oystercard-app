package com.citystoragesystems.service;

import com.citystoragesystems.entity.Order;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import java.io.IOException;
import java.util.List;

public class InputParserServiceTest {

    @Test(expected = IllegalArgumentException.class)
    public void testParseWrongNumberOfArguments() throws IOException {
        OrderService orderService = Mockito.mock(OrderService.class);
        InputParserService inputParserService = new InputParserService(orderService);
        Assert.assertNotNull(inputParserService);
        String[] args = {"2.0"};
        inputParserService.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIngestionRateNotDouble() throws IOException {
        OrderService orderService = Mockito.mock(OrderService.class);
        InputParserService inputParserService = new InputParserService(orderService);
        Assert.assertNotNull(inputParserService);
        String[] args = {"x", "path"};
        inputParserService.parse(args);
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFilePath() throws IOException {
        OrderService orderService = Mockito.mock(OrderService.class);
        InputParserService inputParserService = new InputParserService(orderService);
        Assert.assertNotNull(inputParserService);
        String[] args = {"2.0", "path"};
        inputParserService.parse(args);
    }

    @Test(expected = IOException.class)
    public void testParseEmptyJSONFile() throws IOException {
        OrderService orderService = Mockito.mock(OrderService.class);
        InputParserService inputParserService = new InputParserService(orderService);
        orderService = Mockito.spy(orderService);
        Assert.assertNotNull(inputParserService);
        String[] args = {"2.0", "src/test/resources/empty.json"};
        inputParserService.parse(args);
    }

    @Test
    public void testParseInvalidFormatJSONFile() throws IOException {
        OrderService orderService = Mockito.mock(OrderService.class);
        InputParserService inputParserService = new InputParserService(orderService);
        Assert.assertNotNull(inputParserService);
        String[] args = {"2.0", "src/test/resources/invalid_format.json"};
        inputParserService.parse(args);
        Mockito.verify(orderService, Mockito.times(1)).processOrders(Mockito.argThat(new ArgumentMatcher<List<Order>>() {
            @Override
            public boolean matches(List<Order> orders) {
                return orders.size() == 0;
            }
        }), Mockito.eq(2.0));
    }

    @Test(expected = IOException.class)
    public void testParseInvalidJSONFile() throws IOException {
        OrderService orderService = Mockito.mock(OrderService.class);
        InputParserService inputParserService = new InputParserService(orderService);
        Assert.assertNotNull(inputParserService);
        String[] args = {"2.0", "src/test/resources/invalid_json.json"};
        inputParserService.parse(args);
    }

    @Test
    public void testParseValidFormatFileJSON() throws IOException {
        OrderService orderService = Mockito.mock(OrderService.class);
        InputParserService inputParserService = new InputParserService(orderService);
        Assert.assertNotNull(inputParserService);
        String[] args = {"2.0", "src/test/resources/orders.json"};
        inputParserService.parse(args);
        Mockito.verify(orderService, Mockito.times(1)).processOrders(Mockito.argThat(new ArgumentMatcher<List<Order>>() {
            @Override
            public boolean matches(List<Order> orders) {
                return orders.size() == 132;
            }
        }), Mockito.eq(2.0));
    }
}
