DROP DATABASE IF EXISTS caloriemate_database;
CREATE DATABASE caloriemate_database;
USE caloriemate_database;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL
);

CREATE TABLE foods (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    calories INT NOT NULL,
    category VARCHAR(50),
    date DATE,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE journals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    note TEXT,
    mood VARCHAR(50),
    date DATE,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE weights (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    weight DOUBLE NOT NULL,
    date DATE,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO users (username, password, email)
VALUES ('ahsan', '123456', 'ahsan@example.com');

INSERT INTO foods (user_id, name, calories, category, date, is_deleted)
VALUES 
    ((SELECT id FROM users WHERE username = 'ahsan'), 'Nasi Goreng', 500, 'Dinner', CURDATE(), FALSE),
    ((SELECT id FROM users WHERE username = 'ahsan'), 'Ayam Goreng', 300, 'Lunch', CURDATE(), FALSE),
    ((SELECT id FROM users WHERE username = 'ahsan'), 'Apel', 100, 'Snack', CURDATE(), FALSE);

INSERT INTO journals (user_id, note, mood, date, is_deleted)
VALUES 
    ((SELECT id FROM users WHERE username = 'ahsan'), 'Hari ini makan sehat!', 'Happy', CURDATE(), FALSE),
    ((SELECT id FROM users WHERE username = 'ahsan'), 'Lelah setelah olahraga', 'Neutral', DATE_SUB(CURDATE(), INTERVAL 1 DAY), FALSE);

INSERT INTO weights (user_id, weight, date, is_deleted)
VALUES 
    ((SELECT id FROM users WHERE username = 'ahsan'), 70.5, CURDATE(), FALSE),
    ((SELECT id FROM users WHERE username = 'ahsan'), 71.0, DATE_SUB(CURDATE(), INTERVAL 1 DAY), FALSE);