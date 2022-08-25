package wilson.coding.task;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.stream.Stream;

@Controller("/")
public class TradeController{

    @Inject
    private EnrichService enrichService;

    @Post(value="enrich", consumes = {MediaType.TEXT_CSV}, produces = {MediaType.TEXT_CSV})
    public Flowable<String> enrichTradeData(@Body String fileContent ) {

        Stream<String> lines = fileContent.lines();

        return Flowable.fromStream(lines).skip(1) //omit the header
                .filter(line -> enrichService.isValidLine(line))
                //skip invalid date format line
                .filter(line -> enrichService.isValidDate(line.split(",")[0]))
                .map(enrichService::enrichData)
                .startWithItem(EnrichService.HEADER);
    }

}
