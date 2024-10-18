package csci.ooad.layout.intf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ImageFactory {
    private static final Logger logger = LoggerFactory.getLogger(ImageFactory.class);

    private final Map<String, Image> images = new HashMap<>();
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
        List<String> imageFileNames = getAllMatchingFileNamesInResourceDirectory("/images", "png");
        for (String imageFileName : imageFileNames) {
            loadImage(imageFileName);
            logger.debug("image file {} loaded...", imageFileName);
        }
    }

    public List<String> getImageNames() {
        return new ArrayList<>(images.keySet());
    }

    public void loadCustomImage(String imageFilePath) {
        try {
            File imageFile = new File(imageFilePath);
            Image image = ImageIO.read(imageFile);
            String imageName = imageFile.getName().toLowerCase().split("\\.")[0].split("-")[0];
            images.put(imageName, image);
        } catch (IOException e) {
            logger.error("Error loading image file {}", imageFilePath, e);
        }
    }

    private void loadImage(String fileName) {
        String imageName = fileName.split("-")[0].toLowerCase();
        try {
            images.put(
                    imageName,
                    ImageIO.read(getClass().getResourceAsStream("/images/" + fileName)));
        } catch (java.io.IOException e) {
            logger.warn("Could not load image: {}", fileName, e);
        }
    }

    private List<String> getAllMatchingFileNamesInResourceDirectory(String resourcePath, String containsSnippet) {
        try {
            byte[] dirListingBytes = getClass().getResourceAsStream(resourcePath).readAllBytes();
            String dirListing = asciiToString(dirListingBytes);
            List<String> fileNames = List.of(dirListing.split("\n"));
            return fileNames.stream().filter(s -> s.contains(containsSnippet)).toList();
        } catch (java.io.IOException e) {
            logger.warn("Could not load image files: {}", resourcePath, e);
            return Collections.emptyList();
        }
    }

    private static String asciiToString(byte[] asciiArray) {
        StringBuilder sb = new StringBuilder();
        for (int asciiValue : asciiArray) {
            sb.append((char) asciiValue);
        }
        return sb.toString();
    }

    private Optional<String> getBestImageNameMatch(String roomName) {
        String lowerCaseName = roomName.toLowerCase();
        return Optional.ofNullable(images.keySet().stream()
                .filter(imageName -> lowerCaseName.contains(imageName))
                .findFirst()
                .orElse(getNextRandomImageName()));
    }

    public Image getNextImage(String roomName) {
        Optional<String> imageName = getBestImageNameMatch(roomName);
        return images.get(imageName.get());
    }

    private String getNextRandomImageName() {
        List<String> keys = images.keySet().stream().toList();
        index = (index + 1) % images.size();
         return keys.get(index);
    }
}
