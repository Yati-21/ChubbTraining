CREATE TABLE IF NOT EXISTS flight (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  airline VARCHAR(255) NOT NULL,
  flight_number VARCHAR(255) NOT NULL UNIQUE,
  from_city VARCHAR(10) NOT NULL,
  to_city VARCHAR(10) NOT NULL,
  departure_time DATETIME NOT NULL,
  arrival_time DATETIME NOT NULL,
  total_seats INT NOT NULL,
  available_seats INT NOT NULL,
  price DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS booking (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  pnr VARCHAR(50),
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  seats_booked INT NOT NULL,
  meal_type VARCHAR(20),
  flight_id BIGINT,
  CONSTRAINT fk_booking_flight FOREIGN KEY (flight_id) REFERENCES flight(id)
);

CREATE TABLE IF NOT EXISTS passenger (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  gender CHAR(1),
  age INT,
  seat_number VARCHAR(50),
  booking_id BIGINT,
  flight_id BIGINT,
  CONSTRAINT fk_passenger_booking FOREIGN KEY (booking_id) REFERENCES booking(id),
  UNIQUE (seat_number, booking_id)
);
