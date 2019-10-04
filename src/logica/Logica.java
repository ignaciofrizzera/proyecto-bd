package logica;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class Logica {

    private final String server = "localhost:3306";
    private final String base_datos = "vuelos";
    private final String admin = "admin";

    private final String url = "jdbc:mysql://" + server + "/" + base_datos +
            "?serverTimezone=America/Argentina/Buenos_Aires";

    private Connection con;


    /**
     * Main de testeo
     */
    public static void main(String[] args) {
    }

    /**
     * conexion del admin. a la base de datos
     *
     * @param password contraseña del administrador
     * @return true si se puede conectar a la base de datos el admin
     * false caso contrario
     */
    public boolean conectar_admin(char[] password) {
        String password_aux = String.valueOf(password);
        if (this.establecer_conexion("admin", password_aux)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * conexion de un usuario a la base de datos
     *
     * @param legajo_ingresado identificador del usuario
     * @param password         contraseña del usuario
     * @return true si el legajo y contraseña corresponden a un empleado de la base de datos
     * false en el caso contrario
     */
    public boolean conectar_empleado(String legajo_ingresado, char[] password) {
        if (!this.establecer_conexion("empleado", "empleado")) {
            return false;
        } else {
        /* Una vez establecida la conexion con la base de datos se analiza
           si el usuario esta en esta
        */
            try {
                int legajo_aux = Integer.parseInt(legajo_ingresado);
                String password_aux = String.valueOf(password);
                String query = "select legajo from empleados";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                int legajo;
                boolean existe = false;

                //Se analiza legajo ingresado
                while (rs.next()) {
                    legajo = rs.getInt(1);
                    if (legajo == legajo_aux) {
                        existe = true;
                        break;
                    }
                }
                //Legajo no existe, se corta la conexion con la base de datos
                if (!existe) {
                    rs.close();
                    st.close();
                    con.close();
                    return false;
                } else {
                    query = "select password from empleados where legajo = " + legajo_aux;
                    rs = st.executeQuery(query);
                    String aux = null;
                    if (rs.next()) {
                        aux = rs.getString(1);
                    }
                    //Encriptar la contraseña ingresada por el usuario para compararla con la de la base de datos
                    password_aux = this.encriptar(password_aux);

                    if (password_aux.equals(aux)) {
                        //Se logea con exito y se mantiene la conexion con la base de datos
                        rs.close();
                        st.close();
                        return true;
                    } else {
                        //Cortar conexion con la base de datos
                        rs.close();
                        st.close();
                        con.close();
                        return false;
                    }
                }
            } catch (NumberFormatException | SQLException e) {
                return false;
            }
        }
    }

    /**
     * Encripta con el algoritmo md5 una contraseña
     *
     * @param str contraseña a encriptar
     * @return la contraseña encriptada con el algoritmo md5
     */
    private String encriptar(String str) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(str.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Establece conexion con la base de datos
     */
    private boolean establecer_conexion(String usuario, String password) {
        try {
            System.out.println("Estableciendo conexión entre db");
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, usuario, password);
            System.out.println("Conexión establecida");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Retorna todas las tablas de la base de datos
     *
     * @return todas las tablas de la base de datos en uso
     */
    public Collection<String> get_tablas() {
        LinkedList<String> tablas = new LinkedList<String>();
        try {
            String query = "show tables";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            String nombre_tabla;
            while (rs.next()) {
                nombre_tabla = rs.getString(1);
                tablas.add(nombre_tabla);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tablas;
    }

    /**
     * Retorna los atributos de una tabla
     *
     * @param tabla tabla de la cual se retornaran los atributos
     * @return lista de atributos de la tabla pasada por parametro
     */
    public Collection<String> get_atributos(String tabla) {
        LinkedList<String> atributos = new LinkedList<String>();
        try {
            String query = "describe " + tabla;
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            String nombre_atributo;
            while (rs.next()) {
                nombre_atributo = rs.getString(1);
                atributos.add(nombre_atributo);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return atributos;
    }


    /**
     * Recibe una sentencia SQL del usuario e intenta ejecutarla
     *
     * @param statement sentencia a ejecutarse
     * @return coleccion con datos de una consulta o null en caso de hacer un insert / update / create
     */
    public Collection<Collection<String>> recibir_statement(String statement) throws SQLException {
        StringTokenizer str_tok = new StringTokenizer(statement, " ");
        String primer_palabra = str_tok.nextToken();
        primer_palabra = primer_palabra.toLowerCase();

        if (primer_palabra.equals("insert") ||
                primer_palabra.equals("update") ||
                primer_palabra.equals("create") ||
                primer_palabra.equals("delete")) {
            this.ejecutar_update(statement);
            return null;
        } else {
            return ejecutar_query(statement);
        }
    }

    /**
     * Ejecuta un update/insert/delete o create sobre la base de datos
     * @param update sentencia a ejecutar
     * @throws SQLException excepcion en caso de sentencia invalida o dato ya creado
     */
    private void ejecutar_update(String update) throws SQLException {
        Statement st = con.createStatement();
        st.executeUpdate(update);
    }

    /**
     * Ejecuta una query recibida por parametro
     *
     * @param query query a ejecutarse
     * @return conjunto de tablas y valores sobre los que se realizo la query
     * @throws SQLException caso de que la query posea algun error
     */
    private Collection<Collection<String>> ejecutar_query(String query) throws SQLException {
        Collection<Collection<String>> data = new LinkedList<>();
        PreparedStatement pst = con.prepareStatement(query);
        ResultSet rst = pst.executeQuery();
        ResultSetMetaData rsmd = pst.getMetaData();
        int cant_columnas = rsmd.getColumnCount();
        int i;

        LinkedList<String> nombre_atributos = new LinkedList<String>();
        for (i = 1; i <= cant_columnas; i++) {
            nombre_atributos.addLast(rsmd.getColumnName(i));
        }
        data.add(nombre_atributos);

        i = 1;
        LinkedList<String> lista_aux;
        while (rst.next()) {
            lista_aux = new LinkedList<String>();
            while (i <= cant_columnas) {
                lista_aux.addLast(rst.getString(i));
                i++;
            }
            data.add(lista_aux);
            i = 1;
        }
        pst.close();
        rst.close();
        rsmd = null;
        return data;
    }

    /**
     * Calcula todas las ciudades de las que parte un vuelo
     *
     * @return coleccion con las ciudades de las que parte un vuelo
     */
    public Collection<String> ciudades_origen() {
        LinkedList<String> ciudades = new LinkedList<String>();
        try {
            String query = "select ciudad" +
                    " from vuelos_programados join aeropuertos on aeropuerto_salida = codigo" +
                    " group by ciudad";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                ciudades.add(rs.getString(1));
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
        }
        return ciudades;
    }

    /**
     * Calcula todas las ciudades a las que llega un vuelo
     *
     * @return coleccion con las ciudades de las que llega un vuelo
     */
    public Collection<String> ciudades_destino() {
        LinkedList<String> ciudades = new LinkedList<String>();
        try {
            String query = "select ciudad" +
                    " from vuelos_programados join aeropuertos on aeropuerto_llegada = codigo" +
                    " group by ciudad";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                ciudades.add(rs.getString(1));
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
        }
        return ciudades;
    }

    /**
     * Retorna vuelo/s que van de ciudad_origen a ciudad_destino en la fecha pasaa por parametor
     *
     * @param ciudad_origen  ciudad de donde parte el vuelo
     * @param ciudad_destino ciudad a donde se dirige el vuelo
     * @param fecha          fecha en la que sale el vuelo
     * @return tabla que contiene el numero de vuelo, el aeropuerto de salida, la hora de salida, el aeropuerto de llegada
     * la hora de llegada, el modelo del avion y el tiempo estimado
     */
    public Collection<Collection<String>> buscar_vuelos(String ciudad_origen, String ciudad_destino, Date fecha) {
        Collection<Collection<String>> data = new LinkedList<>();
        try {
            Date fecha_sql = fechas.Fechas.convertirDateADateSQL(fecha);
            String formato_devuelto = "'%d/%m/%Y'";
            String query = " select vuelo, nombre_salida as aeropuerto_salida , hora_sale," +
                    " aeropuerto_llegada, hora_llega, modelo_avion, tiempo_estimado, date_format(fecha, " + formato_devuelto +") as fecha" +
                    " from vuelos_disponibles" +
                    " where fecha = '" + fecha_sql + "' and ciudad_salida = '" + ciudad_origen + "' and ciudad_llegada = '" + ciudad_destino + "' " +
                    " group by vuelo, aeropuerto_salida, hora_sale, aeropuerto_llegada, hora_llega, modelo_avion, tiempo_estimado";
            data = this.ejecutar_query(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    /**
     * Metodo que obtiene informacion de las clases, precios y asientos disponibles de un vuelo
     * @param num_vuelo numero del vuelo
     * @param fecha fecha en la que es el vuelo
     * @return coleccion con la informacion de asientos disponibles, clases y precios del vuelo
     */
    public Collection<Collection<String>> info_vuelo(int num_vuelo, Date fecha){
        Collection<Collection<String>> data = new LinkedList<>();
        try {
            Date fecha_sql = fechas.Fechas.convertirDateADateSQL(fecha);
            String query = "select clase, cant_libres as asientos_disponibles, precio" +
                    " from vuelos_disponibles" +
                    " where vuelo = " + num_vuelo + " and fecha = '" + fecha_sql + "'";
            data = this.ejecutar_query(query);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return data;
    }

    /**
     * Metodo utilizado para finalizar la conexion con la base de datos
     */
    public void shutdown(){
        try {
            if(con!= null && !con.isClosed()) {
                con.close();
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }



}
