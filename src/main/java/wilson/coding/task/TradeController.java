package wilson.coding.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Controller("/")
public class TradeController{

    @Inject
    private EnrichService enrichService;

    @Post(value="/enrich", consumes = {MediaType.TEXT_CSV}, produces = {MediaType.TEXT_CSV})
    public Flowable<String> enrichData(@Body String fileContent ) throws IOException {

        Stream<String> lines = fileContent.lines();

        return Flowable.fromStream(lines).skip(1)
                .filter(line -> enrichService.isValidLine(line))
                .filter(line -> enrichService.isValidDate(line.split(",")[0]))
                .map(enrichService::enrichData)
                .startWithItem(EnrichService.HEADER);
    }

    /*@Post(value="/enrich", consumes = {MediaType.TEXT_CSV}, produces = {MediaType.TEX})
    @ExecuteOn(TaskExecutors.IO)
    public String enrichData(@Body String fileContent ) throws IOException {
        Flowable
        return enrichService.enrichData(fileContent);
    }*/
}
