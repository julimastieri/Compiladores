package analizadorLexico;


public class Matriz {
	
	 private final int cantEstados = 21;
	 private final int cantColumnas = 27;

	 private CasillaMatriz[][] matriz;

	 public Matriz() {
		 
		 matriz = new CasillaMatriz[cantEstados][cantColumnas];
	     llenarMatriz();
	 }
	 
	 private void llenarMatriz() {
		 int fila = 0;
		 //cargar las acciones semanticas nuestras
	     AccionSemantica[] AS = {new AccionSemantica0(), new AccionSemantica1(), new AccionSemantica2(), new AccionSemantica3(), new AccionSemantica4(), new AccionSemantica5(), new AccionSemantica6(), new AccionSemantica7(), new AccionSemantica8(), new AccionSemanticaE(), new AccionSemantica10(), new AccionSemantica11(), new AccionSemantica12()};
	     CasillaMatriz[] filaMatriz;
	     
	     //fila 0
	     filaMatriz = new CasillaMatriz[]{crearCasilla(AS[1], 4), c(AS[1], 16), c(AS[1], 16), c(AS[1], 1), c(AS[7], -1), c(AS[7], -1), c(AS[7], -1), c(AS[7], -1), c(AS[1], 12), c(AS[1], 13), c(AS[1], 14), c(AS[1], 15), c(AS[3], 0), c(AS[1], 18), c(AS[1], 5), c(AS[7], -1), c(AS[7], -1), c(AS[7], -1), c(AS[7], -1), c(AS[1], 17), c(AS[1], 16), c(AS[1], 16), c(AS[7], -1), c(AS[7], -1),c(AS[3], 0),c(AS[3], 0),c(AS[0],0)};
	     addFila(filaMatriz, fila++);
	     //asi con todas las filas
	 }
	 
	 
	 
	 private CasillaMatriz crearCasilla (AccionSemantica as, int e) {
		 return new CasillaMatriz(as, e);
	 }
	 
	 
	 
	 private void addFila(CasillaMatriz[] casilleros , int fila) {
		 
		 if (fila < cantEstados) {
			 
			 for (int i = 0; i < cantColumnas; i++) {
				 matriz[fila][i] = casilleros[i];
	         } 
	     }
	 }
	 
	 public int getEstado (int fil, int col) {
		 if (verificaLimites(fil,col)) {
			 return matriz[fil][col].estado;
		 }
		 return -1;
	 }
	 
	 public AccionSemantica getAS (int fil, int col) {
		 if (verificaLimites(fil,col)) {
			 return matriz[fil][col].AS;
		 }
		 return null;
	 }
	 
	 private boolean verificaLimites (int f, int c) {
		 
		 return (f < cantEstados) && (c < cantColumnas) && (f > -1) && (c > -1);
	 }

}
