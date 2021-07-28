-- Show table schema 
\d+ retail;

-- Show first 10 rows
SELECT * 
FROM retail limit 10;

-- Check # of records
SELECT COUNT(*) 
FROM retail;

-- number of clients (e.g. unique client ID)
SELECT COUNT(DISTINCT customer_id) 
FROM retail;

-- invoice date range (e.g. max/min dates)
SELECT MAX(invoice_date), MIN(invoice_date)
FROM retail;

-- number of SKU/merchants (e.g. unique stock code)
SELECT COUNT(DISTINCT stock_code) 
FROM retail;

-- Calculate average invoice amount excluding invoices with a negative amount (e.g. canceled orders have negative amount)
SELECT AVG(sum)
FROM(
	SELECT SUM(quantity*unit_price)
	FROM retail
	GROUP BY invoice_no
	HAVING SUM(quantity*unit_price) > 0
) AS amount;

-- Calculate total revenue (e.g. sum of unit_price * quantity)
SELECT SUM(quantity*unit_price)
FROM retail;

-- Calculate total revenue by YYYYMM
SELECT (EXTRACT(YEAR FROM invoice_date)*100 + EXTRACT(MONTH FROM invoice_date)) as yyyymm, SUM(quantity*unit_price)
FROM retail
GROUP BY yyyymm
ORDER BY yyyymm;
