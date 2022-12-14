package org.example.library.gui;

import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import org.example.library.dao.ResourceDao;
import org.example.library.models.Author;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuthorTable extends Table<String> {

    private final ResourceDao<Author> authorDao;
    private final TableModel<String> tableModel;
    private final Map<Integer, UUID> rowAuthorId;

    public AuthorTable(final ResourceDao<Author> authorDao) {
        super("Id", "First Name", "Last Name");
        this.authorDao = checkNotNull(authorDao);
        setVisibleRows(10);
        setVisibleColumns(3);
        tableModel = getTableModel();
        rowAuthorId = new HashMap<>();
    }

    public void refresh() {
        tableModel.clear();
        rowAuthorId.clear();
        authorDao.scan(author -> {
            tableModel.addRow(authorToList(author));
            rowAuthorId.put(tableModel.getRowCount() - 1, author.getId());
        });
    }

    public UUID getSelectedAuthor() {
        return rowAuthorId.get(getSelectedRow());
    }

    public int size() {
        return tableModel.getRowCount();
    }

    private Collection<String> authorToList(final Author author) {
        return List.of(
                author.getId().toString().substring(0, 6) + "...",
                author.getFirstName(),
                author.getLastName());
    }
}
