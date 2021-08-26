-- 1
select model, speed, hd from pc where price < 500;

-- 2 
select distinct marker from product where type = "Printer";

-- 3
select model, ram, screen from laptop where price > 1000;

-- 4
select * from printer where color = "y";

-- 5
select model, speed, hd from pc where (cd = "12x" or cd = "24x") and price < 600;

-- 6
select product.marker, laptop.speed from product 
join laptop on product.model = laptop.model where laptop.hd >= 100;

-- 7
select pc.model, pc.price from pc join product on
pc.model = product.model where product.marker = "B"
union
select laptop.model, laptop.price from laptop join product on
laptop.model = product.model where product.marker = "B"
union
select printer.model, printer.price from printer join product on
printer.model = product.model where product.marker = "B";

-- 8


-- 9
select distinct product.marker from pc
join product on pc.model = product.model
where pc.speed >= 450;

-- 10
select model, price from printer where price =
(select MAX(price) from printer)