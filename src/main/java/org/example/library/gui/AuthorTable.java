package org.example.library.gui;

import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import org.example.library.dao.ResourceDao;
import org.example.library.models.Author;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuthorTable extends Table<String> {

    private final ResourceDao<Author> authorDao;
    private final TableModel<String> tableModel;

    public AuthorTable(final ResourceDao<Author> authorDao) {
        super("Id", "First Name", "Last Name");
        this.authorDao = checkNotNull(authorDao);
        setVisibleRows(10);
        setVisibleColumns(3);
        tableModel = getTableModel();
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
