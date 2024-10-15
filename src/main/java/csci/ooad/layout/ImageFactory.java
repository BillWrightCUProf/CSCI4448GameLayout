package csci.ooad.layout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class ImageFactory {
    private static final Logger logger = LoggerFactory.getLogger(ImageFactory.class);

    private final Map<String, Image> images = new HashMap<>();
    private int index = 0;

    private static ImageFactory instance;

    public static ImageFactory getInstance() {
        if (instance == null) {
            instance = new ImageFactory();
            try {
                instance.loadImages();
            } catch (URISyntaxException e) {
                logger.warn("Could not load images", e);
                return null;
            }
            logger.debug("ImageFactory instance created");
        }
        return instance;
    }

    private ImageFactory() {
    }

    private void loadImages() throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resourcePath = classLoader.getResource("images/");

        if (resourcePath != null) {
            File imageFolder = new File(resourcePath.toURI());
            File[] imageFiles = imageFolder.listFiles(
                    file -> file.isFile() && file.getName().endsWith(".png")
            );

            for (File imageFile : imageFiles) {
                loadImage(imageFile);
            }
        } else {
            System.err.println("Resource path 'images/' not found.");
        }
    }

    private void loadImage(File imageFile) {
        try {
            images.put(imageFile.getName(), ImageIO.read(imageFile));
        } catch (java.io.IOException e) {
            logger.warn("Could not load image: {}", imageFile.getName(), e);
        }
    }

    public String getBestImageNameMatch(String roomName) {
        for (String imageName : images.keySet()) {
            if (roomName.toLowerCase().contains(imageName.toLowerCase().split("-")[0])) {
                return imageName;
            }
        }
        return null;
    }

    public Image getNextImage(String roomName) {
        String imageName = getBestImageNameMatch(roomName);
        if (imageName == null) {
            List<String> keys = images.keySet().stream().toList();
            index = (index + 1) % images.size();
            imageName = keys.get(index);
        }
        return images.get(imageName);
    }
}
