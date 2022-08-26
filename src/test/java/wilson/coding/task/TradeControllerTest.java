package wilson.coding.task;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.rxjava3.http.client.Rx3HttpClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;


import java.util.Optional;

import static io.micronaut.http.HttpRequest.POST;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class TradeControllerTest {

    @Inject
    @Client("/")
    Rx3HttpClient rx3HttpClient;

    @Test
    void enrichTradeDataShouldReturnCorrectData(){
        Flowable<HttpResponse<String>> call = rx3HttpClient.exchange(
                POST("/enrich", """
                        date,product_id,currency,price
                        20220820,1,EUR,10.0
                        20220821,2,EUR,20.0""")
                        .contentType(MediaType.TEXT_CSV)
                        .accept(MediaType.TEXT_CSV),
                String.class
        );

        HttpResponse<String> response = call.blockingFirst();
        Optional<String> message = response.getBody(String.class);
        // Check the status
        assertEquals(HttpStatus.OK, response.getStatus());
        // Check the response body
        assertTrue(message.isPresent());
        assertEquals(String.format("date,product_name,currency,price" +
                        "%n20220820,Treasury Bills Domestic,EUR,10.0" +
                        "%n20220821,Corporate Bonds Domestic,EUR,20.0"),
                message.get()
        );
        rx3HttpClient.stop();
    }

    @Test
    void invalidTradeDataShouldBeOmitInResponse(){
        Flowable<HttpResponse<String>> call = rx3HttpClient.exchange(
                POST("/enrich", """
                        date,product_id,currency,price
                        20220820,1,EUR,10.0,abc
                        20220821,2,EUR,20.0,123,456""")
                        .contentType(MediaType.TEXT_CSV)
                        .accept(MediaType.TEXT_CSV),
                String.class
        );

        HttpResponse<String> response = call.blockingFirst();
        Optional<String> message = response.getBody(String.class);
        // Check the status
        assertEquals(HttpStatus.OK, response.getStatus());
        // Check the response body
        assertTrue(message.isPresent());
        assertEquals("date,product_name,currency,price", message.get());
        rx3HttpClient.stop();
    }

    @Test
    void invalidDateInTradeDataShouldBeOmitInResponse(){
        Flowable<HttpResponse<String>> call = rx3HttpClient.exchange(
                POST("/enrich", "date,product_id,currency,price\n" +
                        "20222020,1,EUR,10.0")
                        .contentType(MediaType.TEXT_CSV)
                        .accept(MediaType.TEXT_CSV),
                String.class
        );

        HttpResponse<String> response = call.blockingFirst();
        Optional<String> message = response.getBody(String.class);
        // Check the status
        assertEquals(HttpStatus.OK, response.getStatus());
        // Check the response body
        assertTrue(message.isPresent());
        assertEquals("date,product_name,currency,price", message.get());
        rx3HttpClient.stop();
    }
}