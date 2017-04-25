/**
 * 
 */
package ve.org.bcv.rhei.ajax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import ve.org.bcv.rhei.bean.Proveedor;

import com.bcv.dao.jdbc.FacturaDao;
import com.bcv.dao.jdbc.impl.FacturaDaoImpl;
import com.bcv.model.Factura;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 23/07/2015 11:27:50
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class FacturasAjax  extends HttpServlet {
 
    private static final long serialVersionUID = 1L;
    private FacturaDao facturaDao= new FacturaDaoImpl();
 
    // This will store all received articles
    //List<Article> articles = new LinkedList<Article>();
    List<Factura> facturas = new ArrayList<Factura>();
 
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
        
        /** Solo los proveedores qiue son colegios */
        List<Proveedor> proveedors = new ArrayList<Proveedor>();
        if (!StringUtils.isEmpty(proveedor.getNombre())){
        	try {
        		int coRepStatus=0;
    			facturas= facturaDao.consultarFacturasByNroSolicitudId(new Integer(proveedor.getNombre()),coRepStatus);
    			for (Factura factura:facturas){
    				proveedor= new Proveedor();
    				proveedor.setNombre(factura.getNroFactura());
    				proveedor.setValor(factura.getNroFactura());
    				proveedors.add(proveedor);
    			}
    		} catch (NumberFormatException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}	
        } 
        
		
		
 
        // 4. Set response type to JSON
        response.setContentType("application/json");           
 
        // 5. Add article to List<Article>
       // articles.add(article);
 
        // 6. Send List<Article> as JSON to client
        mapper.writeValue(response.getOutputStream(), proveedors);
    }
}