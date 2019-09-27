package analizadorLexico;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PushbackReader;

public class FileManager {
	
	private static final int ASCIInl = 10; //codigo ASCII de salto de linea
	boolean nl, salto;
	private PushbackReader file;
	
	
	public FileManager(File f) throws FileNotFoundException {
		
		file = new PushbackReader(new FileReader(f));
		nl = false;
		salto = false; // ver
	}
	
    public Character readChar() throws IOException {
    	
    	int c = file.read();
    	if ( c != -1) { //si no es fin de archivo
    		return (char) c;
    	}else {
			return null;
		}
    	
    	
    	/*if ( nl == true ) {
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
        */
    }
    
    public static void write(String datos, File f) throws IOException {
        f.delete();
       // datos = datos.replace("[", ""); 
       // datos = datos.replace("]", "");
        BufferedWriter writer = new BufferedWriter(new FileWriter(f, true));
        writer.write(datos);
        writer.close();
    }
    
    public void unread(char c) throws IOException { //Devuelve caracter leido 
    	
    	if ( c == '\n') {
    		AnalizadorLexico.unreadNl = true;
    	}
    	char[] aux = {c};
    	file.unread(aux);
    }

}
