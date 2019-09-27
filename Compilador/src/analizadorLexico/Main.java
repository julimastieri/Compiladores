package analizadorLexico;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;


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

    //SOLO PARA PROBARLO AHORA. LUEGO, SE HACE EN yyparse()
    	Token t;

    	for (int j=0; j<10; j++) { //repetir la cantidad de tokens que queramos
    		//preguntar si token es null -> error
    		t = aLexico.getNextToken();
    		
    	}
    	
    	
    //Genero archivo con los tokens y los errores
    	File archTokens = new File("Tokens.txt");
    	FileManager.write(aLexico.tokensToString() , archTokens);
    	
    	File archErrores = new File("Errores.txt");
    	FileManager.write(aLexico.erroresToString(), archErrores);
	
	}
	
}
	



