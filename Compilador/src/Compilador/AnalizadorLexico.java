package Compilador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JFileChooser;

public class AnalizadorLexico {
	
	static final int FINAL = 10;
	
	BufferedReader br;
	HashMap<String, Integer> tablaTokens;
	int estadoActual = 0;
	CasillaMatriz[][] matrizTransicion = new CasillaMatriz[9][24];
	AccionSemantica accionesSemanticas = new AccionSemantica();
	
	public AnalizadorLexico () {
		tablaTokens = new HashMap<String, Integer>();
		construirTablaTokens();
		llenarMatriz();
	}
	
	private void llenarMatriz() {
		
		int[] arr = {-1};
		CasillaMatriz estado = new CasillaMatriz(FINAL, arr);
		matrizTransicion[0][3] = estado;
		matrizTransicion[0][4] = estado;
		matrizTransicion[0][5] = estado;
		matrizTransicion[0][6] = estado;
		matrizTransicion[0][9] = estado;
		matrizTransicion[0][14] = estado;
		matrizTransicion[0][15] = estado;
		matrizTransicion[0][16] = estado;
		matrizTransicion[0][17] = estado;
		matrizTransicion[0][18] = estado;
		matrizTransicion[0][19] = estado;
		matrizTransicion[3][8] = estado;
		matrizTransicion[8][11] = estado;
		matrizTransicion[7][9] = estado;
		matrizTransicion[6][8] = estado;
		for (int i=4; i<=5; i++) {
			for (int j=0; j<24; j++) {
				matrizTransicion[i][j] = estado;
			}
		}
		
		arr[0] = 3;
		estado = new CasillaMatriz(1, arr);
		matrizTransicion[0][0] = estado;
		
		estado = new CasillaMatriz(2, arr);
		matrizTransicion[0][1] = estado ;
		
		arr[0] = 6;
		estado = new CasillaMatriz(FINAL, arr);
		matrizTransicion[0][2] = estado;
		matrizTransicion[0][11] = estado;
		matrizTransicion[0][23] = estado;
		matrizTransicion[8][9] = estado;
		for (int j=0; j<24; j++) {
			if (j != 8) {
				matrizTransicion[3][j] = estado;
			}
		}
		for (int j=0; j<24; j++) {
			if (j != 8) {
				matrizTransicion[6][j] = estado;
			}
		}
		
		arr[0] = -1;
		estado = new CasillaMatriz(8, arr);
		matrizTransicion[0][10] = estado;
		for (int j=0; j<24; j++) {
			if ((j != 9) && (j != 11)) {
				matrizTransicion[8][j] = estado;
			}
		}
		
		estado = new CasillaMatriz(4, arr);
		matrizTransicion[0][12] = estado;
		
		estado = new CasillaMatriz(4, arr);
		matrizTransicion[0][13] = estado;

		estado = new CasillaMatriz(0, arr);
		matrizTransicion[0][20] = estado;
		matrizTransicion[0][21] = estado;
		matrizTransicion[7][21] = estado;
		
		estado = new CasillaMatriz(7, arr);
		matrizTransicion[0][22] = estado;
		for (int j=0; j<24; j++) {
			if ((j != 9) && (j != 21)) {
				matrizTransicion[7][j] = estado;
			}
		}
		
		arr[0] = 4;
		estado = new CasillaMatriz(1, arr);
		matrizTransicion[1][0] = estado;
		matrizTransicion[1][1] = estado;
		matrizTransicion[1][2] = estado;
		
		estado = new CasillaMatriz(2, arr);
		matrizTransicion[2][1] = estado;

		arr[0] = 5;
		estado = new CasillaMatriz(FINAL, arr);
		for (int j=0; j<24; j++) {
			if (j != 1) {
				matrizTransicion[2][j] = estado;
			}
		}
		
		arr = new int [2];
		arr[0] = 1; arr[1] = 2;
		estado = new CasillaMatriz(FINAL, arr);
		for (int j=3; j<24; j++) {
			matrizTransicion[1][j] = estado;	
		}
		
	}
	
	private void construirTablaTokens() {
		tablaTokens.put("ID", 27);
		tablaTokens.put("CTE", 28);
		tablaTokens.put("IF", 59);
		tablaTokens.put("THEN", 60);
		tablaTokens.put("ELSE", 61);
		tablaTokens.put("ulong", 62);
		tablaTokens.put("+", 70);
		tablaTokens.put("-", 71);
		tablaTokens.put("*", 72);
		tablaTokens.put("/", 73);
		tablaTokens.put("_", 74);
		tablaTokens.put(":=", 75);
		tablaTokens.put("==", 76);
		tablaTokens.put(">", 77);
		tablaTokens.put("<", 78);
		tablaTokens.put(">=", 79);
		tablaTokens.put("<=", 80);
		tablaTokens.put("<>", 81);
		tablaTokens.put("{", 82);
		tablaTokens.put("}", 83);
		tablaTokens.put("(", 84);
		tablaTokens.put(")", 85);
		tablaTokens.put(",", 86);
		tablaTokens.put(";", 87);
		tablaTokens.put("[", 88);
		tablaTokens.put("]", 89);
		tablaTokens.put("#", 90);
		tablaTokens.put("nl", 91);
		tablaTokens.put("bl-tab", 92);
		tablaTokens.put("$", 93);
	}
	
	public void leerArchivo() throws FileNotFoundException {
		
		File miDir = new File (".");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(miDir);
        
        fileChooser.showOpenDialog(fileChooser);
        String ruta = fileChooser.getSelectedFile().getAbsolutePath();
    	File file = new File(ruta); 
    	FileReader fr = new FileReader (file);
    	br = new BufferedReader(fr);

	}
	
	
	public int getNextToken() throws IOException {
		char caracter;
		
		while (estadoActual != FINAL) {
			caracter = (char)br.read();
			matrizTransicion[]
			
		}
		return 1;
		
	}

}
