package business.apis.nasa;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Wrapper for NASA's Astronomy Picture Of the Day API
 */
public class NASAApiProxy {

  private static final Logger LOG = LoggerFactory.getLogger(NASAApiProxy.class);

  private static final String API_PATH = "https://api.nasa.gov/planetary/apod";

  private static final String API_KEY_QUERY_PARAM = "?api_key=<fillIn>";

  private static final HttpClient client = HttpClient.newHttpClient();

  public Optional<AstronomyPicture> getAstronomyPictureOfTheDay() {
      return getAstronomyPictureOfTheDay(API_PATH + API_KEY_QUERY_PARAM)
          .flatMap(this::mapToPicture);
   }

   private Optional<AstronomyPicture> mapToPicture(String json) {
      try {
        AstronomyPicture picture = getObjectMapper().readValue(json, AstronomyPicture.class);
        return Optional.ofNullable(picture);
      } catch (IOException e) {
        LOG.error("Error mapping NASA Api Response {} to Astronomy Picture", json, e);
        return Optional.empty();
      }
   }

   private Optional<String> getAstronomyPictureOfTheDay(String uri) {
     HttpRequest request = HttpRequest.newBuilder()
         .uri(URI.create(API_PATH + API_KEY_QUERY_PARAM))
         .header("Content-Type", "application/json")
         .build();

     try {
       HttpResponse<String> response =
           client.send(request, HttpResponse.BodyHandlers.ofString());
       return Optional.ofNullable(response).map(HttpResponse::body);
     } catch (Exception e) {
       LOG.error("Error retrieving astronomy picture of the day", e);
       return Optional.empty();
     }
   }

   private static ObjectMapper getObjectMapper() {
     return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
   }

}
