CREATE ROLE postgres WITH LOGIN PASSWORD 'pass12345';

--
-- PostgreSQL database dump
--

-- Dumped from database version 14.11 (Ubuntu 14.11-0ubuntu0.22.04.1)
-- Dumped by pg_dump version 16.2 (Ubuntu 16.2-1.pgdg22.04+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO postgres;

--
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry and geography spatial types and functions';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: brand; Type: TABLE; Schema: public; Owner: username
--

CREATE TABLE public.brand (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    type_vehicle character varying(50) NOT NULL,
    tank_capacity double precision NOT NULL,
    load_capacity double precision NOT NULL,
    passenger_capacity integer NOT NULL,
    engine_power double precision NOT NULL
);


ALTER TABLE public.brand OWNER TO username;

--
-- Name: brand_id_seq; Type: SEQUENCE; Schema: public; Owner: username
--

ALTER TABLE public.brand ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.brand_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: driver; Type: TABLE; Schema: public; Owner: username
--

CREATE TABLE public.driver (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    salary double precision NOT NULL,
    enterprise_id integer,
    is_active boolean DEFAULT false,
    active_vehicle_id integer
);


ALTER TABLE public.driver OWNER TO username;

--
-- Name: driver_id_seq; Type: SEQUENCE; Schema: public; Owner: username
--

ALTER TABLE public.driver ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.driver_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: driver_vehicle; Type: TABLE; Schema: public; Owner: username
--

CREATE TABLE public.driver_vehicle (
    driver_id integer NOT NULL,
    vehicle_id integer NOT NULL
);


ALTER TABLE public.driver_vehicle OWNER TO username;

--
-- Name: enterprise; Type: TABLE; Schema: public; Owner: username
--

CREATE TABLE public.enterprise (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    city character varying(50) NOT NULL,
    phone character varying(50) NOT NULL,
    timezone character varying(20)
);


ALTER TABLE public.enterprise OWNER TO username;

--
-- Name: enterprise_id_seq; Type: SEQUENCE; Schema: public; Owner: username
--

ALTER TABLE public.enterprise ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.enterprise_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: enterprise_manager; Type: TABLE; Schema: public; Owner: username
--

CREATE TABLE public.enterprise_manager (
    enterprise_id integer NOT NULL,
    manager_id integer NOT NULL
);


ALTER TABLE public.enterprise_manager OWNER TO username;

--
-- Name: manager; Type: TABLE; Schema: public; Owner: username
--

CREATE TABLE public.manager (
    id integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying NOT NULL,
    timezone character varying(20)
);


ALTER TABLE public.manager OWNER TO username;

--
-- Name: manager_id_seq; Type: SEQUENCE; Schema: public; Owner: username
--

ALTER TABLE public.manager ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.manager_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: person; Type: TABLE; Schema: public; Owner: username
--

CREATE TABLE public.person (
    id integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying NOT NULL,
    role character varying(100) NOT NULL,
    timezone character varying(20)
);


ALTER TABLE public.person OWNER TO username;

--
-- Name: person_id_seq; Type: SEQUENCE; Schema: public; Owner: username
--

ALTER TABLE public.person ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.person_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: track; Type: TABLE; Schema: public; Owner: username
--

CREATE TABLE public.track (
    id integer NOT NULL,
    vehicle_id integer,
    coordinates public.geometry(Point,4326),
    time_point character varying(50)
);


ALTER TABLE public.track OWNER TO username;

--
-- Name: track_id_seq; Type: SEQUENCE; Schema: public; Owner: username
--

ALTER TABLE public.track ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.track_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: trip; Type: TABLE; Schema: public; Owner: username
--

CREATE TABLE public.trip (
    id integer NOT NULL,
    vehicle_id integer,
    time_start character varying(50),
    time_end character varying(50),
    distance double precision
);


ALTER TABLE public.trip OWNER TO username;

--
-- Name: trip_id_seq; Type: SEQUENCE; Schema: public; Owner: username
--

ALTER TABLE public.trip ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.trip_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: vehicle; Type: TABLE; Schema: public; Owner: username
--

CREATE TABLE public.vehicle (
    id integer NOT NULL,
    registration_number character varying(9) NOT NULL,
    year_of_production integer NOT NULL,
    price double precision NOT NULL,
    mileage double precision NOT NULL,
    date_of_sale character varying(50),
    brand_id integer,
    enterprise_id integer
);


ALTER TABLE public.vehicle OWNER TO username;

--
-- Name: vehicle_id_seq; Type: SEQUENCE; Schema: public; Owner: username
--

ALTER TABLE public.vehicle ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.vehicle_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Data for Name: brand; Type: TABLE DATA; Schema: public; Owner: username
--

COPY public.brand (id, name, type_vehicle, tank_capacity, load_capacity, passenger_capacity, engine_power) FROM stdin;
1	Scania	BUS	105	500	35	200
2	Man	TRUCK	155	500	3	220
3	Lada	PASSENGER_CAR	85	300	5	100
\.


--
-- Data for Name: driver; Type: TABLE DATA; Schema: public; Owner: username
--

COPY public.driver (id, name, salary, enterprise_id, is_active, active_vehicle_id) FROM stdin;
1	Иванов И.К.	100000	1	f	\N
2	Нужных Г.П.	90000	1	f	\N
3	Голованов Д.И.	50000	1	f	\N
4	Майоров Е.В.	80000	3	f	\N
5	Савелин А.Д	70000	3	f	\N
6	Ревко С.Н.	110000	3	f	\N
\.


--
-- Data for Name: driver_vehicle; Type: TABLE DATA; Schema: public; Owner: username
--

COPY public.driver_vehicle (driver_id, vehicle_id) FROM stdin;
\.


--
-- Data for Name: enterprise; Type: TABLE DATA; Schema: public; Owner: username
--

COPY public.enterprise (id, name, city, phone, timezone) FROM stdin;
3	С ветерком	Углич	7321	+00:00
2	Прокат ТС	Суздаль	4848	-04:00
1	Машина мечты	Иваново	3513	-06:00
\.


--
-- Data for Name: enterprise_manager; Type: TABLE DATA; Schema: public; Owner: username
--

COPY public.enterprise_manager (enterprise_id, manager_id) FROM stdin;
1	1
2	1
2	2
3	2
\.


--
-- Data for Name: manager; Type: TABLE DATA; Schema: public; Owner: username
--

COPY public.manager (id, username, password, timezone) FROM stdin;
1	manager1	$2a$10$YFVdDAgSEPcY7kUzI0N8d.kUFvmF4gOfEOmX0ZlzMsIVozqmO0wMO	-02:00
2	manager2	$2a$10$ivCWUYla7sdak/BeFj/yLOOlln4VIjrZd9wb.jta3nYh84uKJf2X.	+02:00
\.


--
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: username
--

COPY public.person (id, username, password, role, timezone) FROM stdin;
3	user	$2a$10$aUasnkQNFtrv/80J5vR7qufJVGwDgS1FOjIEwMJLwM06fhArWqjDS	ROLE_USER	\N
1	manager1	$2a$10$YFVdDAgSEPcY7kUzI0N8d.kUFvmF4gOfEOmX0ZlzMsIVozqmO0wMO	ROLE_MANAGER1	-02:00
2	manager2	$2a$10$ivCWUYla7sdak/BeFj/yLOOlln4VIjrZd9wb.jta3nYh84uKJf2X.	ROLE_MANAGER2	+02:00
\.


--
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: username
--

COPY public.spatial_ref_sys (srid, auth_name, auth_srid, srtext, proj4text) FROM stdin;
\.


--
-- Data for Name: track; Type: TABLE DATA; Schema: public; Owner: username
--

COPY public.track (id, vehicle_id, coordinates, time_point) FROM stdin;
\.


--
-- Data for Name: trip; Type: TABLE DATA; Schema: public; Owner: username
--

COPY public.trip (id, vehicle_id, time_start, time_end, distance) FROM stdin;
\.


--
-- Data for Name: vehicle; Type: TABLE DATA; Schema: public; Owner: username
--

COPY public.vehicle (id, registration_number, year_of_production, price, mileage, date_of_sale, brand_id, enterprise_id) FROM stdin;
5	K675TC45	2002	900000	130	2005/12/25 17:00	2	3
6	E421EK99	2010	500000	140	2012/05/17 08:00	3	3
1	A125CT163	2000	300000	200	2000/06/08 00:00	1	1
2	C548MK174	2015	400000	100	2020/01/01 24:00	2	1
3	M732OP97	2020	400000	150	2010/03/10 23:00	3	2
4	A124CT102	2014	600000	180	2015/07/30 01:00	1	2
\.


--
-- Name: brand_id_seq; Type: SEQUENCE SET; Schema: public; Owner: username
--

SELECT pg_catalog.setval('public.brand_id_seq', 3, true);


--
-- Name: driver_id_seq; Type: SEQUENCE SET; Schema: public; Owner: username
--

SELECT pg_catalog.setval('public.driver_id_seq', 6, true);


--
-- Name: enterprise_id_seq; Type: SEQUENCE SET; Schema: public; Owner: username
--

SELECT pg_catalog.setval('public.enterprise_id_seq', 3, true);


--
-- Name: manager_id_seq; Type: SEQUENCE SET; Schema: public; Owner: username
--

SELECT pg_catalog.setval('public.manager_id_seq', 2, true);


--
-- Name: person_id_seq; Type: SEQUENCE SET; Schema: public; Owner: username
--

SELECT pg_catalog.setval('public.person_id_seq', 3, true);


--
-- Name: track_id_seq; Type: SEQUENCE SET; Schema: public; Owner: username
--

SELECT pg_catalog.setval('public.track_id_seq', 1, false);


--
-- Name: trip_id_seq; Type: SEQUENCE SET; Schema: public; Owner: username
--

SELECT pg_catalog.setval('public.trip_id_seq', 1, false);


--
-- Name: vehicle_id_seq; Type: SEQUENCE SET; Schema: public; Owner: username
--

SELECT pg_catalog.setval('public.vehicle_id_seq', 6, true);


--
-- Name: brand brand_pkey; Type: CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.brand
    ADD CONSTRAINT brand_pkey PRIMARY KEY (id);


--
-- Name: driver driver_active_vehicle_id_key; Type: CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.driver
    ADD CONSTRAINT driver_active_vehicle_id_key UNIQUE (active_vehicle_id);


--
-- Name: driver driver_pkey; Type: CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.driver
    ADD CONSTRAINT driver_pkey PRIMARY KEY (id);


--
-- Name: driver_vehicle driver_vehicle_pkey; Type: CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.driver_vehicle
    ADD CONSTRAINT driver_vehicle_pkey PRIMARY KEY (driver_id, vehicle_id);


--
-- Name: enterprise_manager enterprise_manager_pkey; Type: CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.enterprise_manager
    ADD CONSTRAINT enterprise_manager_pkey PRIMARY KEY (enterprise_id, manager_id);


--
-- Name: enterprise enterprise_pkey; Type: CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.enterprise
    ADD CONSTRAINT enterprise_pkey PRIMARY KEY (id);


--
-- Name: manager manager_pkey; Type: CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.manager
    ADD CONSTRAINT manager_pkey PRIMARY KEY (id);


--
-- Name: person person_pkey; Type: CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);


