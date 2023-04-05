package org.example.library;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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

    @Parameter(names = { "-region" }, description = "AWS Region name")
    private String region;

    @Parameter(names = { "-profile" }, description = "Profile name of AWS credentials to use")
    private String profile;

    private Regions getRegion() {
        return Stream.of(
                region,
                System.getenv("AWS_REGION"),
                System.getenv("AWS_DEFAULT_REGION"))
                .filter(Objects::nonNull)
                .findFirst()
                .map(Regions::fromName)
                .orElseThrow(() -> new RuntimeException("No AWS region specified"));
    }

    private AWSCredentialsProvider getCredentialsProvider() {
        return Optional.ofNullable(profile)
                .map(ProfileCredentialsProvider::new)
                .map(AWSCredentialsProvider.class::cast)
                .orElse(DefaultAWSCredentialsProviderChain.getInstance());
    }

    private void run() throws IOException {
        final Injector injector;
        checkArgument(useInMemoryDb || useAwsDdb, "Must specify which DDB type to use");

        if (useInMemoryDb) {
            injector = Guice.createInjector(new LocalDaoModule());
        } else {
            injector = Guice.createInjector(
                    new AwsClientModule(getRegion(), getCredentialsProvider()),
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
