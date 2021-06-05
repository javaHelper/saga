# Event Carried State Transfer – Microservice Design Pattern


We can maintain data consistency across all the microservices using Apache/Confluent Kafka. This approach avoids many unnecessary network calls among microservices, improves the performance of microservices and make the microservices loosely coupled. 
For ex: `Order-service` does not have to be up and running when user details are updated via `user-service`. `User-service` would be raising an event. `Order-service` can subscribe to that whenever it is up and running. So that information is not going to be lost! In the old approach, it makes microservices tightly coupled in such a way that all the dependent microservices have to be up and running together. Otherwise it would make the system unavailable.

```sql
CREATE TABLE `test`.`users` (
  `id` INT NOT NULL,
  `firstname` VARCHAR(45) NULL,
  `lastname` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
  
INSERT INTO `test`.`users` (`id`, `firstname`, `lastname`, `email`) VALUES ('1', 'Neha', 'Parate', 'neha.parate@gmail.com');
INSERT INTO `test`.`users` (`id`, `firstname`, `lastname`, `email`) VALUES ('2', 'Aravind', 'Dekate', 'aravind.dekate@gmail.com');
INSERT INTO `test`.`users` (`id`, `firstname`, `lastname`, `email`) VALUES ('3', 'Mayur', 'Devghare', 'mayur.devghare@gmail.com');
INSERT INTO `test`.`users` (`id`, `firstname`, `lastname`, `email`) VALUES ('4', 'Suchita', 'Vinchurkar', 'suchita.vinchurkar@gmail.com');


CREATE TABLE `test`.`product` (
  `id` INT NOT NULL,
  `description` VARCHAR(500) NULL,
  `price` INT NULL,
  `qty_available` INT NULL,
  PRIMARY KEY (`id`));

INSERT INTO `test`.`product` (`id`, `description`, `price`, `qty_available`) VALUES ('1', 'IPad', '300', '10');
INSERT INTO `test`.`product` (`id`, `description`, `price`, `qty_available`) VALUES ('2', 'IPhone', '650', '50');
INSERT INTO `test`.`product` (`id`, `description`, `price`, `qty_available`) VALUES ('3', 'Sony TV', '320', '100');

```

# MySQL

```
mysql> use test;
Database changed
mysql> show tables;
+----------------+
| Tables_in_test |
+----------------+
| product        |
| users          |
+----------------+
2 rows in set (2.56 sec)

mysql> select * from users;
+----+-----------+------------+------------------------------+
| id | firstname | lastname   | email                        |
+----+-----------+------------+------------------------------+
|  1 | Neha      | Parate     | neha.parate@gmail.com        |
|  2 | Aravind   | Dekate     | aravind.dekate@gmail.com     |
|  3 | Mayur     | Devghare   | mayur.devghare@gmail.com     |
|  4 | Suchita   | Vinchurkar | suchita.vinchurkar@gmail.com |
+----+-----------+------------+------------------------------+
4 rows in set (1.06 sec)

mysql> select * from products;
ERROR 1146 (42S02): Table 'test.products' doesn't exist
mysql> select * from product;
+----+-------------+-------+---------------+
| id | description | price | qty_available |
+----+-------------+-------+---------------+
|  1 | IPad        |   300 |            10 |
|  2 | IPhone      |   650 |            50 |
|  3 | Sony TV     |   320 |           100 |
+----+-------------+-------+---------------+
3 rows in set (1.12 sec)
```

# MongoDB

```json
db.getCollection('purchase_order').find({})
{
    "_id" : ObjectId("60a536412306ab5bdd7a6b06"),
    "user" : {
        "id" : NumberLong(1),
        "firstname" : "Parate",
        "lastname" : "Parate",
        "email" : "neha.parate@gmail.com"
    },
    "product" : {
        "id" : NumberLong(1),
        "description" : "ipad"
    },
    "price" : 300.0,
    "_class" : "com.example.demo.model.PurchaseOrder"
}
```

Now when user details have been updated, user-service will raise an event and iterested order-service will listen to that event update the information.

For ex: For Neha as user, looking to update her email address from `gmail.com` to `hotmail.com`.

# POST Request

```curl
curl --location --request PUT 'http://localhost:8080/user-service/update' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id" : 1,
    "firstname" : "Neha",
    "lastname" : "Parate",
    "email" : "neha.parate@hotmail.com"
}'
```

Now MySQL DB has been updated and kafka event have been raise and listen by order-service to update the `purchase_order` collection. Now check `purchase_order`should have updated details

![image](https://user-images.githubusercontent.com/54174687/120895619-71d3ad00-c63b-11eb-9bd5-bab90a6ac13e.png)

# MySQL

```
mysql> select * from users;
+----+-----------+------------+------------------------------+
| id | firstname | lastname   | email                        |
+----+-----------+------------+------------------------------+
|  1 | Parate    | Parate     | neha.parate@hotmail.com      |
|  2 | Aravind   | Dekate     | aravind.dekate@gmail.com     |
|  3 | Mayur     | Devghare   | mayur.devghare@gmail.com     |
|  4 | Suchita   | Vinchurkar | suchita.vinchurkar@gmail.com |
+----+-----------+------------+------------------------------+
4 rows in set (0.00 sec)
```

# Control Center Confluent Kafka

![image](https://user-images.githubusercontent.com/54174687/120895726-f292a900-c63b-11eb-93ca-d9638577273e.png)




http://localhost:8081/order-service/all

```json
[
    {
        "id": "60a536412306ab5bdd7a6b06",
        "user": {
            "id": 1,
            "firstname": "Parate",
            "lastname": "Parate",
            "email": "neha.parate@hotmail.com"
        },
        "product": {
            "productId": 1,
            "description": "ipad"
        },
        "price": 300.0
    }
]
```

# Architecture 
![image](https://user-images.githubusercontent.com/54174687/120896426-1c010400-c63f-11eb-9dd6-75c8254e4d53.png)


