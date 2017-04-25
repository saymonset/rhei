package ve.org.bcv.rhei.util;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 02/02/2015 14:33:45
 * 
 * Constantes mapeadas igual que en bd companias
 * 
 */
public enum Compania {
      BCV("01"),SUBSEDEMRCBO("02"),CASAMONEDA("03");
   
      
      
      private final String codCompania;
      
        Compania(String codCompania){
    	  this.codCompania=codCompania;
      }
      
      public String getCodCompania(){
    	  return this.codCompania;
      }
}
