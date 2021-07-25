CREATE TABLE `products` (
  `product_id` varchar(255) NOT NULL,
  `price` decimal(19,2) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `UK_8xtpej5iy2w4cte2trlvrlayy` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `productlookup` (
  `product_id` varchar(255) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `UK_3qy6ao6gbk08863trvgcikyfa` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;