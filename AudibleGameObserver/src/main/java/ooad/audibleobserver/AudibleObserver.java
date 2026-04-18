package ooad.audibleobserver;

import ooad.gameobserver.intf.IGameObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import java.io.IOException;
import java.util.Locale;

public class AudibleObserver implements IGameObserver {
    private AudiblePlayer audiblePlayer;
    private static Synthesizer synthesizer;

    public AudibleObserver(AudiblePlayer audiblePlayer) {
        this.audiblePlayer = audiblePlayer;
    }

    @Override
    public void update(String eventDescription) {
        speak(eventDescription);
    }

    private void speak(String message) {
        audiblePlayer.say(message);
    }

}
