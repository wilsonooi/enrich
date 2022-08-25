package wilson.coding.task;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

import java.nio.file.Path;

@Factory
public class ConfigurationFactory {

    @Singleton
    public EnrichService enrichService(){
        return new EnrichService(Path.of("src", "main", "resources", "product.csv"));
    }
}
