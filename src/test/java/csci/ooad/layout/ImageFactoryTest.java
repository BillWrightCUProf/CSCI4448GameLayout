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

    @Disabled
    void testAddingCustomImages() {
        ImageFactory imageFactory = ImageFactory.getInstance();
        imageFactory.loadCustomImage("/tmp/test-circle.png");
        List<String> imageFileNames = imageFactory.getImageNames();
        System.out.println(imageFileNames);

        assertTrue(imageFileNames.contains("test"));
    }
}