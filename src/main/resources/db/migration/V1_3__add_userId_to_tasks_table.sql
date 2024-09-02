ALTER TABLE tasks
    ADD user_id BIGINT,
    ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES users (id);
