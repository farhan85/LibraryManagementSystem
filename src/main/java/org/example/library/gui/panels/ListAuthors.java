package org.example.library.gui.panels;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import org.example.library.dao.ResourceDao;
import org.example.library.gui.MainWindow;
import org.example.library.models.Author;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ListAuthors extends Panel {

    private final MainWindow mainWindow;
    private final ResourceDao<Author> authorDao;
    private final TableModel<String> tableModel;

    public ListAuthors(final MainWindow mainWindow, final ResourceDao<Author> authorDao) {
        this.mainWindow = checkNotNull(mainWindow);
        this.authorDao = checkNotNull(authorDao);

        final Table<String> table = new Table<>("Id", "First Name", "Last Name");
        table.setVisibleRows(10);
        tableModel = table.getTableModel();

        setLayoutManager(new LinearLayout(Direction.VERTICAL));
        addComponent(table);
        addComponent(new Button("Back", mainWindow::displayMainMenu));
    }

    public void refresh() {
        tableModel.clear();
        authorDao.scan(author -> tableModel.addRow(authorToList(author)));
    }

    private Collection<String> authorToList(final Author author) {
        return List.of(
                author.getId().toString().substring(0, 6) + "...",
                author.getFirstName(),
                author.getLastName());
    }
}
