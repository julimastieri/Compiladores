package analizadorLexico;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
	
	private static final int ASCIInl = 10;
	boolean nl, salto;
	private FileReader file;
	
	
	public FileManager(File f) throws FileNotFoundException {
		
		file = new FileReader(f);
		nl = false;
		salto = false; // ver
	}
	
    public Character readChar() throws IOException {
    	
    	if ( nl == true ) {
            nl = false;
            return ' ';
        }
    	
        int ch = file.read(); //retorna -1 si es fin de archivo
        
        if (ch != -1){  //Si no es fin de archivo
        	
        	if(ch == ASCIInl) //Si es un salto de linea
        		nl = true;
            
        	return (char) ch;
            
        }else{ //si es fin de archivo
        	
        	if (!salto){
        		salto = true;
        		return new Character('\n');
        	}
        	else 
        		return null;
        }
    }
    
    public static void write(String datos, File f) throws IOException {
        f.delete();
        datos = datos.replace("[", ""); //debe ser porque el toString debe poner los datos entre []
        datos = datos.replace("]", "");
        BufferedWriter writer = new BufferedWriter(new FileWriter(f, true));
        writer.write(datos);
        writer.close();
    }

}
