package csci.ooad.layout.example;

import csci.ooad.layout.example.ExampleSubject;
import org.junit.jupiter.api.Test;

class ExampleSubjectTest {

    @Test
    void testPlayGame() throws InterruptedException {
        // The arguments here aren't used, but show an example of how you could customize the running of an app
        ExampleSubject.main("--shape", "Circle");
    }

}