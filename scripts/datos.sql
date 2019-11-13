
#Carga de datos de prueba

use vuelos;

#INSERCION EN MODELOS AVION
INSERT INTO modelos_avion(modelo, fabricante, cabinas, cant_asientos) VALUES("Boeing 0","Boeing.Co",2,50);
INSERT INTO modelos_avion(modelo, fabricante, cabinas, cant_asientos) VALUES("Boeing 1","Boeing.Co",3,75);
INSERT INTO modelos_avion(modelo, fabricante, cabinas, cant_asientos) VALUES("Boeing 2","Boeing.Co",4,100);
INSERT INTO modelos_avion(modelo, fabricante, cabinas, cant_asientos) VALUES("Boeing 3","Boeing.Co",6,140);
INSERT INTO modelos_avion(modelo, fabricante, cabinas, cant_asientos) VALUES("Delta 0","Delta.Co",2,100);


#INSERCION EN COMODIDADES
INSERT INTO comodidades(codigo, descripcion) VALUES(1, "Asiento mas grande");
INSERT INTO comodidades(codigo, descripcion) VALUES(2, "Asiento con masajeador");
INSERT INTO comodidades(codigo, descripcion) VALUES(3, "Asiento plegable");
INSERT INTO comodidades(codigo, descripcion) VALUES(4, "Televisor 62 pulgadas");
INSERT INTO comodidades(codigo, descripcion) VALUES(5, "Asiento con calentador de espalda");


#INSERCION EN CLASES
INSERT INTO clases(nombre, porcentaje) VALUES("Economica", 0.65);
INSERT INTO clases(nombre, porcentaje) VALUES("Primera clase", 0.3);
INSERT INTO clases(nombre, porcentaje) VALUES("Media", 0.5);
INSERT INTO clases(nombre, porcentaje) VALUES("Super economica", 0.8);
INSERT INTO clases(nombre, porcentaje) VALUES("Intermedia", 0.72);


#INSERCION EN POSEE
INSERT INTO posee(clase, comodidad) VALUES("Economica", 4);
INSERT INTO posee(clase, comodidad) VALUES("Primera clase", 2);
INSERT INTO posee(clase, comodidad) VALUES("Media", 5);
INSERT INTO posee(clase, comodidad) VALUES("Super economica", 3);
INSERT INTO posee(clase, comodidad) VALUES("Intermedia", 1);


#INSERCION EN UBICACIONES
INSERT INTO ubicaciones(pais, estado, ciudad, huso) VALUES("Argentina", "Buenos Aires", "Bahia Blanca", 6);
INSERT INTO ubicaciones(pais, estado, ciudad, huso) VALUES("Argentina", "Buenos Aires", "La plata", 6);
INSERT INTO ubicaciones(pais, estado, ciudad, huso) VALUES("Argentina", "Buenos Aires", "Mar del Plata", 6);
INSERT INTO ubicaciones(pais, estado, ciudad, huso) VALUES("Argentina", "La Pampa", "Alpachiri", 6);
INSERT INTO ubicaciones(pais, estado, ciudad, huso) VALUES("Argentina", "Buenos Aires", "Coronel Pringles", 6);


#INSERCION EN AEROPUERTOS
INSERT INTO aeropuertos(codigo, nombre, telefono, direccion, pais, estado, ciudad) VALUES("LP001", "Aeropuerto de La Plata", "0115918", "Av. 25 de Mayo", "Argentina", "Buenos Aires", "La Plata");
INSERT INTO aeropuertos(codigo, nombre, telefono, direccion, pais, estado, ciudad) VALUES("BB002", "Aeropuerto de Bahia Blanca", "0115910", "Av. Libertadores", "Argentina", "Buenos Aires", "Bahia Blanca");
INSERT INTO aeropuertos(codigo, nombre, telefono, direccion, pais, estado, ciudad) VALUES("MP003", "Aeropuerto de Mar del Plata", "011591", "Av. Corrientes", "Argentina", "Buenos Aires", "Mar del Plata");
INSERT INTO aeropuertos(codigo, nombre, telefono, direccion, pais, estado, ciudad) VALUES("A004", "Aeropuerto de Alpachiri", "019124", "Suarez 520", "Argentina", "La Pampa", "Alpachiri");
INSERT INTO aeropuertos(codigo, nombre, telefono, direccion, pais, estado, ciudad) VALUES("CP005", "Aeropuerto de Coronel Pringles", "011912", "Dorrego 915", "Argentina", "Buenos Aires", "Coronel Pringles");


