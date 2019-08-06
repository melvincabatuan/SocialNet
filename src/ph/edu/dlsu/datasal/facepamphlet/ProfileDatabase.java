/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ph.edu.dlsu.datasal.facepamphlet;

import com.sybit.airtable.*;
import com.sybit.airtable.exception.AirtableException;
import com.sybit.airtable.vo.Attachment;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.client.HttpResponseException;

/**
 *
 * @author cobalt
 */
public class ProfileDatabase {

    private static final String AIRTABLE_API_KEY = "keyGJDh2uvzgVsg0J";
    public static final String ENDPOINT_URL = "https://api.airtable.com/v0"; // default
    private static final String AIRTABLE_BASE = "app2CMKKwghonbEXG";
    private static final String PROXY = "http://proxy.dlsu.edu.ph:80";
    private static final String TABLE_NAME = "Profiles";

    private final Table<Profile> profileTable;
    private List<Profile> profileList;
    private int foundIndex;

    private final ImageUploader imageUploader;

    /**
     * Constructor This method takes care of any initialization needed for the
     * AirTable database.
     *
     * @throws com.sybit.airtable.exception.AirtableException
     * @throws org.apache.http.client.HttpResponseException
     */
    public ProfileDatabase() throws AirtableException, HttpResponseException {
        Airtable airtable = new Airtable().configure(new Configuration(AIRTABLE_API_KEY, ENDPOINT_URL, PROXY));
        Base base = airtable.base(AIRTABLE_BASE);
        profileTable = base.table(TABLE_NAME, Profile.class);
        profileList = profileTable.select();
        imageUploader = new ImageUploader();
    }

        /**
     * This method adds the given profile to the database. If the name
     * associated with the profile is the same as an existing name in the
     * database, the existing profile is replaced by the new profile passed in.
     *
     * @param profile
     * @return
     * @throws com.sybit.airtable.exception.AirtableException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws java.lang.NoSuchMethodException
     * @throws org.apache.http.client.HttpResponseException
     */
    public Profile addProfile(Profile profile) throws AirtableException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, HttpResponseException {
        Profile result;
        if (!containsProfile(profile.getName())) {
            result = profileTable.create(profile);
        } else {
            result = profileTable.update(profileList.get(foundIndex));
        }
        profileList = profileTable.select(); // update the profile list
        return result;
    }

    /**
     * This method returns the profile associated with the given name in the
     * database. If there is no profile in the database with the given name, the
     * method returns null.
     *
     * @param name
     * @return
     */
    public Profile getProfileByName(String name) {
        if (containsProfile(name)) {
            return (profileList.get(foundIndex));
        } else {
            return null;
        }
    }

    /**
     * This method returns the profile associated with the given id in the
     * database. If there is no profile in the database with the given name, the
     * method returns null.
     *
     * @param id
     * @return
     * @throws com.sybit.airtable.exception.AirtableException
     */
    public Profile getProfileById(String id) throws AirtableException {
        return profileTable.find(id);
    }

    /**
     * This method check whether the profile name exists (case-sensitive) This
     * method returns true if there is a profile in the database that has the
     * given name. It returns false otherwise.
     *
     * @param name
     * @return
     */
    public boolean containsProfile(String name) {
        boolean exists = false;
        foundIndex = -1;
        for (int i = 0; i < profileList.size(); i++) {
            if ((profileList.get(i).getName()).equals(name)) {
                exists = true;
                foundIndex = i;
            }
        }
        return exists;
    }

    /**
     * This method removes the profile associated with the given name from the
     * database. It also updates the list of friends of all other profiles in
     * the database to make sure that this name is removed from the list of
     * friends of any other profile.
     *
     * If there is no profile in the database with the given name, then the
     * database is unchanged after calling this method.
     *
     * @param name
     * @throws com.sybit.airtable.exception.AirtableException
     * @throws org.apache.http.client.HttpResponseException
     */
    public void deleteProfile(String name) throws AirtableException, HttpResponseException {
        if (containsProfile(name)) {
            profileTable.destroy((profileList.get(foundIndex)).getId());
        }
        profileList = profileTable.select(); // update the profile list
    }

