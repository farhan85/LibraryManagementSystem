package org.example.library.gui;

import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import org.example.library.dao.ResourceDao;
import org.example.library.models.Author;
import org.example.library.models.AuthorId;
import org.example.library.models.Email;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuthorTable extends Table<String> {

    private final ResourceDao<AuthorId, Author> authorDao;
    private final TableModel<String> tableModel;
    private final Map<Integer, AuthorId> rowAuthorId;

    public AuthorTable(final ResourceDao<AuthorId, Author> authorDao) {
        super("Id", "First Name", "Last Name", "Email");
        this.authorDao = checkNotNull(authorDao);
        setVisibleRows(10);
        setVisibleColumns(4);
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

    public AuthorId getSelectedAuthor() {
        return rowAuthorId.get(getSelectedRow());
    }

    public int size() {
        return tableModel.getRowCount();
    }

    private Collection<String> authorToList(final Author author) {
        return List.of(
                author.getId().value().substring(0, 6) + "...",
                author.getFirstName(),
                author.getLastName(),
                author.getEmail()
                        .map(Email::value)
                        .orElse(""));
    }
}
