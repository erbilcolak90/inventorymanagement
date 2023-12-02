-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Anamakine: 127.0.0.1
-- Üretim Zamanı: 29 Kas 2023, 23:31:58
-- Sunucu sürümü: 10.4.24-MariaDB
-- PHP Sürümü: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

-- settings character sets
/*!40101 SET NAMES 'utf8' */;
/*!40101 SET CHARACTER_SET_CLIENT = 'utf8' */;
/*!40101 SET CHARACTER_SET_RESULTS = 'utf8' */;
/*!40101 SET COLLATION_CONNECTION = 'utf8_general_ci' */;

--
-- Veritabanı: `inventorymanagement`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `category`
--

CREATE TABLE `category` (
  `id` int(11) NOT NULL ,
  `create_date` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `category`
--

INSERT INTO `category` (`id`, `create_date`, `is_deleted`, `update_date`, `name`) VALUES
(1, '2023-11-29 23:45:52', 0, '2023-11-29 23:45:52', 'yiyecek'),
(2, '2023-11-29 23:45:52', 0, '2023-11-29 23:45:52', 'dekor'),
(3, '2023-11-28 14:59:38', 0, '2023-11-28 14:59:38', 'giyim'),
(4, '2023-11-28 14:59:49', 0, '2023-11-28 14:59:49', 'araba'),
(5, '2023-11-28 14:59:56', 0, '2023-11-28 14:59:56', 'telefon'),
(6, '2023-11-28 15:00:00', 0, '2023-11-28 15:00:00', 'mobilya');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(1);

COMMIT;

/*!40101 SET NAMES 'utf8' */;
/*!40101 SET CHARACTER_SET_CLIENT = 'utf8' */;
/*!40101 SET CHARACTER_SET_RESULTS = 'utf8' */;
/*!40101 SET COLLATION_CONNECTION = 'utf8_general_ci' */;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- Tablo için tablo yapısı `history`
-- --------------------------------------------------------

CREATE TABLE `history` (
  `id` int(11) NOT NULL,
  `log_level` varchar(255) DEFAULT NULL,
  `log_message` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
-- Tablo döküm verisi `history`
-- --------------------------------------------------------

INSERT INTO `history` (`id`, `log_level`, `log_message`) VALUES
(31, 'INFO', 'CREATE_PRODUCT Product Id : 14');

-- --------------------------------------------------------
-- Tablo için tablo yapısı `product`
-- --------------------------------------------------------

CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `critical_level` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
-- Tablo döküm verisi `product`
-- --------------------------------------------------------

INSERT INTO `product` (`id`, `create_date`, `is_deleted`, `update_date`, `category_id`, `critical_level`, `name`, `quantity`) VALUES
(1, '2023-11-28 15:00:22', 0, '2023-11-30 00:43:11', 3, 500, 'gömlek', 458),
(2, '2023-11-28 18:22:08', 0, '2023-11-30 00:44:27', 4, 350, 'toyota', 1642),
(3, '2023-11-28 18:22:39', 0, '2023-11-30 00:44:41', 6, 10000, 'sandalye', 363158),
(4, '2023-11-29 08:51:19', 0, '2023-11-30 00:45:03', 4, 1680, 'bisiklet', 6606),
(5, '2023-11-29 08:52:51', 0, '2023-11-30 00:45:13', 5, 500000, 'scooter', 2824835),
(14, '2023-11-30 01:29:40', 0, '2023-11-30 01:29:40', 3, 15000, 'pantolon', 120000);

-- --------------------------------------------------------
-- Tablo için tablo yapısı `store`
-- --------------------------------------------------------

CREATE TABLE `store` (
  `id` int(11) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
-- Tablo döküm verisi `store`
-- --------------------------------------------------------

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
(10, '2023-11-29 22:17:00', 0, '2023-11-29 22:17:00', 'menekşe sokak no:33', 'ANTALYA', 'antalye merkez mağazası', 'AKDENIZ'),
(12, '2023-11-29 22:17:00', 0, '2023-11-29 22:17:00', 'bayirgülü sokak no:33', 'ANTALYA', 'antalye kaş merkez mağazası', 'AKDENIZ'),
(13, '2023-11-29 22:17:00', 0, '2023-11-29 22:17:00', 'menekşe sokak no:33', 'BURDUR', 'burdur merkez mağazası', 'AKDENIZ');

-- --------------------------------------------------------
-- Tablo için tablo yapısı `store_products`
-- --------------------------------------------------------

CREATE TABLE `store_products` (
  `store_id` int(11) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `product_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
-- Tablo döküm verisi `store_products`
-- --------------------------------------------------------

INSERT INTO `store_products` (`store_id`, `quantity`, `product_id`) VALUES
(1, 25, 1),
(1, 250, 2),
(1, 2250, 3),
(1, 750, 4),
(1, 77750, 5),
(2, 29, 1),
(2, 464, 2),
(2, 1930, 3),
(2, 550, 4),
(2, 8550, 5),
(3, 68, 1),
(3, 110, 2),
(3, 11110, 3),
(3, 111, 4),
(3, 665, 5),
(4, 200, 1),
(4, 189, 2),
(4, 66600, 3),
(4, 1200, 4),
(4, 10000, 5),
(5, 500, 1),
(5, 1500, 2),
(5, 4952, 3),
(5, 782, 4),
(5, 78200, 5),
(8, 60, 1),
(8, 20, 2),
(8, 350, 3),
(8, 10, 4),
(9, 150, 1),
(9, 25, 2),
(9, 200, 3);

-- --------------------------------------------------------
-- Tablo için indeksler `category`
-- --------------------------------------------------------

ALTER TABLE `category`
  ADD PRIMARY KEY (`id`);

-- --------------------------------------------------------
-- Tablo için indeksler `history`
-- --------------------------------------------------------

ALTER TABLE `history`
  ADD PRIMARY KEY (`id`);

-- --------------------------------------------------------
-- Tablo için indeksler `product`
-- --------------------------------------------------------

ALTER TABLE `product`
  ADD PRIMARY KEY (`id`);

-- --------------------------------------------------------
-- Tablo için indeksler `store`
-- --------------------------------------------------------

ALTER TABLE `store`
  ADD PRIMARY KEY (`id`);

-- --------------------------------------------------------
-- Tablo için indeksler `store_products`
-- --------------------------------------------------------

ALTER TABLE `store_products`
  ADD PRIMARY KEY (`store_id`,`product_id`);

-- --------------------------------------------------------
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
-- --------------------------------------------------------

-- Tablo için AUTO_INCREMENT değeri `history`
ALTER TABLE `history`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

-- --------------------------------------------------------
-- Dökümü yapılmış tablolar için kısıtlamalar
-- --------------------------------------------------------

-- Tablo kısıtlamaları `store_products`
ALTER TABLE `store_products`
  ADD CONSTRAINT `FK7ajkqsjo8mdfh213onp1r0kcm` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`);

COMMIT;

/*!40101 SET NAMES 'utf8' */;
/*!40101 SET CHARACTER_SET_CLIENT = 'utf8' */;
/*!40101 SET CHARACTER_SET_RESULTS = 'utf8' */;
/*!40101 SET COLLATION_CONNECTION = 'utf8_general_ci' */;