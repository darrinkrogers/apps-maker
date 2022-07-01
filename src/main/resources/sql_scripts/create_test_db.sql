-- Create the tables & relationships
CREATE TABLE IF NOT EXISTS movie (
    movie_id INT AUTO_INCREMENT NOT NULL,
    title VARCHAR(255) NOT NULL,
    summary VARCHAR(4000),
  PRIMARY KEY (movie_id)
);

CREATE TABLE IF NOT EXISTS actor (
    actor_id INT AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(30),
  PRIMARY KEY (actor_id)
);

CREATE TABLE IF NOT EXISTS col_type (
    type_id BIGINT AUTO_INCREMENT NOT NULL,
    type_varchar VARCHAR(30),
    type_bit BIT,
    type_tinyint TINYINT,
    type_boolean BOOLEAN,
    type_smallint SMALLINT(30),
    type_medium_int MEDIUMINT(30),
    type_integer INTEGER(30),
    type_float FLOAT(5,2),
    type_double DOUBLE(7,2),
    type_decimal DECIMAL(5,2),
    type_date DATE,
  PRIMARY KEY (type_id)
);

-- Populate all tables in order of dependency

insert into movie(title, summary) values('Rocky', 'An underdog fighter is given shot at the title');
insert into movie(title, summary) values('American Werefolf in London', 'Two American teenagers get attacked by a Werewolf on vacation');
insert into movie(title, summary) values('Casablanca', 'Humphrey Bogart and Ingrid Bergman in a classic romance. Considered one of the best movies of all time.');
insert into movie(title, summary) values('The Outsiders', 'They just wanted to belong');
insert into movie(title, summary) values('Body of Evidence', 'Steamy movie with Madonna');
insert into movie(title, summary) values('Dark Knight', 'Best Batman movie of all time');
insert into movie(title, summary) values('Halloween', 'The original by John Carpenter. Very low budget, fake leaves.');
insert into movie(title, summary) values('Escape from Alcatraz', 'Clint Eastwood, based on a true story.');
insert into movie(title, summary) values('Point Break', '100% pure adrenaline !!!');
insert into movie(title, summary) values('Stir Crazy', 'Great comedy from early 1980');

insert into actor(first_name, last_name) values('Tom', 'Cruise');
insert into actor(first_name, last_name) values('Angie', 'Dickinson');
insert into actor(first_name, last_name) values('Jack', 'Nicholson');
insert into actor(first_name, last_name) values('Gene', 'Hackman');
insert into actor(first_name, last_name) values('Denzel', 'Washington');
