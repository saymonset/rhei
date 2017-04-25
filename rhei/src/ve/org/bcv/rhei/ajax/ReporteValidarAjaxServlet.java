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

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.bcv.comparator.ProvSortByValueComparator;
import com.bcv.dao.jdbc.ReporteEstatusDao;
import com.bcv.dao.jdbc.impl.ReporteEstatusDaoImpl;

import ve.org.bcv.rhei.bean.Proveedor;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 20/10/2015 09:23:16
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class ReporteValidarAjaxServlet extends HttpServlet {
 
    private static final long serialVersionUID = 1L;
    private ReporteEstatusDao reporteEstatusDao= new ReporteEstatusDaoImpl();
 
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
 
        String valor="1";
        /** si el valor es 2, entonces viene de reporte transitivo definitivo*/
        if (proveedor!=null && "2".equalsIgnoreCase(proveedor.getValor()) && !StringUtils.isEmpty(proveedor.getNombre())&& !StringUtils.isEmpty(proveedor.getPeriodoEscolar())){
        	boolean existe = reporteEstatusDao.existeReporteStatusNameAnio(proveedor.getNombre(), proveedor.getPeriodoEscolar());
        	if (existe ){
        		valor="0";
        	}
        }
        if (proveedor==null ){
        	proveedor= new Proveedor();
        }
       // 
        proveedor.setValor(valor);        
        proveedors.add(proveedor);
        	Collections.sort(proveedors,new ProvSortByValueComparator());

         
        	
        	
        	  mapper = new ObjectMapper();
        	  
        	  
        	   
        	 
//        	    final byte[] data = out.toByteArray();
//        	    System.out.println(new String(data));
        	 
              
        // 4. Set response type to JSON
        response.setContentType("application/json");           
 
        // 5. Add article to List<Article>
       // articles.add(article);
 
        // 6. Send List<Article> as JSON to client
       
        
        
        mapper.writeValue(response.getOutputStream(),proveedors);
    }
}