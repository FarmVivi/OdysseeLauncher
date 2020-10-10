package fr.odyssee.application.color;

import java.awt.*;

public class ColorAPI {
    public static final Color BLUE = new Color(0, 120, 255);
    public static final Color BLUE_HOVERED = new Color(0, 135, 255);
    public static final Color BLUE_DISABLED = new Color(0, 150, 255);
    public static final Color RED = new Color(255, 10, 0);
    public static final Color RED_HOVERED = new Color(255, 30, 0);
    public static final Color RED_DISABLED = new Color(255, 50, 0);

    public static Color principalColor = BLUE;
    public static Color principalColor_hovered = BLUE_HOVERED;
    public static Color principalColor_disabled = BLUE_DISABLED;
    public static Color secondColor = RED;
    public static Color secondColor_hovered = RED_HOVERED;
    public static Color secondColor_disabled = RED_DISABLED;

    public static Color ORANGE_CUSTOM = new Color(240, 81, 37);
}
