package org.example.library.gui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Window;
import org.example.library.gui.panels.MainMenu;

import java.util.List;

public class MainWindow extends BasicWindow {

    private final MainMenu mainMenu;

    public MainWindow() {
        super("Library Management System");
        setHints(List.of(Window.Hint.CENTERED));
        setFixedSize(new TerminalSize(50, 15));

        this.mainMenu = new MainMenu();
    }

    public void displayMainMenu() {
        setComponent(mainMenu);
    }
}
