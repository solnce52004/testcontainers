package com.talks.container;

import org.testcontainers.containers.PostgreSQLContainer;

public class CitizenPostgresqlContainer extends PostgreSQLContainer<CitizenPostgresqlContainer> {

    private final static String DOCKER_IMAGE_NAME = "14.4-alpine";
    private static volatile CitizenPostgresqlContainer container;

    private CitizenPostgresqlContainer() {
        super(DOCKER_IMAGE_NAME);
    }

    public CitizenPostgresqlContainer getInstance() {
        if (container == null) {
            synchronized (CitizenPostgresqlContainer.class) {
                container = new CitizenPostgresqlContainer();
            }
        }

        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        super.stop();
    }
}
