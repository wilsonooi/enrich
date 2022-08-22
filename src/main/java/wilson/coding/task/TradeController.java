package wilson.coding.task;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller("/")
public class TradeController{

    @Inject
    private EnrichService enrichService;

    @Post(value="/enrich", consumes = {MediaType.TEXT_CSV}, produces = {MediaType.TEXT_CSV})
    public String enrichData(CompletedFileUpload file ) throws IOException {

        //File tempFile = File.createTempFile(file.getFilename(), "temp");
        //Path path = Paths.get(tempFile.getAbsolutePath());
        //Files.write(path, file.getBytes());

        enrichService.enrichData("abcd");
        return "";
    }
}
