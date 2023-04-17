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
import org.example.library.models.Email;
import org.example.library.models.ImmutableAuthor;
import org.example.library.models.ImmutableAuthorId;

import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateAuthor extends Panel {

    private final MainWindow mainWindow;
    private final ResourceDao<AuthorId, Author> authorDao;
    private final Label authorIdLabel;
    private final TextBox firstNameTextBox;
    private final TextBox lastNameTextBox;
    private final TextBox emailTextBox;

    public UpdateAuthor(final MainWindow mainWindow, final ResourceDao<AuthorId, Author> authorDao) {
        this.mainWindow = checkNotNull(mainWindow);
        this.authorDao = checkNotNull(authorDao);

        firstNameTextBox = new TextBox(new TerminalSize(40, 1)).setValidationPattern(Pattern.compile("[a-zA-Z ]*"));
        lastNameTextBox = new TextBox(new TerminalSize(40, 1)).setValidationPattern(Pattern.compile("[a-zA-Z ]*"));
        emailTextBox = new TextBox(new TerminalSize(40, 1));
        authorIdLabel = new Label("");

        setLayoutManager(new GridLayout(2));
        addComponent(new Label("ID"));
        addComponent(authorIdLabel);
        addComponent(new Label("First name"));
        addComponent(firstNameTextBox);
        addComponent(new Label("Last Name"));
        addComponent(lastNameTextBox);
        addComponent(new Label("Email"));
        addComponent(emailTextBox);
        addComponent(new EmptySpace());
        addComponent(new EmptySpace());

        addComponent(new EmptySpace());
        addComponent(new Button("Update", this::updateAuthor));
        addComponent(new EmptySpace());
        addComponent(new Button("Back (author selection)", mainWindow::displayUpdateAuthorSelector));
        addComponent(new EmptySpace());
        addComponent(new Button("Back (main menu)", mainWindow::displayMainMenu));
    }

    public void displayAuthor(final AuthorId authorId) {
        Optional.ofNullable(getAuthor(authorId))
                .ifPresent(this::updateAuthorLabels);
    }

    private Author updateAuthorLabels(final Author author) {
        authorIdLabel.setText(author.getId().value());
        firstNameTextBox.setText(author.getFirstName());
        lastNameTextBox.setText(author.getLastName());
        author.getEmail()
                .map(Email::value)
                .ifPresent(emailTextBox::setText);
        return author;
    }

    private void updateAuthor() {
        Optional.of(authorIdLabel.getText())
                .map(UUID::fromString)
                .map(ImmutableAuthorId::of)
                .map(this::getAuthor)
                .map(this::toUpdatedAuthor)
                .ifPresent(this::saveChanges);
    }

    private Author getAuthor(final AuthorId authorId) {
        return authorDao.get(authorId)
                .orElseGet(() -> {
                    MessageDialog.showMessageDialog(mainWindow.getTextGUI(), "Error", "Author not found");
                    return null;
                });
    }

    private Author toUpdatedAuthor(final Author originalAuthor) {
        final String firstName = firstNameTextBox.getText();
        final String lastName = lastNameTextBox.getText();
        final String email = emailTextBox.getText();
        try {
            final ImmutableAuthor.Builder builder = ImmutableAuthor.builder()
                    .from(originalAuthor)
                    .withFirstName(firstName)
                    .withLastName(lastName);
            Optional.ofNullable(Strings.emptyToNull(email))
                    .ifPresent(builder::withEmail);
            return builder.build();
        } catch (final IllegalArgumentException e) {
            MessageDialog.showMessageDialog(mainWindow.getTextGUI(), "Error", e.getMessage());
            return null;
        }
    }

    private void saveChanges(final Author author) {
        try {
            authorDao.update(author);
            MessageDialog.showMessageDialog(mainWindow.getTextGUI(), "Success", "Updated Author");
        } catch (final ConcurrentModificationException e) {
            MessageDialog.showMessageDialog(mainWindow.getTextGUI(), "Error", "Author was updated by another process.\nTry again");
        }
    }
}
