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

    private final Map<String, Image> roomImages = new HashMap<>();
    private final Map<String, Image> characterImages = new HashMap<>();
    private int roomNameIndex = 0;
    private int characterNameIndex = 0;

    private static ImageFactory instance;

    public static ImageFactory getInstance() {
        if (instance == null) {
            instance = new ImageFactory();
            instance.loadRoomImages();
            instance.loadCharacterImages();
        }
        return instance;
    }

    private ImageFactory() {
    }

    private void loadCharacterImages() {
        // First load a default hard-coded image so that we never have zero images
        loadImage("arwen.png", characterImages, "characterImages");
    }

    private void loadRoomImages() {
        // First load a default hard-coded image so that we never have zero images
        loadImage("rivendell-circle.png", roomImages, "roomImages");
        loadImages(roomImages, "roomImages");
    }

    private void loadImages(Map<String, Image> images, String dirName) {
        List<String> imageFileNames = getAllMatchingFileNamesInResourceDirectory(dirName, "png");
        if (imageFileNames.isEmpty()) {
            logger.warn("Automatic loading of images failed!");
            loadDefaultRoomImages();
        } else {
            for (String imageFileName : imageFileNames) {
                loadImage(imageFileName, images, dirName);
                logger.info("image file {} loaded...", imageFileName);
            }
        }
    }

    private void loadDefaultRoomImages() {
        // This is only run if the automatic loading fails
        logger.info("loading default room images...");
        loadImage("bagend-circle.png", roomImages, "roomImages");
        loadImage("crystal-circle.png", roomImages, "roomImages");
        loadImage("fangorn-forest-circle.png", roomImages, "roomImages");
        loadImage("fountain-circle.png", roomImages, "roomImages");
        loadImage("lava-circle.png", roomImages, "roomImages");
        loadImage("rivendell-circle.png", roomImages, "roomImages");
        loadImage("stalactite-circle.png", roomImages, "roomImages");
        loadImage("swamp-circle.png", roomImages, "roomImages");
    }

    public List<String> getRoomImageNames() {
        return new ArrayList<>(roomImages.keySet());
    }

    public List<String> getCharacterImageNames() {
        return new ArrayList<>(characterImages.keySet());
    }

    public void loadCustomImage(String imageFilePath, Map<String, Image> images) {
        try {
            File imageFile = new File(imageFilePath);
            Image image = ImageIO.read(imageFile);
            String imageName = imageFile.getName().toLowerCase().split("\\.")[0].split("-")[0];
            images.put(imageName, image);
        } catch (IOException e) {
            logger.error("Error loading image file {}", imageFilePath, e);
        }
    }

    public void loadCustomImage(String imageFilePath) {
        loadCustomImage(imageFilePath, roomImages);
    }

    private void loadImage(String fileName, Map<String, Image> images, String dirName) {
        String imageName = fileName.split("-")[0].toLowerCase();
        try {
            images.put(
                    imageName,
                    ImageIO.read(getClass().getResourceAsStream("/" + dirName + "/" + fileName)));
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

    public List<String> getAllMatchingFileNamesFromJarFile(String pathToJarFile, String pathInResource, String containsSnippet) {
        try {
            Enumeration<JarEntry> entries;
            JarFile jarFile = new JarFile(pathToJarFile);
            entries = jarFile.entries();
            List<String> imageNames = new ArrayList<>();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().startsWith(pathInResource) && entry.getName().contains(containsSnippet)) {
                    String fileName = entry.getName().substring(pathInResource.length() + 1); // Extract file name
                    imageNames.add(fileName);
                }
            }
            return imageNames;
        } catch (Throwable e) {
            logger.warn("Could not load image files from within jar file: {}", pathToJarFile, e);
            return Collections.emptyList();
        }
    }

    public String getBestImageNameMatch(String name, Map<String, Image> images) {
        for (String imageName : images.keySet()) {
            if (name.toLowerCase().contains(imageName.toLowerCase().split("-")[0])) {
                return imageName;
            }
        }
        return null;
    }

    public Image getNextRoomImage(String name) {
        String imageName = getBestImageNameMatch(name, roomImages);
        if (imageName == null) {
            imageName = getNextRandomRoomImageName();
        }
        return roomImages.get(imageName);
    }

    public Image getNextCharacterImage(String name) {
        String imageName = getBestImageNameMatch(name, characterImages);
        if (imageName == null) {
            imageName = getNextRandomRoomImageName();
        }
        return characterImages.get(imageName);
    }

    private String getNextRandomRoomImageName() {
        List<String> keys = roomImages.keySet().stream().toList();
        roomNameIndex = (roomNameIndex + 1) % roomImages.size();
        return keys.get(roomNameIndex);
    }

    private String getNextRandomCharacterImageName() {
        List<String> keys = characterImages.keySet().stream().toList();
        characterNameIndex = (characterNameIndex + 1) % characterImages.size();
        return keys.get(characterNameIndex);
    }
}
