package org.example.library.gui.panels;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import org.example.library.dao.ResourceDao;
import org.example.library.gui.MainWindow;
import org.example.library.models.Author;
import org.example.library.models.ImmutableAuthor;

import java.util.UUID;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

public class CreateAuthor extends Panel {

    private final MainWindow mainWindow;
    private final ResourceDao<Author> authorDao;
    private final TextBox firstNameTextBox;
    private final TextBox lastNameTextBox;

    public CreateAuthor(final MainWindow mainWindow, final ResourceDao<Author> authorDao) {
        this.mainWindow = checkNotNull(mainWindow);
        this.authorDao = checkNotNull(authorDao);

        firstNameTextBox = new TextBox().setValidationPattern(Pattern.compile("[a-zA-Z ]*"));
        lastNameTextBox = new TextBox().setValidationPattern(Pattern.compile("[a-zA-Z ]*"));

        setLayoutManager(new GridLayout(2));
        addComponent(new Label("First name"));
        addComponent(firstNameTextBox);
        addComponent(new Label("Last Name"));
        addComponent(lastNameTextBox);
        addComponent(new EmptySpace());
        addComponent(new EmptySpace());
        addComponent(new Button("Create", this::createAuthor));
        addComponent(new Button("Back", mainWindow::displayMainMenu));
    }

    private void createAuthor() {
        final String firstName = firstNameTextBox.getText();
        final String lastName = lastNameTextBox.getText();

        final Author author = ImmutableAuthor.builder()
                .withId(UUID.randomUUID())
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();
        authorDao.create(author);
        firstNameTextBox.setText("");
        lastNameTextBox.setText("");
    }
}
