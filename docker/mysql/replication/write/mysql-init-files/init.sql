CREATE DATABASE `large-scale-system` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `large-scale-system`.temp
(
    id varchar(100) PRIMARY KEY
);

-- create masteruser and grant privileges
CREATE USER masteruser@'172.16.0.%' IDENTIFIED BY 'masterpassword';
GRANT ALL PRIVILEGES ON *.* TO masteruser@'172.16.0.%' IDENTIFIED BY 'masterpassword';

-- replication
GRANT REPLICATION SLAVE ON *.* TO slaveuser@'172.16.0.%' IDENTIFIED BY 'slavepassword';