#INSERCION EN VUELOS PROGRAMADOS
INSERT INTO vuelos_programados(numero, aeropuerto_salida, aeropuerto_llegada) VALUES("A0001", "LP001", "CP005");
INSERT INTO vuelos_programados(numero, aeropuerto_salida, aeropuerto_llegada) VALUES("A0002", "CP005", "LP001");
INSERT INTO vuelos_programados(numero, aeropuerto_salida, aeropuerto_llegada) VALUES("A0003", "A004", "MP003");
INSERT INTO vuelos_programados(numero, aeropuerto_salida, aeropuerto_llegada) VALUES("A0004", "BB002", "LP001");
INSERT INTO vuelos_programados(numero, aeropuerto_salida, aeropuerto_llegada) VALUES("A0005", "MP003", "LP001");


#INSERCION EN SALIDAS
INSERT INTO salidas(vuelo, dia, hora_sale, hora_llega, modelo_avion) VALUES("A0001", "Lu", '09:15', '12:45', "Boeing 0");
INSERT INTO salidas(vuelo, dia, hora_sale, hora_llega, modelo_avion) VALUES("A0002", "Do", '21:00', '06:30', "Boeing 1");
INSERT INTO salidas(vuelo, dia, hora_sale, hora_llega, modelo_avion) VALUES("A0003", "Ma", '14:20', '19:30', "Boeing 2");
INSERT INTO salidas(vuelo, dia, hora_sale, hora_llega, modelo_avion) VALUES("A0004", "Mi", '16:20', '22:45', "Boeing 3");
INSERT INTO salidas(vuelo, dia, hora_sale, hora_llega, modelo_avion) VALUES("A0005", "Vi", '05:35', '11:15', "Delta 0");


#INSERCION EN INSTANCIAS VUELO
INSERT INTO instancias_vuelo(vuelo, fecha, dia, estado) VALUES("A0001", "2019/08/21", "Lu", "Cancelado");
INSERT INTO instancias_vuelo(vuelo, fecha, dia, estado) VALUES("A0002", "2019/11/07", "Do", "Demorado");
INSERT INTO instancias_vuelo(vuelo, fecha, dia, estado) VALUES("A0003", "2019/10/19", "Ma", "En horario");
INSERT INTO instancias_vuelo(vuelo, fecha, dia, estado) VALUES("A0004", "2019/12/31", "Mi", "Demorado");
INSERT INTO instancias_vuelo(vuelo, fecha, dia, estado) VALUES("A0005", "2020/02/01", "Vi", "En horario");


#INSERCION EN BRINDA
INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos) VALUES("A0001", "Lu", "Economica", 8905.94, 100);
INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos) VALUES("A0001", "Lu", "Media", 10000, 5);
INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos) VALUES("A0002", "Do", "Primera Clase", 18905.94, 40);
INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos) VALUES("A0003", "Ma", "Media", 12905.94, 70);
INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos) VALUES("A0004", "Mi", "Super economica", 3905.94, 140);
INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos) VALUES("A0005", "Vi", "Intermedia", 6905.94, 75);


