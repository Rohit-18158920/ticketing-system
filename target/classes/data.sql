DROP TABLE IF EXISTS Agent;

create TABLE Agent(
	id long AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(250) NOT NULL,
	email VARCHAR(250) NOT NULL,
	ticket_assigned int DEFAULT 0
);
INSERT INTO Agent (name, email) VALUES
('Rohit Kumar','rohitnet20@gmail.com'),
('RK','rk86207@gmail.com'),
('Ravi','rvrcks@gmail.com'),
('Palash','palash@gmail.com'),
('Test','testing@gmail.com'),
('Mukesh','mukesh@gmail.com'),
('Adarsh','adarsh@gmail.com'),
('Rajesh','rajesh@gmail.com'),
('Anubhav','anubhav@gmail.com'),
('Bittu','bittu@gmail.com')
;