# Base de datos Vuelos

# Creacion de la base de datos
CREATE DATABASE vuelos;
USE vuelos;

# --------------------------------------------
#	
#	Entidades
# 

CREATE TABLE pasajeros(
	doc_tipo VARCHAR(10) NOT NULL,
	doc_nro INT(10) UNSIGNED NOT NULL,
	apellido VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,
	nacionalidad VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_pasajeros
	PRIMARY KEY (doc_tipo, doc_nro)	
)	ENGINE = InnoDB;

CREATE TABLE empleados(
	legajo INT(10) UNSIGNED NOT NULL,
	password CHAR(32) NOT NULL,
	doc_tipo VARCHAR(10) NOT NULL,
	doc_nro INT(10) UNSIGNED NOT NULL,
	apellido VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_empleados
	PRIMARY KEY (legajo)	
)	ENGINE = InnoDB;

CREATE TABLE ubicaciones(
	pais VARCHAR(45) NOT NULL,
	estado VARCHAR(45) NOT NULL,
	ciudad VARCHAR(45) NOT NULL,
	huso INT CHECK (huso >= -12 AND huso<= 12) NOT NULL,
	
	CONSTRAINT pk_ubicaciones
	PRIMARY KEY (pais, estado, ciudad)
)	ENGINE = InnoDB;

CREATE TABLE aeropuertos(	
	codigo VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	pais VARCHAR(45) NOT NULL,
	estado VARCHAR(45) NOT NULL,
	ciudad VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_aeropuertos
	PRIMARY KEY (codigo),
	
	CONSTRAINT fk_aeropuertos
	FOREIGN KEY (pais, estado, ciudad) REFERENCES ubicaciones (pais, estado, ciudad)
)	ENGINE = InnoDB;

CREATE TABLE vuelos_programados(
	numero VARCHAR(45) NOT NULL,
	aeropuerto_salida VARCHAR(45) NOT NULL,
	aeropuerto_llegada VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_vuelos_programados
	PRIMARY KEY (numero),
	
	CONSTRAINT fk_vuelos_programados_aeropuerto_salida
	FOREIGN KEY (aeropuerto_salida) references aeropuertos (codigo),
	
	CONSTRAINT fk_vuelos_programados_aeropuerto_llegada
	FOREIGN KEY (aeropuerto_llegada) references aeropuertos (codigo)
)	ENGINE = InnoDB;

CREATE TABLE modelos_avion(
	modelo VARCHAR(45) NOT NULL,
	fabricante VARCHAR(45) NOT NULL,
	cabinas INT UNSIGNED NOT NULL,
	cant_asientos INT UNSIGNED NOT NULL,
	
	CONSTRAINT pk_modelos_avion
	PRIMARY KEY (modelo)
)	ENGINE = InnoDB;

CREATE TABLE salidas(
	vuelo VARCHAR(45) NOT NULL,
	dia ENUM('Do','Lu','Ma','Mi','Ju','Vi','Sa') NOT NULL,
	hora_sale TIME NOT NULL,
	hora_llega TIME NOT NULL,
	modelo_avion VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_salidas
	PRIMARY KEY (vuelo, dia),
	
	CONSTRAINT fk_salidas_vuelo
	FOREIGN KEY (vuelo) references vuelos_programados(numero),
	
	CONSTRAINT fk_salidas_modelo_avion
	FOREIGN KEY (modelo_avion) references modelos_avion(modelo)
) ENGINE = InnoDB;

CREATE TABLE instancias_vuelo(
	vuelo VARCHAR(45) NOT NULL,
	fecha DATE NOT NULL,
	
	dia ENUM('Do','Lu','Ma','Mi','Ju','Vi','Sa') NOT NULL,
	estado VARCHAR(45),	
	
	CONSTRAINT pk_instancias_vuelo
	PRIMARY KEY (vuelo, fecha),
	
	CONSTRAINT fk_instancias_vuelo
	FOREIGN KEY (vuelo, dia) references salidas(vuelo, dia)
) ENGINE = InnoDB;

CREATE TABLE clases(
	nombre VARCHAR(45) NOT NULL,
	porcentaje DECIMAL(2,2) unsigned  CHECK(porcentaje>=0 AND porcentaje<= 0.99) NOT NULL,
	
	CONSTRAINT pk_nombre
	PRIMARY KEY (nombre)
)	ENGINE = InnoDB;

CREATE TABLE comodidades(
	codigo INT UNSIGNED NOT NULL,
	descripcion TEXT NOT NULL,
	
	CONSTRAINT pk_comodidades
	PRIMARY KEY (codigo)
)	ENGINE = InnoDB;

