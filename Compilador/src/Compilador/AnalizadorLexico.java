package Compilador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFileChooser;

public class AnalizadorLexico {
	
	BufferedReader br;
	HashMap<String, Integer> tablaTokens;
	int estadoActual;
	CasillaMatriz[][] matrizTransicion = new CasillaMatriz[][]():
	
	public AnalizadorLexico () {
		tablaTokens = new HashMap<String, Integer>();
		construirTablaTokens();
		
	}
	
	private void construirTablaTokens() {
		tablaTokens.put("ID", 27);
		tablaTokens.put("CTE", 28);
		tablaTokens.put("IF", value);
		tablaTokens.put("THEN", value);
		tablaTokens.put("ELSE", value);
		tablaTokens.put("+", value);
		tablaTokens.put("ID", value);
		tablaTokens.put("ID", value);
		tablaTokens.put("ID", value);
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

		char caracter = (char)br.read();
		
		return 1;
		
	}

}
