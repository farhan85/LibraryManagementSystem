package org.example.library.gui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Window;
import org.example.library.dao.ResourceDao;
import org.example.library.gui.panels.CreateAuthor;
import org.example.library.gui.panels.DeleteAuthor;
import org.example.library.gui.panels.ListAuthors;
import org.example.library.gui.panels.MainMenu;
import org.example.library.gui.panels.UpdateAuthor;
import org.example.library.gui.panels.UpdateAuthorSelector;
import org.example.library.models.Author;
import org.example.library.models.AuthorId;

import java.util.List;

public class MainWindow extends BasicWindow {

    private final MainMenu mainMenu;
    private final CreateAuthor createAuthor;
    private final ListAuthors listAuthors;
    private final DeleteAuthor deleteAuthor;
    private final UpdateAuthorSelector updateAuthorSelector;
    private final UpdateAuthor updateAuthor;

    public MainWindow(final ResourceDao<AuthorId, Author> authorDao) {
        super("Library Management System");
        setHints(List.of(Window.Hint.CENTERED));
        setFixedSize(new TerminalSize(60, 15));

        this.mainMenu = new MainMenu(this, authorDao);
        this.createAuthor = new CreateAuthor(this, authorDao);
        this.listAuthors = new ListAuthors(this, authorDao);
        this.deleteAuthor = new DeleteAuthor(this, authorDao);
        this.updateAuthorSelector = new UpdateAuthorSelector(this, authorDao);
        this.updateAuthor = new UpdateAuthor(this, authorDao);
    }

    public void displayMainMenu() {
        setComponent(mainMenu);
    }

    public void displayCreateAuthor() {
        setComponent(createAuthor);
    }

    public void displayListAuthors() {
        setComponent(listAuthors);
        listAuthors.refresh();
    }

    public void displayUpdateAuthorSelector() {
        setComponent(updateAuthorSelector);
        updateAuthorSelector.refresh();
    }

    public void displayUpdateAuthor(final AuthorId authorId) {
        setComponent(updateAuthor);
        updateAuthor.displayAuthor(authorId);
    }

    public void displayDeleteAuthor() {
        setComponent(deleteAuthor);
        deleteAuthor.refresh();
    }

}
