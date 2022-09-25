package org.example.library.gui.panels;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import org.example.library.dao.ResourceDao;
import org.example.library.gui.AuthorTable;
import org.example.library.gui.MainWindow;
import org.example.library.models.Author;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class DeleteAuthor extends Panel {

    private final MainWindow mainWindow;
    private final ResourceDao<Author> authorDao;
    private final AuthorTable authorTable;

    public DeleteAuthor(final MainWindow mainWindow, final ResourceDao<Author> authorDao) {
        this.mainWindow = checkNotNull(mainWindow);
        this.authorDao = checkNotNull(authorDao);

        authorTable = new AuthorTable(authorDao);
        authorTable.setSelectAction(this::deleteAuthor);

        setLayoutManager(new LinearLayout(Direction.VERTICAL));
        addComponent(authorTable);
        addComponent(new Button("Back", mainWindow::displayMainMenu));
    }

    public void refresh() {
        authorTable.refresh();
    }

    private void deleteAuthor() {
        int selectedRow = authorTable.getSelectedRow();
        final UUID authorId = authorTable.getSelectedAuthor();
        authorDao.delete(authorId);
        authorTable.refresh();

        selectedRow = Math.min(selectedRow, authorTable.size() - 1);
        selectedRow = Math.max(selectedRow, 0);
        authorTable.setSelectedRow(selectedRow);
    }
}
