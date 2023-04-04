package org.example.library.gui.panels;

import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import org.example.library.dao.ResourceDao;
import org.example.library.gui.MainWindow;
import org.example.library.models.Author;
import org.example.library.models.ImmutableAuthor;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainMenu extends Panel {

    private final MainWindow mainWindow;
    private final ResourceDao<Author> authorDao;

    public MainMenu(final MainWindow mainWindow, final ResourceDao<Author> authorDao) {
        this.mainWindow = checkNotNull(mainWindow);
        this.authorDao = checkNotNull(authorDao);
        initialize();
    }

    public void initialize() {
        final ActionListBox actionListBox = new ActionListBox();
        actionListBox.addItem("Create Author", mainWindow::displayCreateAuthor);
        actionListBox.addItem("List Authors", mainWindow::displayListAuthors);
        actionListBox.addItem("Update Author", mainWindow::displayUpdateAuthorSelector);
        actionListBox.addItem("Delete Author", mainWindow::displayDeleteAuthor);
        actionListBox.addItem("Load test data", this::loadTestData);
        actionListBox.addItem("Exit", () -> System.exit(0));

        setLayoutManager(new LinearLayout(Direction.VERTICAL));
        addComponent(new Label("Make a selection:"));
        addComponent(new EmptySpace());
        addComponent(actionListBox);
    }

    private void loadTestData() {
        authorDao.create(createAuthor("John", "Doe"));
        authorDao.create(createAuthor("Fred", "Smith"));
        authorDao.create(createAuthor("Jane", "Doe"));
        authorDao.create(createAuthor("Alex", "Miller"));
        MessageDialog.showMessageDialog(mainWindow.getTextGUI(), "Info", "Test data loaded",
                MessageDialogButton.Close);
    }

    private static Author createAuthor(final String firstName, final String lastName) {
        return ImmutableAuthor.builder()
                .withId(UUID.randomUUID())
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();
    }
}
