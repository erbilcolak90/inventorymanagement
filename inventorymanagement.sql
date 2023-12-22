-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Anamakine: mysqldb
-- Üretim Zamanı: 20 Ara 2023, 20:50:29
-- Sunucu sürümü: 8.2.0
-- PHP Sürümü: 8.2.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET NAMES 'utf8' */;
/*!40101 SET @OLD_CHARACTER_SET_CLIENT= 'utf8' */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS= 'utf8' */;
/*!40101 SET @OLD_COLLATION_CONNECTION= 'utf8_general_ci' */;


--
-- Veritabanı: `inventorymanagement`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `category`
--

CREATE TABLE `category` (
  `id` int NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `category`
--

INSERT INTO `category` (`id`, `create_date`, `is_deleted`, `update_date`, `name`) VALUES
(1, '2023-12-20 19:52:23', b'0', '2023-12-20 19:52:23', 'giyim'),
(2, '2023-12-20 19:52:30', b'0', '2023-12-20 19:52:30', 'araba'),
(3, '2023-12-20 19:57:35', b'0', '2023-12-20 19:57:35', 'telefon'),
(4, '2023-12-20 19:59:28', b'0', '2023-12-20 19:59:28', 'dekorasyon'),
(5, '2023-12-20 19:59:36', b'0', '2023-12-20 19:59:36', 'hobi');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(1);

COMMIT;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `history`
--

CREATE TABLE `history` (
  `id` int NOT NULL,
  `log_level` varchar(255) DEFAULT NULL,
  `log_message` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `history`
--

INSERT INTO `history` (`id`, `log_level`, `log_message`) VALUES
(1, 'INFO', 'CREATE_CATEGORY Category Id : 1'),
(2, 'INFO', 'CREATE_CATEGORY Category Id : 2'),
(3, 'INFO', 'CREATE_CATEGORY Category Id : 3'),
(4, 'INFO', 'CREATE_CATEGORY Category Id : 4'),
(5, 'INFO', 'CREATE_CATEGORY Category Id : 5'),
(6, 'INFO', 'CREATE_PRODUCT Product Id : 6'),
(7, 'INFO', 'CREATE_PRODUCT Product Id : 7'),
(8, 'INFO', 'CREATE_PRODUCT Product Id : 8'),
(9, 'INFO', 'CREATE_PRODUCT Product Id : 9'),
(10, 'INFO', 'CREATE_STORE  Store Id : 10'),
(11, 'INFO', 'CREATE_STORE  Store Id : 11'),
(12, 'INFO', 'CREATE_STORE  Store Id : 12'),
(13, 'INFO', 'ADD_PRODUCT_TO_STORE 5000 pieces of the product Id : 6 have been added to store Id : 10'),
(14, 'INFO', 'ADD_PRODUCT_TO_STORE 2000 pieces of the product Id : 6 have been added to store Id : 11'),
(15, 'INFO', 'ADD_PRODUCT_TO_STORE 1000 pieces of the product Id : 6 have been added to store Id : 12'),
(16, 'INFO', 'ADD_PRODUCT_TO_STORE 100 pieces of the product Id : 7 have been added to store Id : 10');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `product`
--

CREATE TABLE `product` (
  `id` int NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  `critical_level` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `product`
--

INSERT INTO `product` (`id`, `create_date`, `is_deleted`, `update_date`, `category_id`, `critical_level`, `name`, `quantity`) VALUES
(6, '2023-12-20 20:00:27', b'0', '2023-12-20 20:46:54', 1, 2000, 'gömlek', 1000),
(7, '2023-12-20 20:00:43', b'0', '2023-12-20 20:47:13', 2, 200, 'seat', 400),
(8, '2023-12-20 20:01:02', b'0', '2023-12-20 20:01:02', 3, 500, 'iphone', 3800),
(9, '2023-12-20 20:01:21', b'0', '2023-12-20 20:01:21', 4, 40, 'iran halısı', 90);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `store`
--

CREATE TABLE `store` (
  `id` int NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `store`
--

INSERT INTO `store` (`id`, `create_date`, `is_deleted`, `update_date`, `address`, `city`, `name`, `region`) VALUES
(1, '2023-11-29 23:45:52', 0, '2023-11-30 00:38:47', 'bahçelievler mahallesi kırlangıç sokak no:14', 'KOCAELI', 'gebze bölgesi', 'MARMARA'),
(2, '2023-11-29 23:46:52', 0, '2023-11-30 00:40:14', 'gazi bulvarı atakent plaza no:66', 'ANKARA', 'ankara bölge mağazası', 'IC_ANADOLU'),
(3, '2023-11-29 22:16:00', 0, '2023-11-30 00:41:10', 'meydan caddesi ismet mahallesi no:11', 'SIVAS', 'sivas mağazası', 'IC_ANADOLU'),
(4, '2023-11-29 22:17:00', 0, '2023-11-30 00:42:38', 'gül sokak no:33', 'ERZURUM', 'erzurum merkez mağazası', 'DOGU_ANADOLU'),
(5, '2023-11-29 22:17:00', 0, '2023-11-30 00:45:13', 'gül sokak no:35', 'ERZINCAN', 'erzincan merkez mağazası', 'DOGU_ANADOLU'),
(6, '2023-11-29 22:17:00', 0, '2023-11-29 22:17:00', 'sümbül sokak no:33', 'IZMIR', 'izmir merkez mağazası', 'EGE'),
(7, '2023-11-29 22:17:00', 0, '2023-11-29 22:17:00', 'lale sokak no:33', 'IZMIR', 'izmir sanayi bölge mağazası', 'EGE'),
(8, '2023-11-28 15:12:37', 0, '2023-11-29 08:52:15', 'test mahallesi test sokak test plaza', 'ISTANBUL', 'istanbul merkez şube', 'MARMARA'),
(9, '2023-11-28 15:12:50', 0, '2023-11-28 18:23:59', 'test mahallesi test sokak test plaza', 'EDIRNE', 'edirne merkez şube', 'MARMARA'),
(10, '2023-12-20 20:45:08', 0, '2023-12-20 20:47:13', 'gül sokak no:!', 'ADANA', 'akdeniz merkez depo', 'AKDENIZ'),
(11, '2023-12-20 20:45:31', 0, '2023-12-20 20:46:40', 'gül sokak no:1', 'ANKARA', 'ankara merkez depo', 'IC_ANADOLU'),
(12, '2023-12-20 20:45:47', 0, '2023-12-20 20:46:54', 'gül sokak no:1', 'CANKIRI', 'cankiri merkez depo', 'IC_ANADOLU'),
(13, '2023-11-29 22:17:00', 0, '2023-11-29 22:17:00', 'menekşe sokak no:33', 'BURDUR', 'burdur merkez mağazası', 'AKDENIZ');
-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `store_products`
--

CREATE TABLE `store_products` (
  `store_id` int NOT NULL,
  `quantity` int DEFAULT NULL,
  `product_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Tablo döküm verisi `store_products`
--

INSERT INTO `store_products` (`store_id`, `quantity`, `product_id`) VALUES
(10, 5000, 6),
(10, 100, 7),
(11, 2000, 6),
(12, 1000, 6);

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `history`
--
ALTER TABLE `history`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `store`
--
ALTER TABLE `store`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `store_products`
--
ALTER TABLE `store_products`
  ADD PRIMARY KEY (`store_id`,`product_id`);

--
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `history`
--
ALTER TABLE `history`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Dökümü yapılmış tablolar için kısıtlamalar
--

--
-- Tablo kısıtlamaları `store_products`
--
ALTER TABLE `store_products`
  ADD CONSTRAINT `FK7ajkqsjo8mdfh213onp1r0kcm` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`);

COMMIT;

/*!40101 SET NAMES 'utf8' */;
/*!40101 SET @OLD_CHARACTER_SET_CLIENT= 'utf8' */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS= 'utf8' */;
/*!40101 SET @OLD_COLLATION_CONNECTION= 'utf8' */;
