-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 18. Sep 2018 um 18:45
-- Server-Version: 10.1.13-MariaDB
-- PHP-Version: 7.0.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `caas`
--
CREATE DATABASE `caas` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `caas`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `catalog`
--

CREATE TABLE `catalog` (
  `id` bigint(20) NOT NULL,
  `catalogName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `catalog`
--

INSERT INTO `catalog` (`id`, `catalogName`) VALUES(1, 'Kleidung');
INSERT INTO `catalog` (`id`, `catalogName`) VALUES(2, 'Lebensmittel');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `catalog_product`
--

CREATE TABLE `catalog_product` (
  `Catalog_id` bigint(20) NOT NULL,
  `productList_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_py4pcnvo3te8e1xnmgwoav90d` (`productList_id`),
  KEY `FKixnf96kdpymmxbrdjup3swe90` (`Catalog_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `catalog_product`
--

INSERT INTO `catalog_product` (`Catalog_id`, `productList_id`) VALUES(1, 1);
INSERT INTO `catalog_product` (`Catalog_id`, `productList_id`) VALUES(1, 3);
INSERT INTO `catalog_product` (`Catalog_id`, `productList_id`) VALUES(1, 4);
INSERT INTO `catalog_product` (`Catalog_id`, `productList_id`) VALUES(2, 2);
INSERT INTO `catalog_product` (`Catalog_id`, `productList_id`) VALUES(2, 5);
INSERT INTO `catalog_product` (`Catalog_id`, `productList_id`) VALUES(2, 6);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES(1);
INSERT INTO `hibernate_sequence` (`next_val`) VALUES(1);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `product`
--

CREATE TABLE `product` (
  `id` bigint(20) NOT NULL,
  `price` double NOT NULL,
  `productName` varchar(255) DEFAULT NULL,
  `taxRate` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `product`
--

INSERT INTO `product` (`id`, `price`, `productName`, `taxRate`) VALUES(1, 25, 'Damen Schuhe', 19);
INSERT INTO `product` (`id`, `price`, `productName`, `taxRate`) VALUES(2, 2.99, 'Erbsen', 7);
INSERT INTO `product` (`id`, `price`, `productName`, `taxRate`) VALUES(3, 27, 'Herren Schuhe', 19);
INSERT INTO `product` (`id`, `price`, `productName`, `taxRate`) VALUES(4, 45, 'Herren Jacke', 19);
INSERT INTO `product` (`id`, `price`, `productName`, `taxRate`) VALUES(5, 1.49, 'Smarties', 7);
INSERT INTO `product` (`id`, `price`, `productName`, `taxRate`) VALUES(6, 1.55, 'Fritz Cola', 7);

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `catalog_product`
--
ALTER TABLE `catalog_product`
  ADD CONSTRAINT `FKgox50modtn7orwu0nm6fqt3fj` FOREIGN KEY (`productList_id`) REFERENCES `product` (`id`),
  ADD CONSTRAINT `FKixnf96kdpymmxbrdjup3swe90` FOREIGN KEY (`Catalog_id`) REFERENCES `catalog` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
