package Compilador;

import java.io.FileNotFoundException;


public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		AnalizadorLexico aLexico = new AnalizadorLexico();
		
		aLexico.leerArchivo();
		aLexico.getNextToken();

	}
	
	

}
