/*CREATE TABLE vuelos.aux
(select selected_date, elt(dayofweek(selected_date), 'Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa') AS dia from 
(select t1.i AS aux, adddate(curdate(), t2.i*100 + t1.i*10 + t0.i) selected_date from
 (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0,
 (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1,
 (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2) v
where selected_date between curdate() and date_add(curdate(), INTERVAL 1 YEAR));*/

CREATE TRIGGER vuelos.trigger_vuelos
AFTER INSERT ON vuelos.salidas
FOR EACH ROW 
	INSERT INTO vuelos.instancias_vuelo(vuelo, fecha, dia, estado) 
	SELECT vuelo, selected_date AS fecha, dia, 'a tiempo' 
	FROM (SELECT * FROM salidas WHERE vuelo = NEW.vuelo) AS t1 NATURAL JOIN (select selected_date, elt(dayofweek(selected_date), 'Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa') AS dia from 
																			(select t1.i AS aux, adddate(curdate(), t2.i*100 + t1.i*10 + t0.i) selected_date from
																			(select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0,
																			(select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1,
																			(select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2) v
																			where selected_date between curdate() and date_add(curdate(), INTERVAL 1 YEAR))	 AS t2		
;

/*WITH aux_table AS (
		SELECT curdate() AS from_date
		UNION ALL
		(SELECT date_add(CURDATE(), INTERVAL 1 DAY), dayofweek(date_add(CURDATE(), INTERVAL 1 DAY))
		FROM aux_table
		WHERE 	DATEDIFF(date_add(CURDATE(), INTERVAL 1 YEAR), date_add(CURDATE(), INTERVAL 1 DAY))>0
)
SELECT
	*
FROM
	aux_table;*/

/*SELECT dayofweek(date_add(curdate(), INTERVAL 1 DAY) AS day_of_week, curdate(), date_add(curdate(), INTERVAL 1 YEAR), vuelo 
	WHERE DATEDIFF(date_add(curdate(), INTERVAL 1 DAY), date_add(curdate(), INTERVAL 1 YEAR))>0
	
	FROM salidas*/