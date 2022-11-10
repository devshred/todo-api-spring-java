CREATE TABLE todo_entity
(
    id UUID DEFAULT GEN_RANDOM_UUID() NOT NULL,
    done BOOLEAN,
    text VARCHAR(255),
    PRIMARY KEY (id)
);