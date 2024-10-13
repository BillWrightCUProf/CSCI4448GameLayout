package csci.ooad.layout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class ImageFactory {
    private static final Logger logger = LoggerFactory.getLogger(ImageFactory.class);

    private List<Image> images = new ArrayList<>();
    private int index = 0;

    private static ImageFactory instance;

    public static ImageFactory getInstance() {
        if (instance == null) {
            instance = new ImageFactory();
            instance.loadImages();
            logger.debug("ImageFactory instance created");
        }
        return instance;
    }

    private ImageFactory() {
    }

    private void loadImages() {
        loadImage("cave-crystal-circle.png");
        loadImage("cave-stalactite-circle.png");
        loadImage("cave-fountain-circle.png");
        loadImage("cave-lava-circle.png");
        loadImage("cave-swamp-circle.png");
//        loadImage("cave-crystal.jpg");
//        loadImage("cave-stalactite.jpg");
//        loadImage("cave-fountain.jpg");
//        loadImage("cave-lava.jpg");
//        loadImage("cave-swamp.jpg");
        logger.debug("images loaded...");
    }

    private void loadImage(String fileName) {
        try {
            images.add(ImageIO.read(getClass().getResourceAsStream("/images/" + fileName)));
        } catch (java.io.IOException e) {
            logger.warn("Could not load image: " + fileName, e);
        }
    }

    public Image getNextImage() {
        index = (index + 1) % images.size();
        return images.get(index);
    }
}
