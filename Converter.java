package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Converter implements TextGraphicsConverter {
    private int maxWidth;
    private int maxHeight;
    private double maxRatio;
    private TextColorSchema schema;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        int newWidth = img.getWidth();
        int newHeight = img.getHeight();
        double ratio = (double) img.getWidth() / (double) img.getHeight();
        if (maxRatio != 0 && maxRatio < ((double) img.getWidth() / (double) img.getHeight())) {
            throw new BadImageSizeException(ratio, maxRatio);
        }
        if (maxWidth == 0 || maxWidth > img.getWidth()) {
            setMaxWidth(img.getWidth());
        } else if (maxWidth > 0 && maxWidth < img.getWidth()) {
            newWidth = maxWidth;
        }
        if (maxHeight == 0 || maxHeight > img.getHeight()) {
            setMaxHeight(img.getHeight());
        } else if (maxHeight > 0 && maxHeight < img.getHeight()) {
            newHeight = maxHeight;
        }

        double newRatio = (double) newWidth / (double) newHeight;
        if (newRatio > ratio) {
            newWidth = (int) (newHeight * ratio);
        } else if (newRatio < ratio) {
            newHeight = (int) (newWidth / ratio);
        }
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);

        WritableRaster bwRaster = bwImg.getRaster();
        ImageIO.write(bwImg, "png", new File("out.png"));
        setTextColorSchema(schema);
        String[][] imgChar = new String[newHeight][newWidth];
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                imgChar[h][w] = String.valueOf(c) + c;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                sb.append(imgChar[h][w]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) { this.schema = new Schema(); }

}
