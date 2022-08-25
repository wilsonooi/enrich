package wilson.coding.task;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class EnrichServiceTest {

    @Test
    void invalidProductFileShouldReturnRuntimeException(){
        try {
            EnrichService enrichService =
                    new EnrichService(Path.of("src", "main", "resources", "invalid_product.csv"));
            assertTrue(false);
        } catch (RuntimeException e){
            assertTrue(e.getMessage().contains("Invalid format in product.csv, line: 3,REPO Domestic,new field"));
        }
    }

    @Test
    void validProductFileShouldPopulateProductMap(){
            EnrichService enrichService =
                    new EnrichService(Path.of("src", "main", "resources", "product.csv"));
            assertTrue(enrichService.products.size() != 0);
    }

    @Test
    void validDateFormatShouldReturnTrue(){
        EnrichService enrichService = new EnrichService(
                Path.of("src", "main", "resources", "product.csv"));
        assertTrue(enrichService.isValidDate("20220823"));
    }

    @Test
    void invalidDateFormatShouldReturnFalse(){
        EnrichService enrichService = new EnrichService(
                Path.of("src", "main", "resources", "product.csv"));
        assertFalse(enrichService.isValidDate("20222308"));
    }

    @Test
    void invalidProductIdShouldBeLogged() throws IOException {
        Logger logger = (Logger) LoggerFactory.getLogger(EnrichService.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        EnrichService enrichService = new EnrichService(
                Path.of("src", "main", "resources", "product.csv"));

        String enrichedData = enrichService.enrichData("20160101,11,EUR,35.34");
        // JUnit assertions
        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals("Product name not found for product_id: 11", logsList.get(0)
                .getMessage());
        assertEquals(Level.WARN, logsList.get(0)
                .getLevel());

    }



}