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

