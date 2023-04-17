package org.example.library.gui.panels;

import com.google.common.base.Strings;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import org.example.library.dao.ResourceDao;
import org.example.library.gui.MainWindow;
import org.example.library.models.Author;
import org.example.library.models.AuthorId;
import org.example.library.models.ImmutableAuthor;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

public class CreateAuthor extends Panel {

    private final MainWindow mainWindow;
    private final ResourceDao<AuthorId, Author> authorDao;
    private final TextBox firstNameTextBox;
    private final TextBox lastNameTextBox;
    private final TextBox emailTextBox;

    public CreateAuthor(final MainWindow mainWindow, final ResourceDao<AuthorId, Author> authorDao) {
        this.mainWindow = checkNotNull(mainWindow);
        this.authorDao = checkNotNull(authorDao);

        firstNameTextBox = new TextBox(new TerminalSize(40, 1)).setValidationPattern(Pattern.compile("[a-zA-Z ]*"));
        lastNameTextBox = new TextBox(new TerminalSize(40, 1)).setValidationPattern(Pattern.compile("[a-zA-Z ]*"));
        emailTextBox = new TextBox(new TerminalSize(40, 1));

        setLayoutManager(new GridLayout(2));
        addComponent(new Label("First name"));
        addComponent(firstNameTextBox);
        addComponent(new Label("Last Name"));
        addComponent(lastNameTextBox);
        addComponent(new Label("Email"));
        addComponent(emailTextBox);
        addComponent(new EmptySpace());
        addComponent(new EmptySpace());
        addComponent(new Button("Create", this::createAuthor));
        addComponent(new Button("Back", mainWindow::displayMainMenu));
    }

    private void createAuthor() {
        final String firstName = firstNameTextBox.getText();
        final String lastName = lastNameTextBox.getText();
        final String email = emailTextBox.getText();

        try {
            final ImmutableAuthor.Builder builder = ImmutableAuthor.builder()
                    .withId(UUID.randomUUID())
                    .withFirstName(firstName)
                    .withLastName(lastName);
            Optional.ofNullable(Strings.emptyToNull(email))
                    .ifPresent(builder::withEmail);
            authorDao.create(builder.build());
            firstNameTextBox.setText("");
            lastNameTextBox.setText("");
            emailTextBox.setText("");
        } catch (final IllegalArgumentException e) {
            MessageDialog.showMessageDialog(mainWindow.getTextGUI(), "Error", e.getMessage());
        }
    }
}
