package analizadorLexico;

import java.io.File;
import java.io.IOException;

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
    	
    //Creo mi analizador lexico
    	AnalizadorLexico aLexico = new AnalizadorLexico(file);
    	
    	Parser parser = new Parser(false);
        parser.errores = aLexico.
        Parser.estructuras = new ArrayList<>();
        parser.analizador_lex = aLexico;
        int resultado = p.parsepublico();


        if (resultado == 0)
            System.out.print("ACCEPT, se reconocio la gramatica");
        else
            System.out.print("No se reconocio la gramatica");


    //SOLO PARA PROBARLO AHORA. LUEGO, SE HACE EN yyparse()
    	/*
    	Token t;

    	for (int j=0; j<100; j++) { //repetir la cantidad de tokens que queramos
    		t = aLexico.getNextToken();
    	}
    	*/
    	
    //Genero archivo con los tokens y los errores
    	File archTokens = new File("Tokens.txt");
    	FileManager.write(aLexico.tokensToString() , archTokens);
    	
    	File archErrores = new File("Errores.txt");
    	FileManager.write(aLexico.erroresToString(), archErrores);
	
    	
   //Genero archivo con la tabla de simbolos	
    	File archTdeS = new File("TablaDeSimbolos.txt");
    	FileManager.write(aLexico.tdeStoString(), archTdeS);
	}
	
}
	



