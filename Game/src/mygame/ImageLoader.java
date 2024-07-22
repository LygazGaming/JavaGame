package mygame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageLoader {

    public static BufferedImage loadImage(String path) {
        try (InputStream input = ImageLoader.class.getResourceAsStream(path)) {
            return ImageIO.read(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
