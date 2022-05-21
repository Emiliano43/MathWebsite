package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

@SpringBootApplication
public class TheMathWebsite {
    public static void main(String[] args) {
        SpringApplication.run(TheMathWebsite.class, args);
        System.setProperty("java.awt.headless", "false");
        /*SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Plot viewer");
            frame.setVisible(false);
        });*/

    }
}
