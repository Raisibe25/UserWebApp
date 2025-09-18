CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password_hash VARCHAR(60) NOT NULL,
  full_name VARCHAR(100),
  email VARCHAR(100) UNIQUE,
  role ENUM('USER','ADMIN') DEFAULT 'USER'
);
INSERT INTO users (username, password_hash, role)
VALUES ('admin', '$2a$10$YkqR1Ie5w22rH3O.65AThOm0NBXmZlyRmBq24.5LTc5XMpaEaIOoO', 'ADMIN');
