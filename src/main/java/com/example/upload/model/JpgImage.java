package com.example.upload.model;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class JpgImage {

    public static final String EXTENSION = "jpg";

    private final File source;

    public JpgImage(File source) {
        this.source = Objects.requireNonNull(source);
    }

    public void compressTo(File target) throws IOException {
        FileImageOutputStream targetOutputStream = new FileImageOutputStream(target);
        BufferedImage sourceImage = ImageIO.read(source);

        ImageWriter writer = getWriter();
        ImageWriteParam writerSettings = getWriterSettings(writer);

        try {
            writer.setOutput(targetOutputStream);
            writer.write(null, new IIOImage(sourceImage, null, null), writerSettings);
        } finally {
            writer.dispose();
            targetOutputStream.close();
            sourceImage.flush();
        }
    }

    private ImageWriter getWriter() {
        Iterator<ImageWriter> imageWritersIterator =
                ImageIO.getImageWritersByFormatName(EXTENSION);

        if (!imageWritersIterator.hasNext()) {
            throw new NoSuchElementException(
                    String.format("Could not find an image writer for %s format", EXTENSION));
        }

        return imageWritersIterator.next();
    }

    private ImageWriteParam getWriterSettings(ImageWriter imageWriter) {
        ImageWriteParam imageWriteParams = imageWriter.getDefaultWriteParam();

        imageWriteParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParams.setCompressionQuality(0.8f);

        return imageWriteParams;
    }
}