    /**
     * This method updates the whole profile of the user
     *
     * @param name
     * @param newProfile
     */
    public void updateProfile(String name, Profile newProfile) {
        if (containsProfile(name)) {
            Profile temp = profileList.get(foundIndex);
            temp.copyFrom(newProfile);
            try {
                profileTable.update(temp);
            } catch (InvocationTargetException | NoSuchMethodException | AirtableException | IllegalAccessException ex) {
                Logger.getLogger(ProfileDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This method updates the name profile of the user
     *
     * @param name
     * @param newName
     */
    public void updateProfileName(String name, String newName) {
        if (containsProfile(name) && !containsProfile(newName)) {
            Profile temp = getProfileByName(name);
            temp.setName(newName);
            try {
                profileTable.update(temp);
            } catch (InvocationTargetException | NoSuchMethodException | AirtableException | IllegalAccessException ex) {
                Logger.getLogger(ProfileDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void updateProfileName(Profile current, String newName) {
        if (containsProfile(current.getName()) && !containsProfile(newName)) {
            current.setName(newName);
            try {
                profileTable.update(current);
            } catch (InvocationTargetException | NoSuchMethodException | AirtableException | IllegalAccessException ex) {
                Logger.getLogger(ProfileDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This method updates the name profile of the user
     *
     * @param name
     * @param status
     */
    public Profile updateProfileStatus(String name, String status) {
        Profile result = null;
        if (containsProfile(name)) {
            Profile temp = getProfileByName(name);
            temp.setStatus(status);
            try {
                result = profileTable.update(temp);
            } catch (InvocationTargetException | NoSuchMethodException | AirtableException | IllegalAccessException ex) {
                Logger.getLogger(ProfileDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public void updateProfileStatus(Profile current, String status) {
        Profile result = null;
        if (containsProfile(current.getName())) {
            current.setStatus(status);
            try {
                profileTable.update(current);
            } catch (InvocationTargetException | NoSuchMethodException | AirtableException | IllegalAccessException ex) {
                Logger.getLogger(ProfileDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
        /**
     * This method updates the name profile of the user
     *
     * @param name
     * @param quote
     * @return 
     */
    public Profile updateProfileQuote(String name, String quote) {
        Profile result = null;
        if (containsProfile(name)) {
            Profile temp = getProfileByName(name);
            temp.setQuote(quote);
            try {
                result = profileTable.update(temp);
            } catch (InvocationTargetException | NoSuchMethodException | AirtableException | IllegalAccessException ex) {
                Logger.getLogger(ProfileDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public void updateProfileQuote(Profile current, String quote) {
        Profile result = null;
        if (containsProfile(current.getName())) {
            current.setQuote(quote);
            try {
                profileTable.update(current);
            } catch (InvocationTargetException | NoSuchMethodException | AirtableException | IllegalAccessException ex) {
                Logger.getLogger(ProfileDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    

    /**
     * This method updates the name profile of the user
     *
     * @param name
     * @param src
     * @throws java.io.IOException
     */
    public Profile updateProfilePhoto(String name, String src) throws IOException {
        Profile temp = null;
        if (containsProfile(name)) {
            temp = getProfileByName(name);
            temp.setPhoto(converttoAttachmentList(src));
            try {
               temp = profileTable.update(temp);
            } catch (InvocationTargetException | NoSuchMethodException | AirtableException | IllegalAccessException ex) {
                Logger.getLogger(ProfileDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return temp;
    }

    public Profile updateProfilePhoto(Profile current, String src) throws IOException {
        if (containsProfile(current.getName())) {
            current = profileList.get(foundIndex);
            current.setPhoto(converttoAttachmentList(src));
            try {
                current =  profileTable.update(current);
            } catch (InvocationTargetException | NoSuchMethodException | AirtableException | IllegalAccessException ex) {
                Logger.getLogger(ProfileDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return current;
    }

    /**
     * This method converts a file path or url to attachment list for photo
     * update
     *
     * @param src
     * @return
     * @throws IOException
     */
    public List<Attachment> converttoAttachmentList(String src) throws IOException {
        if (!isURL(src)) {
            src = imageUploader.upload(src);
        }
        Attachment pic = new Attachment();
        pic.setUrl(src);
        List<Attachment> attachedPhoto = new ArrayList<>();
        attachedPhoto.add(pic);
        return attachedPhoto;
    }

    /**
     * This method checks whether a string is a valid URL
     *
     * @param urlStr
     * @return
     */
    static private boolean isURL(String urlStr) {
        try {
            URI uri = new URI(urlStr);
            return uri.getScheme().equals("http") || uri.getScheme().equals("https");
        } catch (URISyntaxException e) {
            return false;
        }
    }

    /**
     * This method adds a friend to the friend list if profile is found in the
     * database
     *
     * @param self
     * @param friend
     */
    public Profile addFriend(Profile self, Profile friend) throws AirtableException, HttpResponseException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (containsProfile(self.getName()) && containsProfile(friend.getName())) {
            String[] oldFriendList1 = self.getFriends();
            if (oldFriendList1 == null) // get rid of null pointer exception
            {
                oldFriendList1 = new String[0];
            }
            String[] newFriendList1 = new String[oldFriendList1.length + 1];
            System.arraycopy(oldFriendList1, 0, newFriendList1, 0, oldFriendList1.length);
            newFriendList1[oldFriendList1.length] = friend.getId();
            self.setFriends(newFriendList1);

            String[] oldFriendList2 = friend.getFriends();
            if (oldFriendList2 == null) // get rid of null pointer exception
            {
                oldFriendList2 = new String[0];
            }
            String[] newFriendList2 = new String[oldFriendList2.length + 1];
            System.arraycopy(oldFriendList2, 0, newFriendList2, 0, oldFriendList2.length);
            newFriendList2[oldFriendList2.length] = self.getId();
            friend.setFriends(newFriendList2);
            try {
                self = profileTable.update(self);
                friend = profileTable.update(friend);
            } catch (InvocationTargetException | NoSuchMethodException | AirtableException | IllegalAccessException ex) {
                Logger.getLogger(ProfileDatabase.class.getName()).log(Level.SEVERE, null, ex);
//                profileList = profileTable.select(); // update the profile list
//                self = getProfileByName(self.getName());
//                self.setFriends(newFriendList1);
//                self = profileTable.update(self);
//                friend = getProfileByName(friend.getName());
//                friend.setFriends(newFriendList2);
//                friend = profileTable.update(friend);  // 422 error
            }
        }
         profileList = profileTable.select();
        return self;
    }

    /**
     * This method removes a Profile from the friend list (unfriend)
     *
     * @param self
     * @param friend
     */
    public Profile unFriend(Profile self, Profile friend) throws AirtableException, HttpResponseException {
        if (containsProfile(self.getName()) && containsProfile(friend.getName())) {
            String[] oldFriendList1 = self.getFriends();
            String[] newFriendList1 = (String[]) ArrayUtils.removeElement(oldFriendList1, friend.getId());
            self.setFriends(newFriendList1);

            String[] oldFriendList2 = friend.getFriends();
            String[] newFriendList2 = (String[]) ArrayUtils.removeElement(oldFriendList2, self.getId());
            friend.setFriends(newFriendList2);
            try {
                self = profileTable.update(self);
//                try {  // Works without this: API of Airtable itself is limited to 5 requests per second NO MORE?
//                    Thread.sleep(200);     
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(ProfileDatabase.class.getName()).log(Level.SEVERE, null, ex);
//                }
                friend = profileTable.update(friend);
            } catch (InvocationTargetException | NoSuchMethodException | AirtableException | IllegalAccessException ex) {
                Logger.getLogger(ProfileDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         profileList = profileTable.select();
        return self;
    }

    /**
     * This method returns all records of table "Profiles"
     *
     * @return
     */
    public List<Profile> getProfileList() {
        return profileList;
    }
}
