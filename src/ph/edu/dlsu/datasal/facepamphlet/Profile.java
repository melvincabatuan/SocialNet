/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ph.edu.dlsu.datasal.facepamphlet;

import com.google.gson.annotations.SerializedName;
import com.sybit.airtable.vo.Attachment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author cobalt mkc 2017
 */
public class Profile {

    private String id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Status")
    private String status;
    @SerializedName("Photo")
    private List<Attachment> photo;
    @SerializedName("Friends")
    private String[] friends;
    @SerializedName("Quote")
    private String quote;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() { // Primary key
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Attachment> getPhoto() {
        return photo;
    }

    public BufferedImage getImage() {
        BufferedImage image = null;
        if (this.getPhoto() != null) {
            Attachment temp = (this.getPhoto()).get(0);
            try {
                URL url = new URL(temp.getUrl());
                image = ImageIO.read(url);
            } catch (IOException e) {
            }
        }
        return image;
    }

    public BufferedImage getImageProxy() {
        BufferedImage image = null;
        if (this.getPhoto() != null) {
            Attachment temp = (this.getPhoto()).get(0);
            try {
             SocketAddress address = new InetSocketAddress("proxy.dlsu.edu.ph", 80);
             Proxy proxy = new Proxy(Proxy.Type.HTTP, address);             
             URL url = new URL(temp.getUrl());
             URLConnection conn = url.openConnection(proxy);
             InputStream inStream = conn.getInputStream();             
             image = ImageIO.read(inStream);
            } catch (IOException e) {
            }
        }
        return image;
    }

    public void setPhoto(List<Attachment> photo) {
        this.photo = photo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getFriends() {
        return friends;
    }

    public void setFriends(String[] friends) {
        this.friends = friends;
    }
    
    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    /**
     * Copies everything except the id
     *
     * @param profile
     */
    public void copyFrom(Profile profile) {
        this.name = profile.getName();
        this.status = profile.getStatus();
        this.quote = profile.getQuote();
        this.photo = profile.getPhoto();
        this.friends = profile.getFriends();
    }
}
