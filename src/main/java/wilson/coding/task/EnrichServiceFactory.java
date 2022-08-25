package wilson.coding.task;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class EnrichServiceFactory {

    @Singleton
    public EnrichService enrichService(){
        return new EnrichService("product.csv");
    }
}
