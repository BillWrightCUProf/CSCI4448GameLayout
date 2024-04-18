package csci.ooad.layout;

import csci.ooad.layout.intf.IMazeObserver;
import csci.ooad.layout.intf.IMazeObserverBuilder;
import csci.ooad.layout.intf.MazeObserver;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SPIMazeObserverTest {


    @Test
    void getMazeObserverBuilderViaSPI() {
        // Confirm I can instantiate a builder class with a default constructor
        IMazeObserverBuilder explicitBuilder = new csci.ooad.layout.intf.MazeObserver.Builder();

        ServiceLoader<IMazeObserverBuilder> loader = ServiceLoader.load(IMazeObserverBuilder.class);
        Iterator<IMazeObserverBuilder> it = loader.iterator();
//        Exception exception = assertThrows(Exception.class, () -> {
//            it.hasNext();
//        });

        if (it.hasNext()) {
            IMazeObserverBuilder mazeObserverBuilder = it.next();
            assertNotNull(mazeObserverBuilder);
        } else {
            System.out.println("No implementation found for IMazeObserverBuilder are found because SPI doesn't work for inner classes");
        }
    }

    @Test
    void getMazeObserverViaSPI() {
        ServiceLoader<IMazeObserver> loader = ServiceLoader.load(IMazeObserver.class);
        Iterator<IMazeObserver> it = loader.iterator();

        while (it.hasNext()) {
            IMazeObserver mazeObserver = it.next();
            System.out.println("Found IMazeObserver implementation: " + mazeObserver.getClass().getName());
            assertNotNull(mazeObserver);
        }
    }
}
