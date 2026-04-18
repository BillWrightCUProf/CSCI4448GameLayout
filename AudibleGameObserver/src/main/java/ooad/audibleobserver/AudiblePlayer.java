package ooad.audibleobserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import java.io.IOException;
import java.util.Locale;

public class AudiblePlayer {
    private static final Logger logger = LoggerFactory.getLogger(AudiblePlayer.class);
    private static Synthesizer synthesizer;
    private boolean forceLibraryUse = false;

    // reference: https://www.geeksforgeeks.org/converting-text-speech-java/
    static {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        try {
            Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
            // Create a Synthesizer
            synthesizer = Central.createSynthesizer(
                    new SynthesizerModeDesc(Locale.US)
            );
            // Allocate synthesizer
            synthesizer.allocate();
        } catch (EngineException e) {
            logger.error("Could not initialize the synthesizer: {}", e.getMessage());
            logger.warn("Falling back to using the say command, if on a Mac, otherwise I'll be silent");
        }
    }

    public AudiblePlayer() {
    }

    public AudiblePlayer(Boolean forceLibraryUse) {
        this.forceLibraryUse = forceLibraryUse;
    }

    public void say(String message) {
        try {
            if (!forceLibraryUse && System.getProperty("os.name").contains("Mac")) {
                String[] cmd = {"say", message};
                Process sayProcess = Runtime.getRuntime().exec(cmd);
                sayProcess.waitFor();
            } else {
                synthesizer.speakPlainText(message, null);
                synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Could not speak message: {}", message);
        }
    }

}