package Compilador;

import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {

	public static void main(String[] args) throws IOException {
		
		AnalizadorLexico aLexico = new AnalizadorLexico();
		
		
		aLexico.leerArchivo();
		
		int token = aLexico.getNextToken();
		
		//while (token != -1) {
			//imprimir el token
			//token = aLexico.getNextToken();
		//}

	}
	
	

}
