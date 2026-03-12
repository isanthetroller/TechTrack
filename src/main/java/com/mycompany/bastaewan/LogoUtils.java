package com.mycompany.bastaewan;

import javax.swing.*;
import java.net.URL;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Image;

/**
 * Helper for loading the application logo from the classpath.
 */
public class LogoUtils {
    public static ImageIcon getLogoIcon() {
        return getLogoIcon(64); // default medium size
    }

    /**
     * Load logo and scale to fit within maxSize (width or height) keeping aspect ratio.
     */
    public static ImageIcon getLogoIcon(int maxSize) {
        try {
            // resources now live under /image/logo.png
            URL res = LogoUtils.class.getResource("/image/logo.png");
            if (res != null) {
                Image img = ImageIO.read(res);
                int w = img.getWidth(null);
                int h = img.getHeight(null);
                if (w > 0 && h > 0 && maxSize > 0) {
                    double ratio = Math.min((double)maxSize / w, (double)maxSize / h);
                    int nw = (int) (w * ratio);
                    int nh = (int) (h * ratio);
                    Image scaled = img.getScaledInstance(nw, nh, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                }
                return new ImageIcon(img);
            }
        } catch (IOException e) {
            // ignore, will return null
        }
        return null;
    }
}
