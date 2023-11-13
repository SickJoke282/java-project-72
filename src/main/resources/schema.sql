DROP TABLE IF EXISTS url_checks;
DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
	id SERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	created_at TIMESTAMP NOT NULL
);

CREATE TABLE url_checks (
	id SERIAL PRIMARY KEY,
	url_id INT NOT NULL,
	status_code INT NOT NULL,
	title VARCHAR(500),
	h1 VARCHAR(500),
	description VARCHAR(500),
	created_at TIMESTAMP NOT NULL,
	FOREIGN KEY(url_id) REFERENCES urls(id)
);