# Saga Compensating Transaction

# Create Tables

```sql
CREATE TABLE `products` (
  `product_id` varchar(255) NOT NULL,
  `price` decimal(19,2) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `UK_8xtpej5iy2w4cte2trlvrlayy` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

```sql
CREATE TABLE `orders` (
  `order_id` varchar(255) NOT NULL,
  `address_id` varchar(255) DEFAULT NULL,
  `order_status` varchar(255) DEFAULT NULL,
  `product_id` varchar(255) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

```sql
CREATE TABLE `productlookup` (
  `product_id` varchar(255) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `UK_3qy6ao6gbk08863trvgcikyfa` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```


```sql
CREATE TABLE `payments` (
  `payment_id` varchar(255) NOT NULL,
  `order_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`payment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```



```sql
CREATE TABLE `token_entry` (
  `processor_name` varchar(255) NOT NULL,
  `segment` int(11) NOT NULL,
  `owner` varchar(255) DEFAULT NULL,
  `timestamp` varchar(255) NOT NULL,
  `token` longblob,
  `token_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`processor_name`,`segment`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

```sql
CREATE TABLE `saga_entry` (
  `saga_id` varchar(255) NOT NULL,
  `revision` varchar(255) DEFAULT NULL,
  `saga_type` varchar(255) DEFAULT NULL,
  `serialized_saga` longblob,
  PRIMARY KEY (`saga_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

# Sequence to start the services

- discovery-service
- order-service
- payment-service
- product-service
- user-service
- api-gateway

# Create Products

```
curl -X POST \
  http://localhost:8082/product-service/products \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: a95967b8-cab6-42de-f435-cb3fbf011867' \
  -d '{
	"title" : "iPad Pro 4",
	"price" : 200,
	"quantity" : 2
}'
```

![image](https://user-images.githubusercontent.com/54174687/126899481-2593e8e2-8259-4faa-b053-0079f1a723bf.png)

![image](https://user-images.githubusercontent.com/54174687/126899485-fd65a596-be23-44e0-b658-63faaedf7e3c.png)

# GET the list of all products

http://localhost:8082/product-service/products
