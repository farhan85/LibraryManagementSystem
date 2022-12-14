package org.example.library;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import org.example.library.dao.ResourceDao;
import org.example.library.gui.MainWindow;
import org.example.library.guice.LocalDaoModule;
import org.example.library.models.Author;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Runs the Library Management System application.
 */
public class MainApp {

    private final Screen screen;
    private final WindowBasedTextGUI textGUI;
    private final MainWindow mainWindow;

    @Inject
    private MainApp(final ResourceDao<Author> authorDao) throws IOException {
        checkNotNull(authorDao);

        screen = new DefaultTerminalFactory().createScreen();
        textGUI = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        mainWindow = new MainWindow(authorDao);
    }

    public void run() throws IOException {
        mainWindow.displayMainMenu();
        screen.startScreen();
        textGUI.addWindowAndWait(mainWindow);
    }

    public static void main(final String[] args) throws IOException {
        final Injector injector = Guice.createInjector(new LocalDaoModule());
        injector.getInstance(MainApp.class).run();
    }
}
