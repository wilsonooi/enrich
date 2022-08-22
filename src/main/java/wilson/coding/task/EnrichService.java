package wilson.coding.task;

import jakarta.inject.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class EnrichService {

    private static final Map<String, String> products = init();
    private static final String dateFormat = "YYYYMMDD";

    private static Map<String,String> init() {
        Path path = Path.of("src", "main", "resources", "product.csv");

        try {
            return  Files.lines(path)
                    .skip(1)
                    .map(EnrichService::getFields)
                    .collect(Collectors.toMap(key -> key[0], value -> value[1]));
        } catch (IOException  e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String[] getFields(String line) {
        String[] fields = line.split(",");
        if(fields.length != 2)
            throw new RuntimeException("Invalid format in product.csv");
        return fields;
    }


    public String enrichData(String filePath) throws IOException {

        return "";
    }

    private InputStream getFileFromResourceAsStream(String fileName) {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    public boolean isDateValid(String dateStr){
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }


}
