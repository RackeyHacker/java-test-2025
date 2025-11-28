select * from product;
select * from pc;
select * from laptop;
select * from printer;


-- Задание 2

-- 1
select code, speed, hd
from pc
where price::numeric < 500;

-- 2
select distinct p2.maker
from printer p1
left join  product p2 on p1.model = p2.model
order by maker;

-- 3
select model, ram, screen
from laptop
where price::numeric > 1000;

-- 4
select *
from printer
where color = 'y';

-- 5
select code, speed, hd
from pc
where cd in ('12x', '24x') and price::numeric < 600;

-- 6
select p.maker, l.speed
from laptop l
left join product p on p.model = l.model
where hd >= 100;

-- 7
select p1.model, coalesce (p2.price, l.price, p3.price) as price
from product p1
left join pc p2 on p1.model = p2.model
left join laptop l on p1.model = l.model
left join printer p3 on p1.model = p3.model
where p1.maker = 'B';

-- 8
select maker
from product
where type = 'PC'
except
select maker
from product
where type = 'Laptop';

-- 9
select distinct p1.maker
from product p1
join pc p2 on p1.model = p2.model
where p2.speed >= 450;

-- 10
select model, price
from printer
where price = (select max(price) from printer);

-- 11
select avg(speed) as avg_speed
from pc;

-- 12
select avg(speed) as avg_speed
from laptop
where price::numeric > 1000;

-- 13
select avg(speed)
from pc p1
join product p2 on p1.model = p2.model
where p2.maker = 'A';

-- 14
select speed, avg(price::numeric)
from pc
group by speed;

-- 15
select distinct p1.hd
from pc p1
join pc p2 on p1.hd = p2.hd and p1.code != p2.code;

-- 16
select p1.model, p2.model
from pc p1
join pc p2 on p1.speed = p2.speed and
p1.ram = p2.ram and p1.code < p2.code;

-- 17
select 'Laptop' as type, model, speed
from laptop
where speed < (select min(speed) from pc);

-- 18
select p1.maker, p2.price
from product p1
join printer p2 on p1.model = p2.model
where p2.color = 'y' and price = (select min(price) from printer where color='y');

-- 19
select p.maker, avg(screen)
from laptop l
left join product p on p.model = l.model
group by p.maker;

-- 20
select maker
from product
where type = 'PC'
group by maker 
having count(model) >= 3;

-- 21
select p1.maker, max(p2.price)
from product p1
join pc p2 on p1.model = p2.model
group by p1.maker;

-- 22
select speed, avg(price::numeric)
from pc
where speed > 600
group by speed;

-- 23
select p1.maker
from product p1
left join pc p2 on p1.model = p2.model and p2.speed >= 750
left join laptop l on l.model = p1.model and l.speed >= 750
group by p1.maker
having count (p2.model) > 0 and count (l.model) > 0;

-- 24
with all_prices as (
    select model, price::numeric as price from pc
    union all
    select model, price::numeric from laptop
    union all
    select model, price::numeric from printer
)
select model
from all_prices
where price = (select max(price) from all_prices);

-- 25
with lowest_ram as (
	select min (ram) from pc
),
fastest_cpu as (
	select max(speed) from pc
	where ram = (select * from lowest_ram)
),
target_models as (
	select model from pc
	where speed = (select * from fastest_cpu) and
	ram = (select * from lowest_ram)
)
select p1.maker from product p1
join product p2 on p1.maker = p2.maker
where p1.type = 'Printer' and
p2.model in (select model from target_models) and
p2.type = 'PC';



