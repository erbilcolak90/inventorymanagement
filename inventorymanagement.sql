-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Anamakine: 127.0.0.1
-- Üretim Zamanı: 03 Oca 2024, 15:57:04
-- Sunucu sürümü: 10.4.24-MariaDB
-- PHP Sürümü: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET NAMES 'utf8' */;
/*!40101 SET @OLD_CHARACTER_SET_CLIENT= 'utf8' */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS= 'utf8' */;
/*!40101 SET @OLD_COLLATION_CONNECTION= 'utf8_general_ci' */;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(9);

--

CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `create_date` datetime DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `category`
--

INSERT INTO `category` (`id`, `create_date`, `is_deleted`, `update_date`, `name`) VALUES
(1, '2023-12-12 12:52:23', b'0', '2023-12-20 19:52:23', 'giyim'),
(2, '2023-12-13 19:45:30', b'0', '2023-12-20 19:52:30', 'araba'),
(3, '2023-12-15 02:57:35', b'0', '2023-12-20 19:57:35', 'telefon'),
(4, '2023-12-15 07:36:28', b'0', '2023-12-20 19:59:28', 'dekorasyon'),
(5, '2023-12-18 09:18:36', b'0', '2023-12-20 19:59:36', 'hobi'),
(6, '2023-12-18 19:19:36', b'0', '2023-12-20 19:59:36', 'ev gereçleri'),
(7, '2023-12-20 14:43:36', b'0', '2023-12-20 19:59:36', 'mutfak'),
(8, '2023-12-20 14:59:36', b'0', '2023-12-20 19:59:36', 'spor');

--

CREATE TABLE `history` (
  `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `log_level` varchar(255) DEFAULT NULL,
  `log_message` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--

CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` bit(1) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  `critical_level` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  CONSTRAINT `FK1mtsbur82frn64de7balymq9s` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--

INSERT INTO `product` (`id`, `create_date`, `is_deleted`, `update_date`, `category_id`, `critical_level`, `name`, `quantity`) VALUES
(1, '2023-12-20 20:00:27', b'0', '2023-12-20 20:46:54', 1, 2000, 'gömlek', 1000),
(2, '2023-12-20 20:00:43', b'0', '2024-01-02 19:29:37', 2, 200, 'seat', 199),
(3, '2023-12-20 20:01:02', b'0', '2023-12-20 20:01:02', 3, 500, 'iphone', 3800),
(4, '2023-12-20 20:01:21', b'0', '2023-12-29 19:50:26', 4, 40, 'iran halısı', 110),
(5, '2024-01-02 18:59:51', b'0', '2024-01-02 18:59:51', 5, 100, 'android telefon', 5000),
(6, '2024-01-02 18:59:51', b'0', '2024-01-02 18:59:51', 7, 100, 'bıçak seti', 5000),
(7, '2024-01-02 18:59:51', b'0', '2024-01-02 18:59:51', 6, 100, 'takım seti', 5000),
(8, '2024-01-02 18:59:51', b'0', '2024-01-02 18:59:51', 4, 100, 'kilim', 5000);


--

CREATE TABLE `store` (
  `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `create_date` datetime DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--

INSERT INTO `store` (`id`, `create_date`, `is_deleted`, `update_date`, `address`, `city`, `name`, `region`) VALUES
(1, '2023-11-29 23:45:52', b'0', '2023-11-30 00:38:47', 'bahçelievler mahallesi kırlangıç sokak no:14', 'KOCAELI', 'gebze bölgesi', 'MARMARA'),
(2, '2023-11-29 23:46:52', b'0', '2023-11-30 00:40:14', 'gazi bulvarı atakent plaza no:66', 'ANKARA', 'ankara bölge mağazası', 'IC_ANADOLU'),
(3, '2023-11-29 22:16:00', b'0', '2023-11-30 00:41:10', 'meydan caddesi ismet mahallesi no:11', 'SIVAS', 'sivas mağazası', 'IC_ANADOLU'),
(4, '2023-11-29 22:17:00', b'0', '2023-11-30 00:42:38', 'gül sokak no:33', 'ERZURUM', 'erzurum merkez mağazası', 'DOGU_ANADOLU'),
(5, '2023-11-29 22:17:00', b'0', '2023-11-30 00:45:13', 'gül sokak no:35', 'ERZINCAN', 'erzincan merkez mağazası', 'DOGU_ANADOLU'),
(6, '2023-11-29 22:17:00', b'0', '2023-11-29 22:17:00', 'sümbül sokak no:33', 'IZMIR', 'izmir merkez mağazası', 'EGE'),
(7, '2023-11-29 22:17:00', b'0', '2023-11-29 22:17:00', 'lale sokak no:33', 'IZMIR', 'izmir sanayi bölge mağazası', 'EGE'),
(8, '2023-11-28 15:12:37', b'0', '2023-11-29 08:52:15', 'test mahallesi test sokak test plaza', 'ISTANBUL', 'istanbul merkez şube', 'MARMARA');

--

CREATE TABLE `store_products` (
  `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `store_id` int NOT NULL,
  `quantity` int DEFAULT NULL,
  `product_id` int NOT NULL,
  CONSTRAINT `FK7ajkqsjo8mdfh213onp1r0kcm` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--

INSERT INTO `store_products` (`id`, `store_id`, `quantity`, `product_id`) VALUES
(1, 1, 5000, 1),
(2, 2, 100, 2),
(3, 5, 2000, 1),
(4, 6, 1000, 1),
(5, 5, 500, 4),
(6, 6, 201, 3),
(7, 5, 201, 5),
(8, 3, 201, 6);

COMMIT;

/*!40101 SET NAMES 'utf8' */;
/*!40101 SET @OLD_CHARACTER_SET_CLIENT= 'utf8' */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS= 'utf8' */;
/*!40101 SET @OLD_COLLATION_CONNECTION= 'utf8' */;