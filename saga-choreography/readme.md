# Choreography Saga Pattern With Spring Boot

A Simple Transaction:
Let’s assume that our business rule says, when an user places an order, order will be fulfilled if the product’s price is within the user’s credit limit/balance. Otherwise it will not be fulfilled. It looks really simple.

This is very easy to implement in a monolith application. The entire workflow can be considered as 1 single transaction. It is easy to commit / rollback when everything is in a single DB. With distributed systems with multiple databases, It is going to be very complex! Lets look at our architecture first to see how to implement this.

We have an order-service with its own database which is responsible for order management. Similarly we also have payment-service which is responsible for managing payments. So the order-service receives the request and checks with the payment-service if the user has the balance. If the payment-service responds OK, order-service completes the order and payment-service deducts the payment. Otherwise, the order-service cancels the order. For a very simple business requirement, here we have to send multiple requests across the network.

![image](https://user-images.githubusercontent.com/54174687/118979615-43e13e00-b996-11eb-9549-a7d9009572fd.png)


What if we also need to check with inventory-service for the availability of inventory before making the order as complete! Now you see the problem?

In the traditional system design approach, order-service simply sends a HTTP request to get the information about the user’s credit balance. The problem with this approach is order-service assumes that payment-service will be up and running always. Any network issue or performance issue at the payment-service will be propagated to the order-service. It could lead to poor user-experience & we also might lose revenue. Let’s see how we could handle transactions in the distributed systems with loose coupling by using a pattern called Saga Pattern with Event Sourcing approach.

```curl
drop table if exists user_balance;

drop table if exists user_transaction;

create table user_balance (
   user_id integer not null,
	balance integer not null,
	primary key (user_id)
) engine=MyISAM;

create table user_transaction (
   order_id binary(255) not null,
	amount integer not null,
	user_id integer not null,
	primary key (order_id)
) engine=MyISAM;



drop table if exists order_inventory;
drop table if exists order_inventory_consumption;

create table order_inventory (
       product_id integer not null,
        available_inventory integer not null,
        primary key (product_id)
    ) engine=MyISAM;
	
create table order_inventory_consumption (
       order_id binary(255) not null,
        product_id integer not null,
        quantity_consumed integer not null,
        primary key (order_id)
    ) engine=MyISAM;
	
	
drop table if exists purchase_order;
create table purchase_order (
       id binary(255) not null,
        inventory_status integer,
        order_status integer,
        payment_status integer,
        price integer,
        product_id integer,
        user_id integer,
        version integer not null,
        primary key (id)
    ) engine=MyISAM;
	
	
INSERT INTO `inventory-service`.`order_inventory` (`product_id`, `available_inventory`) VALUES ('1', '5');
INSERT INTO `inventory-service`.`order_inventory` (`product_id`, `available_inventory`) VALUES ('2', '5');
INSERT INTO `inventory-service`.`order_inventory` (`product_id`, `available_inventory`) VALUES ('3', '5');


INSERT INTO `inventory-service`.`user_balance` (`user_id`, `balance`) VALUES ('1', '1000');
INSERT INTO `inventory-service`.`user_balance` (`user_id`, `balance`) VALUES ('2', '1000');
INSERT INTO `inventory-service`.`user_balance` (`user_id`, `balance`) VALUES ('3', '1000');
```



# saga pattern
Choreography Saga Pattern With Spring Boot – Microservice Design Patterns

![image](https://user-images.githubusercontent.com/54174687/118979574-3926a900-b996-11eb-9dd2-37e3fc0ee550.png)


 
Overview:
In this tutorial, I would like to show you a simple implementation of Choreography Saga Pattern with Spring Boot.

Over the years, Microservices have become very popular. Microservices are distributed systems. They are smaller, modular, easy to deploy and scale etc. Developing a single microservice application might be interesting! But handling a business transaction which spans across multiple Microservices is not fun!  In order to complete an application workflow / a task, multiple Microservices might have to work together.

Lets see how difficult it could be in dealing with transactions / data consistency in the distributed systems in this article.

A Simple Transaction:
Let’s assume that our business rule says, when an user places an order, order will be fulfilled if the product’s price is within the user’s credit limit/balance. Otherwise it will not be fulfilled. It looks really simple.

This is very easy to implement in a monolith application. The entire workflow can be considered as 1 single transaction. It is easy to commit / rollback when everything is in a single DB. With distributed systems with multiple databases, It is going to be very complex! Lets look at our architecture first to see how to implement this.

We have an order-service with its own database which is responsible for order management. Similarly we also have payment-service which is responsible for managing payments. So the order-service receives the request and checks with the payment-service if the user has the balance. If the payment-service responds OK, order-service completes the order and payment-service deducts the payment. Otherwise, the order-service cancels the order. For a very simple business requirement, here we have to send multiple requests across the network.

What if we also need to check with inventory-service for the availability of inventory before making the order as complete! Now you see the problem?

In the traditional system design approach, order-service simply sends a HTTP request to get the information about the user’s credit balance. The problem with this approach is order-service assumes that payment-service will be up and running always. Any network issue or performance issue at the payment-service will be propagated to the order-service. It could lead to poor user-experience & we also might lose revenue. Let’s see how we could handle transactions in the distributed systems with loose coupling by using a pattern called Saga Pattern with Event Sourcing approach.

# Saga Pattern:
Each business transaction which spans multiple Microservices are split into Microservice specific local transactions and they are executed in a sequence to complete the business workflow. It is called Saga. It can be implemented in 2 ways.

- Choreography approach
- Orchestration approach

In this article, we will be discussing the choreography based approach by using event-sourcing. For orchestration based Saga, check here.

$ Event Sourcing:
In this approach every change to the state of an application is captured as an event. This event is stored in the database /event store (for tracking purposes) and is also published in the event-bus for other parties to consume.


The order-service receives a command to create a new order. This request is processed and raised as an order-created event. Couple of things to note here.

Order created event basically informs that a new order request has been received and kept in the PENDING/CREATED status by order-service. It is not yet fulfilled.
The event object will be named in the past tense always as it already happened!
Now the payment-service/inventory-service could be interested in listening to those events and reserve/reject payment/inventory. Even these could be treated as an event. Payment reserved/rejected event. Order-service might listen to these events and fulfill / cancel the order request it had originally received.

This approach has many advantages.

There is no service dependency. payment-service/inventory-service do not have to be up and running always.
- Loose coupling
- Horizontal scaling
- Fault tolerant

The business workflow is implemented as shown here.

![image](https://user-images.githubusercontent.com/54174687/120899161-e9113d00-c64b-11eb-834e-22afd00560e0.png)



- order-services receives a POST request for a new order
- It places an order request in the DB in the ORDER_CREATED state and raises an event
- payment-service listens to the event, confirms about the credit reservation
- inventory-service also listens to the order-event and conforms the inventory reservation
- order-service fulfills order or rejects the order based on the credit & inventory reservation status.


# Request

Make the multiple POST requests - 

```
curl -X POST \
  http://localhost:8080/order/create \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 6761bb84-405a-8c05-7bf0-331a350fe3bc' \
  -d '{
	"userId" : 1,
	"productId" : 3
}'
```

Response

```
{
    "id": "1184f86b-40a7-4f0a-b216-a451af427cc6",
    "userId": 1,
    "productId": 1,
    "price": 100,
    "orderStatus": "ORDER_CREATED",
    "paymentStatus": null,
    "inventoryStatus": null,
    "version": 0
}
```

```curl
curl --location --request POST 'http://localhost:8080/order/create' \
--header 'Content-Type: application/json' \
--data-raw '{
    "userId" : 1,
	"productId" : 2
}'
```

Response

```
{
    "id": "00f51f31-07d3-4637-a259-8bd18dca8f93",
    "userId": 1,
    "productId": 2,
    "price": 200,
    "orderStatus": "ORDER_CREATED",
    "paymentStatus": null,
    "inventoryStatus": null,
    "version": 0
}
```


```
curl --location --request POST 'http://localhost:8080/order/create' \
--header 'Content-Type: application/json' \
--data-raw '{
    "userId" : 2,
	"productId" : 1
}'
```

Response
```
{
    "id": "7f716f36-78f6-42f8-bed6-c994e957a7f4",
    "userId": 2,
    "productId": 1,
    "price": 100,
    "orderStatus": "ORDER_CREATED",
    "paymentStatus": null,
    "inventoryStatus": null,
    "version": 0
}
```

```curl
curl --location --request POST 'http://localhost:8080/order/create' \
--header 'Content-Type: application/json' \
--data-raw '{
    "userId" : 2,
	"productId" : 2
}'
```

Response

```
{
    "id": "12636116-c6f4-4b12-8314-ddca7c016e44",
    "userId": 2,
    "productId": 2,
    "price": 200,
    "orderStatus": "ORDER_CREATED",
    "paymentStatus": null,
    "inventoryStatus": null,
    "version": 0
}
```

# Database details

```
mysql> SELECT * FROM `order-service`.order_line_items;
+----+---------+----------+---------------+
| id | price   | quantity | sku_code      |
+----+---------+----------+---------------+
|  1 | 1230.00 |        1 | IPHONE_12_RED |
|  2 | 1230.00 |        1 | IPHONE_12_RED |
+----+---------+----------+---------------+
2 rows in set (0.00 sec)

mysql> SELECT * FROM `order-service`.purchase_order;
+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+------------------+--------------+----------------+-------+------------+---------+---------+
| id                                                                                                                                                                                                                                                              | inventory_status | order_status | payment_status | price | product_id | user_id | version |
+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+------------------+--------------+----------------+-------+------------+---------+---------+
| ä°k@ºO
▓ñQ»B|ã                                                                                                                                                                                                                                                |             NULL |            0 |           NULL |   100 |          1 |       1 |       0 |
|  §1ËF7óYïÐì╩Åô                                                                                                                                                                                                                                                |             NULL |            0 |           NULL |   200 |          2 |       1 |       0 |
| qo6x÷B°¥Í╔öÚWº¶                                                                                                                                                                                                                                                |             NULL |            0 |           NULL |   100 |          1 |       2 |       0 |
| caã¶Kâ¦╩|nD                                                                                                                                                                                                                                                |             NULL |            0 |           NULL |   200 |          2 |       2 |       0 |
+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+------------------+--------------+----------------+-------+------------+---------+---------+
4 rows in set (0.30 sec)

mysql> SELECT * FROM `order-service`.t_orders;
+----+--------------------------------------+
| id | order_number                         |
+----+--------------------------------------+
|  1 | 5715617b-6cc1-4c79-801d-93ccfedc0805 |
|  2 | 2153145a-8e1c-4106-a97a-da0476443ee4 |
+----+--------------------------------------+
2 rows in set (0.00 sec)

mysql> SELECT * FROM `order-service`.t_orders_order_line_items;
+----------+---------------------+
| order_id | order_line_items_id |
+----------+---------------------+
|        1 |                   1 |
|        2 |                   2 |
+----------+---------------------+
2 rows in set (0.00 sec)

mysql>

mysql> SELECT * FROM `inventory-service`.order_inventory;
+------------+---------------------+
| product_id | available_inventory |
+------------+---------------------+
|          1 |                   3 |
|          2 |                   3 |
|          3 |                   5 |
+------------+---------------------+
3 rows in set (0.01 sec)

mysql> SELECT * FROM `inventory-service`.order_inventory_consumption;
+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+------------+-------------------+
| order_id                                                                                                                                                                                                                                                        | product_id | quantity_consumed |
+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+------------+-------------------+
| ä°k@ºO
▓ñQ»B|ã                                                                                                                                                                                                                                                |          1 |                 1 |
|  §1ËF7óYïÐì╩Åô                                                                                                                                                                                                                                                |          2 |                 1 |
| qo6x÷B°¥Í╔öÚWº¶                                                                                                                                                                                                                                                |          1 |                 1 |
| caã¶Kâ¦╩|nD                                                                                                                                                                                                                                                |          2 |                 1 |
+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+------------+-------------------+
4 rows in set (0.58 sec)

mysql> SELECT * FROM `inventory-service`.user_balance;
+---------+---------+
| user_id | balance |
+---------+---------+
|       1 |     700 |
|       2 |     700 |
|       3 |    1000 |
+---------+---------+
3 rows in set (0.00 sec)

mysql> SELECT * FROM `inventory-service`.user_transaction;
+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+--------+---------+
| order_id                                                                                                                                                                                                                                                        | amount | user_id |
+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+--------+---------+
| ä°k@ºO
▓ñQ»B|ã                                                                                                                                                                                                                                                |    100 |       1 |
|  §1ËF7óYïÐì╩Åô                                                                                                                                                                                                                                                |    200 |       1 |
| qo6x÷B°¥Í╔öÚWº¶                                                                                                                                                                                                                                                |    100 |       2 |
| caã¶Kâ¦╩|nD                                                                                                                                                                                                                                                |    200 |       2 |
+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+--------+---------+
4 rows in set (0.00 sec)

mysql>
```


http://localhost:8080/order/all

```
[
    {
        "id": "1184f86b-40a7-4f0a-b216-a451af427cc6",
        "userId": 1,
        "productId": 1,
        "price": 100,
        "orderStatus": "ORDER_CREATED",
        "paymentStatus": null,
        "inventoryStatus": null,
        "version": 0
    },
    {
        "id": "00f51f31-07d3-4637-a259-8bd18dca8f93",
        "userId": 1,
        "productId": 2,
        "price": 200,
        "orderStatus": "ORDER_CREATED",
        "paymentStatus": null,
        "inventoryStatus": null,
        "version": 0
    },
    {
        "id": "7f716f36-78f6-42f8-bed6-c994e957a7f4",
        "userId": 2,
        "productId": 1,
        "price": 100,
        "orderStatus": "ORDER_CREATED",
        "paymentStatus": null,
        "inventoryStatus": null,
        "version": 0
    },
    {
        "id": "12636116-c6f4-4b12-8314-ddca7c016e44",
        "userId": 2,
        "productId": 2,
        "price": 200,
        "orderStatus": "ORDER_CREATED",
        "paymentStatus": null,
        "inventoryStatus": null,
        "version": 0
    }
]
```



![image](https://user-images.githubusercontent.com/54174687/120922586-aac95b80-c6e7-11eb-8b85-c7af7bd49584.png)

![image](https://user-images.githubusercontent.com/54174687/120922615-cdf40b00-c6e7-11eb-9c51-ad353a972693.png)

![image](https://user-images.githubusercontent.com/54174687/120922635-e82de900-c6e7-11eb-9921-01e5fe2d88e3.png)



