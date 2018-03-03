package com.github.teamclc.seniorious.api.data.message;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Picture implements MessageSequence {
    private final ByteBuffer imageBuffer;

    public Picture(ByteBuffer imageBuffer) {
        this.imageBuffer = imageBuffer;
    }

    public Picture(byte... bytes) {
        imageBuffer = ByteBuffer.wrap(bytes);
    }

    @Override
    public String getRawMessage() {
        return null; // TODO Bytes -> QQ message
    }

    /**
     * Creates picture from an {@link BufferedImage}
     */
    public static Picture of(BufferedImage image) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", outputStream); // TODO Update in QQ
            return new Picture(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates picture from an {@link javafx.scene.image.Image}
     */
    public static Picture of(Image image) {
        return Picture.of(SwingFXUtils.fromFXImage(image, null)); // Wraps to awt image
    }
}
