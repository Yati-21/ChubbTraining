CREATE TABLE IF NOT EXISTS FLIGHT (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    airline VARCHAR(50),
    flight_number VARCHAR(20),
    origin VARCHAR(20),
    destination VARCHAR(20),
    date VARCHAR(20),
    time VARCHAR(20),
    price FLOAT,
    round_trip_price FLOAT
);

CREATE TABLE IF NOT EXISTS BOOKING (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    flight_id INTEGER,
    pnr VARCHAR(20),
    email VARCHAR(100),
    seats INTEGER,
    meal VARCHAR(20),
    seat_numbers VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS PASSENGER (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    booking_id INTEGER,
    name VARCHAR(50),
    gender VARCHAR(10),
    age INTEGER
);
