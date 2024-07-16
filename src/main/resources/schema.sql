
CREATE TABLE brand (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    type_vehicle VARCHAR(50) NOT NULL,
    tank_capacity DOUBLE NOT NULL,
    load_capacity DOUBLE NOT NULL,
    passenger_capacity INT NOT NULL,
    engine_power DOUBLE NOT NULL
);

CREATE TABLE driver (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    salary double precision NOT NULL,
    enterprise_id integer,
    is_active boolean DEFAULT false,
    active_vehicle_id integer
);

CREATE TABLE driver_vehicle (
    driver_id integer NOT NULL,
    vehicle_id integer NOT NULL
);

CREATE TABLE enterprise (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    city character varying(50) NOT NULL,
    phone character varying(50) NOT NULL,
    timezone character varying(20)
);

CREATE TABLE enterprise_manager (
    enterprise_id integer NOT NULL,
    manager_id integer NOT NULL
);

CREATE TABLE manager (
    id integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying NOT NULL,
    timezone character varying(20)
);

CREATE TABLE person (
    id integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying NOT NULL,
    role character varying(100) NOT NULL,
    timezone character varying(20)
);

CREATE TABLE trip (
    id integer NOT NULL,
    vehicle_id integer,
    time_start character varying(50),
    time_end character varying(50),
    distance double precision
);

CREATE TABLE vehicle (
    id integer NOT NULL,
    registration_number character varying(9) NOT NULL,
    year_of_production integer NOT NULL,
    price double precision NOT NULL,
    mileage double precision NOT NULL,
    date_of_sale character varying(50),
    brand_id integer,
    enterprise_id integer
);