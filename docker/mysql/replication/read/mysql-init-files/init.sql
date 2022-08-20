CREATE DATABASE `large-scale-system` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `large-scale-system`.temp
(
    id varchar(100) PRIMARY KEY
);

CREATE USER slaveuser@'%' IDENTIFIED BY 'slavepassword';
GRANT ALL PRIVILEGES ON `large-scale-system`.* TO slaveuser@'%' IDENTIFIED BY 'slavepassword';
