DROP TABLE IF EXISTS movie;
CREATE TABLE movie As SELECT * FROM CSVREAD('classpath:movie.csv')