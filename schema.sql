CREATE DATABASE stocktrading;

CREATE TABLE stocktrading.bankroll (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` varchar(45) NOT NULL,
  `amountUsd` double NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `userId_UNIQUE` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

INSERT INTO stocktrading.bankroll (userId, amountUsd) VALUES ('baldy', 10000.0);

CREATE TABLE stocktrading.positions (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `price` double NOT NULL,
  `symbol` varchar(45) NOT NULL,
  `quantity` double NOT NULL,
  `userId` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
