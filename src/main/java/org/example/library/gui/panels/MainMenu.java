package org.example.library.gui.panels;

import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import org.example.library.gui.MainWindow;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainMenu extends Panel {

    private final MainWindow mainWindow;

    public MainMenu(final MainWindow mainWindow) {
        this.mainWindow = checkNotNull(mainWindow);
        initialize();
    }

    public void initialize() {
        final ActionListBox actionListBox = new ActionListBox();
        actionListBox.addItem("Create Author", mainWindow::displayCreateAuthor);
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