--
-- Name: track track_pkey; Type: CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.track
    ADD CONSTRAINT track_pkey PRIMARY KEY (id);


--
-- Name: trip trip_pkey; Type: CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.trip
    ADD CONSTRAINT trip_pkey PRIMARY KEY (id);


--
-- Name: vehicle vehicle_pkey; Type: CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.vehicle
    ADD CONSTRAINT vehicle_pkey PRIMARY KEY (id);


--
-- Name: driver driver_active_vehicle_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.driver
    ADD CONSTRAINT driver_active_vehicle_id_fkey FOREIGN KEY (active_vehicle_id) REFERENCES public.vehicle(id) ON DELETE CASCADE;


--
-- Name: driver driver_enterprise_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.driver
    ADD CONSTRAINT driver_enterprise_id_fkey FOREIGN KEY (enterprise_id) REFERENCES public.enterprise(id) ON DELETE SET NULL;


--
-- Name: driver_vehicle driver_vehicle_driver_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.driver_vehicle
    ADD CONSTRAINT driver_vehicle_driver_id_fkey FOREIGN KEY (driver_id) REFERENCES public.driver(id) ON DELETE SET NULL;


--
-- Name: driver_vehicle driver_vehicle_vehicle_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.driver_vehicle
    ADD CONSTRAINT driver_vehicle_vehicle_id_fkey FOREIGN KEY (vehicle_id) REFERENCES public.vehicle(id) ON DELETE SET NULL;


