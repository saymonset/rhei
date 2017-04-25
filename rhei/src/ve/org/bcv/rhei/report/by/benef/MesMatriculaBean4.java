package ve.org.bcv.rhei.report.by.benef;

import java.io.Serializable;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 28/04/2015 15:54:03
 * 
 */
public class MesMatriculaBean4 implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mesmtricula;
	private double montocei;
	private double montobcv;
	
	
	public String getMesmtricula() {
		return mesmtricula;
	}
	public void setMesmtricula(String mesmtricula) {
		this.mesmtricula = mesmtricula;
	}
	public double getMontocei() {
		return montocei;
	}
	public void setMontocei(double montocei) {
		this.montocei = montocei;
	}
	public double getMontobcv() {
		return montobcv;
	}
	public void setMontobcv(double montobcv) {
		this.montobcv = montobcv;
	}
 
}
