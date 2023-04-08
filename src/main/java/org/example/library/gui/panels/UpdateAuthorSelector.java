package org.example.library.gui.panels;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import org.example.library.dao.ResourceDao;
import org.example.library.gui.AuthorTable;
import org.example.library.gui.MainWindow;
import org.example.library.models.Author;
import org.example.library.models.AuthorId;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateAuthorSelector extends Panel {

    private final MainWindow mainWindow;
    private final ResourceDao<AuthorId, Author> authorDao;
    private final AuthorTable authorTable;

    public UpdateAuthorSelector(final MainWindow mainWindow, final ResourceDao<AuthorId, Author> authorDao) {
        this.mainWindow = checkNotNull(mainWindow);
        this.authorDao = checkNotNull(authorDao);

        authorTable = new AuthorTable(authorDao);
        authorTable.setSelectAction(this::displayAuthor);

        setLayoutManager(new LinearLayout(Direction.VERTICAL));
        addComponent(authorTable);
        addComponent(new Button("Back", mainWindow::displayMainMenu));
    }

    public void refresh() {
        authorTable.refresh();
    }

    private void displayAuthor() {
        final AuthorId authorId = authorTable.getSelectedAuthor();
        mainWindow.displayUpdateAuthor(authorId);
    }
}
