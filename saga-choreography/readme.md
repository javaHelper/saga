# Choreography Saga Pattern With Spring Boot

A Simple Transaction:
Let’s assume that our business rule says, when an user places an order, order will be fulfilled if the product’s price is within the user’s credit limit/balance. Otherwise it will not be fulfilled. It looks really simple.

This is very easy to implement in a monolith application. The entire workflow can be considered as 1 single transaction. It is easy to commit / rollback when everything is in a single DB. With distributed systems with multiple databases, It is going to be very complex! Lets look at our architecture first to see how to implement this.

We have an order-service with its own database which is responsible for order management. Similarly we also have payment-service which is responsible for managing payments. So the order-service receives the request and checks with the payment-service if the user has the balance. If the payment-service responds OK, order-service completes the order and payment-service deducts the payment. Otherwise, the order-service cancels the order. For a very simple business requirement, here we have to send multiple requests across the network.


What if we also need to check with inventory-service for the availability of inventory before making the order as complete! Now you see the problem?

In the traditional system design approach, order-service simply sends a HTTP request to get the information about the user’s credit balance. The problem with this approach is order-service assumes that payment-service will be up and running always. Any network issue or performance issue at the payment-service will be propagated to the order-service. It could lead to poor user-experience & we also might lose revenue. Let’s see how we could handle transactions in the distributed systems with loose coupling by using a pattern called Saga Pattern with Event Sourcing approach.

saga pattern
Choreography Saga Pattern With Spring Boot – Microservice Design Patterns
14 Comments / Architectural Design Pattern, Architecture, Articles, Data Stream / Event Stream, Design Pattern, Framework, Java, Kafka, Kubernetes Design Pattern, MicroService, Reactive Programming, Reactor, Spring, Spring Boot, Spring WebFlux / By vIns / March 19, 2021

 
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

![image](https://user-images.githubusercontent.com/54174687/118979170-d3d2b800-b995-11eb-8e5c-224df8ee7e00.png)


- order-services receives a POST request for a new order
- It places an order request in the DB in the ORDER_CREATED state and raises an event
- payment-service listens to the event, confirms about the credit reservation
- inventory-service also listens to the order-event and conforms the inventory reservation
- order-service fulfills order or rejects the order based on the credit & inventory reservation status.


# Request

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
    "id": "e9e4b83f-9a9f-4005-b391-dc90c01a4eea",
    "userId": 1,
    "productId": 3,
    "price": 300,
    "orderStatus": "ORDER_CREATED",
    "paymentStatus": null,
    "inventoryStatus": null,
    "version": 0
}
```

