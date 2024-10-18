package csci.ooad.layout.intf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ImageFactory {
    private static final Logger logger = LoggerFactory.getLogger(ImageFactory.class);

    private final Map<String, Image> images = new HashMap<>();
    private int index = 0;

    private static ImageFactory instance;

    public static ImageFactory getInstance() {
        if (instance == null) {
            instance = new ImageFactory();
            instance.loadImages();
        }
        return instance;
    }

    private ImageFactory() {
    }

    private void loadImages() {
        // First load a default hard-coded image so that we never have zero images
        loadImage("rivendell-circle.png");

        List<String> imageFileNames = getAllMatchingFileNamesInResourceDirectory("images", "png");
        if (imageFileNames.isEmpty()) {
            logger.warn("Automatic loading of images failed!");
            loadDefaultImages();
        } else {
            for (String imageFileName : imageFileNames) {
                loadImage(imageFileName);
                logger.info("image file {} loaded...", imageFileName);
            }
        }
    }

    private void loadDefaultImages() {
        // This is only run if the automatic loading fails
        logger.info("loading default images...");
        loadImage("bagend-circle.png");
        loadImage("crystal-circle.png");
        loadImage("fangorn-forest-circle.png");
        loadImage("fountain-circle.png");
        loadImage("lava-circle.png");
        loadImage("rivendell-circle.png");
        loadImage("stalactite-circle.png");
        loadImage("swamp-circle.png");
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
        } catch (java.io.IOException | IllegalArgumentException e) {
            logger.warn("Could not load image: {}", fileName, e);
        }
    }

    private List<String> getAllMatchingImageNamesInResourceDirectory(String directory, String extension) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resourcePath = classLoader.getResource(directory);
        List<String> imageFileNames = new ArrayList<>();
        try {
            if (resourcePath != null) {
                File imageFolder = new File(resourcePath.toURI());
                File[] imageFiles = imageFolder.listFiles(
                        file -> file.isFile() && file.getName().endsWith(extension)
                );

                if (imageFiles != null) {
                    for (File imageFile : imageFiles) {
                        imageFileNames.add(imageFile.getName());
                    }
                }
            } else {
                logger.warn("Resource path {} not found.", directory);
            }
        } catch (URISyntaxException e) {
            logger.warn("Error loading image files", e);
        }
        return imageFileNames;
    }

    private List<String> getAllMatchingFileNamesInResourceDirectory(String pathInResource, String containsSnippet) {
        String resourcePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if (resourcePath.endsWith(".jar")) {
            // If this code is executing inside a jar file, we have to extract the file names in
            // a completely different manner... This sucks that it is not consistent.
            return getAllMatchingFileNamesFromJarFile(resourcePath, pathInResource, containsSnippet);
        }
        return getAllMatchingImageNamesInResourceDirectory(pathInResource, containsSnippet);
    }

    private List<String> getAllMatchingFileNamesFromJarFile(String pathToJarFile, String pathInResource, String containsSnippet) {
        try {
            Enumeration<JarEntry> entries;
            try (JarFile jarFile = new JarFile(pathToJarFile)) {
                entries = jarFile.entries();
            }
            List<String> imageNames = new ArrayList<>();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().startsWith(pathInResource) && entry.getName().contains(containsSnippet)) {
                    String fileName = entry.getName().substring(pathInResource.length() + 1); // Extract file name
                    imageNames.add(fileName);
                }
            }
            return imageNames;
        } catch (IOException e) {
            logger.warn("Could not load image files from within jar file: {}", pathToJarFile, e);
            return Collections.emptyList();
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
            imageName = getNextRandomImageName();
        }
        return images.get(imageName);
    }

    private String getNextRandomImageName() {
        List<String> keys = images.keySet().stream().toList();
        index = (index + 1) % images.size();
        return keys.get(index);
    }
}
