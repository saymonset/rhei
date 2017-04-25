/**
 * 
 */
package ve.org.bcv.rhei.ajax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;

import ve.org.bcv.rhei.bean.Proveedor;
import ve.org.bcv.rhei.bean.ValorNombre;
import ve.org.bcv.rhei.negocio.MesesBean;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.comparator.ProvSortByValueComparator;
import com.bcv.comparator.SortByValueComparatorAsc;
import com.bcv.dao.jdbc.RelacionDePagosDao;
import com.bcv.dao.jdbc.impl.RelacionDePagosDaoImpl;
import com.bcv.reporte.relacionpago.ProveedorRpago1Bean;

/**
 * Segun los filtros, traeme todas las solicitud es para hacer el reporte
 * 
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 31/07/2015 13:43:01
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class ReporteAjaxServlet extends HttpServlet {
 
    private static final long serialVersionUID = 1L;
    private RelacionDePagosDao relacionDePagosDao= new RelacionDePagosDaoImpl();
 
    // This will store all received articles
    //List<Article> articles = new LinkedList<Article>();
    List<Integer> numSolicituds = new ArrayList<Integer>();
 
    /***************************************************
     * URL: /jsonservlet
     * doPost(): receives JSON data, parse it, map it and send back as JSON
     ****************************************************/
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
    	HttpSession sesion = null;
	     sesion = request.getSession(false);

        // 1. get received JSON data from request
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
        if(br != null){
            json = br.readLine();
        }
 
        // 2. initiate jackson mapper
        ObjectMapper mapper = new ObjectMapper();
 
        // 3. Convert received JSON to Article
        Proveedor proveedor = mapper.readValue(json, Proveedor.class);
        
        List<Proveedor> proveedors = new ArrayList<Proveedor>();
     
        	try {
        		
        		/**************MESES********/
				MesesBean mesesBean= new MesesBean();
				String[] mes=mesesBean.getMes();
				String[] meses = mesesBean.getMeses();

				List<ValorNombre> listadoMeses = new ArrayList<ValorNombre>();
				ValorNombre valorNombre = null;
				for (int j = 0; j <= 12; j++) {
					valorNombre = new ValorNombre(meses[j], mes[j]);
					listadoMeses.add(valorNombre);
					System.out.println("Saymojns="+valorNombre.getNombre());
				}
				Collections.sort(listadoMeses,new SortByValueComparatorAsc());
			    request.setAttribute("listadoMeses", listadoMeses);
			    
			    
			    
	 
        		
        		String ninoEspecial="";
        		String numSolicituds=null;
        		String isToCreateReportDefinitivo=Constantes.PAGADO_NOPAGADO_AMBOS;
        		List<ProveedorRpago1Bean> proveedorRpago1Bean= relacionDePagosDao.searchSolicitudesToReporte(proveedor.getCoFormaPago(),proveedor.getReceptorPago(),proveedor.getPeriodoEscolar(),numSolicituds,
        				proveedor.getCoStatus(),proveedor.getTipoEmpleado(),isToCreateReportDefinitivo,ninoEspecial);
        		  /** Solo los proveedores qiue son colegios */
             
    			for (ProveedorRpago1Bean numSolicitud:proveedorRpago1Bean){
    				proveedor= new Proveedor();
    				proveedor.setNombre(numSolicitud.getNuSolicitud()+"");
    				proveedor.setValor(numSolicitud.getNuSolicitud()+"");
    				proveedor.setValue((int)numSolicitud.getNuSolicitud());
    				proveedor.setLabel(numSolicitud.getNuSolicitud() +Constantes.SEPARADORENTRENUMSOLINOMBRE+numSolicitud.getNinio());
    				proveedor.setSelected(false);
    				proveedors.add(proveedor);
    			}
    		} catch (NumberFormatException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}	
       
        	Collections.sort(proveedors,new ProvSortByValueComparator());


        	  mapper = new ObjectMapper();
        	 
              
        // 4. Set response type to JSON
        response.setContentType("application/json");           
 
        // 5. Add article to List<Article>
       // articles.add(article);
 
        // 6. Send List<Article> as JSON to client
        //Map<Integer,Proveedor> unico = new HashMap<Integer,Proveedor>();
        
        
        mapper.writeValue(response.getOutputStream(), proveedors);
    }
}