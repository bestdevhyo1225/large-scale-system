DROP TABLE IF EXISTS urls;

CREATE TABLE urls
(
    id          BIGINT NOT NULL AUTO_INCREMENT,
    encoded_url VARCHAR(255),
    long_url    VARCHAR(255),
    created_at  TIMESTAMP DEFAULT NOW(),
    PRIMARY KEY (id)
);