CREATE TABLE reservas(
	numero INT UNSIGNED NOT NULL AUTO_INCREMENT,
	fecha DATE NOT NULL,
	vencimiento DATE NOT NULL,
	estado VARCHAR(45) NOT NULL,
	doc_tipo VARCHAR(10) NOT NULL,		
	doc_nro INT(10) UNSIGNED NOT NULL,
	legajo INT(10) UNSIGNED NOT NULL,
	
	CONSTRAINT pk_reservas
	PRIMARY KEY (numero),
	
	CONSTRAINT fk_reservas_doc_tipo
	FOREIGN KEY (doc_tipo, doc_nro) references pasajeros(doc_tipo, doc_nro),
	
	CONSTRAINT fk_reservas_legajo
	FOREIGN KEY (legajo) references empleados(legajo)
)	ENGINE = InnoDB;

#
#	Relaciones
#

CREATE TABLE brinda(
	vuelo VARCHAR(45) NOT NULL,
	dia ENUM('Do','Lu','Ma','Mi','Ju','Vi','Sa') NOT NULL,
	clase VARCHAR(45) NOT NULL,
	precio DECIMAL(7,2) UNSIGNED NOT NULL,
	cant_asientos INT UNSIGNED NOT NULL,
	
	CONSTRAINT pk_brinda
	PRIMARY KEY (vuelo, dia, clase),
	
	CONSTRAINT fk_brinda_vuelo_dia
	FOREIGN KEY (vuelo, dia) references salidas(vuelo, dia),
	
	CONSTRAINT fk_brinda_clase
	FOREIGN KEY (clase) references clases(nombre)
)	ENGINE = InnoDB;

CREATE TABLE posee(
	clase VARCHAR(45) NOT NULL,	
	comodidad INT UNSIGNED NOT NULL,
	
	CONSTRAINT pk_posee
	PRIMARY KEY (clase, comodidad),
	
	CONSTRAINT fk_posee_clase
	FOREIGN KEY (clase) references clases(nombre),
	
	CONSTRAINT fk_posee_comodidad
	FOREIGN KEY (comodidad) references comodidades(codigo)
) ENGINE = InnoDB;

CREATE TABLE reserva_vuelo_clase(
	numero INT UNSIGNED NOT NULL,
	vuelo VARCHAR(45) NOT NULL,
	fecha_vuelo DATE NOT NULL,		
	clase VARCHAR(45) NOT NULL,

	CONSTRAINT pk_reserva_vuelo_clase
	PRIMARY KEY (numero, vuelo, fecha_vuelo),
	
	CONSTRAINT fk_reserva_vuelo_clase_numero
	FOREIGN KEY (numero) references reservas(numero),
	
	CONSTRAINT fk_reserva_vuelo_clase_fecha_vuelo
	FOREIGN KEY (vuelo, fecha_vuelo) references instancias_vuelo(vuelo, fecha),
	 
	CONSTRAINT fk_reserva_vuelo_clase
	FOREIGN KEY (clase) references clases(nombre)
) ENGINE = InnoDB;

CREATE TABLE asientos_reservados(
	vuelo VARCHAR(45) NOT NULL,
	fecha DATE NOT NULL,	
	clase VARCHAR(45) NOT NULL,
	cantidad INT UNSIGNED NOT NULL,
	
	CONSTRAINT pk_asientos_reservados
	PRIMARY KEY (vuelo, fecha ,clase),
	
	CONSTRAINT fk_asientos_reservados_clase
	FOREIGN KEY (clase) references clases(nombre),
	
	CONSTRAINT fk_asientos_reservados_fecha_vuelo
	FOREIGN KEY(vuelo, fecha) references instancias_vuelo(vuelo, fecha)
) ENGINE = InnoDB;

#
#	Creacion de vistas
#

CREATE VIEW datos AS
	SELECT 	*
	FROM  	((((((vuelos_programados as v 
			join (select	codigo as cod_llegada,
							nombre as nombre_llegada, 
							ciudad as ciudad_llegada, 
							estado as estado_llegada, 
							pais as pais_llegada 
							from aeropuertos) as t1
			on v.aeropuerto_llegada=t1.cod_llegada)
			
			
			
			join (select	codigo as cod_salida, 
							nombre as nombre_salida, 
							ciudad as ciudad_salida,
							estado as estado_salida,
							pais as pais_salida
							from aeropuertos) as t2
			on v.aeropuerto_salida=t2.cod_salida)
			
			join (select	hora_sale, 
							hora_llega,	
							modelo_avion,
							dia as dia_salida,
							vuelo as vuelo_salida
							from salidas) as t3
			on v.numero = t3.vuelo_salida)
			
			join (brinda t4)
			on t3.vuelo_salida = t4.vuelo and t3.dia_salida = t4.dia)
			
			join (select 	fecha,
							vuelo as vuelo_iv,
							dia as dia_iv
							from instancias_vuelo) as t5
			on t4.vuelo = t5.vuelo_iv and t4.dia = t5.dia_iv)
			
			join (select 	porcentaje as porcentaje_clase,
							nombre as nombre_clase_brinda
							from clases) as t6
			on clase = t6.nombre_clase_brinda);
									
		