#INSERCION EN EMPLEADOS
INSERT INTO empleados(legajo, password, doc_tipo, doc_nro, apellido, nombre, direccion, telefono) VALUES(117815, md5("hola123"), "Tarjeta", 1001, "Gomez", "Juan", "Suarez 100", "466061");
INSERT INTO empleados(legajo, password, doc_tipo, doc_nro, apellido, nombre, direccion, telefono) VALUES(117825, md5("1234567890"), "Tarjeta", 1002, "Perez", "Cruz", "Brandsen 101", "447819");
INSERT INTO empleados(legajo, password, doc_tipo, doc_nro, apellido, nombre, direccion, telefono) VALUES(117885, md5("blasgarcia1"), "Libreta", 1003, "Garcia", "Blas", "Alem 98", "445678");
INSERT INTO empleados(legajo, password, doc_tipo, doc_nro, apellido, nombre, direccion, telefono) VALUES(117889, md5("pabloclon2"), "Tarjeta", 1004, "Perez", "Pablo", "Colon 1", "901234");
INSERT INTO empleados(legajo, password, doc_tipo, doc_nro, apellido, nombre, direccion, telefono) VALUES(117890, md5("abcdef1234"), "Tarjeta", 1005, "Alem", "Ciro", "Colon 95", "121212");


#INSERCION EN PASAJEROS
INSERT INTO pasajeros(doc_tipo, doc_nro, apellido, nombre, direccion, telefono, nacionalidad) VALUES("Tarjeta", 101, "Frizzera", "Ignacio", "Caronti 100", "474747", "Argentina");
INSERT INTO pasajeros(doc_tipo, doc_nro, apellido, nombre, direccion, telefono, nacionalidad) VALUES("Libreta", 102, "Chia", "Guido Giuliano", "San Martin 1001", "484848", "Argentina");
INSERT INTO pasajeros(doc_tipo, doc_nro, apellido, nombre, direccion, telefono, nacionalidad) VALUES("Tarjeta", 103, "Hammilton", "Luis", "Francia 101", "494949", "Francia");
INSERT INTO pasajeros(doc_tipo, doc_nro, apellido, nombre, direccion, telefono, nacionalidad) VALUES("Libreta", 104, "Perez", "Roberto", "Cabrera 69", "505050", "Noruega");
INSERT INTO pasajeros(doc_tipo, doc_nro, apellido, nombre, direccion, telefono, nacionalidad) VALUES("Libreta", 105, "Lopez", "Raul", "Dorrego 1410", "515151", "Australia");


#INSERCION EN RESERVAS
INSERT INTO reservas(numero, fecha, vencimiento, estado, doc_tipo, doc_nro, legajo) VALUES(501, "2019/08/21", "2019/08/29", "Activa", "Tarjeta", 101, 117815);
INSERT INTO reservas(numero, fecha, vencimiento, estado, doc_tipo, doc_nro, legajo) VALUES(502, "2019/10/21", "2019/10/30", "Activa", "Libreta", 102, 117815);
INSERT INTO reservas(numero, fecha, vencimiento, estado, doc_tipo, doc_nro, legajo) VALUES(503, "2019/04/21", "2019/08/11", "Activa", "Libreta", 105, 117890);
INSERT INTO reservas(numero, fecha, vencimiento, estado, doc_tipo, doc_nro, legajo) VALUES(504, "2019/11/21", "2020/02/26", "Activa", "Tarjeta", 103, 117825);
INSERT INTO reservas(numero, fecha, vencimiento, estado, doc_tipo, doc_nro, legajo) VALUES(505, "2019/01/21", "2019/11/21", "Activa", "Libreta", 104, 117889);
INSERT INTO reservas(numero, fecha, vencimiento, estado, doc_tipo, doc_nro, legajo) VALUES(506, "2019/08/21", "2019/08/29", "Activa", "Libreta", 105, 117815);


#INSERCION EN RESERVA VUELO CLASE
INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) VALUES(501, "A0001", "2019/08/21", "Economica");
INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) VALUES(502, "A0001", "2019/08/21", "Economica");
INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) VALUES(503, "A0003", "2019/10/19", "Media");
INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) VALUES(504, "A0004", "2019/12/31", "Intermedia");
INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) VALUES(505, "A0005", "2020/02/01", "Economica");
INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) VALUES(506, "A0001", "2019/08/21", "Media");

