package ve.org.bcv.rhei.util;

import java.io.Serializable;

/**
 * @author Simon Alberto Rodriguez Pacheco Clase usada para generar el control
 *         del paginador de las tablas html
 */
public class Paginador implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int indMenor;
	private int indMayor;
	private int cuantosReg;

	/**
	 * siguiente registros a recorrer
	 * 
	 * @param indMenor
	 * @param indMayor
	 * @param cuantosReg
	 * @return
	 */
	public Paginador siguiente(int indMenor, int indMayor, int cuantosReg) {
		Paginador paginador = new Paginador();

		if (Constantes.paginas + indMayor > cuantosReg) {
			if (indMayor>=cuantosReg){
				return ultimo( indMenor,   indMayor,   cuantosReg);
			}else{
				indMenor = indMayor;
				indMayor = cuantosReg;	
			}
			
		} else {
			indMenor = indMayor+1;
			indMayor += Constantes.paginas;
		}
		paginador.setIndMayor(indMayor);
		paginador.setIndMenor(indMenor);
		return paginador;
	}

	/**
	 * Anteriores registros a recorrer
	 * 
	 * @param indMenor
	 * @param indMayor
	 * @param cuantosReg
	 * @return
	 */
	public Paginador anterior(int indMenor, int indMayor, int cuantosReg) {
		Paginador paginador = new Paginador();
		if (indMenor - Constantes.paginas > 0) {
			indMayor = indMenor-1;
			indMenor -= Constantes.paginas;
		} else {
			indMenor = 0;
			indMayor = Constantes.paginas;
		}
		paginador.setIndMayor(indMayor);
		paginador.setIndMenor(indMenor);
		return paginador;
	}

	/**
	 * primero registros a recorrer
	 * 
	 * @param indMenor
	 * @param indMayor
	 * @param cuantosReg
	 * @return
	 */
	public Paginador primero(int indMenor, int indMayor, int cuantosReg) {
		Paginador paginador = new Paginador();
		indMenor = 0;
		indMayor = Constantes.paginas;
		paginador.setIndMayor(indMayor);
		paginador.setIndMenor(indMenor);
		return paginador;
	}

	/**
	 * ultimo registros a recorrer
	 * 
	 * @param indMenor
	 * @param indMayor
	 * @param cuantosReg
	 * @return
	 */
	public Paginador ultimo(int indMenor, int indMayor, int cuantosReg) {
		Paginador paginador = new Paginador();
		paginador.setIndMayor(cuantosReg);
		if ((cuantosReg+1) - Constantes.paginas > 0) {
			paginador.setIndMenor((cuantosReg+1) - Constantes.paginas);
		} else {
			paginador.setIndMenor(0);
		}

		return paginador;
	}

	public Paginador devolverSegunPeticion(int indMenor, int indMayor, int cuantosReg,char peticion) {
		Paginador paginador = new Paginador();
		switch (peticion) {
		case 'p'://primero
			paginador=primero(  indMenor,   indMayor,   cuantosReg) ;
			break;
		case 'u'://ultimo
			paginador=ultimo(indMenor, indMayor, cuantosReg);
			break;
		case 'a'://anterior
			paginador=anterior(  indMenor,   indMayor,   cuantosReg) ;
			break;
		case 's'://siguiente
			paginador=siguiente(  indMenor,   indMayor,   cuantosReg);
			break;

		default:
			paginador=primero(  indMenor,   indMayor,   cuantosReg) ;
			break;
		}
		return paginador;
	}

	public int getIndMenor() {
		return indMenor;
	}

	public void setIndMenor(int indMenor) {
		this.indMenor = indMenor;
	}

	public int getIndMayor() {
		return indMayor;
	}

	public void setIndMayor(int indMayor) {
		this.indMayor = indMayor;
	}

	public int getcuantosReg() {
		return cuantosReg;
	}

	public void setcuantosReg(int cuantosReg) {
		this.cuantosReg = cuantosReg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
