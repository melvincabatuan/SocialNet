/*
 * File: FacePamphletConstants.java
 * ----------------------
 * This file declares several constants that are shared by the
 * different modules in the FacePamphlet application.  Any class
 * that implements this interface can use these constants.
 */
/**
 * This interface declares various constants that are used in the FacePamphlet
 * application. To use the constants in this interface, all you need to do is
 * add <code>FacePamphletConstants</code> to the <code>implements</code> clause
 * for your class.
 */
public interface FacePamphletConstants {

    /**
     * The width of the application window.
     */
    public static final int APPLICATION_WIDTH = 1640;

    /**
     * The height of the application window.
     */
    public static final int APPLICATION_HEIGHT = 900;

    /**
     * Name of font used to display the application message at the bottom of the
     * display canvas.
     */
    public static final String MESSAGE_FONT = "Dialog-18";

    /**
     * Name of font used to display the name in a user's profile.
     */
    public static final String PROFILE_NAME_FONT = "Dialog-24";

    /**
     * Name of font used to display the text "No Image" in user profiles that do
     * not contain an actual image.
     */
    public static final String PROFILE_IMAGE_FONT = "Dialog-24";

    /**
     * Name of font used to display the status in a user's profile.
     */
    public static final String PROFILE_STATUS_FONT = "Dialog-bold-16";

    /**
     * The maximum height (in pixels) that profile images should be displayed.
     * The maximum width is the width of the graphics window minus the margins.
     */
    public static final double IMAGE_HEIGHT = 200;

    /**
     * The number of pixels in the horizontal margin between the left side of
     * the canvas display area and the Name, Image, Status, and Message
     * components that are displayed in the profile.
     */
    public static final double LEFT_MARGIN = 20;

    /**
     * The number of pixels in the horizontal margin between the right side of
     * the canvas display area and the Name, Image, and Status components that
     * are displayed in the profile.
     */
    public static final double RIGHT_MARGIN = LEFT_MARGIN;

    /**
     * The number of pixels in the vertical margin between the top of the canvas
     * display area and the top (NOT the baseline) of the Name component that is
     * displayed in the profile.
     */
    public static final double TOP_MARGIN = 20;

    /**
     * The number of pixels in the vertical margin between the baseline of the
     * Name component and the top of the Image displayed in the profile.
     */
    public static final double NAME_IMAGE_SEP = 20;

    /**
     * The number of vertical pixels in the vertical margin between the bottom
     * of the Image and the top of the Status component in the profile.
     */
    public static final double IMAGE_STATUS_SEP = 20;

    /**
     * The number of pixels in the vertical margin between the bottom of the
     * canvas display area and the baseline of the Message display in the
     * profile.
     */
    public static final double BOTTOM_MARGIN = 20;
    
    
    public static final double QUOTE_HEIGHT_FROM_BOTTOM = 180;
    
    public static final String QUOTE_LABEL_FONT = "SansSerif-22-italic";

    /**
     * Number of characters for each of the text input fields
     */
    public static final int TEXT_FIELD_SIZE = 15;

    /**
     * Text to be used to create an "empty" label to put space between
     * interactors on EAST border of application. Note this label is not
     * actually the empty string, but rather a single space
     */
    public static final String EMPTY_LABEL_TEXT = " ";

    /**
     * Name of font used to display the label "Friends" above the user's list of
     * friends in a profile
     */
    public static final String PROFILE_FRIEND_LABEL_FONT = "Dialog-16-bold";

    /**
     * Name of font used to display the names from the user's list of friends in
     * a profile
     */
    public static final String PROFILE_FRIEND_FONT = "Dialog-16";

    /**
     * The width (in pixels) that profile images should be displayed
     */
    public static final double IMAGE_WIDTH = 200;

    /**
     * The number of pixels in the vertical margin between the bottom of the
     * canvas display area and the baseline for the message text that appears
     * near the bottom of the display
     */
    public static final double BOTTOM_MESSAGE_MARGIN = 20;

    /**
     * The number of pixels in the vertical margin between the baseline of the
     * Name component and the top of the Image displayed in the profile
     */
    public static final double IMAGE_MARGIN = 20;

    /**
     * The number of vertical pixels in the vertical margin between the bottom
     * of the Image and the top of the Status component in the profile
     */
    public static final double STATUS_MARGIN = 20;
    
    
    
    public static final double UPPER_MARGIN = TOP_MARGIN + IMAGE_MARGIN;
}
