package ooad.audibleobserver;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class AudibleObserverTest {

    @Test
    void mandatoryTest() {
        AudibleObserver audibleObserver = new AudibleObserver();
        assert audibleObserver != null;
    }

    @Disabled
    void testUpdate() {
        AudibleObserver audibleObserver = new AudibleObserver();
        audibleObserver.update("Game is over");
    }

}