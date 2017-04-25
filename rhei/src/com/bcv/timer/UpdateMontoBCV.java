/**
 * 
 */
package com.bcv.timer;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 08/03/2016 08:50:47
 * 2016
 * mail : oraclefedora@gmail.com
 */
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.BcvDao;
import com.bcv.dao.jdbc.ParametroDao;
@Singleton
@LocalBean
@Startup
public class UpdateMontoBCV extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Chequearemos los proveedores que no han sido activado en determinado
	 * tiempo, eStos sera eliminados de bd
	 */
	@Inject
	private BcvDao bcvDao;
	@Inject
	private ParametroDao parametroDao;
	
	private static final Logger log = Logger.getLogger(UpdateMontoBCV.class);
	
	  @PostConstruct
	    public void onStartup() {
	        System.out.println("Initialization success.");
	    }

	  @Schedule(second="0", minute="*", hour="*/1", persistent=false)
	   public void execute() {
		   
		  String  accion = "Modificar";
				  String  codigoCompania = "01";
				  String   tipoEmpleado = "EMP";
				  String   tipoBeneficio = "RHEI";
				  String   codigoParametro = "MTOBCV";
				  
				  String   tipoDatoParametro="N";
				  String    observaciones="ACTUALIZACION AUTOMATICA";
				  
				  SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				  String   fechaVigencia= sdf.format(new Date()); 
				  
				  
				  /***Calculamos el total en el memorando con su formato*******/   		    
   		    DecimalFormat df2 = new DecimalFormat(     Constantes.FORMATO_DOUBLE, 	      new DecimalFormatSymbols(new Locale("es", "VE")));
    	    BigDecimal value = new BigDecimal(bcvDao.sueldoMinimo());
    	    String   valorParametro=new String(df2.format(value.floatValue()));
		   try {
			parametroDao.guardarParametro(
						  accion,  codigoCompania,  tipoEmpleado,  tipoBeneficio,  codigoParametro,  fechaVigencia,
						  valorParametro,  tipoDatoParametro,  observaciones) ;
			//System.out.println("Monto BCV "+observaciones);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		   /**
		   accion = Modificar
				   codigoCompania = 01
				   tipoEmpleado = EMP
				   tipoBeneficio = RHEI
				   CODIGOpARAMETRO = MTOBCV
				   FECHAVIGENCIA=25-05-2015
				   VALORPARAMETRO=15.100,00
				   tipoDatoParametro=N
				   observaciones="aCTUALIZACION AUTOMATICA"
				   ***/
		   
		   
	   }

	 

}