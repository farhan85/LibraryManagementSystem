package org.example.library.gui.panels;

import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;

public class MainMenu extends Panel {

    public MainMenu() {
        initialize();
    }

    public void initialize() {
        final ActionListBox actionListBox = new ActionListBox();
        actionListBox.addItem("Create Author", () -> {});
        actionListBox.addItem("List Authors", () -> {});
        actionListBox.addItem("Update Author", () -> {});
        actionListBox.addItem("Delete Author", () -> {});
        actionListBox.addItem("Exit", () -> System.exit(0));

        setLayoutManager(new LinearLayout(Direction.VERTICAL));
        addComponent(new Label("Make a selection:"));
        addComponent(new EmptySpace());
        addComponent(actionListBox);
    }
}
