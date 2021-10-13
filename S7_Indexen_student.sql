-- ------------------------------------------------------------------------
-- Data & Persistency
-- Opdracht S7: Indexen
--
-- (c) 2020 Hogeschool Utrecht
-- Tijmen Muller (tijmen.muller@hu.nl)
-- André Donk (andre.donk@hu.nl)
-- ------------------------------------------------------------------------
-- LET OP, zoals in de opdracht op Canvas ook gezegd kun je informatie over
-- het query plan vinden op: https://www.postgresql.org/docs/current/using-explain.html


-- S7.1.
--
-- Je maakt alle opdrachten in de 'sales' database die je hebt aangemaakt en gevuld met
-- de aangeleverde data (zie de opdracht op Canvas).
--
-- Voer het voorbeeld uit wat in de les behandeld is:
-- 1. Voer het volgende EXPLAIN statement uit:
--    EXPLAIN SELECT * FROM order_lines WHERE stock_item_id = 9;
--    Bekijk of je het resultaat begrijpt. Kopieer het explain plan onderaan de opdracht
   EXPLAIN SELECT * FROM order_lines WHERE stock_item_id = 9;
-- "Gather  (cost=1000.00..6151.67 rows=1004 width=96)"
-- "  Workers Planned: 2"
-- "  ->  Parallel Seq Scan on order_lines  (cost=0.00..5051.27 rows=418 width=96)"
-- "        Filter: (stock_item_id = 9)"
-- 2. Voeg een index op stock_item_id toe:
   CREATE INDEX ord_lines_si_id_idx ON order_lines (stock_item_id);
-- 3. Analyseer opnieuw met EXPLAIN hoe de query nu uitgevoerd wordt
--    Kopieer het explain plan onderaan de opdracht
   EXPLAIN SELECT * FROM order_lines WHERE stock_item_id = 9;
-- "Bitmap Heap Scan on order_lines  (cost=20.20..2306.51 rows=1004 width=96)"
-- "  Recheck Cond: (stock_item_id = 9)"
-- "  ->  Bitmap Index Scan on ord_lines_si_id_idx  (cost=0.00..19.95 rows=1004 width=0)"
-- "        Index Cond: (stock_item_id = 9)"
-- 4. Verklaar de verschillen. Schrijf deze hieronder op.
-- Er is nu een index die het opzoeken van de id optimaliseerd

-- S7.2.
--
-- 1. Maak de volgende twee query’s:
-- 	  A. Toon uit de order tabel de order met order_id = 73590
SELECT * FROM orders WHERE order_id = 74
-- 	  B. Toon uit de order tabel de order met customer_id = 1028
SELECT * FROM orders WHERE customer_id = 1028

-- 2. Analyseer met EXPLAIN hoe de query’s uitgevoerd worden en kopieer het explain plan onderaan de opdracht
EXPLAIN SELECT * FROM orders WHERE order_id = 74
-- "Index Scan using pk_sales_orders on orders  (cost=0.29..8.31 rows=1 width=155)"
-- "  Index Cond: (order_id = 74)"
EXPLAIN SELECT * FROM orders WHERE customer_id = 1028
-- "Seq Scan on orders  (cost=0.00..1819.94 rows=107 width=155)"
-- "  Filter: (customer_id = 1028)"

-- 3. Verklaar de verschillen en schrijf deze op
-- eerste is een index want is een primary key, andere is dit niet

-- 4. Voeg een index toe, waarmee query B versneld kan worden
CREATE INDEX order_customer_id_index ON orders (customer_id);

-- 5. Analyseer met EXPLAIN en kopieer het explain plan onder de opdracht
EXPLAIN SELECT * FROM orders WHERE customer_id = 1028
-- "Bitmap Heap Scan on orders  (cost=5.12..308.96 rows=107 width=155)"
-- "  Recheck Cond: (customer_id = 1028)"
-- "  ->  Bitmap Index Scan on order_customer_id_index  (cost=0.00..5.10 rows=107 width=0)"
-- "        Index Cond: (customer_id = 1028)"
-- 6. Verklaar de verschillen en schrijf hieronder op
-- Er is een index die hij nu kan gebruiken voor het opzoeken van de customer_id


-- S7.3.A
--
-- Het blijkt dat customers regelmatig klagen over trage bezorging van hun bestelling.
-- Het idee is dat verkopers misschien te lang wachten met het invoeren van de bestelling in het systeem.
-- Daar willen we meer inzicht in krijgen.
-- We willen alle orders (order_id, order_date, salesperson_person_id (als verkoper),
--    het verschil tussen expected_delivery_date en order_date (als levertijd),  
--    en de bestelde hoeveelheid van een product zien (quantity uit order_lines).
-- Dit willen we alleen zien voor een bestelde hoeveelheid van een product > 250
--   (we zijn nl. als eerste geïnteresseerd in grote aantallen want daar lijkt het vaker mis te gaan)
-- En verder willen we ons focussen op verkopers wiens bestellingen er gemiddeld langer over doen.
-- De meeste bestellingen kunnen binnen een dag bezorgd worden, sommige binnen 2-3 dagen.
-- Het hele bestelproces is er op gericht dat de gemiddelde bestelling binnen 1.45 dagen kan worden bezorgd.
-- We willen in onze query dan ook alleen de verkopers zien wiens gemiddelde levertijd 
--  (expected_delivery_date - order_date) over al zijn/haar bestellingen groter is dan 1.45 dagen.
-- Maak om dit te bereiken een subquery in je WHERE clause.
-- Sorteer het resultaat van de hele geheel op levertijd (desc) en verkoper.
-- 1. Maak hieronder deze query (als je het goed doet zouden er 377 rijen uit moeten komen, en het kan best even duren...)
SELECT order_id, order_date, salesperson_person_id, DATEDIFF(day, '2017/08/25', '2011/08/25')


-- S7.3.B
--
-- 1. Vraag het EXPLAIN plan op van je query (kopieer hier, onder de opdracht)
-- 2. Kijk of je met 1 of meer indexen de query zou kunnen versnellen
-- 3. Maak de index(en) aan en run nogmaals het EXPLAIN plan (kopieer weer onder de opdracht) 
-- 4. Wat voor verschillen zie je? Verklaar hieronder.



-- S7.3.C
--
-- Zou je de query ook heel anders kunnen schrijven om hem te versnellen?


