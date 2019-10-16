package analizadorLexico;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PushbackReader;


public class FileManager {
	
	private PushbackReader file;
	
	public FileManager(File f) throws FileNotFoundException {
		file = new PushbackReader(new FileReader(f));
	}
	
    public Character readChar() throws IOException {
    	
    	int c = file.read();
    	if ( c != -1) //si no es fin de archivo
    		return (char) c;
    	else 
			return null;
    }
    
    public static void write(String datos, File f) throws IOException {
        f.delete();
        BufferedWriter writer = new BufferedWriter(new FileWriter(f, true));
        writer.write(datos);
        writer.close();
    }
    
    public void unread(Character c) throws IOException { //Devuelve caracter leido 
    	if (c != null) {
    		if ( (c == '\r') || (c == '\n') )
    			AnalizadorLexico.unreadNl = true;
    		char[] aux = {c};
            file.unread(aux);
    	}   	
    }

}
