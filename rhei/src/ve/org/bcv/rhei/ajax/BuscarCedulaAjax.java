package ve.org.bcv.rhei.ajax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;

import com.bcv.dao.jdbc.TrabajadorDao;
import com.bcv.dao.jdbc.impl.TrabajadorDaoImpl;

import ve.org.bcv.rhei.bean.Proveedor;
@WebServlet(description = "Buscar cedula", urlPatterns = { "/buscarCedulaAjax" })
public class BuscarCedulaAjax extends HttpServlet {
 
    private static final long serialVersionUID = 1L;
 
    // This will store all received articles
    //List<Article> articles = new LinkedList<Article>();
  private  List<Proveedor> proveedors = new ArrayList<Proveedor>();
  private TrabajadorDao trabajadorDao = new TrabajadorDaoImpl();
 
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

        System.out.println("proveedor.getNombre()="+proveedor.getNombre());
        System.out.println("proveedor.getApellido()="+proveedor.getApellido());
        /** Solo los proveedores qiue son colegios */
		List<Proveedor> proveedors =  trabajadorDao.buscarCedulas(proveedor.getNombre(),proveedor.getApellido());
 
        // 4. Set response type to JSON
        response.setContentType("application/json");           
 
        // 5. Add article to List<Article>
       // articles.add(article);
 
        // 6. Send List<Article> as JSON to client
        mapper.writeValue(response.getOutputStream(), proveedors);
    }
    
 
}