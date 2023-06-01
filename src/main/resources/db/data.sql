--CREATING CARS
insert into CARS (FIPE_VALUE, CAR_YEAR, MANUFACTURER, MODEL)
values (40000.00, '2015', 'VOLKSWAGEN', 'FOX');

insert into CARS (FIPE_VALUE, CAR_YEAR, MANUFACTURER, MODEL)
values (35000.00, '2015', 'UP', 'FOX');

insert into CARS (FIPE_VALUE, CAR_YEAR, MANUFACTURER, MODEL)
values (220000.00, '2023', 'TOYOTA', 'COROLLA');

insert into CARS (FIPE_VALUE, CAR_YEAR, MANUFACTURER, MODEL)
values (180000.00, '2020', 'TOYOTA', 'COROLLA');

insert into CARS (FIPE_VALUE, CAR_YEAR, MANUFACTURER, MODEL)
values (120000.00, '2018', 'CIVIC', 'HONDA');

insert into CARS (FIPE_VALUE, CAR_YEAR, MANUFACTURER, MODEL)
values (250000.00, '2023', 'CIVIC', 'HONDA');

insert into CARS (FIPE_VALUE, CAR_YEAR, MANUFACTURER, MODEL)
values (170000.00, '2022', 'CIVIC', 'HONDA');

insert into CARS (FIPE_VALUE, CAR_YEAR, MANUFACTURER, MODEL)
values (12000.00, '2009', 'CELTA', 'CHEVROLET');

insert into CARS (FIPE_VALUE, CAR_YEAR, MANUFACTURER, MODEL)
values (80000.00, '2022', 'ONIX', 'CHEVROLET');

insert into CARS (FIPE_VALUE, CAR_YEAR, MANUFACTURER, MODEL)
values (60000.00, '2020', 'ONIX', 'CHEVROLET');
-------------------------------------------------------------10

--CREATING DRIVERS and CUSTOMERS

insert into DRIVERS (BIRTHDATE, DOCUMENT)
values (DATE '1996-10-23', 'CNH-123456789');

insert into CUSTOMER (DRIVER_ID, NAME)
values ((select id from DRIVERS where DOCUMENT = 'CNH-123456789'), 'Klayton');

insert into DRIVERS (BIRTHDATE, DOCUMENT)
values (DATE '2000-01-23', 'CNH-123456');

insert into CUSTOMER (DRIVER_ID, NAME)
values ((select id from DRIVERS where DOCUMENT = 'CNH-123456'), 'Jubileu');

insert into DRIVERS (BIRTHDATE, DOCUMENT)
values (DATE '2003-01-23', 'CNH-20030123');

insert into CUSTOMER (DRIVER_ID, NAME)
values ((select id from DRIVERS where DOCUMENT = 'CNH-20030123'), 'Nala');

insert into DRIVERS (BIRTHDATE, DOCUMENT)
values (DATE '2005-01-23', 'CNH-20050123');

insert into CUSTOMER (DRIVER_ID, NAME)
values ((select id from DRIVERS where DOCUMENT = 'CNH-20050123'), 'Belinha');

insert into DRIVERS (BIRTHDATE, DOCUMENT)
values (DATE '1999-01-23', 'CNH-19990123');

insert into CUSTOMER (DRIVER_ID, NAME)
values ((select id from DRIVERS where DOCUMENT = 'CNH-19990123'), 'Kiara');

insert into DRIVERS (BIRTHDATE, DOCUMENT)
values (DATE '1980-01-23', 'CNH-19800123');

insert into CUSTOMER (DRIVER_ID, NAME)
values ((select id from DRIVERS where DOCUMENT = 'CNH-19800123'), 'Mufasa');

insert into DRIVERS (BIRTHDATE, DOCUMENT)
values (DATE '1990-01-23', 'CNH-19900123');

insert into CUSTOMER (DRIVER_ID, NAME)
values ((select id from DRIVERS where DOCUMENT = 'CNH-19900123'), 'Simba');

insert into DRIVERS (BIRTHDATE, DOCUMENT)
values (DATE '1995-01-23', 'CNH-19950123');

insert into CUSTOMER (DRIVER_ID, NAME)
values ((select id from DRIVERS where DOCUMENT = 'CNH-19950123'), 'Scar');

-------------------------------------------------------------16

--CREATING CAR_DRIVERS

