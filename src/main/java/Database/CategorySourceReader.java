package Database;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class CategorySourceReader {

    private CategorySource categorySource;

    public CategorySourceReader()  {

        String categoryURL;
        categoryURL = "https://opentdb.com/api_category.php";

        try {

            URL url = new URI(categoryURL).toURL();
            Path p = Paths.get("src/main/java/Database/CategorySource.json");

            try (
                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream())); //se to-do
                    BufferedWriter bw = Files.newBufferedWriter(p)
            ) {
                String temp;
                temp = br.readLine();
                bw.write(temp);

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                this.categorySource = mapper.readValue(p.toFile(), CategorySource.class);
                System.out.println(this.categorySource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public CategorySource getCategorySource() {
        return this.categorySource;
    }

    public static void main(String[] args) {
        new CategorySourceReader();
    }
}