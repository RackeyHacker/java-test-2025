select * from product;
select * from pc;
select * from laptop;
select * from printer;


-- Задание 1

-- Создание таблиц

create table product (
	maker varchar(10),
	model varchar(50) primary key,
	type varchar(50) check (type in ('PC', 'Laptop', 'Printer'))
);


create table pc (
	code int primary key,
	model varchar(50) references product(model),
	speed smallint,
	ram smallint,
	hd real,
	cd varchar(10),
	price money
);

create table laptop (
	code int primary key,
	model varchar(50) references product(model),
	speed smallint,
	ram smallint,
	hd real,
	price money,
	screen smallint
);


create table printer (
	code int primary key,
	model varchar(50) references product(model),
	color char(1) check (color in ('y', 'n')),
	type varchar(10) check (type in ('Laser', 'Jet', 'Matrix')),
	price money
);

-- Заполнение таблиц данными

INSERT INTO product(maker, model, type) VALUES
('A','p1','PC'), ('A','p2','PC'), ('B','p3','PC'), ('C','p4','PC'),
('D','p5','PC'), ('D','p6','PC'), ('D','p7','PC'), ('E','p8','PC'),
('F','p9','PC'), ('G','p10','PC'),

('A','l1','Laptop'), ('B','l2','Laptop'), ('H','l3','Laptop'),
('D','l4','Laptop'), ('I','l5','Laptop'), ('J','l6','Laptop'),
('A','l7','Laptop'), ('E','l8','Laptop'), ('F','l9','Laptop'),
('G','l10','Laptop'),

('A','r1','Printer'), ('B','r2','Printer'), ('C','r3','Printer'),
('D','r4','Printer'), ('E','r5','Printer'), ('F','r6','Printer'),
('G','r7','Printer'), ('H','r8','Printer'), ('I','r9','Printer'),
('J','r10','Printer');



INSERT INTO pc(code, model, speed, ram, hd, cd, price) VALUES
(1, 'p1', 800, 512, 200, '48x', 1200.00),
(2, 'p2', 750, 256, 120, '24x', 950.00),
(3, 'p3', 400, 128, 40,  '12x', 450.00),
(4, 'p4', 300, 64,  20,  '4x',  350.00),
(5, 'p5', 600, 256, 120, '24x', 580.00),
(6, 'p6', 600, 256, 120, '24x', 580.00),
(7, 'p7', 450, 128, 80,  '8x',  700.00),
(8, 'p8', 200, 32,  10,  '4x',  300.00),
(9, 'p9', 300, 64,  50,  '12x', 550.00),
(10,'p10',1000,1024,500, '52x', 5000.00);



INSERT INTO laptop(code, model, speed, ram, hd, price, screen) VALUES
(1,'l1', 800, 512, 250, 2000.00, 17),
(2,'l2', 850, 512, 500, 5000.00, 15),
(3,'l3', 90,  64,  80,  400.00,  13),
(4,'l4', 700, 256, 120, 1200.00, 15),
(5,'l5', 750, 128, 100, 1100.00, 14),
(6,'l6', 100, 32,  40,  300.00,  12),
(7,'l7', 800, 256, 200, 1500.00, 16),
(8,'l8', 650, 128, 80,  900.00,  13),
(9,'l9', 760, 64,  100, 1300.00, 15),
(10,'l10',1000,1024,500,5000.00, 18);



INSERT INTO printer(code, model, color, type, price) VALUES
(1,'r1','y','Laser',300.00),
(2,'r2','n','Jet',150.00),
(3,'r3','y','Jet',120.00),
(4,'r4','y','Laser',120.00),
(5,'r5','n','Matrix',80.00),
(6,'r6','y','Laser',200.00),
(7,'r7','y','Jet',5000.00),
(8,'r8','n','Jet',90.00),
(9,'r9','y','Matrix',120.00),
(10,'r10','y','Laser',250.00);

