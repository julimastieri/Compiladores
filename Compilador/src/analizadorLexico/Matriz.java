package analizadorLexico;


public class Matriz {
	
	 private final int cantEstados = 8;
	 private final int cantColumnas = 23;

	 private CasillaMatriz[][] matriz;

	 public Matriz() {
		 
		 matriz = new CasillaMatriz[cantEstados][cantColumnas];
	     llenarMatriz();
	 }
	 
	 private void llenarMatriz() {
		 int fila = 0;
	     AccionSemantica[] AS = {new AccionSemantica1(), new AccionSemantica2(), new AccionSemantica3(), new AccionSemantica4(), new AccionSemantica5(), new AccionSemantica6(), new AccionSemantica7(), new AccionSemantica8(), new AccionSemantica9()};
	     CasillaMatriz[] filaMatriz;
	     
	     //fila 0
	     filaMatriz = new CasillaMatriz[]
	    		 	{/*0*/new CasillaMatriz(AS[0], 1),/*1*/new CasillaMatriz(AS[0], 2),/*2*/new CasillaMatriz(null, null),
	    		 	/*3*/ new CasillaMatriz(AS[2], -1),/*4*/new CasillaMatriz(AS[2], -1),/*5*/new CasillaMatriz(AS[2], -1),
	    		 	/*6*/new CasillaMatriz(AS[2], -1),/*7*/ new CasillaMatriz(AS[0], 3),/*8*/ new CasillaMatriz(AS[0], 6),
	    		 	/*9*/ new CasillaMatriz(null, -1), /*10*/ new CasillaMatriz(null, 8), /*11*/ new CasillaMatriz(null, null),
	    		 	/*12*/ new CasillaMatriz(AS[0], 4), /*13*/ new CasillaMatriz(AS[0], 5), /*14*/ new CasillaMatriz(AS[2], -1),
	    		 	/*15*/ new CasillaMatriz(AS[2], -1), /*16*/ new CasillaMatriz(AS[2], -1), /*17*/ new CasillaMatriz(AS[2], -1),
	    		 	/*16*/ new CasillaMatriz(AS[2], -1), /*19*/ new CasillaMatriz(AS[2], -1), /*20*/ new CasillaMatriz(null, 0),
	    		 	/*21*/ new CasillaMatriz(null, 0), /*22*/ new CasillaMatriz(null, 7), /*23*/ new CasillaMatriz(null, null)};
	     addFila(filaMatriz, fila);
	     fila++;
	     
	     //fila 1
	     filaMatriz = new CasillaMatriz[]
	    		 	{/*0*/new CasillaMatriz(AS[0], 1),/*1*/new CasillaMatriz(AS[0], 1),/*2*/new CasillaMatriz(AS[0], 1),
	    		 	/*3*/ new CasillaMatriz(AS[1], -1),/*4*/new CasillaMatriz(AS[1], -1),/*5*/new CasillaMatriz(AS[1], -1),
	    		 	/*6*/new CasillaMatriz(AS[1], -1),/*7*/ new CasillaMatriz(AS[1], -1),/*8*/ new CasillaMatriz(AS[1], -1),
	    		 	/*9*/ new CasillaMatriz(AS[1], -1), /*10*/ new CasillaMatriz(AS[1], -1), /*11*/ new CasillaMatriz(AS[1], -1),
	    		 	/*12*/ new CasillaMatriz(AS[1], -1), /*13*/ new CasillaMatriz(AS[1], -1), /*14*/ new CasillaMatriz(AS[1], -1),
	    		 	/*15*/ new CasillaMatriz(AS[1], -1), /*16*/ new CasillaMatriz(AS[1], -1), /*17*/ new CasillaMatriz(AS[1], -1),
	    		 	/*16*/ new CasillaMatriz(AS[1], -1), /*19*/ new CasillaMatriz(AS[1], -1), /*20*/ new CasillaMatriz(AS[1], -1),
	    		 	/*21*/ new CasillaMatriz(AS[1], -1), /*22*/ new CasillaMatriz(AS[1], -1), /*23*/ new CasillaMatriz(AS[1], -1)};
	     addFila(filaMatriz, fila);
	     fila++;
	     
	     //fila 2
	     filaMatriz = new CasillaMatriz[]
	    		 	{/*0*/new CasillaMatriz(AS[3], -1),/*1*/new CasillaMatriz(AS[0], 2),/*2*/new CasillaMatriz(AS[3], -1),
	    		 	/*3*/ new CasillaMatriz(AS[3], -1),/*4*/new CasillaMatriz(AS[3], -1),/*5*/new CasillaMatriz(AS[3], -1),
	    		 	/*6*/new CasillaMatriz(AS[3], -1),/*7*/ new CasillaMatriz(AS[3], -1),/*8*/ new CasillaMatriz(AS[3], -1),
	    		 	/*9*/ new CasillaMatriz(AS[3], -1), /*10*/ new CasillaMatriz(AS[3], -1), /*11*/ new CasillaMatriz(AS[3], -1),
	    		 	/*12*/ new CasillaMatriz(AS[3], -1), /*13*/ new CasillaMatriz(AS[3], -1), /*14*/ new CasillaMatriz(AS[3], -1),
	    		 	/*15*/ new CasillaMatriz(AS[3], -1), /*16*/ new CasillaMatriz(AS[3], -1), /*17*/ new CasillaMatriz(AS[3], -1),
	    		 	/*16*/ new CasillaMatriz(AS[3], -1), /*19*/ new CasillaMatriz(AS[3], -1), /*20*/ new CasillaMatriz(AS[3], -1),
	    		 	/*21*/ new CasillaMatriz(AS[3], -1), /*22*/ new CasillaMatriz(AS[3], -1), /*23*/ new CasillaMatriz(AS[3], -1)};
	     addFila(filaMatriz, fila);
	     fila++;
	     
	     //fila 3
	     filaMatriz = new CasillaMatriz[]
	    		 	{/*0*/new CasillaMatriz(AS[7], -1),/*1*/new CasillaMatriz(AS[7], -1),/*2*/new CasillaMatriz(AS[7], -1),
	    		 	/*3*/ new CasillaMatriz(AS[7], -1),/*4*/new CasillaMatriz(AS[7], -1),/*5*/new CasillaMatriz(AS[7], -1),
	    		 	/*6*/new CasillaMatriz(AS[7], -1),/*7*/ new CasillaMatriz(AS[7], -1),/*8*/ new CasillaMatriz(AS[4], -1),
	    		 	/*9*/ new CasillaMatriz(AS[7], -1), /*10*/ new CasillaMatriz(AS[7], -1), /*11*/ new CasillaMatriz(AS[7], -1),
	    		 	/*12*/ new CasillaMatriz(AS[7], -1), /*13*/ new CasillaMatriz(AS[7], -1), /*14*/ new CasillaMatriz(AS[7], -1),
	    		 	/*15*/ new CasillaMatriz(AS[7], -1), /*16*/ new CasillaMatriz(AS[7], -1), /*17*/ new CasillaMatriz(AS[7], -1),
	    		 	/*16*/ new CasillaMatriz(AS[7], -1), /*19*/ new CasillaMatriz(AS[7], -1), /*20*/ new CasillaMatriz(AS[7], -1),
	    		 	/*21*/ new CasillaMatriz(AS[7], -1), /*22*/ new CasillaMatriz(AS[7], -1), /*23*/ new CasillaMatriz(AS[7], -1)};
	     addFila(filaMatriz, fila);
	     fila++;
	     
	     //fila 4
	     filaMatriz = new CasillaMatriz[]
	    		 	{/*0*/new CasillaMatriz(AS[6], -1),/*1*/new CasillaMatriz(AS[6], -1),/*2*/new CasillaMatriz(AS[6], -1),
	    		 	/*3*/ new CasillaMatriz(AS[6], -1),/*4*/new CasillaMatriz(AS[6], -1),/*5*/new CasillaMatriz(AS[6], -1),
	    		 	/*6*/new CasillaMatriz(AS[6], -1),/*7*/ new CasillaMatriz(AS[6], -1),/*8*/ new CasillaMatriz(AS[4], -1),
	    		 	/*9*/ new CasillaMatriz(AS[6], -1), /*10*/ new CasillaMatriz(AS[6], -1), /*11*/ new CasillaMatriz(AS[6], -1),
	    		 	/*12*/ new CasillaMatriz(AS[6], -1), /*13*/ new CasillaMatriz(AS[4], -1), /*14*/ new CasillaMatriz(AS[6], -1),
	    		 	/*15*/ new CasillaMatriz(AS[6], -1), /*16*/ new CasillaMatriz(AS[6], -1), /*17*/ new CasillaMatriz(AS[6], -1),
	    		 	/*16*/ new CasillaMatriz(AS[6], -1), /*19*/ new CasillaMatriz(AS[6], -1), /*20*/ new CasillaMatriz(AS[6], -1),
	    		 	/*21*/ new CasillaMatriz(AS[6], -1), /*22*/ new CasillaMatriz(AS[6], -1), /*23*/ new CasillaMatriz(AS[6], -1)};
	     addFila(filaMatriz, fila);
	     fila++;
	     
	   //fila 5
	     filaMatriz = new CasillaMatriz[]
	    		 	{/*0*/new CasillaMatriz(AS[6], -1),/*1*/new CasillaMatriz(AS[6], -1),/*2*/new CasillaMatriz(AS[6], -1),
	    		 	/*3*/ new CasillaMatriz(AS[6], -1),/*4*/new CasillaMatriz(AS[6], -1),/*5*/new CasillaMatriz(AS[6], -1),
	    		 	/*6*/new CasillaMatriz(AS[6], -1),/*7*/ new CasillaMatriz(AS[6], -1),/*8*/ new CasillaMatriz(AS[4], -1),
	    		 	/*9*/ new CasillaMatriz(AS[6], -1), /*10*/ new CasillaMatriz(AS[6], -1), /*11*/ new CasillaMatriz(AS[6], -1),
	    		 	/*12*/ new CasillaMatriz(AS[6], -1), /*13*/ new CasillaMatriz(AS[6], -1), /*14*/ new CasillaMatriz(AS[6], -1),
	    		 	/*15*/ new CasillaMatriz(AS[6], -1), /*16*/ new CasillaMatriz(AS[6], -1), /*17*/ new CasillaMatriz(AS[6], -1),
	    		 	/*16*/ new CasillaMatriz(AS[6], -1), /*19*/ new CasillaMatriz(AS[6], -1), /*20*/ new CasillaMatriz(AS[6], -1),
	    		 	/*21*/ new CasillaMatriz(AS[6], -1), /*22*/ new CasillaMatriz(AS[6], -1), /*23*/ new CasillaMatriz(AS[6], -1)};
	     addFila(filaMatriz, fila);
	     fila++;
	     
	   //fila 6
	     filaMatriz = new CasillaMatriz[]
	    		 	{/*0*/new CasillaMatriz(AS[7], -1),/*1*/new CasillaMatriz(AS[7], -1),/*2*/new CasillaMatriz(AS[7], -1),
	    		 	/*3*/ new CasillaMatriz(AS[7], -1),/*4*/new CasillaMatriz(AS[7], -1),/*5*/new CasillaMatriz(AS[7], -1),
	    		 	/*6*/new CasillaMatriz(AS[7], -1),/*7*/ new CasillaMatriz(AS[7], -1),/*8*/ new CasillaMatriz(AS[4], -1),
	    		 	/*9*/ new CasillaMatriz(AS[7], -1), /*10*/ new CasillaMatriz(AS[7], -1), /*11*/ new CasillaMatriz(AS[7], -1),
	    		 	/*12*/ new CasillaMatriz(AS[7], -1), /*13*/ new CasillaMatriz(AS[7], -1), /*14*/ new CasillaMatriz(AS[7], -1),
	    		 	/*15*/ new CasillaMatriz(AS[7], -1), /*16*/ new CasillaMatriz(AS[7], -1), /*17*/ new CasillaMatriz(AS[7], -1),
	    		 	/*16*/ new CasillaMatriz(AS[7], -1), /*19*/ new CasillaMatriz(AS[7], -1), /*20*/ new CasillaMatriz(AS[7], -1),
	    		 	/*21*/ new CasillaMatriz(AS[7], -1), /*22*/ new CasillaMatriz(AS[7], -1), /*23*/ new CasillaMatriz(AS[7], -1)};
	     addFila(filaMatriz, fila);
	     fila++;
	     
	   //fila 7
	     filaMatriz = new CasillaMatriz[]
	    		 	{/*0*/new CasillaMatriz(null, 7),/*1*/new CasillaMatriz(null, 7),/*2*/new CasillaMatriz(null, 7),
	    		 	/*3*/ new CasillaMatriz(null, 7),/*4*/new CasillaMatriz(null, 7),/*5*/new CasillaMatriz(null, 7),
	    		 	/*6*/new CasillaMatriz(null, 7),/*7*/ new CasillaMatriz(null, 7),/*8*/ new CasillaMatriz(null, 7),
	    		 	/*9*/ new CasillaMatriz(null, -1), /*10*/ new CasillaMatriz(null, 7), /*11*/ new CasillaMatriz(null, 7),
	    		 	/*12*/ new CasillaMatriz(null, 7), /*13*/ new CasillaMatriz(null, 7), /*14*/ new CasillaMatriz(null, 7),
	    		 	/*15*/ new CasillaMatriz(null, 7), /*16*/ new CasillaMatriz(null, 7), /*17*/ new CasillaMatriz(null, 7),
	    		 	/*16*/ new CasillaMatriz(null, 7), /*19*/ new CasillaMatriz(null, 7), /*20*/ new CasillaMatriz(null, 7),
	    		 	/*21*/ new CasillaMatriz(null, 0), /*22*/ new CasillaMatriz(null, 7), /*23*/ new CasillaMatriz(null, 7)};
	     addFila(filaMatriz, fila);
	     fila++;
	     
	   //fila 8
	     filaMatriz = new CasillaMatriz[]
	    		 {/*0*/new CasillaMatriz(AS[0], 8),/*1*/new CasillaMatriz(AS[0], 8),/*2*/new CasillaMatriz(AS[0], 8),
	    		 /*3*/ new CasillaMatriz(AS[0], 8),/*4*/new CasillaMatriz(AS[0], 8),/*5*/new CasillaMatriz(AS[0], 8),
	 	    	 /*6*/new CasillaMatriz(AS[0], 8),/*7*/ new CasillaMatriz(AS[0], 8),/*8*/ new CasillaMatriz(AS[0], 8),
	 	    	 /*9*/ new CasillaMatriz(AS[8], -1), /*10*/ new CasillaMatriz(AS[0], 8), /*11*/ new CasillaMatriz(AS[5], -1),
	 	    	 /*12*/ new CasillaMatriz(AS[0], 8), /*13*/ new CasillaMatriz(AS[0], 8), /*14*/ new CasillaMatriz(AS[0], 8),
	 	    	 /*15*/ new CasillaMatriz(AS[0], 8), /*16*/ new CasillaMatriz(AS[0], 8), /*17*/ new CasillaMatriz(AS[0], 8),
	 	    	 /*16*/ new CasillaMatriz(AS[0], 8), /*19*/ new CasillaMatriz(AS[0], 8), /*20*/ new CasillaMatriz(AS[0], 8),
	 	    	 /*21*/ new CasillaMatriz(null, 8), /*22*/ new CasillaMatriz(AS[0], 8), /*23*/ new CasillaMatriz(AS[0], 8)};
	     addFila(filaMatriz, fila);
	     fila++;
	     
	     
	     
	     
	     
	 }
	 
	 private void addFila(CasillaMatriz[] casilleros , int fila) {
		 if (fila <= cantEstados) {
			 for (int i = 0; i <= cantColumnas; i++) {
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
