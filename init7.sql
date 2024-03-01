DROP TABLE driver_vehicle;
DROP TABLE driver;
DROP TABLE vehicle;

CREATE TABLE vehicle (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	registration_number varchar(9) NOT NULL,
	year_of_production int NOT NULL,
	price double precision NOT NULL,
	mileage double precision NOT NULL,
	date_of_sale varchar(50),
	brand_id int REFERENCES brand(id) ON DELETE SET NULL,
	enterprise_id int REFERENCES enterprise(id) ON DELETE SET NULL
);

CREATE TABLE driver (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name varchar(50) NOT NULL,
	salary double precision NOT NULL,
	enterprise_id int REFERENCES enterprise(id) ON DELETE SET NULL,
	is_active boolean DEFAULT FALSE,
	active_vehicle_id int UNIQUE REFERENCES vehicle(id) ON DELETE CASCADE
);

CREATE TABLE driver_vehicle (
	driver_id int REFERENCES driver(id) ON DELETE SET NULL,
	vehicle_id int REFERENCES vehicle(id) ON DELETE SET NULL,
	PRIMARY KEY(driver_id, vehicle_id)
);

INSERT INTO vehicle (registration_number, year_of_production, price, mileage, date_of_sale, brand_id, enterprise_id) VALUES ('A125CT163', 2000, 300000, 200, '2000/06/08 15:00', 1, 1);
INSERT INTO vehicle (registration_number, year_of_production, price, mileage, date_of_sale, brand_id, enterprise_id) VALUES ('C548MK174', 2015, 400000, 100, '2000/06/08 15:00', 2, 1);
INSERT INTO vehicle (registration_number, year_of_production, price, mileage, date_of_sale, brand_id, enterprise_id) VALUES ('M732OP97', 2020, 400000, 150, '2000/06/08 15:00', 3, 2);
INSERT INTO vehicle (registration_number, year_of_production, price, mileage, date_of_sale, brand_id, enterprise_id) VALUES ('A124CT102', 2014, 600000, 180, '2000/06/08 15:00', 1, 2);
INSERT INTO vehicle (registration_number, year_of_production, price, mileage, date_of_sale, brand_id, enterprise_id) VALUES ('K675TC45', 2002, 900000, 130, '2000/06/08 15:00', 2, 3);
INSERT INTO vehicle (registration_number, year_of_production, price, mileage, date_of_sale, brand_id, enterprise_id) VALUES ('E421EK99', 2010, 500000, 140, '2000/06/08 15:00', 3, 3);

UPDATE vehicle SET date_of_sale = '2000/06/08 18:00' WHERE id = 1;
UPDATE vehicle SET date_of_sale = '2020/01/01 15:00' WHERE id = 2;
UPDATE vehicle SET date_of_sale = '2010/03/10 10:00' WHERE id = 3;
UPDATE vehicle SET date_of_sale = '2015/07/30 05:00' WHERE id = 4;
UPDATE vehicle SET date_of_sale = '2005/12/25 17:00' WHERE id = 5;
UPDATE vehicle SET date_of_sale = '2012/05/17 08:00' WHERE id = 6;


INSERT INTO driver (name, salary, enterprise_id) VALUES ('Иванов И.К.', 100000, 1);
INSERT INTO driver (name, salary, enterprise_id) VALUES ('Нужных Г.П.', 90000, 1);
INSERT INTO driver (name, salary, enterprise_id) VALUES ('Голованов Д.И.', 50000, 1);
INSERT INTO driver (name, salary, enterprise_id) VALUES ('Майоров Е.В.', 80000, 3);
INSERT INTO driver (name, salary, enterprise_id) VALUES ('Савелин А.Д', 70000, 3);
INSERT INTO driver (name, salary, enterprise_id) VALUES ('Ревко С.Н.', 110000, 3);

SELECT * FROM manager;
SELECT * FROM person;

ALTER TABLE manager ADD COLUMN timezone VARCHAR (20);
UPDATE manager SET timezone = '-02:00' WHERE username = 'manager1';
UPDATE manager SET timezone = '+02:00' WHERE username = 'manager2';

ALTER TABLE person ADD COLUMN timezone VARCHAR (20);
UPDATE person SET timezone = '-02:00' WHERE username = 'manager1';
UPDATE person SET timezone = '+02:00' WHERE username = 'manager2';

SELECT * FROM enterprise;
ALTER TABLE enterprise ADD COLUMN timezone VARCHAR (20);
UPDATE enterprise SET timezone = '+03:00' WHERE id = 1;
UPDATE enterprise SET timezone = '-03:00' WHERE id = 2;
UPDATE enterprise SET timezone = '+00:00' WHERE id = 3;

так ошибки!
UPDATE person SET timezone = '00:00' WHERE username = 'manager1'; 
UPDATE person SET timezone = '+1:00' WHERE username = 'manager2';

CREATE EXTENSION postgis;

CREATE TABLE track (
    id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    vehicle_id int REFERENCES vehicle(id) ON DELETE SET NULL,
    location GEOMETRY(Point, 4326)
);

INSERT INTO track (vehicle_id, location) VALUES (1, ST_GeomFromText('POINT(40.7128 -74.0060)', 4326));
INSERT INTO track (vehicle_id, location) VALUES (1, ST_GeomFromText('POINT(34.0522 -118.2437)', 4326));
INSERT INTO track (vehicle_id, location) VALUES (1, ST_GeomFromText('POINT(51.5074 -0.1278)', 4326));

