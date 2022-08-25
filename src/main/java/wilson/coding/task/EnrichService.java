package wilson.coding.task;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Null;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnrichService {

    private static final Logger logger = LoggerFactory.getLogger(EnrichService.class);
    Map<String, String> products;
    private static final String DATE_FORMAT = "yyyyMMdd";
    public static final String HEADER = "date,product_name,currency,price";

    public EnrichService(Path path) {

        try {
            products =  Files.lines(path)
                    .skip(1)
                    .map(EnrichService::getProductKeyValuePair)
                    .collect(Collectors.toMap(key -> key[0], value -> value[1]));
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    private static String[] getProductKeyValuePair(String line) {
        String[] fields = line.split(",");
        if(fields.length != 2)
            throw new RuntimeException("Invalid format in product.csv, line: " + line);
        return fields;
    }


    public String enrichData(String line) throws IOException {

        String[] fields = line.split(",");
        //if(fields.length != 4)
          //  throw new RuntimeException("Invalid line in trade.csv, line: " + line);
        if(!products.containsKey(fields[1]))
            logger.warn("Product name not found for product_id: " + fields[1]);

        return String.format("%n%s,%s,%s,%s", fields[0]
                , products.getOrDefault(fields[1], "Missing Product Name"), fields[2], fields[3]);

        //return sb.append(fileContent.lines()
          //      .skip(1).map(EnrichService::processLine)
            //    .collect(Collectors.joining())).toString();

    }

    public boolean isValidDate(String dateStr){
        DateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            logger.error(String.format("Date [%s] is not a valid yyyyMMdd format", dateStr));
            return false;
        }
        return true;
    }

    public boolean isValidLine(String line){
         return line.split(",").length == 4;
    }


}
