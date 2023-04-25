package business;

import java.util.StringJoiner;

import business.apis.nasa.AstronomyPicture;
import business.apis.nasa.NASAApiProxy;
import business.apis.twilio.TwilioApiProxy;

/**
 * Sends the NASA astronomy picture of the day to the configured phone number.
 */
public class PictureSender {

  private TwilioApiProxy twilioApiProxy;

  private NASAApiProxy nasaApiProxy;

  public PictureSender() {
    this.twilioApiProxy = new TwilioApiProxy();
    this.nasaApiProxy = new NASAApiProxy();
  }

  public void sendPicture() {
    nasaApiProxy.getAstronomyPictureOfTheDay()
        .map(this::buildMessageBody)
        .ifPresent(message -> twilioApiProxy.sendMessage(message));
  }

  public String buildMessageBody(AstronomyPicture astronomyPicture) {
    StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());
    stringJoiner.add("Hi! Here's the astronomy picture of the day: ");
    stringJoiner.add(astronomyPicture.getTitle());
    stringJoiner.add(astronomyPicture.getUrl());
    stringJoiner.add(astronomyPicture.getExplanation());
    stringJoiner.add(astronomyPicture.getCopyright());
    return stringJoiner.toString();
  }

}