CREATE VIEW vuelos_disponibles AS
	SELECT  vuelo,
			precio,
			porcentaje_clase,
			pais_salida,
			pais_llegada,
			nombre_salida,
			nombre_llegada,
			modelo_avion,
			hora_sale,
			hora_llega,
			fecha,
			estado_salida,
			estado_llegada,
			dia,
			cod_salida,
			cod_llegada,
			clase,
			ciudad_salida,
			ciudad_llegada,
			cant_asientos,
			aeropuerto_salida,
			aeropuerto_llegada,
			round(((cant_asientos + cant_asientos*porcentaje_clase) - count(numero_reserva)), 0) as cant_libres,
			if (hora_llega > hora_sale, 
				timediff(hora_llega,hora_sale), SEC_TO_TIME (TIME_TO_SEC(hora_llega) + TIME_TO_SEC(TIMEDIFF('24:00:00',hora_sale)))      
			) as tiempo_estimado
	FROM datos natural left join (select vuelo,	fecha_vuelo as fecha, clase, numero as numero_reserva from reserva_vuelo_clase) as t1
	GROUP BY vuelo, fecha, clase;

#
#	Creacion de usuarios y privilegios
#

/*
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON vuelos.* TO 'admin'@'localhost' WITH GRANT OPTION;


CREATE USER 'empleado'@'%' IDENTIFIED BY 'empleado';
GRANT SELECT ON vuelos.* TO 'empleado'@'%';
GRANT DELETE, INSERT, UPDATE ON vuelos.reservas TO 'empleado'@'%';
GRANT DELETE, INSERT, UPDATE ON vuelos.pasajeros TO 'empleado'@'%';
GRANT DELETE, INSERT, UPDATE ON vuelos.reserva_vuelo_clase TO 'empleado'@'%';

CREATE USER 'cliente'@'%' IDENTIFIED BY 'cliente';
GRANT SELECT ON vuelos.vuelos_disponibles TO 'cliente'@'%';
*/

#
#	Creacion de stored procedures
#

DELIMITER ! # delimitiador a usar para los procedures

#numero, fecha, clase de vuelo - vuelo VARCHAR(45) NOT NULL - fecha DATE - clase VARCHAR(45) NOT NULL,
#tipo y numero doc del pasajero - 	doc_tipo VARCHAR(10) - doc_nro INT(10) UNSIGNED,
#legajo del empleado que gestiona la reserva - 	legajo INT(10) UNSIGNED NOT NULL,
CREATE PROCEDURE realizar_reserva_ida(IN id_vuelo VARCHAR(45), IN fecha_vuelo DATE, IN clase_vuelo VARCHAR(45), IN tipo_doc VARCHAR(10),
									  IN nro_doc INT(10) UNSIGNED, IN legajo_emp INT(10) UNSIGNED, OUT res VARCHAR(100))
	BEGIN
		/*Verificar datos*/
		# Casos donde los datos no existan
		DECLARE row_count INT;
		
		SET row_count = (SELECT count(*) FROM vuelos_disponibles WHERE id_vuelo = vuelo AND fecha = fecha_vuelo AND clase = clase_vuelo);

		IF (row_count = 0) THEN /*Caso que el vuelo con la fecha y clase no existan en la b.d*/
			SET res = 'No hay asientos disponibles para realizar la reserva';
		ELSE BEGIN
				 SET row_count = (SELECT count(*) FROM pasajeros WHERE doc_tipo = tipo_doc AND nro_doc = doc_nro);
				 IF (row_count = 0) THEN /*Caso que la persona que desea realizar la reserva no este en la b.d*/
				 	SET res = 'No existe la persona que desea realizar la reserva en la base de datos';
				 ELSE BEGIN
					 	  SET row_count = (SELECT count(*) from empleados where legajo_emp = legajo);
						  IF (row_count = 0) THEN /*Caso que el empleado que va a manejar la reserva no este en la b.d*/
						  	SET res = 'No existe el empleado que debe atender la reserva en la base de datos';
						  ELSE BEGIN /*Caso que los datos sean validos se procede a realizar la reserva*/
							  	   SET row_count = (SELECT count(*) FROM asientos_reservados);
							  	   IF(row_count = 0) THEN /*Caso que no se haya realizado ninguna reserva la tabla estara vacia*/
							  	   		CALL realizar_reserva_ida_aux(id_vuelo,fecha_vuelo,clase_vuelo,tipo_doc,nro_doc,legajo_emp,res);
							  	   ELSE BEGIN  
								  	   DECLARE cant_reservados INT;
									   DECLARE cant_disponibles INT;
									   
									   SELECT cant_libres INTO cant_disponibles FROM vuelos_disponibles WHERE vuelo = id_vuelo AND fecha_vuelo = fecha AND clase_vuelo = clase;
									   SELECT cantidad INTO cant_reservados FROM asientos_reservados WHERE vuelo = id_vuelo AND fecha_vuelo = fecha AND clase_vuelo = clase;
									   IF(cant_reservados < cant_disponibles) THEN # Caso que se puede realizar una reserva
											CALL realizar_reserva_ida_aux(id_vuelo,fecha_vuelo,clase_vuelo,tipo_doc,nro_doc,legajo_emp,res);
									   ELSE
									  	SET res = 'No hay asientos disponibles para realizar la reserva';
						  			  END IF;
						  			END;
						  		 	END IF;
						  	 	END;
						  	 	END IF;
						  	 END;
						  	 END IF;
		END;
		END IF;
	END
	!

	
