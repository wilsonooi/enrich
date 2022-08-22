package wilson.coding.task;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class EnrichServiceTest {

    @Test
    void invalidDataFileShouldReturnRuntimeException(){
        assertThatThrownBy(EnrichService::new)
                .isInstanceOf(ExceptionInInitializerError.class)
                .hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void validDateFormat(){
        var enrichService = new EnrichService();
        //assertTrue(EnrichService.isDateValid("20221202"));
    }





}