--FOX 2015, Klayton -motorista principal, Nala -outro motorista
insert into CAR_DRIVERS (IS_MAIN_DRIVER, CAR_ID, DRIVER_ID)
values (true,
        (select id from CARS where FIPE_VALUE = 40000.00 AND MANUFACTURER LIKE 'VOLKSWAGEN' AND CAR_YEAR LIKE '2015' AND MODEL LIKE 'FOX'),
        (select id from DRIVERS where DOCUMENT = 'CNH-123456789')
 );
insert into CAR_DRIVERS (IS_MAIN_DRIVER, CAR_ID, DRIVER_ID)
values (false,
        (select id from CARS where FIPE_VALUE = 40000.00 AND MANUFACTURER LIKE 'VOLKSWAGEN' AND CAR_YEAR LIKE '2015' AND MODEL LIKE 'FOX'),
        (select id from DRIVERS where DOCUMENT = 'CNH-20030123')
);

--UP 2015, Jubileu -motorista principal, Belinha -outro motorista
insert into CAR_DRIVERS (IS_MAIN_DRIVER, CAR_ID, DRIVER_ID)
values (true,
        (select id from CARS where FIPE_VALUE = 35000.00 AND MANUFACTURER LIKE 'UP' AND CAR_YEAR LIKE '2015' AND MODEL LIKE 'FOX'),
        (select id from DRIVERS where DOCUMENT = 'CNH-123456')
);
insert into CAR_DRIVERS (IS_MAIN_DRIVER, CAR_ID, DRIVER_ID)
values (false,
        (select id from CARS where FIPE_VALUE = 35000.00 AND MANUFACTURER LIKE 'UP' AND CAR_YEAR LIKE '2015' AND MODEL LIKE 'FOX'),
        (select id from DRIVERS where DOCUMENT = 'CNH-20050123')
);

--ONIX 2022, Mufasa -motorista principal, Scar -outro motorista
insert into CAR_DRIVERS (IS_MAIN_DRIVER, CAR_ID, DRIVER_ID)
values (true,
        (select id from CARS where FIPE_VALUE = 80000.00 AND MANUFACTURER LIKE 'ONIX' AND CAR_YEAR LIKE '2022' AND MODEL LIKE 'CHEVROLET'),
        (select id from DRIVERS where DOCUMENT = 'CNH-19800123')
);
insert into CAR_DRIVERS (IS_MAIN_DRIVER, CAR_ID, DRIVER_ID)
values (false,
        (select id from CARS where FIPE_VALUE = 80000.00 AND MANUFACTURER LIKE 'ONIX' AND CAR_YEAR LIKE '2022' AND MODEL LIKE 'CHEVROLET'),
        (select id from DRIVERS where DOCUMENT = 'CNH-19950123')
);


--CELTA 2009, Simba --motorista principal
insert into CAR_DRIVERS (IS_MAIN_DRIVER, CAR_ID, DRIVER_ID)
values (true,
        (select id from CARS where FIPE_VALUE = 12000.00 AND MANUFACTURER LIKE 'CELTA' AND CAR_YEAR LIKE '2009' AND MODEL LIKE 'CHEVROLET'),
        (select id from DRIVERS where DOCUMENT = 'CNH-19900123')
);



--CIVIC 2018, Kiara --motorista principal
insert into CAR_DRIVERS (IS_MAIN_DRIVER, CAR_ID, DRIVER_ID)
values (true,
        (select id from CARS where FIPE_VALUE = 120000.00 AND MANUFACTURER LIKE 'CIVIC' AND CAR_YEAR LIKE '2018' AND MODEL LIKE 'HONDA'),
        (select id from DRIVERS where DOCUMENT = 'CNH-19990123')
);

-------------------------------------------------------------

--Criando Claims

--CELTA 2009 - Sinistro
insert into CLAIMS (EVENT_DATE, CAR_ID)
values (DATE '2020-01-23',
        (select id from CARS where FIPE_VALUE = 12000.00 AND MANUFACTURER LIKE 'CELTA' AND CAR_YEAR LIKE '2009' AND MODEL LIKE 'CHEVROLET')
);

--CIVIC 2018 e Kiara (motorista) - Sinistro
insert into CLAIMS (EVENT_DATE, CAR_ID, DRIVER_ID)
values (DATE '2020-01-23',
        (select id from CARS where FIPE_VALUE = 120000.00 AND MANUFACTURER LIKE 'CIVIC' AND CAR_YEAR LIKE '2018' AND MODEL LIKE 'HONDA'),
        (select id from DRIVERS where DOCUMENT = 'CNH-19990123')
);

-------------------------------------------------------------
