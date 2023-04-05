package org.example.library.gui.panels;

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
import org.example.library.models.ImmutableAuthor;

import java.util.ConcurrentModificationException;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateAuthor extends Panel {

    private final MainWindow mainWindow;
    private final ResourceDao<Author> authorDao;
    private final Label authorIdLabel;
    private final TextBox firstNameTextBox;
    private final TextBox lastNameTextBox;

    public UpdateAuthor(final MainWindow mainWindow, final ResourceDao<Author> authorDao) {
        this.mainWindow = checkNotNull(mainWindow);
        this.authorDao = checkNotNull(authorDao);

        firstNameTextBox = new TextBox().setValidationPattern(Pattern.compile("[a-zA-Z ]*"));
        lastNameTextBox = new TextBox().setValidationPattern(Pattern.compile("[a-zA-Z ]*"));
        authorIdLabel = new Label("");

        setLayoutManager(new GridLayout(2));
        addComponent(new Label("ID"));
        addComponent(authorIdLabel);
        addComponent(new Label("First name"));
        addComponent(firstNameTextBox);
        addComponent(new Label("Last Name"));
        addComponent(lastNameTextBox);
        addComponent(new EmptySpace());
        addComponent(new EmptySpace());

        addComponent(new EmptySpace());
        addComponent(new Button("Update", this::updateAuthor));
        addComponent(new EmptySpace());
        addComponent(new Button("Back (author selection)", mainWindow::displayUpdateAuthorSelector));
        addComponent(new EmptySpace());
        addComponent(new Button("Back (main menu)", mainWindow::displayMainMenu));
    }

    public void displayAuthor(final UUID authorId) {
        final Author author = authorDao.get(authorId).orElseThrow();
        authorIdLabel.setText(author.getId().toString());
        firstNameTextBox.setText(author.getFirstName());
        lastNameTextBox.setText(author.getLastName());
    }

    private void updateAuthor() {
        final UUID authorId = UUID.fromString(authorIdLabel.getText());
        authorDao.get(authorId)
                .map(this::toUpdatedAuthor)
                .map(this::saveChanges)
                .orElseGet(() -> {
                    MessageDialog.showMessageDialog(mainWindow.getTextGUI(), "Error", "Author not found");
                    return null;
                });
    }

    private Author toUpdatedAuthor(final Author originalAuthor) {
        final String firstName = firstNameTextBox.getText();
        final String lastName = lastNameTextBox.getText();
        return ImmutableAuthor.builder()
                .from(originalAuthor)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();
    }

    private Author saveChanges(final Author author) {
        try {
            authorDao.update(author);
            MessageDialog.showMessageDialog(mainWindow.getTextGUI(), "Success", "Updated Author");
        } catch (final ConcurrentModificationException e) {
            MessageDialog.showMessageDialog(mainWindow.getTextGUI(), "Error", "Author was updated by another process.\nTry again");
        }
        return author;
    }
}
