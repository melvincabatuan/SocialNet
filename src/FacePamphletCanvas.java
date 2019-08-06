/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import acm.graphics.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import ph.edu.dlsu.datasal.facepamphlet.Profile;

/**
 *
 * @author cobalt mkc 2017
 */
public class FacePamphletCanvas extends GCanvas implements FacePamphletConstants {

    private final BufferedImage unknownPic;
    private GLabel nameLabel;
    private GLabel messageLabel;

    /**
     * Constructor This method takes care of any initialization needed for the
     * display
     *
     * @throws java.io.IOException
     */
    public FacePamphletCanvas() throws IOException {
        unknownPic = ImageIO.read(FacePamphletCanvas.class.getResource("unknown.png"));
    }

    /**
     * This method displays a message string near the bottom of the canvas.
     * Every time this method is called, the previously displayed message (if
     * any) is replaced by the new message text passed in.
     *
     * @param message
     */
    public void showMessage(String message) {
        messageLabel = new GLabel(message);
        messageLabel.setFont(MESSAGE_FONT);

        double labelX = (getWidth() - messageLabel.getWidth()) / 2;
        double labelY = getHeight() - BOTTOM_MESSAGE_MARGIN;

        GObject oldLabel = getElementAt(getWidth() / 2, labelY);

        if (oldLabel != null) {
            remove(oldLabel);
        }

        messageLabel.setLocation(labelX, labelY);
        add(messageLabel);
    }

    /**
     * This method displays the given profile on the canvas. The canvas is first
     * cleared of all existing items (including messages displayed near the
     * bottom of the screen) and then the given profile is displayed. The
     * profile display includes the name of the user from the profile, the
     * corresponding image (or an indication that an image does not exist), the
     * status of the user, and a list of the user's friends in the social
     * network.
     */
    public void displayProfile(Profile profile) {
        removeAll();
        displayName(profile.getName());
        Image profilePic = profile.getImageProxy();
        if (profilePic != null) {
            displayImage(new GImage(profilePic));
        } else {
            displayImage(new GImage(unknownPic));
        }
        displayStatus(profile.getName(), profile.getStatus());
        displayQuote(profile.getName(), profile.getQuote());
    }

    private void displayName(String name) {
        nameLabel = new GLabel(name);
        nameLabel.setColor(Color.BLUE);
        nameLabel.setFont(PROFILE_NAME_FONT);
        nameLabel.setLocation(LEFT_MARGIN, nameLabel.getHeight() + TOP_MARGIN);
        add(nameLabel);
    }

    private void displayImage(GImage picture) {
        if (picture != null) {
            GImage image = picture;
            image.setLocation(LEFT_MARGIN, nameLabel.getHeight() + UPPER_MARGIN);
            image.setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
            add(image);
        } else {
            GRect imageRect = new GRect(LEFT_MARGIN, nameLabel.getHeight() + UPPER_MARGIN, IMAGE_WIDTH, IMAGE_HEIGHT);
            add(imageRect);
            GLabel imageLabel = new GLabel("No Image");
            imageLabel.setFont(PROFILE_IMAGE_FONT);
            double labelX = LEFT_MARGIN + (IMAGE_WIDTH - imageLabel.getWidth()) / 2;
            double labelY = nameLabel.getHeight() + UPPER_MARGIN + IMAGE_HEIGHT / 2;
            imageLabel.setLocation(labelX, labelY);
            add(imageLabel);
        }
    }

    private void displayStatus(String name, String status) {
        GLabel statusLabel = new GLabel("");
        double totalMargins = UPPER_MARGIN + IMAGE_HEIGHT + STATUS_MARGIN;
        statusLabel.setFont(PROFILE_STATUS_FONT);
        statusLabel.setLocation(LEFT_MARGIN, nameLabel.getHeight() + totalMargins + statusLabel.getHeight());
        if (status != null && !status.equals("")) {
            statusLabel.setLabel(name + " is " + status);
        } else {
            statusLabel.setLabel("No current status");
        }
        add(statusLabel);

    }

    private void displayQuote(String name, String quote) {
        GLabel quoteLabel = new GLabel("");
        quoteLabel.setFont(QUOTE_LABEL_FONT);
        if (quote != null && !quote.equals("")) {
            quoteLabel.setLabel("\"" + quote + "\"");
        } else {
            quoteLabel.setLabel("No current quote");
        }
        double quoteX = (getWidth() - quoteLabel.getWidth()) / 2;
        double quoteY = getHeight() - QUOTE_HEIGHT_FROM_BOTTOM;
        quoteLabel.setLocation(quoteX, quoteY);
        add(quoteLabel);

    }

    public void displayFriends(Profile profile, String[] friends) {
        GLabel friendsHeading = new GLabel("Friends: ");
        double friendsHeadingYpos = nameLabel.getHeight() + UPPER_MARGIN;
        friendsHeading.setFont(PROFILE_FRIEND_LABEL_FONT);
        friendsHeading.setLocation(getWidth() / 2, friendsHeadingYpos);
        add(friendsHeading);
        if (friends != null) {
            for (int i = 0; i < friends.length; ++i) {
                GLabel friendLabel = new GLabel(friends[i]);
                friendLabel.setLocation(getWidth() / 2, friendsHeadingYpos + friendsHeading.getHeight() + (i * friendLabel.getHeight()));
                add(friendLabel);
            }
        }
    }

}
