package fechas;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Fechas
{

   public static java.util.Date convertirStringADate(String p_fecha)
   {
      java.util.Date retorno = null;
      if (p_fecha != null)
      {
         try
         {
            retorno = (new SimpleDateFormat("dd/MM/yyyy")).parse(p_fecha);
         }
         catch (ParseException ex)
         {
            System.out.println(ex.getMessage());
         }
      }
      return retorno;
   }

   public static String convertirDateAString(java.util.Date p_fecha)
   {
      String retorno = null;
      if (p_fecha != null)
      {
         retorno = (new SimpleDateFormat("dd/MM/yyyy")).format(p_fecha);
      }
      return retorno;
   }

   public static String convertirDateAStringDB(java.util.Date p_fecha)
   {
      String retorno = null;
      if (p_fecha != null)
      {
         retorno = (new SimpleDateFormat("yyyy-MM-dd")).format(p_fecha);
      }
      return retorno;
   }

   public static java.sql.Date convertirDateADateSQL(java.util.Date p_fecha)
   {
      java.sql.Date retorno = null;
      if (p_fecha != null)
      {
         retorno = java.sql.Date.valueOf((new SimpleDateFormat("yyyy-MM-dd")).format(p_fecha));
      }
      return retorno;
   }


   public static java.sql.Date convertirStringADateSQL(String p_fecha)
   {
      java.sql.Date retorno = null;
      if (p_fecha != null)
      {
         retorno = Fechas.convertirDateADateSQL(Fechas.convertirStringADate(p_fecha));
      }
      return retorno;
   }

   public static boolean validar(String p_fecha)
   {
      if (p_fecha != null)
      {
         try
         {
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        	sdf.setLenient(false);
        	sdf.parse(p_fecha);
            return true;
         }
         catch (ParseException ex) {}
      }
      return false;
   }
   
}
