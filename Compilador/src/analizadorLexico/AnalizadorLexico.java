package analizadorLexico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AnalizadorLexico {
	
	private static final int ESTADO_FINAL = -1;
	public static final long MAX_LONG = 4294967295l;
	public static final long MAX_INT = 32768;
	
	private Matriz Mtransicion;
    public static HashMap <String, Token> tablaSimbolos;
    public static List<Error> errores;
    public static List<Token> tokens; //Lista de tokens para dsp pasarlos a un archivo
    
    private static FileManager fm;
    public static int cantLineas;
    private String ultCharLeido = "0"; //0 es invalido, 1 es valido
    
    static final String TIPO_ID = "identificador";
    static final String TIPO_CTE_ENTERA = "constante entera";
    static final String TIPO_CTE_ULONG = "constante ulong";
    static final String TIPO_CADENA = "cadena";
    static final String TIPO_OPERADOR = "operador";
    
    static final int ID = 257;
    static final int CTE = 258;
    static final int CADENA = 259;
    static final int ASIGNACION = 260;
    
    
    public AnalizadorLexico(File file) throws FileNotFoundException {
    	
        Mtransicion = new Matriz();
        errores = new ArrayList<>();
        tokens= new ArrayList<>();
        fm = new FileManager(file);
        
        AnalizadorLexico.tablaSimbolos = new HashMap<>();
        llenarTablaDeSimbolos();
        
        cantLineas = 1; 
    }
    
    private void llenarTablaDeSimbolos() {
    	
    }
    
    public Token getNextToken() throws IOException {
    	
    	 StringBuilder buffer = new StringBuilder(); //Donde voy guardando los chars que voy leyendo hasta formar un token 
    	 int estadoActual = 0;
         Token token = null;
         Character c;
         
         if ( ultCharLeido.charAt(0) == '0')  //Si el ultimo caracter fue invalido
             c = fm.readChar();
         else //Si el ultimo era valido 
             c = ultCharLeido.substring(1).charAt(0); //Lo recupero
         
         
         while (estadoActual != ESTADO_FINAL && c != null) { // y no sea fin de archivo
            
        	 int columna = this.getColumna(c); //HACER
             
             int estadoProx = Mtransicion.getEstado(estadoActual, columna);
             AccionSemantica as = Mtransicion.getAS(estadoActual, columna);
             
             if (as != null)
            	 token = as.ejecutar(buffer, c);
             
             
             if (( c == '\n') || (c == '\r')){
            	 cantLineas++;
             }
             
             estadoActual = estadoProx;
             
             if (estadoActual != ESTADO_FINAL ) { 
                 c = fm.readChar();
                 ultCharLeido = "1" + c;
             }else if (){ //tengo q reutilizar el ultimo? 
                 ultCharLeido = "0";
			}
             
         }
         
         if(token!=null) //si se formo un token
         	AnalizadorLexico.tokens.add(token); 
         
         return token;
    }
    

    
	private int getColumna(Character c) {
		
		return 0;
	}

}
