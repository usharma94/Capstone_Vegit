package sheridan.sharmupm.vegit_capstone.ui.assistant;

import com.google.cloud.dialogflow.v2.DetectIntentResponse;

public interface BotReply {

    void callback(DetectIntentResponse returnResponse);
}