CREATE PROCEDURE realizar_reserva_ida_aux(IN id_vuelo VARCHAR(45), IN vuelo_fecha DATE, IN clase_vuelo VARCHAR(45), IN tipo_doc VARCHAR(10),
									  IN nro_doc INT(10) UNSIGNED, IN legajo_emp INT(10) UNSIGNED, OUT res VARCHAR(100))
	BEGIN
		DECLARE cant_reservados INT;
		DECLARE cant_disponibles INT;
		DECLARE asientos_cant INT;
		
		DECLARE id_nueva_reserva INT;
		DECLARE fecha_reserva DATE;
		DECLARE fecha_vencimiento DATE;
		DECLARE estado_res VARCHAR(45);
				
		SELECT cant_libres INTO cant_disponibles FROM vuelos_disponibles WHERE vuelo = id_vuelo AND vuelo_fecha = fecha AND clase_vuelo = clase;
		SELECT cant_asientos INTO asientos_cant FROM vuelos_disponibles WHERE vuelo = id_vuelo AND vuelo_fecha = fecha AND clase_vuelo = clase;
	
		SET cant_reservados = (SELECT count(*) FROM asientos_reservados WHERE vuelo = id_vuelo AND vuelo_fecha = fecha AND clase_vuelo = clase);
		if(cant_reservados = 0) THEN
			SET cant_reservados = 0;
		ELSE
			SELECT cantidad INTO cant_reservados FROM asientos_reservados WHERE vuelo = id_vuelo AND vuelo_fecha = fecha AND clase_vuelo = clase;
		END IF;
											
		SELECT curdate() INTO fecha_reserva;
		SELECT subdate(vuelo_fecha, INTERVAL 15 DAY) INTO fecha_vencimiento;
											
		IF (cant_reservados < asientos_cant) THEN
			SET estado_res = 'confirmada';
		ELSE
			SET estado_res = 'en espera';
		END IF;
											
		# Se inserta la reserva en la BD
		INSERT INTO reservas(fecha, vencimiento, estado, doc_tipo, doc_nro, legajo) VALUES(fecha_reserva, fecha_vencimiento, estado_res, tipo_doc, nro_doc, legajo_emp);

		SELECT last_insert_id() INTO id_nueva_reserva;
	
		# Se inserta en reserva_vuelo_clase
		INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) VALUES(id_nueva_reserva, id_vuelo, vuelo_fecha, clase_vuelo);					
	
		# Se aumenta la cantidad de asientos reservados
		SET cant_reservados = cant_reservados + 1;
		INSERT INTO asientos_reservados(vuelo, fecha, clase, cantidad) VALUES(id_vuelo, vuelo_fecha, clase_vuelo, cant_reservados) 
		ON DUPLICATE KEY UPDATE cantidad = cant_reservados;
						
											
		SET res = 'Se pudo realizar la reserva con exito';

		
	END
	!
									  

DELIMITER ; # una vez creados los procedures se vuelve a establecer ; como delimitador

/*
GRANT EXECUTE ON PROCEDURE vuelos.realizar_reserva_ida TO 'empleado'@'%';
GRANT EXECUTE ON PROCEDURE vuelos.realizar_reserva_ida_aux TO 'empleado'@'%';
*/