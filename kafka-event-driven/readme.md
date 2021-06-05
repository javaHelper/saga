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

MongoDB

```json
db.getCollection('purchase_order').find({})
{
    "_id" : ObjectId("60a536412306ab5bdd7a6b06"),
    "user" : {
        "id" : NumberLong(1),
        "firstname" : "Parate",
        "lastname" : "Parate",
        "email" : "neha.parate@vinsguru.com"
    },
    "product" : {
        "id" : NumberLong(1),
        "description" : "ipad"
    },
    "price" : 300.0,
    "_class" : "com.example.demo.model.PurchaseOrder"
}
```


http://localhost:8081/order-service/all

```json
[
    {
        "id": "60a536412306ab5bdd7a6b06",
        "user": {
            "id": 1,
            "firstname": "Parate",
            "lastname": "Parate",
            "email": "neha.parate@vinsguru.com"
        },
        "product": {
            "productId": 1,
            "description": "ipad"
        },
        "price": 300
    }
]
```

wne we make the PUT request

```
curl -X PUT \
  http://localhost:8080/user-service/update \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: b5ed5c72-2478-2cbb-82b3-13c563334177' \
  -d '{
    "id": 1,
    "firstname":"Neha",
    "lastname": "Parate",
    "email": "neha.parate-updated@vinsguru.com"
}'
```
![image](https://user-images.githubusercontent.com/54174687/118947489-01f3d000-b975-11eb-85be-12315bc5f564.png)

![image](https://user-images.githubusercontent.com/54174687/118947335-d96bd600-b974-11eb-8b68-23917e899a69.png)


![image](https://user-images.githubusercontent.com/54174687/118952308-74ff4580-b979-11eb-8501-6dea64f13906.png)

