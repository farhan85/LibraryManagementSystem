package org.example.library;

import com.amazonaws.regions.Regions;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.example.library.gui.MainScreen;
import org.example.library.guice.AwsClientModule;
import org.example.library.guice.DDBDaoModule;
import org.example.library.guice.LocalDaoModule;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Runs the Library Management System application.
 */
public class MainApp {

    @Parameter(names = { "-memdb" }, description = "Use in-memory DB")
    private boolean useInMemoryDb = false;

    @Parameter(names = { "-ddb" }, description = "Use AWS DDB")
    private boolean useAwsDdb = false;

    @Parameter(names = { "-r", "-region" }, description = "AWS Region name")
    private String region;

    private void run() throws IOException {
        final Injector injector;
        checkArgument(useInMemoryDb || useAwsDdb, "Must specify which DDB type to use");

        if (useInMemoryDb) {
            injector = Guice.createInjector(new LocalDaoModule());
        } else {
            checkNotNull(region);
            injector = Guice.createInjector(
                    new AwsClientModule(Regions.fromName(region)),
                    new DDBDaoModule());
        }
        injector.getInstance(MainScreen.class).run();
    }

    public static void main(final String[] args) throws IOException {
        MainApp mainApp = new MainApp();
        JCommander.newBuilder()
                .addObject(mainApp)
                .build()
                .parse(args);
        mainApp.run();
    }
}
