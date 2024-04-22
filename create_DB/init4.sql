DROP TABLE driver_vehicle;
DROP TABLE driver;
DROP TABLE vehicle;
DROP TABLE brand;
DROP TABLE enterprise;
DROP TABLE enterprise_manager;
DROP TABLE manager;

CREATE TABLE brand (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name varchar(50) NOT NULL,
	type_vehicle varchar(50) NOT NULL,
	tank_capacity double precision NOT NULL,
	load_capacity double precision NOT NULL,
	passenger_capacity int NOT NULL,
	engine_power double precision NOT NULL
);

CREATE TABLE enterprise (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name varchar(50) NOT NULL,
	city varchar(50) NOT NULL,
	phone varchar(50) NOT NULL
);

CREATE TABLE vehicle (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	registration_number varchar(9) NOT NULL,
	year_of_production int NOT NULL,
	price double precision NOT NULL,
	mileage double precision NOT NULL,
	brand_id int REFERENCES brand(id) ON DELETE SET NULL,
	enterprise_id int REFERENCES enterprise(id) ON DELETE SET NULL
);

CREATE TABLE driver (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name varchar(50) NOT NULL,
	salary double precision NOT NULL,
	enterprise_id int REFERENCES enterprise(id) ON DELETE SET NULL
);

CREATE TABLE driver_vehicle (
	driver_id int REFERENCES driver(id) ON DELETE SET NULL,
	vehicle_id int REFERENCES vehicle(id) ON DELETE SET NULL,
	PRIMARY KEY(driver_id, vehicle_id)
);

INSERT INTO brand (name, type_vehicle, tank_capacity, load_capacity, passenger_capacity, engine_power) VALUES ('Scania', 'BUS', 105, 500, 35, 200);
INSERT INTO brand (name, type_vehicle, tank_capacity, load_capacity, passenger_capacity, engine_power) VALUES ('Man', 'TRUCK', 155, 500, 3, 220);
INSERT INTO brand (name, type_vehicle, tank_capacity, load_capacity, passenger_capacity, engine_power) VALUES ('Lada', 'PASSENGER_CAR', 85, 300, 5, 100);

INSERT INTO enterprise (name, city, phone) VALUES ('Машина мечты','Иваново', '3513');
INSERT INTO enterprise (name, city, phone) VALUES ('Прокат ТС','Суздаль', '4848');
INSERT INTO enterprise (name, city, phone) VALUES ('С ветерком','Углич', '7321');

INSERT INTO vehicle (registration_number, year_of_production, price, mileage, brand_id, enterprise_id) VALUES ('A125CT163', 2000, 300000, 200, 1, 1);
INSERT INTO vehicle (registration_number, year_of_production, price, mileage, brand_id, enterprise_id) VALUES ('C548MK174', 2015, 400000, 100, 2, 1);
INSERT INTO vehicle (registration_number, year_of_production, price, mileage, brand_id, enterprise_id) VALUES ('M732OP97', 2020, 400000, 150, 3, 2);
INSERT INTO vehicle (registration_number, year_of_production, price, mileage, brand_id, enterprise_id) VALUES ('A124CT102', 2014, 600000, 180, 1, 2);
INSERT INTO vehicle (registration_number, year_of_production, price, mileage, brand_id, enterprise_id) VALUES ('K675TC45', 2002, 900000, 130, 2, 3);
INSERT INTO vehicle (registration_number, year_of_production, price, mileage, brand_id, enterprise_id) VALUES ('E421EK99', 2010, 500000, 140, 3, 3);

INSERT INTO driver (name, salary, enterprise_id) VALUES ('Иванов И.К.', 100000, 1);
INSERT INTO driver (name, salary, enterprise_id) VALUES ('Нужных Г.П.', 90000, 1);
INSERT INTO driver (name, salary, enterprise_id) VALUES ('Голованов Д.И.', 50000, 1);
INSERT INTO driver (name, salary, enterprise_id) VALUES ('Майоров Е.В.', 80000, 3);
INSERT INTO driver (name, salary, enterprise_id) VALUES ('Савелин А.Д', 70000, 3);
INSERT INTO driver (name, salary, enterprise_id) VALUES ('Ревко С.Н.', 110000, 3);

CREATE TABLE manager (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	username varchar(50) NOT NULL,
	password varchar NOT NULL
);

INSERT INTO manager (username, password) VALUES ('manager1', 'pass123');
INSERT INTO manager (username, password) VALUES ('manager2', 'pass456');

CREATE TABLE enterprise_manager (
	enterprise_id int REFERENCES enterprise(id) ON DELETE SET NULL,
	manager_id int REFERENCES manager(id) ON DELETE SET NULL,
	PRIMARY KEY(enterprise_id, manager_id)
);

INSERT INTO enterprise_manager (enterprise_id, manager_id) VALUES (1, 1);
INSERT INTO enterprise_manager (enterprise_id, manager_id) VALUES (2, 1);
INSERT INTO enterprise_manager (enterprise_id, manager_id) VALUES (2, 2);
INSERT INTO enterprise_manager (enterprise_id, manager_id) VALUES (3, 2);

SELECT * FROM enterprise;
SELECT * FROM manager;
SELECT * FROM enterprise_manager;

SELECT * FROM vehicle;
