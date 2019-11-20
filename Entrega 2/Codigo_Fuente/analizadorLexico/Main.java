package analizadorLexico;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import analizadorSintactico.Parser;
import analizadorSintactico.Traductor;


public class Main {

	public static void main(String[] args) throws IOException {
		
	//Lectura del archivo a compilar
		File miDir = new File (".");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(miDir);
        
        fileChooser.showOpenDialog(fileChooser);
        
        String ruta="";
        if (fileChooser.getSelectedFile() != null) {
        	ruta = fileChooser.getSelectedFile().getAbsolutePath();
        	File file = new File(ruta);

        	List<Error> errores = new ArrayList<Error>();
        	Parser parser = new Parser(errores, file);
        	parser.parse();

		    //Genero archivo con los tokens y los errores
		    	//File archTokens = new File("Tokens.txt");
		    	//FileManager.write(parser.tokensToString() , archTokens);
		    	
		    	File archErrores = new File("Errores.txt");
		    	FileManager.write(parser.erroresToString(), archErrores);
			
		    	
		   //Genero archivo con la tabla de simbolos	
		    	File archTdeS = new File("TablaDeSimbolos.txt");
		    	FileManager.write(parser.tDeStoString(), archTdeS);
		    
		    //Genero archivo con las estructuras	
		    	//File archEstruct = new File("EstructurasSintacticas.txt");
		    	//FileManager.write(parser.estructurasToString(), archEstruct);
		    	
		    //Genero archivo con el arbol sintactico	
		    	if (parser.raiz != null)
		    		parser.imprimirArbol(parser.raiz, "");
		    	File archArbol = new File("ArbolSintactico.txt");
		    	FileManager.write(parser.arbolString.toString(), archArbol);
		    	
		    //Archivo con assembler
		    
		    	if (errores.size() == 0) {
		        	Traductor traductor = new Traductor();
		        	System.out.println("Generando codigo assembler");
		        	String assembler = traductor.traducir(parser.raiz);
		        	File FileAssembler = new File("ProgramaCompilado.asm");
		        	FileManager.write(assembler, FileAssembler);
		        	System.out.println("Codigo assembler generado.");
		    	} else {
		    		System.out.println("Se encontraron errores durante la compilacion." + "\n" + "No se generara codigo assembler.");
		    	}
        }else 
        	System.out.println("No se ha seleccionado ningun archivo");
    	
	}
	
}
	



