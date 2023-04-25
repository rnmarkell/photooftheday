package business.apis.twilio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class TwilioApiProxy {

    private static final Logger LOG = LoggerFactory.getLogger(TwilioApiProxy.class);

    private static final String ACCOUNT_SID = "<fillIn>";
    private static final String AUTH_TOKEN = "<fillIn>";

    private static final String SENDER_PHONE_NUMBER = "<fillIn>";

    private static final String RECEIVER_PHONE_NUMBER = "<fillIn>";

    public void sendMessage(String messageBody) {
      Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
      try {
        Message.creator(
                new com.twilio.type.PhoneNumber(RECEIVER_PHONE_NUMBER),
                new com.twilio.type.PhoneNumber(SENDER_PHONE_NUMBER),
                messageBody)
            .create();
      } catch (com.twilio.exception.ApiException apiException) {
        LOG.error("Exception sending message via Twilio api", apiException);
      } catch (Exception e) {
        LOG.error("Unexpected exception sending message via Twilio api", e);
      }
    }

}
