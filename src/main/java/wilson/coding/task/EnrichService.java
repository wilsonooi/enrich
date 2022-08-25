package wilson.coding.task;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.stream.Collectors;

public class EnrichService {

    private static final Logger logger = LoggerFactory.getLogger(EnrichService.class);
    Map<String, String> products;
    private static final String DATE_FORMAT = "yyyyMMdd";
    public static final String HEADER = "date,product_name,currency,price";


    public EnrichService(String filename) {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        products =  reader.lines()
                .skip(1) //Omit header line
                .map(EnrichService::getProductKeyValuePair)
                .filter(line -> line != null)
                .collect(Collectors.toMap(key -> key[0], value -> value[1]));
    }

    public static String[] getProductKeyValuePair(String line) {
        String[] fields = line.split(",");
        //Check product.csv contains only 2 fields. E.g. product_id,product_name
        if(fields.length != 2) {
            logger.warn("Invalid format in product.csv, line: " + line);
            return null;
        }
        return fields;
    }


    public String enrichData(String line){
        String[] fields = line.split(",");

        if(!products.containsKey(fields[1]))
            logger.warn("Product name not found for product_id: " + fields[1]);

        return String.format("%n%s,%s,%s,%s", fields[0]
                , products.getOrDefault(fields[1], "Missing Product Name"), fields[2], fields[3]);
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
