package fr.odyssee.application.components;

import fr.odyssee.application.LauncherFrame;
import fr.odyssee.application.color.ColorAPI;
import fr.theshark34.swinger.Swinger;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.net.MalformedURLException;
import java.net.URL;

public class NewsComponent extends JComponent {
    private final String newsTitle;
    private final String newsAuthor;
    private final String newsText;
    private ImageIcon newsTitleIcon;
    private ImageIcon newsAuthorIcon;

    public NewsComponent(String newsTitleIconURL, String newsTitle, String newsAuthorIconURL, String newsAuthor, String newsText) {
        if (newsTitleIconURL != null) {
            try {
                this.newsTitleIcon = new ImageIcon(new URL(newsTitleIconURL));
            } catch (MalformedURLException e) {
                LauncherFrame.getInstance().getCrashReporter().catchError(e, "Impossible de charger l'icon de la news");
            }
        }
        this.newsTitle = newsTitle;
        if (newsAuthorIconURL != null) {
            try {
                this.newsAuthorIcon = new ImageIcon(new URL(newsAuthorIconURL));
            } catch (MalformedURLException e) {
                LauncherFrame.getInstance().getCrashReporter().catchError(e, "Impossible de charger l'icon de la news");
            }
        }
        this.newsAuthor = newsAuthor;
        this.newsText = newsText;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Background
        g.setColor(Swinger.getTransparentInstance(ColorAPI.principalColor, 50));
        g.fillRect(0, 0, getWidth(), getHeight());
        //Header
        g.setColor(Swinger.getTransparentInstance(ColorAPI.principalColor, 150));
        g.fillRect(0, 0, getWidth(), 30);
        //Title icon
        if (newsTitleIcon != null)
            g.drawImage(newsTitleIcon.getImage(), 0, 0, null);

        //Author icon
        if (newsAuthorIcon != null)
            g.drawImage(newsAuthorIcon.getImage(), getWidth() - 30, 0, null);
        //Title
        Swinger.activateAntialias(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial Black", Font.PLAIN, 15));
        if (newsTitleIcon != null) {
            g.drawString(newsTitle, 35, 20);
        } else {
            g.drawString(newsTitle, 10, 20);
        }


        //Author
        if (newsAuthor != null) {
            FontMetrics fm = g.getFontMetrics();
            String towrite = "Par " + newsAuthor;
            Rectangle2D stringBounds = fm.getStringBounds(towrite, g);
            if (newsAuthorIcon != null) {
                g.drawString(towrite, getWidth() - (int) stringBounds.getWidth() - 35, 20);
            } else {
                g.drawString(towrite, getWidth() - (int) stringBounds.getWidth() - 10, 20);
            }
        }
        //NewsComponent
        if (newsText != null) {
            g.setFont(new Font("Arial", Font.BOLD, 13));
            FontMetrics fm = g.getFontMetrics();
            StringBuilder tempNewsTextBuilder = new StringBuilder();
            int height = 50;
            for (String newsfragment : newsText.split(" ")) {
                String tempNewsText = tempNewsTextBuilder.toString() + newsfragment;
                Rectangle2D stringBounds = fm.getStringBounds(tempNewsText, g);
                if (stringBounds.getWidth() > getWidth() - 20) {
                    g.drawString(tempNewsTextBuilder.toString(), 10, height);
                    height += stringBounds.getHeight();
                    tempNewsTextBuilder = new StringBuilder();
                }
                tempNewsTextBuilder.append(newsfragment).append(" ");
            }
            if (tempNewsTextBuilder.length() != 0) {
                Rectangle2D stringBounds = fm.getStringBounds(tempNewsTextBuilder.toString(), g);
                g.drawString(tempNewsTextBuilder.toString(), 10, height);
                height += stringBounds.getHeight();
            }
            height += 20;
            if (getHeight() < height) {
                this.setBounds(this.getX(), this.getY(), this.getWidth(), height);
            }
        }
    }
}
