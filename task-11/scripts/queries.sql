USE productsdb;

-- 1
SELECT model, speed, hd FROM pc WHERE price < 500;

-- 2 
SELECT DISTINCT marker FROM product WHERE type = "Printer";

-- 3
SELECT model, ram, screen FROM laptop WHERE price > 1000;

-- 4
SELECT * FROM printer WHERE color = "y";

-- 5
SELECT model, speed, hd FROM pc WHERE (cd = "12x" or cd = "24x") AND price < 600;

-- 6
SELECT product.marker, laptop.speed FROM product 
JOIN laptop ON product.model = laptop.model WHERE laptop.hd >= 100;

-- 7
SELECT pc.model, pc.price FROM pc JOIN product ON
pc.model = product.model WHERE product.marker = "B"
UNION
SELECT laptop.model, laptop.price FROM laptop JOIN product ON
laptop.model = product.model WHERE product.marker = "B"
UNION
SELECT printer.model, printer.price FROM printer JOIN product ON
printer.model = product.model WHERE product.marker = "B";

-- 8
SELECT DISTINCT marker FROM product
WHERE type = "PC" AND marker NOT IN 
(SELECT marker FROM product WHERE type = "Laptop");

-- 9
SELECT DISTINCT product.marker FROM pc
JOIN product ON pc.model = product.model
WHERE pc.speed >= 450;

-- 10
SELECT model, price FROM printer
WHERE price = (SELECT MAX(price) FROM printer );

-- 11
SELECT AVG(speed) FROM pc;

-- 12 
SELECT AVG(speed) FROM laptop WHERE price > 1000;

-- 13
SELECT AVG(pc.speed) FROM pc, product
WHERE pc.model = product.model AND product.marker = 'A';

-- 14
SELECT speed, AVG(price) FROM pc GROUP BY speed;

-- 15
SELECT hd FROM pc GROUP BY hd HAVING COUNT(*) >= 2;

-- 16
SELECT DISTINCT p1.model, p2.model, p1.speed, p1.ram
FROM pc p1, pc p2
WHERE p1.speed = p2.speed AND p1.ram = p2.ram AND p1.model > p2.model;

-- 17
SELECT DISTINCT p.type, p.model, l.speed
FROM laptop l
JOIN product p ON l.model=p.model
WHERE l.speed < (SELECT min(speed) FROM pc);

-- 18
SELECT DISTINCT product.marker, printer.price FROM product, printer
WHERE product.model = printer.model AND printer.color = 'y'
AND printer.price = (SELECT MIN(price) FROM printer WHERE printer.color = 'y');

-- 19
SELECT product.marker, AVG(screen)
FROM laptop LEFT JOIN product ON product.model = laptop.model
GROUP BY product.marker;

-- 20
SELECT marker, COUNT(model) FROM product WHERE type = 'PC'
GROUP BY product.marker HAVING COUNT(model) >= 3;

-- 21
SELECT product.marker, MAX(pc.price) FROM product, pc
WHERE product.model = pc.model GROUP BY product.marker;

-- 22
SELECT pc.speed, AVG(pc.price) FROM pc
WHERE pc.speed > 600 GROUP BY pc.speed;

-- 23
SELECT DISTINCT marker FROM product t1 
JOIN pc t2 ON t1.model=t2.model
WHERE speed>=750 AND marker IN
(SELECT marker FROM product t1 
JOIN laptop t2 ON t1.model=t2.model
WHERE speed>=750);

-- 24
SELECT model FROM (
	SELECT model, price FROM pc
	UNION
	SELECT model, price FROM Laptop
	UNION
	SELECT model, price FROM Printer
) t1
WHERE price = (
SELECT MAX(price) FROM (
	SELECT price FROM pc
	UNION
	SELECT price FROM Laptop
	UNION
	SELECT price FROM Printer
	) t2
);

-- 25
SELECT DISTINCT marker FROM product
WHERE model IN (
SELECT model FROM pc
WHERE ram = (
  SELECT MIN(ram)
  FROM pc
  )
AND speed = (
  SELECT MAX(speed)
  FROM pc
  WHERE ram = (
   SELECT MIN(ram)
   FROM pc
   )
  )
)
AND
marker IN (
	SELECT marker FROM product WHERE type='printer'
)