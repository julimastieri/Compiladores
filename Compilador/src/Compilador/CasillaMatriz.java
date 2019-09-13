package Compilador;

public class CasillaMatriz {
	
	int proxEstado;
	int [] idAs;
	
	public CasillaMatriz (int estado, int [] as) {
		idAs = as.clone();
		proxEstado = estado;
	}
	
	public int [] getIdAccionSemantica() {
		return idAs.clone();
	}
	
	public int getProxEstado() {
		return proxEstado;
	}
}
