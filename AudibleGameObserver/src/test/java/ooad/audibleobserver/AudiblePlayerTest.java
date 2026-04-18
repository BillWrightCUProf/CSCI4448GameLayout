package ooad.audibleobserver;

import org.junit.jupiter.api.Disabled;


class AudiblePlayerTest {

    @Disabled
    void testNativeSoundFirst() {
        AudiblePlayer audiblePlayer = new AudiblePlayer();
        audiblePlayer.say("Hello");
    }

    @Disabled
    void testForcingOfLibrarySound() {
        AudiblePlayer audiblePlayer = new AudiblePlayer(true);
        audiblePlayer.say("Hello");
    }

    @Disabled
    void testWaitingForSoundToEnd() {
        AudiblePlayer audiblePlayer = new AudiblePlayer();
        audiblePlayer.say("Hello");
        audiblePlayer.say("Bye");
    }

}