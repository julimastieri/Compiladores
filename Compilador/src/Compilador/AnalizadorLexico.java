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
	CasillaMatriz[][] matrizTransicion = new CasillaMatriz[][]();
	
	public AnalizadorLexico () {
		tablaTokens = new HashMap<String, Integer>();
		construirTablaTokens();
		
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
			
		}

		
		
		return 1;
		
	}

}
