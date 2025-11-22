CREATE TABLE IF NOT EXISTS flight (
  id INT AUTO_INCREMENT PRIMARY KEY,
  flight_number VARCHAR(50),
  origin VARCHAR(50),
  destination VARCHAR(50),
  departure_time VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS booking (
  id INT AUTO_INCREMENT PRIMARY KEY,
  pnr VARCHAR(50),
  flight_id INT,
  booking_date VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS passenger (
  id INT AUTO_INCREMENT PRIMARY KEY,
  booking_id INT,
  name VARCHAR(100),
  age INT
);
