package csci.ooad.layout;

import java.util.List;

import csci.ooad.layout.intf.ImageFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ImageFactoryTest {

    @Test
    void testAutomaticLoad() {
        ImageFactory imageFactory = ImageFactory.getInstance();
        assertNotNull(imageFactory.getNextImage("Rivendell"));
    }

    @Test
    void testGetImageNames() {
        ImageFactory imageFactory = ImageFactory.getInstance();
        List<String> imageFileNames = imageFactory.getImageNames();
        System.out.println(imageFileNames);
        assertTrue(imageFileNames.size() > 5);
    }

    @Test
    void testLoadingOfRoomImagesFromJarFile() {
        ImageFactory imageFactory = ImageFactory.getInstance();
        String homeDir = System.getProperty("user.home");
        String jarFilePath = homeDir + "/Documents/cu-courses/csci4448/Projects/CSCI4448GameLayout/build/libs/csci4448.gameLayout-1.11.1.jar";
        List<String> imageNames = imageFactory.getAllMatchingFileNamesFromJarFile(jarFilePath, "images", "png");
        assertTrue(imageNames.size() > 5);
    }

    @Disabled
    void testAddingCustomImages() {
        ImageFactory imageFactory = ImageFactory.getInstance();
        imageFactory.loadCustomImage("/tmp/test-circle.png");
        List<String> imageFileNames = imageFactory.getImageNames();
        System.out.println(imageFileNames);

        assertTrue(imageFileNames.contains("test"));
    }
}