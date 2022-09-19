package org.example.library.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.example.library.dao.LocalResourceDao;
import org.example.library.dao.ResourceDao;
import org.example.library.models.Author;

public class LocalDaoModule extends AbstractModule {

    private final TypeLiteral<ResourceDao<Author>> AUTHOR_RESOURCE_DAO = new TypeLiteral<>() {};
    private final TypeLiteral<LocalResourceDao<Author>> AUTHOR_LOCAL_RESOURCE_DAO = new TypeLiteral<>() {};

    @Override
    protected void configure() {
        bind(AUTHOR_RESOURCE_DAO).to(AUTHOR_LOCAL_RESOURCE_DAO);
    }
}
