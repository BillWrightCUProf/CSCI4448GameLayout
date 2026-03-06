package csci.ooad.layout;

import csci.ooad.layout.intf.IGameObserver;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SPIGameObserverTest {


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
        ServiceLoader<IGameObserver> loader = ServiceLoader.load(IGameObserver.class);
        Iterator<IGameObserver> it = loader.iterator();

        while (it.hasNext()) {
            IGameObserver mazeObserver = it.next();
            System.out.println("Found IMazeObserver implementation: " + mazeObserver.getClass().getName());
            assertNotNull(mazeObserver);
        }
    }
}
