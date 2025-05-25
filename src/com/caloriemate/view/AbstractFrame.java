package com.caloriemate.view;

import javax.swing.*;

public abstract class AbstractFrame extends JFrame {
    public AbstractFrame(String title) {
        super(title);
        System.out.println("[AbstractFrame] Konstruktor dipanggil untuk: " + title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    protected abstract void initComponents();
}