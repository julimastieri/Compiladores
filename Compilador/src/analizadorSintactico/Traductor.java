package analizadorSintactico;

public class Traductor {
	
	StringBuilder assembler = new StringBuilder();
	
	public Traductor () {
		
	}
	
	private static boolean esHoja (NodoArbol nodo) {
		if ((nodo.getNodoIzq() == null) && (nodo.getNodoDer()==null)) {
			return true;
		}
		return false;
	}
	
	public static NodoArbol subIzquierdoConHojas(NodoArbol nodo) {
		
		System.out.println("ENTRE");
		if (!(esHoja(nodo.getNodoIzq()) && (esHoja(nodo.getNodoDer())))) {
			
			subIzquierdoConHojas(nodo.getNodoIzq());
		
		}
		return nodo;
	}
	

}
