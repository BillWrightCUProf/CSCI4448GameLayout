package ooad.audibleobserver;

import org.junit.jupiter.api.Disabled;

import java.io.IOException;

class TextToSpeechServiceTest {

    @Disabled
    void testSayingMessage() throws IOException, InterruptedException {
        new TextToSpeechService().speak("Tara", "A troll appears!");
    }

}