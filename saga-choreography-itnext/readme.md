

# Step

https://itnext.io/microservices-architecture-an-implementation-of-saga-pattern-dbbe5d881c78


# Swagger URLs

- Order Service - http://localhost:8090/swagger-ui.html
- Delivery Service - http://localhost:8070/swagger-ui.htm
- Kitchen Service - http://localhost:8080/swagger-ui.html

# Techlogies Used 

- Java 8
- Spring Boot v2.4.5
- MongoDB
- Saga Choreography
- Swagger 

# Steps

When you execute the code make sure `db.getCollection('hamburger').find({})` should hold multiple records of same type like below 

```
/* 1 */
{
    "_id" : ObjectId("60be54182ff7ef03f3f397c0"),
    "hamburgerType" : "SCOTTONA",
    "_class" : "com.example.demo.entity.Hamburger"
}

/* 2 */
{
    "_id" : ObjectId("60be542a2ff7ef03f3f397c1"),
    "hamburgerType" : "CHIANINA",
    "_class" : "com.example.demo.entity.Hamburger"
}

/* 3 */
{
    "_id" : ObjectId("60be56d62ff7ef03f3f397c8"),
    "hamburgerType" : "ANGUS",
    "_class" : "com.example.demo.entity.Hamburger"
}

/* 4 */
{
    "_id" : ObjectId("60be56db2ff7ef03f3f397c9"),
    "hamburgerType" : "ANGUS",
    "_class" : "com.example.demo.entity.Hamburger"
}

/* 5 */
{
    "_id" : ObjectId("60be56de2ff7ef03f3f397ca"),
    "hamburgerType" : "ANGUS",
    "_class" : "com.example.demo.entity.Hamburger"
}
```
# Create Order 

```
curl -X POST "http://localhost:8090/order/create" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"addressDTO\": { \"number\": \"100\", \"street\": \"Achalpur Street\" }, \"cookingType\": \"BLOOD\", \"hamburgerList\": [ { \"hamburgerType\": \"ANGUS\", \"quantity\": 2 } ], \"orderStatus\": \"COOKING\", \"price\": 110, \"statusDescription\": \"Waiting Status\"}"
```
Below things should happe in MongoDB

db.getCollection('order').find({})

```
{
    "_id" : ObjectId("60be57704123a3150882f9bd"),
    "statusDescription" : "WAITING",
    "cookingType" : "BLOOD",
    "orderStatus" : "WAITING",
    "hamburgerList" : [ 
        {
            "hamburgerType" : "ANGUS",
            "quantity" : 2
        }
    ],
    "addressDTO" : {
        "number" : "100",
        "street" : "Achalpur Street"
    },
    "price" : 110.0,
    "_class" : "com.example.demo.entity.Order"
}
```

db.getCollection('delivery').find({})

```
{
    "_id" : ObjectId("60be581f850eb81c07e6b342"),
    "addressDTO" : {
        "number" : "100",
        "street" : "Achalpur Street"
    },
    "orderId" : "60be57704123a3150882f9bd",
    "_class" : "com.example.demo.entity.Delivery"
}
```

![image](https://user-images.githubusercontent.com/54174687/121066835-f6632e80-c7e7-11eb-8c3e-9c7aa10c7c3e.png)

![image](https://user-images.githubusercontent.com/54174687/121066914-0ed34900-c7e8-11eb-9a3e-4a58738b6c71.png)

![image](https://user-images.githubusercontent.com/54174687/121066964-1d216500-c7e8-11eb-999f-bd34980b50ca.png)


# Architecture 

![image](https://user-images.githubusercontent.com/54174687/121066098-278f2f00-c7e7-11eb-8527-0d7298f30997.png)


