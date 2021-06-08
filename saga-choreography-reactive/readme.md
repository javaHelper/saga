# Steps

```
drop table if exists order_purchase;
drop table if exists payment;
drop table if exists product;
drop table if exists transaction;
drop table if exists users;

create table order_purchase (
    id integer not null auto_increment,
	price double precision not null,
	product_id integer,
	status integer,
	user_id integer,
	primary key (id)
) engine=InnoDB;

create table payment (
    id integer not null auto_increment,
	amount decimal(19,2),
	primary key (id)
) engine=InnoDB;

create table product (
    id integer not null auto_increment,
	price double precision not null,
	product_id integer,
	primary key (id)
) engine=InnoDB;


create table transaction (
    id integer not null auto_increment,
	order_id integer,
	price double precision not null,
	primary key (id)
) engine=InnoDB;

create table users (
    id integer not null auto_increment,
	balance double precision not null,
	user_id integer,
	primary key (id)
) engine=InnoDB;


INSERT INTO `test`.`product` (`id`, `price`, `product_id`) VALUES ('1', '50.0', '1');
INSERT INTO `test`.`product` (`id`, `price`, `product_id`) VALUES ('2', '75.0', '2');
INSERT INTO `test`.`product` (`id`, `price`, `product_id`) VALUES ('3', '100.0', '3');
INSERT INTO `test`.`product` (`id`, `price`, `product_id`) VALUES ('4', '500.0', '4');
INSERT INTO `test`.`product` (`id`, `price`, `product_id`) VALUES ('5', '1000.0', '5');


INSERT INTO `test`.`users` (`id`, `balance`, `user_id`) VALUES ('1', '100.0', '1');
INSERT INTO `test`.`users` (`id`, `balance`, `user_id`) VALUES ('2', '200.0', '2');
INSERT INTO `test`.`users` (`id`, `balance`, `user_id`) VALUES ('3', '500.0', '3');
INSERT INTO `test`.`users` (`id`, `balance`, `user_id`) VALUES ('4', '1000.0', '4');
INSERT INTO `test`.`users` (`id`, `balance`, `user_id`) VALUES ('5', '2000.0', '5');
```

Make the multiple POST requests, that will deduct the amount from user table - 

![image](https://user-images.githubusercontent.com/54174687/121203329-a5a90f80-c893-11eb-8275-637a53524ce5.png)

http://localhost:9192/orders/all

```
[
    {
        "id": 1,
        "userId": 1,
        "productId": 1,
        "price": 50,
        "status": "COMPLETED"
    },
    {
        "id": 2,
        "userId": 1,
        "productId": 1,
        "price": 50,
        "status": "COMPLETED"
    },
    {
        "id": 3,
        "userId": 1,
        "productId": 1,
        "price": 50,
        "status": "FAILED"
    }
]
```

![image](https://user-images.githubusercontent.com/54174687/121203578-d721db00-c893-11eb-9142-65f2b2b98d0e.png)

![image](https://user-images.githubusercontent.com/54174687/121203660-e9037e00-c893-11eb-8061-8b42b4fdb7bd.png)

![image](https://user-images.githubusercontent.com/54174687/121203726-f587d680-c893-11eb-9787-5c2de6c77552.png)



# Architecture - 

![image](https://user-images.githubusercontent.com/54174687/121203224-93c76c80-c893-11eb-83e9-e6430677c41e.png)
