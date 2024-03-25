package ru.fedornic.converter;

import ru.fedornic.exception.BadImageSizeException;
import ru.fedornic.interfaces.TextColorSchema;
import ru.fedornic.interfaces.TextGraphicsConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Converter implements TextGraphicsConverter {
    private TextColorSchema schema;
    private double maxRatio;
    private int maxWidth;
    private int maxHeight;

    public Converter() {
        schema = new ColorSchema();
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        int width = img.getWidth();
        int height = img.getHeight();
        double ratio = (double) width / height;
        System.out.printf("ImageSizes: width=%d height=%d ratio=%.2f%n", width, height, ratio);
        System.out.printf("MaxSizes: width=%d height=%d ratio=%.2f%n", maxWidth, maxHeight, maxRatio);
        if (ratio > maxRatio && maxRatio > 0) {
            throw new BadImageSizeException(
                    Math.round(ratio * 100) / 100d, Math.round(maxRatio * 100) / 100d);
        }
        if (height > maxHeight && maxHeight > 0) {
            System.out.printf("\tНедопустимая высота изображения: %d вместо %d%n", height, maxHeight);
            img = resize(img, (int) (width / ((double) height / maxHeight)), maxHeight);
            System.out.println("Произведено сжатие изображения");
            width = img.getWidth();
            height = img.getHeight();
            ratio = (double) width / height;
            System.out.printf("NewImageSizes: width=%d height=%d ratio=%.2f%n", width, height, ratio);
        }
        if (width > maxWidth && maxWidth > 0) {
            System.out.printf("\tНедопустимая ширина изображения: %d вместо %d%n", width, maxWidth);
            img = resize(img, maxWidth, (int) (height / ((double) width / maxWidth)));
            System.out.println("Произведено сжатие изображения");
            width = img.getWidth();
            height = img.getHeight();
            ratio = (double) width / height;
            System.out.printf("NewImageSizes: width=%d height=%d ratio=%.2f%n", width, height, ratio);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = img.getRGB(j, i);
                int red =   (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue =  (rgb) & 0xFF;
                byte color = (byte) ((Byte.MIN_VALUE * 3 + red + green + blue) / 3);
                char c = schema.convert(color);
                sb.append(c);
                sb.append(c);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }

    private BufferedImage resize(BufferedImage oldImage, int newWidth, int newHeight) {
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        newImage.createGraphics().drawImage(oldImage, 0, 0, newWidth, newHeight, null);
        newImage.createGraphics().dispose();
        return newImage;
    }
}