--
-- Name: enterprise_manager enterprise_manager_enterprise_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.enterprise_manager
    ADD CONSTRAINT enterprise_manager_enterprise_id_fkey FOREIGN KEY (enterprise_id) REFERENCES public.enterprise(id) ON DELETE SET NULL;


--
-- Name: enterprise_manager enterprise_manager_manager_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.enterprise_manager
    ADD CONSTRAINT enterprise_manager_manager_id_fkey FOREIGN KEY (manager_id) REFERENCES public.manager(id) ON DELETE SET NULL;


--
-- Name: track track_vehicle_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.track
    ADD CONSTRAINT track_vehicle_id_fkey FOREIGN KEY (vehicle_id) REFERENCES public.vehicle(id) ON DELETE SET NULL;


--
-- Name: trip trip_vehicle_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.trip
    ADD CONSTRAINT trip_vehicle_id_fkey FOREIGN KEY (vehicle_id) REFERENCES public.vehicle(id) ON DELETE SET NULL;


--
-- Name: vehicle vehicle_brand_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.vehicle
    ADD CONSTRAINT vehicle_brand_id_fkey FOREIGN KEY (brand_id) REFERENCES public.brand(id) ON DELETE SET NULL;


--
-- Name: vehicle vehicle_enterprise_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.vehicle
    ADD CONSTRAINT vehicle_enterprise_id_fkey FOREIGN KEY (enterprise_id) REFERENCES public.enterprise(id) ON DELETE SET NULL;


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

