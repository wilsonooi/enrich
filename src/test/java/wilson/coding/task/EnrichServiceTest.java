package wilson.coding.task;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnrichServiceTest {

    @Test
    void invalidDataShouldNotLoadToProductsAndShouldBeLogged(){
        Logger logger = (Logger) LoggerFactory.getLogger(EnrichService.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        //10 lines with 1 invalid line in invalid_product.csv
        EnrichService enrichService = new EnrichService("invalid_product.csv");

        //Should only load remaining 9 valid data to products
        assertEquals(9,enrichService.products.size());
        List<ILoggingEvent> logsList = listAppender.list;

        assertEquals("Invalid format in product.csv, line: 3,REPO Domestic,invalid field", logsList.get(0)
                .getMessage());
        assertEquals(Level.WARN, logsList.get(0).getLevel());

    }

    @Test
    void invalidProductLineShouldReturnNull(){
        assertNull(EnrichService.getProductKeyValuePair("3,REPO Domestic,invalid field"));
    }

    @Test
    void validProductFileShouldPopulateProductMap(){
            EnrichService enrichService =
                    new EnrichService("product.csv");
            assertTrue(enrichService.products.size() > 0);
    }

    @Test
    void validDateFormatShouldReturnTrue(){
        EnrichService enrichService = new EnrichService("product.csv");
        assertTrue(enrichService.isValidDate("20220823"));
    }

    @Test
    void invalidDateFormatShouldReturnFalseAndErrorLogged(){
        Logger logger = (Logger) LoggerFactory.getLogger(EnrichService.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        EnrichService enrichService = new EnrichService("product.csv");
        assertFalse(enrichService.isValidDate("20222308"));

        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals("Date [20222308] is not a valid yyyyMMdd format", logsList.get(0)
                .getMessage());
        assertEquals(Level.ERROR, logsList.get(0)
                .getLevel());
    }

    @Test
    void invalidProductIdShouldBeLogged(){
        Logger logger = (Logger) LoggerFactory.getLogger(EnrichService.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        EnrichService enrichService = new EnrichService("product.csv");

        String enrichedData = enrichService.enrichData("20160101,11,EUR,35.34");
        // JUnit assertions
        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals("Product name not found for product_id: 11", logsList.get(0)
                .getMessage());
        assertEquals(Level.WARN, logsList.get(0)
                .getLevel());

    }

    @Test
    void validDataShouldBeEnriched(){
        EnrichService enrichService = new EnrichService("product.csv");

        assertEquals(String.format("%n20160101,REPO Domestic,EUR,30.34"),
                enrichService.enrichData("20160101,3,EUR,30.34"));
    }



}