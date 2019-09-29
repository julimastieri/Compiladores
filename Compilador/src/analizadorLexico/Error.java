package analizadorLexico;

public class Error {
	
	public String tipo; //Si es error o warning
	public String descripcion;
	public int nroLinea;
	public static boolean huboErrores=false;
	
	
	public Error(String tipo, String descripcion, int linea) {
		
		this.tipo = tipo;
		this.descripcion = descripcion;
		this.nroLinea = linea;
		if(tipo=="ERROR")
			Error.huboErrores=true;
	}
	
	
	public String toString(){
		return tipo +": " + "Linea " + nroLinea + ": " + descripcion + "\r\n";
		// Error: Linea 24: constante fuera de rango permitido. 
	}

}
