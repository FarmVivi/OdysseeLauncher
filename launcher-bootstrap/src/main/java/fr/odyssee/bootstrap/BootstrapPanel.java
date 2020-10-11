package fr.odyssee.bootstrap;

import fr.odyssee.common.components.RoundProgressBarComponent;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

import static fr.theshark34.swinger.Swinger.getResource;

public class BootstrapPanel extends JPanel implements SwingerEventListener {
    private final RoundProgressBarComponent logoProgressBar;
    private final JLabel info;
    private final JLabel loading;
    private final Icon loading_2_red;
    private final Icon loading_2_red_fast;

    public BootstrapPanel(BootstrapFrame tempmain) {
        this.setLayout(null);
        URL loading_2_red_url = tempmain.getClass().getResource("/loading_slow.gif");
        loading_2_red = new ImageIcon(loading_2_red_url);

        URL loading_2_red_fast_url = tempmain.getClass().getResource("/loading_fast.gif");
        loading_2_red_fast = new ImageIcon(loading_2_red_fast_url);

        loading = new JLabel(loading_2_red);
        loading.setBounds(tempmain.getWidth() / 2 - loading_2_red.getIconWidth() / 2, 40, loading_2_red.getIconWidth(), loading_2_red.getIconHeight());
        this.add(loading);

        info = new JLabel("Connexion...", JLabel.CENTER);
        info.setBounds(0, 220, tempmain.getWidth(), 60);
        info.setForeground(Color.WHITE);
        info.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(info);

        JLabel infodev = new JLabel("Mode développeur", JLabel.CENTER);
        infodev.setBounds(0, 0, tempmain.getWidth(), 17);
        infodev.setForeground(Color.ORANGE);
        infodev.setFont(new Font("Arial", Font.BOLD, 17));
        if (Main.dev) {
            this.add(infodev);
        }

        URL logoPath = tempmain.getClass().getResource("/Splash.png");
        JLabel logo = new JLabel("", new ImageIcon(logoPath), JLabel.CENTER);
        logo.setBounds(BootstrapFrame.interval, BootstrapFrame.interval, getResource("Splash.png").getWidth(), getResource("Splash.png").getHeight());
        this.add(logo);

        logoProgressBar = new RoundProgressBarComponent(new Color(240, 81, 37));
        logoProgressBar.setBounds(BootstrapFrame.interval + 4, BootstrapFrame.interval + 4, getResource("Splash.png").getWidth() - 8, getResource("Splash.png").getHeight() - 8);
        this.add(logoProgressBar);
    }

    @Override
    public void onEvent(SwingerEvent e) {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public Icon getLoading_2_red() {
        return loading_2_red;
    }

    public Icon getLoading_2_red_fast() {
        return loading_2_red_fast;
    }

    public JLabel getLoading() {
        return loading;
    }

    public JLabel getInfo() {
        return info;
    }

    public RoundProgressBarComponent getLogoProgressBar() {
        return logoProgressBar;
    }
}