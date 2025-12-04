
select * from books;
select * from customers;
select * from orders;
select * from order_books;

truncate table books CASCADE;
truncate table customers CASCADE;
truncate table orders CASCADE;
truncate table order_books;

drop table books;
drop table customers;
drop table orders;
drop table order_books;


-- Создание таблиц

create table books (
    id serial primary key,
    title varchar(50) not null,
    author varchar(70),
    status varchar(20) check (status in ('AVAILABLE', 'UNAVAILABLE')),
    price numeric(10,2),
    publication_date date,
    description varchar(255),
    request_count int default 0,
    receipt_date date
);

create table customers (
    id serial primary key,
    name varchar(50),
    email varchar(50) unique
);

create table orders (
    id serial primary key,
    status varchar(20) check (status in ('NEW','COMPLETED','CANCELED')),
    has_pending_requests boolean default false,
    execution_date date,
    amount numeric(10,2),
    customer_id int references customers(id) on delete cascade
);

create table order_books (
    order_id int references orders(id) on delete cascade,
    book_id int references books(id) on delete cascade,
    primary key (order_id, book_id)
);


-- Заполение таблицы

insert into books(title, author, status, price, publication_date, description, request_count, receipt_date) values
('Clean Code', 'Robert C. Martin', 'AVAILABLE', 45.99, '2008-08-01', 'Guide to writing clean code', 2, '2023-01-10'),
('Effective Java', 'Joshua Bloch', 'UNAVAILABLE', 55.00, '2017-05-10', 'Best practices for Java', 4, '2023-02-15'),
('Design Patterns', 'GoF', 'AVAILABLE', 39.50, '1994-10-21', 'Classic OOP patterns', 1, '2023-03-20');

insert into customers(name, email) values
('Alice Johnson', 'alice@example.com'),
('Bob Smith', 'bob@example.com');

insert into orders(status, has_pending_requests, execution_date, amount, customer_id) values
('NEW', false, '2024-01-11', 100.99, 1),
('COMPLETED', false, '2024-01-13', 55.00, 2);

insert into order_books(order_id, book_id) values
(1, 1),
(1, 3),
(2, 2);
