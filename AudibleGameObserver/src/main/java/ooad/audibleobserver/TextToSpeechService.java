package ooad.audibleobserver;

import java.io.IOException;

public class TextToSpeechService {

    public void speak(String voice, String text) throws IOException, InterruptedException {
        new ProcessBuilder("say", "-v", voice, text).start().waitFor();
    }
}
