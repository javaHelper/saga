# Event-Driven Microservices, CQRS, SAGA, Axon, Spring Boot

# Create Products

```
curl -X POST \
  http://localhost:8082/products-service/products \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 51c68d11-b6e6-c260-6398-0d1c1be1e7a9' \
  -d '{
	"title" : "Lenovo 1",
	"price" : 1100,
	"quantity" : 4
}'
```

![image](https://user-images.githubusercontent.com/54174687/127033398-fa698eca-2eb3-49e1-85bb-caf00215e0a4.png)

# Place Orders

```
curl -X POST \
  http://localhost:8082/orders-service/orders \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 7b7f8a70-3a03-9ef9-059d-9815d3235a2d' \
  -d '{
	"productId" : "1efa39b2-6101-4af7-8055-2270dde0ffb5",
	"quantity" : 1,
	"addressId" : 1
}'
```

![image](https://user-images.githubusercontent.com/54174687/127033509-29162266-5ef3-4ed5-a4b5-95cfc0eb830a.png)

# Check Subscription

```
curl -X POST \
  http://localhost:8082/products-service/management/eventProcessor/product-group/reset \
  -H 'cache-control: no-cache' \
  -H 'postman-token: f32607aa-094b-797c-2758-7ccf8257d862'
```

![image](https://user-images.githubusercontent.com/54174687/127033594-03feb393-6489-41b8-bb78-171bd2e4240e.png)

# Find Products

```
curl -X GET \
  http://localhost:8082/products-service/products \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 2ef4c17c-aa0c-1b52-9b88-a9c774a1736e'
```

# Show Tables

```
mysql> show tables;
+-------------------------+
| Tables_in_test          |
+-------------------------+
| association_value_entry |
| hibernate_sequence      |
| orders                  |
| payments                |
| productlookup           |
| products                |
| saga_entry              |
| token_entry             |
+-------------------------+
8 rows in set (0.26 sec)

```


```
mysql> select * from products;
+--------------------------------------+---------+----------+----------+
| product_id                           | price   | quantity | title    |
+--------------------------------------+---------+----------+----------+
| 1efa39b2-6101-4af7-8055-2270dde0ffb5 | 1100.00 |        1 | Lenovo 1 |
| d96a998c-8cdb-416d-a431-02fa60e600d8 | 1100.00 |        4 | Lenovo 2 |
+--------------------------------------+---------+----------+----------+
2 rows in set (1.26 sec)

mysql> select * from productlookup;
+--------------------------------------+----------+
| product_id                           | title    |
+--------------------------------------+----------+
| 1efa39b2-6101-4af7-8055-2270dde0ffb5 | Lenovo 1 |
| d96a998c-8cdb-416d-a431-02fa60e600d8 | Lenovo 2 |
+--------------------------------------+----------+
2 rows in set (0.11 sec)

mysql> select * from payments;
Empty set (0.20 sec)

mysql> select * from orders;
+--------------------------------------+------------+--------------+--------------------------------------+----------+--------------------------------------+
| order_id                             | address_id | order_status | product_id                           | quantity | user_id                              |
+--------------------------------------+------------+--------------+--------------------------------------+----------+--------------------------------------+
| 33bc9a71-c0ed-449c-8ee6-48cf738b4d45 | 1          | APPROVED     | 1efa39b2-6101-4af7-8055-2270dde0ffb5 |        1 | 27b95829-4f3f-4ddf-8983-151ba010e35b |
| a1fbfbd5-08ac-49ef-9233-68096b186ddb | 1          | APPROVED     | 1efa39b2-6101-4af7-8055-2270dde0ffb5 |        1 | 27b95829-4f3f-4ddf-8983-151ba010e35b |
| c888c548-02bc-4408-96f0-935faf4a31f9 | 1          | APPROVED     | 1efa39b2-6101-4af7-8055-2270dde0ffb5 |        1 | 27b95829-4f3f-4ddf-8983-151ba010e35b |
+--------------------------------------+------------+--------------+--------------------------------------+----------+--------------------------------------+
3 rows in set (0.13 sec)

mysql> select * from saga_entry;
Empty set (0.11 sec)
```
