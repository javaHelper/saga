# Saga Orchestration

Overview:
Over the years, MicroServices have become very popular. MicroServices are distributed systems. They are smaller, modular, easy to deploy and scale etc. Developing a single microservice application might be interesting! But handling a business transaction which spans across multiple microservices is not fun! MicroService architectures have specific responsibilities. In order to complete an application workflow / a task, multiple MicroServices might have to work together.

Lets see how difficult it could be in dealing with transactions / data consistency in the distributed systems in this article.

Challenges:
Let’s assume that our business rule says, when an user places an order, order will be fulfilled if the product’s price is within the user’s credit limit/balance & the inventory is available for the product. Otherwise it will not be fulfilled. It looks really simple. This is very easy to implement in a monolith application. The entire workflow can be considered as 1 single transaction. It is easy to commit / rollback when everything is in a single DB. With distributed systems with multiple databases, It is going to be very complex! Lets look at our architecture first to see how to implement this.

![image](https://user-images.githubusercontent.com/54174687/121394970-8a133700-c96f-11eb-8358-fc461f0fabd2.png)


We have below microservices with its own DB.

- order-service
- payment-service
- inventory-service

When the order-service receives the request for the new order, It has to check with the payment-service & inventory-service. We deduct the payment, inventory and fulfill the order finally! What will happen if we deducted payment but if inventory is not available? How to roll back? It is difficult with multiple databases involved.

![image](https://user-images.githubusercontent.com/54174687/121395034-9ac3ad00-c96f-11eb-8be6-cfa846b9f4d0.png)


# Saga Pattern:
Each business transaction which spans multiple microservices are split into micro-service specific local transactions and they are executed in a sequence to complete the business workflow. It is called Saga. It can be implemented in 2 ways.

- Choreography approach
- Orchestration approach

In this article, we will be discussing the Orchestration based saga. For more information on Choreography based saga, check here.

# Orchestration:
In this pattern, we will have an orchestrator, a separate service, which will be coordinating all the transactions among all the microservices. If things are fine, it makes the order-request as complete, otherwise marks that as cancelled.

Lets see how we could implement this. Our sample architecture will be more or less like this.!

- In this demo, communication between orchestrator and other services would be a simple HTTP in a non-blocking asynchronous way to make this stateless.
- We can also use Kafka topics for this communication. For that we have to use scatter/gather pattern which is more of a stateful style.

![image](https://user-images.githubusercontent.com/54174687/121395189-c2b31080-c96f-11eb-9587-88392164ddab.png)

![image](https://user-images.githubusercontent.com/54174687/121395215-ce063c00-c96f-11eb-9448-3cb3c367776b.png)


Order Orchestrator:
This is a microservice which is responsible for coordinating all the transactions. It listens to order-created topic. As and when a new order is created, It immediately builds separate request to each service like payment-service/inventory-service etc and validates the responses. If they are OK, fulfills the order. If one of them is not, cancels the oder. It also tries to reset any of local transactions which happened in any of the microservices.

We consider all the local transactions as 1 single workflow. A workflow will contain multiple workflow steps.

- Make the 3 consecutive requests and 4th one will be failed. 

![image](https://user-images.githubusercontent.com/54174687/121395273-dcecee80-c96f-11eb-96cc-fc7023129be4.png)


GET http://localhost:8080/order/all

```
[
    {
        "orderId": "8f0ee140-5e8a-47c8-bcaf-600f7068f226",
        "userId": 1,
        "productId": 3,
        "amount": 300,
        "status": "ORDER_COMPLETED"
    },
    {
        "orderId": "3bad2a6c-0a6b-468d-9b01-4a9b7641fcac",
        "userId": 1,
        "productId": 3,
        "amount": 300,
        "status": "ORDER_COMPLETED"
    },
    {
        "orderId": "7058f8fc-230a-4421-9013-6785e5fe1f2a",
        "userId": 1,
        "productId": 3,
        "amount": 300,
        "status": "ORDER_COMPLETED"
    },
    {
        "orderId": "5a54e545-80d7-4205-ad4a-a34936b67216",
        "userId": 1,
        "productId": 3,
        "amount": 300,
        "status": "ORDER_CANCELLED"
    }
]
```

![image](https://user-images.githubusercontent.com/54174687/121395871-76b49b80-c970-11eb-92eb-487807b182a7.png)


![image](https://user-images.githubusercontent.com/54174687/121395988-8e8c1f80-c970-11eb-9882-2c79a9237e62.png)

