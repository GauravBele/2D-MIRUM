/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._2dmedicalImageprocessingusingmapreduce.service;

import com._2dmedicalImageprocessingusingmapreduce.admin.AdminLoadImages;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author akshay
 */
public class ServiceImpl implements Service {

    private static final Logger LOGGER = Logger.getLogger(AdminLoadImages.class.getName());

    @Override
    public BufferedImage resizeImage(BufferedImage image, int width, int height) {
        int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        LOGGER.info("Image Resized....");
        return resizedImage;
    }

    @Override
    public String getVector(BufferedImage bufferedImage) {
        int vector[] = new int[16];
        Color c = new Color(0);
        int r, g, b, gs;
        String vect = "";
        for (int y = 0; y < bufferedImage.getWidth(); y++) {
            for (int x = 0; x < bufferedImage.getHeight(); x++) {
                c = new Color(bufferedImage.getRGB(x, y));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                gs = (r + g + b) / 3;
                vector[gs / 16]++;
            }
        }
        for (int i = 0; i < vector.length; i++) {
            vect += vector[i] + " ";
        }
        return vect.trim();
    }

    @Override
    public Properties getProperties(String propFileName) {
        Properties prop = new Properties();
         //getClass().getClassLoader().getResourceAsStream("/home/akshay/NetBeansProjects/2DMedicalImageProcessingUsingMapReduce/src/resource/2d-mirum.properties");
        try {
            InputStream inputStream = new FileInputStream(new File("./src/resource/2d-mirum.properties"));
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        } catch (IOException ex) {
            Logger.getLogger(ServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prop;
    }

}
