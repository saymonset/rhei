package ve.org.bcv.rhei.bean;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bcv.dao.jdbc.TrabajadorDao;
import com.bcv.dao.jdbc.impl.TrabajadorDaoImpl;
import com.bcv.model.Trabajador;


/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 26/01/2015 15:15:21
 * Listamos las companias
 */
public class Compania implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String valor;
	private String nombre;
	private List<Compania> companias;
	 
	
	 
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<Compania> getCompanias() {
		return companias;
	}
	public void setCompanias(List<Compania> companias) {
		this.companias = companias;
	}
	
	
	/**
	 * LLenamos los Tipos de datos
	 */
	public List<Compania> llenarData(String filtro){
		List<Compania> companias=new ArrayList<Compania>();
		Trabajador trab = new Trabajador();
		ArrayList<String> listadoCompanias = new ArrayList<String>();
		//listadoCompanias=trab.cargarCompania(request.getSession().getAttribute("companiaAnalista").toString());
		TrabajadorDao trabajadorDao = new TrabajadorDaoImpl();
		try {
			listadoCompanias=(ArrayList<String>) trabajadorDao.cargarCompania(filtro);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Compania compania= null;
		for(int i =0;i<listadoCompanias.size();i+=2){
			compania=new Compania();
			compania.setValor(listadoCompanias.get(i).toString());
			compania.setNombre( listadoCompanias.get(i+1).toString());
			companias.add(compania);
		}
		compania=new Compania();
		compania.setValor("%");
		compania.setNombre("Todas");
		companias.add(compania);
		 
		return companias;
	}
	
}
