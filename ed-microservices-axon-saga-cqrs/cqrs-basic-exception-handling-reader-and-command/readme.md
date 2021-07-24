# Event-Driven Microservices, CQRS, SAGA, Axon, Spring Boot ==> PART-1

# Create Few Products

```curl
curl -X POST \
  http://localhost:8082/product-service/products \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 3596408e-c308-bf92-84f3-23f0c66f673a' \
  -d '{
	"title" : "iPhone 2",
	"price" : 200,
	"quantity" : 2
}'
```


```curl
curl -X POST \
  http://localhost:8082/product-service/products \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 3596408e-c308-bf92-84f3-23f0c66f673a' \
  -d '{
	"title" : "iPhone 3",
	"price" : 300,
	"quantity" : 2
}'
```


```curl
curl -X POST \
  http://localhost:8082/product-service/products \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 3596408e-c308-bf92-84f3-23f0c66f673a' \
  -d '{
	"title" : "iPhone 5",
	"price" : 500,
	"quantity" : 2
}'
```


```curl
curl -X POST \
  http://localhost:8082/product-service/products \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 3596408e-c308-bf92-84f3-23f0c66f673a' \
  -d '{
	"title" : "iPhone 9",
	"price" : 900,
	"quantity" : 2
}'
```

Now again try to save the same record and you should see transaction could come into picture and it will rollback tx

![image](https://user-images.githubusercontent.com/54174687/126867882-8e6e41c7-2296-483b-b85d-19a500695921.png)


# GET Products

```curl
curl -X GET \
  http://localhost:8082/product-service/products \
  -H 'cache-control: no-cache' \
  -H 'postman-token: ff93c85e-55ec-3bd6-f39f-05172f113f05'
```


![image](https://user-images.githubusercontent.com/54174687/126868062-f3dcff57-8ed2-40e7-9b17-af1d9965c0f6.png)


![image](https://user-images.githubusercontent.com/54174687/126868074-083b7deb-d80f-4f49-a6ae-403c9aef8ee0.png)
