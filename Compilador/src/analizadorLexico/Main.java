package analizadorLexico;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import analizadorSintactico.Parser;


public class Main {

	public static void main(String[] args) throws IOException {
		
	//Lectura del archivo a compilar
		File miDir = new File (".");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(miDir);
        
        fileChooser.showOpenDialog(fileChooser);
        String ruta = fileChooser.getSelectedFile().getAbsolutePath();
    	File file = new File(ruta);
    	
    	List<Error> errores = new ArrayList<Error>();
    	
    	Parser parser = new Parser(errores, file);
    	AnalizadorLexico aLex = new AnalizadorLexico(file, errores);
    
    	int res_sintactico = parser.parse();
    	if (res_sintactico == 0) {
    		System.out.println("Programa sintacticamente correcto.");
    	}


    //SOLO PARA PROBARLO AHORA. LUEGO, SE HACE EN yyparse()
    	/*
    	Token t;

    	for (int j=0; j<100; j++) { //repetir la cantidad de tokens que queramos
    		t = aLexico.getNextToken();
    	}
    	*/
    	
    //Genero archivo con los tokens y los errores
    	File archTokens = new File("Tokens.txt");
    	FileManager.write(parser.tokensToString() , archTokens);
    	
    	File archErrores = new File("Errores.txt");
    	FileManager.write(parser.erroresToString(), archErrores);
	
    	
   //Genero archivo con la tabla de simbolos	
    	File archTdeS = new File("TablaDeSimbolos.txt");
    	FileManager.write(parser.tDeStoString(), archTdeS);
    
    //Genero archivo con las estructuras	
    	File archEstruct = new File("EstructurasSintacticas.txt");
    	FileManager.write(parser.estructurasToString(), archEstruct);
    	
	}
	
}
	



