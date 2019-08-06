import com.sybit.airtable.exception.AirtableException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.HttpResponseException;
import ph.edu.dlsu.datasal.facepamphlet.Profile;
import ph.edu.dlsu.datasal.facepamphlet.ProfileDatabase;

import acm.program.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;




/**
 *
 * @author cobalt mkc 2017
 */

public class SocialNet extends Program implements FacePamphletConstants {

    // Data
    private static ProfileDatabase profileDatabase;
    private List<Profile> profileList;
    private static Profile currentProfile;

    // GUI
    private FacePamphletCanvas canvas;
    private JTextField nameField;
    private JTextField statusField;
    private JTextField quoteField;
    private JTextField picField;
    private JTextField friendField;
    private JTextField unFriendField;

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        SocialNet app = new SocialNet();
        app.initDatabase();
        try {
            app.initGui();
        } catch (IOException ex) {
            Logger.getLogger(SocialNet.class.getName()).log(Level.SEVERE, null, ex);
        }
        app.start(args);
    }

    private void initDatabase() {
        try {
            profileDatabase = new ProfileDatabase();
            profileList = profileDatabase.getProfileList();
        } catch (HttpResponseException | AirtableException ex) {
            Logger.getLogger(SocialNet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method has the responsibility for initializing the interactors in
     * the application, and taking care of any other initialization that needs
     * to be performed.
     *
     * @throws java.io.IOException
     */
    public void initGui() throws IOException {

        /*        GUI NORTH : Name Add Delete Lookup     */
 /* Name */
        add(new JLabel("Name"), NORTH);
        nameField = new JTextField(TEXT_FIELD_SIZE);
        add(nameField, NORTH);
        nameField.addActionListener(this);

        add(new JButton("Add"), NORTH);
        add(new JButton("Delete"), NORTH);
        add(new JButton("Lookup"), NORTH);

        /*        GUI WEST : Status Picture AddFriend Unfriend    */
        /* Change Status */
        statusField = new JTextField(TEXT_FIELD_SIZE);
        add(statusField, WEST);
        statusField.addActionListener(this);
        add(new JButton("Change Status"), WEST);
        add(new JLabel(EMPTY_LABEL_TEXT), WEST);
        /* Change Picture */
        picField = new JTextField(TEXT_FIELD_SIZE);
        add(picField, WEST);
        picField.addActionListener(this);
        add(new JButton("Change Picture"), WEST);
        add(new JLabel(EMPTY_LABEL_TEXT), WEST);
        /* Add Friend */
        friendField = new JTextField(TEXT_FIELD_SIZE);
        add(friendField, WEST);
        friendField.addActionListener(this);
        add(new JButton("Add Friend"), WEST);
        /* Un Friend */
        unFriendField = new JTextField(TEXT_FIELD_SIZE);
        add(new JLabel(EMPTY_LABEL_TEXT), WEST);
        add(unFriendField, WEST);
        unFriendField.addActionListener(this);
        add(new JButton("UnFriend"), WEST);
        /* Change Quote */
        quoteField = new JTextField(TEXT_FIELD_SIZE);
        add(new JLabel(EMPTY_LABEL_TEXT), WEST);
        add(quoteField, WEST);
        quoteField.addActionListener(this);
        add(new JButton("Change Quote"), WEST);
        add(new JLabel(EMPTY_LABEL_TEXT), WEST);

        addActionListeners();

        canvas = new FacePamphletCanvas();
        add(canvas);
    }

    /**
     * This method is responsible for detecting when the buttons are clicked or
     * interactors are used, so you will have to add code to respond to these
     * actions.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Change Status") || e.getSource() == statusField) {
            try {
                changeStatus();
            } catch (AirtableException ex) {
                Logger.getLogger(SocialNet.class.getName()).log(Level.SEVERE, null, ex);
            }
            statusField.setText("");
        } else if (e.getActionCommand().equals("Change Picture") || e.getSource() == picField) {
            try {
                changePicture();
            } catch (AirtableException ex) {
                Logger.getLogger(SocialNet.class.getName()).log(Level.SEVERE, null, ex);
            }
            picField.setText("");
        } else if (e.getActionCommand().equals("Add Friend") || e.getSource() == friendField) {
            try {
                try {
                    addFriend();
                } catch (HttpResponseException ex) {
                    Logger.getLogger(SocialNet.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (AirtableException ex) {
                Logger.getLogger(SocialNet.class.getName()).log(Level.SEVERE, null, ex);
            }
            friendField.setText("");
        } else if (e.getActionCommand().equals("UnFriend") || e.getSource() == unFriendField) {
            try {
                unFriend();
            } catch (AirtableException | HttpResponseException ex) {
                Logger.getLogger(SocialNet.class.getName()).log(Level.SEVERE, null, ex);
            }
            unFriendField.setText("");
        } else if (e.getActionCommand().equals("Add") && !nameField.getText().equals("")) {
            try {
                addProfile();
            } catch (AirtableException ex) {
                Logger.getLogger(SocialNet.class.getName()).log(Level.SEVERE, null, ex);
            }
            nameField.setText("");
        } else if (e.getActionCommand().equals("Delete")) {
            removeProfile();
            nameField.setText("");
        } else if (e.getActionCommand().equals("Lookup")) {
            try {
                lookupProfile();
            } catch (AirtableException ex) {
                Logger.getLogger(SocialNet.class.getName()).log(Level.SEVERE, null, ex);
            }
            nameField.setText("");
        } else  if (e.getActionCommand().equals("Change Quote") || e.getSource() == quoteField) {
            try {
                changeQuote();
            } catch (AirtableException ex) {
                Logger.getLogger(SocialNet.class.getName()).log(Level.SEVERE, null, ex);
            }
            quoteField.setText("");
        }
    }

    private void changeStatus() throws AirtableException {
        String status = statusField.getText();
        if (!(status.equals(""))) {
            if (currentProfile != null) {
                profileDatabase.updateProfileStatus(currentProfile, status);
                canvas.displayProfile(currentProfile);
                canvas.displayFriends(currentProfile, acquireFriends(currentProfile));
                canvas.showMessage("Status updated to " + status);
            } else {
                canvas.showMessage("Please select a profile to change status.");
            }
        }
    }

    private static String[] acquireFriends(Profile self) throws AirtableException {
        String[] friends = null;
        String[] friendIds = self.getFriends();
        if (friendIds != null) {
            friends = new String[friendIds.length];
            for (int i = 0; i < friends.length; ++i) {
                friends[i] = (profileDatabase.getProfileById(friendIds[i])).getName();
            }
        }
        return friends;
    }

    private void changePicture() throws AirtableException {
        String picName = picField.getText();
        if (!picName.equals("")) {
            if (currentProfile != null) {
                try {
                    currentProfile = profileDatabase.updateProfilePhoto(currentProfile, picName);
                } catch (IOException ex) {
                    Logger.getLogger(SocialNet.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (currentProfile != null) {
                    canvas.displayProfile(currentProfile);
                    canvas.displayFriends(currentProfile, acquireFriends(currentProfile));
                    canvas.showMessage("Picture updated.");
                }
            }
        } else {
            canvas.showMessage("Please select a profile to change picture.");
        }
    }

    private void addFriend() throws AirtableException, HttpResponseException {
        String friendName = friendField.getText();
        if (!friendName.equals("")) {
            if (currentProfile != null) {
                if (profileDatabase.containsProfile(friendName)) {
                    if (!isFriend(currentProfile, friendName)) {
                        Profile friend = profileDatabase.getProfileByName(friendName);
                        try {
                            currentProfile = profileDatabase.addFriend(currentProfile, friend);
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                            Logger.getLogger(SocialNet.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        canvas.displayProfile(currentProfile);
                        try {
                            canvas.displayFriends(currentProfile, acquireFriends(currentProfile));
                        } catch (AirtableException ex) {
                            Logger.getLogger(SocialNet.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        canvas.showMessage(friendName + " added as a friend.");
                    } else {
                        canvas.showMessage(friendName + " is already a friend.");
                    }
                } else {
                    canvas.showMessage(friendName + " does not exist.");
                }
            } else {
                canvas.showMessage("Please select a profile to add a friend to.");
            }
        }
    }

    private boolean isFriend(Profile self, String friendName) throws AirtableException {
        String[] friendList = acquireFriends(self);
        if (friendList != null && friendList.length > 0) {
            for (String element : friendList) {
                if (element.equals(friendName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addProfile() throws AirtableException {
        String name = nameField.getText();
        if (!profileDatabase.containsProfile(name)) {
            Profile newProfile = new Profile();
            newProfile.setName(name);
            try {
                currentProfile = profileDatabase.addProfile(newProfile);
            } catch (AirtableException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | HttpResponseException ex) {
                Logger.getLogger(SocialNet.class.getName()).log(Level.SEVERE, null, ex);
            }
            canvas.displayProfile(currentProfile);
            canvas.displayFriends(currentProfile, acquireFriends(currentProfile));
            canvas.showMessage("New profile created");
        } else {
            currentProfile = profileDatabase.getProfileByName(name);
            canvas.displayProfile(currentProfile);
            canvas.displayFriends(currentProfile, acquireFriends(currentProfile));
            canvas.showMessage("A profile with the name " + name + " already exists.");
        }
    }

    private void removeProfile() {
        String name = nameField.getText();
        if (!name.equals("")) {
            currentProfile = null;
            canvas.removeAll();
            if (profileDatabase.containsProfile(name)) {
                try {
                    profileDatabase.deleteProfile(name);
                } catch (AirtableException | HttpResponseException ex) {
                    Logger.getLogger(SocialNet.class.getName()).log(Level.SEVERE, null, ex);
                }
                canvas.showMessage("Profile of " + name + " deleted.");
            } else {
                canvas.showMessage("A profile with the name " + name + " does not exist.");
            }
        }
    }

    private void lookupProfile() throws AirtableException {
        String name = nameField.getText();
        if (!name.equals("")) {
            if (profileDatabase.containsProfile(name)) {
                currentProfile = profileDatabase.getProfileByName(name);
                canvas.displayProfile(currentProfile);
                canvas.displayFriends(currentProfile, acquireFriends(currentProfile));
                canvas.showMessage("Displaying " + name + ".");
            } else {
                currentProfile = null;
                canvas.removeAll();
                canvas.showMessage("A profile with the name " + name + " does not exist.");
            }
        }
    }

    private void unFriend() throws AirtableException, HttpResponseException {
        String friendName = unFriendField.getText();
        if (!friendName.equals("")) {
            if (currentProfile != null) {
                if (profileDatabase.containsProfile(friendName)) {
                    if (isFriend(currentProfile, friendName)) {
                        Profile friend = profileDatabase.getProfileByName(friendName);
                        currentProfile = profileDatabase.unFriend(currentProfile, friend);
                        canvas.displayProfile(currentProfile);
                        try {
                            canvas.displayFriends(currentProfile, acquireFriends(currentProfile));
                        } catch (AirtableException ex) {
                            Logger.getLogger(SocialNet.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        canvas.showMessage(friendName + " removed as a friend.");
                    } else {
                        canvas.showMessage(friendName + " and " + currentProfile.getName() + " are not friends.");
                    }

                } else {
                    canvas.showMessage(friendName + " does not exist.");
                }
            } else {
                canvas.showMessage("Please select a profile to remove a friend to.");
            }
        }
    }

       private void changeQuote() throws AirtableException {
        String quote = quoteField.getText();
        if (!(quote.equals(""))) {
            if (currentProfile != null) {
                profileDatabase.updateProfileQuote(currentProfile, quote);
                canvas.displayProfile(currentProfile);
                canvas.displayFriends(currentProfile, acquireFriends(currentProfile));
                canvas.showMessage("Quote updated to " + quote);
            } else {
                canvas.showMessage("Please select a profile to change quote.");
            }
        }
    }

}
