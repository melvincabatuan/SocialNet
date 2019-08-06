/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ph.edu.dlsu.datasal.facepamphlet;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author cobalt mkc 2017
 */
public class ImageUploader {

    private final Cloudinary cloudinary;
    private String uploadUrl;

    public ImageUploader() {
        cloudinary = new Cloudinary("cloudinary://182127436623886:B6y3gl6VQ8eISwLE-k_EbUJZ1Cg@creativ");
    }

    public String upload(String urlOrPath) throws IOException {
        // No proxy
        //Map uploadResult = (cloudinary.uploader()).upload(urlOrPath, ObjectUtils.emptyMap());
        // behind Proxy
        Map uploadResult = (cloudinary.uploader()).upload(urlOrPath, ObjectUtils.asMap("proxy", "proxy.dlsu.edu.ph:80"));
        uploadUrl = (String) uploadResult.get("url");
        return uploadUrl;
    }

    public Image getImage() {
        Image image = null;
        try {
            URL url = new URL(uploadUrl);
            image = ImageIO.read(url);
        } catch (IOException e) {
        }
        return image;
    }
}
