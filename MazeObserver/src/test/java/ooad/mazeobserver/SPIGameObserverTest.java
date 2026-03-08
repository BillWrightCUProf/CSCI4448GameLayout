package ooad.mazeobserver;

import ooad.gameobserver.intf.*;
import ooad.mazeobserver.example.ExampleSubject;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SPIGameObserverTest {

    @Test
    void getMazeObserverViaSPI() {
        IMutableServiceContext serviceContext = new SimpleServiceContext();
        serviceContext.register(IMazeSubject.class, new ExampleSubject());

        ServiceLoader<IGameObserverProvider> loader = ServiceLoader.load(IGameObserverProvider.class);
        Iterator<IGameObserverProvider> iterator = loader.iterator();

        while (iterator.hasNext()) {
            IGameObserverProvider gameObserverProvider = iterator.next();
            System.out.println("Found provider: " + gameObserverProvider.getClass().getName());
            assertNotNull(gameObserverProvider);

            IGameObserver gameObserver = gameObserverProvider.create(serviceContext);
            System.out.println("\tFound observer: " + gameObserver.getClass().getName());
            assertNotNull(gameObserver);
        }
    }

    @Test
    void findsAllFourGameObserverProviders() {
        IMutableServiceContext serviceContext = new SimpleServiceContext();
        serviceContext.register(IMazeSubject.class, new ExampleSubject());

        ServiceLoader<IGameObserverProvider> loader = ServiceLoader.load(IGameObserverProvider.class);

        List<IGameObserverProvider> providers = loader.stream()
                .map(ServiceLoader.Provider::get)
                .toList();

        Set<String> providerClassNames = providers.stream()
                .map(provider -> provider.getClass().getName())
                .collect(Collectors.toSet());

        System.out.println("Providers found:");
        providerClassNames.forEach(System.out::println);

        Set<String> expected = Set.of(
                "ooad.mazeobserver.GameObserverProvider",
                "ooad.alternatemazeobserver.AlternateMazeObserverProvider",
                "ooad.gameobserver.GameObserverProvider",
                "ooad.audibleobserver.AudibleObserverProvider"
        );

        assertEquals(expected, providerClassNames);

        for (IGameObserverProvider provider : providers) {
            IGameObserver observer = provider.create(serviceContext);
            assertNotNull(observer, "Provider returned null: " + provider.getClass().getName());
        }
    }
}
