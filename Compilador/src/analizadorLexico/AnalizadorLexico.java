package analizadorLexico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




public class AnalizadorLexico {
	
	static final int ESTADO_FINAL = -1;
	public static final long MAX_LONG = 4294967295l;
	public static final long MAX_INT = 32768;
	
	private Matriz Mtransicion;
    public static HashMap <String, Token> tablaSimbolos;
    public static List<Error> errores;
    public static List<Token> tokens; //Lista de tokens para dsp pasarlos a un archivo
    private HashMap<String, Integer> equivalentes; //Equivalente entre caracter y columna
    public static HashMap<String, Integer> palabras_reservadas; //Los codigos de token
    
    static FileManager fm;
    public static int cantLineas;
    
    static final String TIPO_ID = "identificador";
    static final String TIPO_CTE_ENTERA = "constante entera";
    static final String TIPO_CTE_ULONG = "constante ulong";
    static final String TIPO_CADENA = "cadena";
    static final String TIPO_OPERADOR = "operador";
    static final String TIPO_COMPARADOR = "comparador";
    static final String TIPO_PALABRA_RESERVADA = "palabra reservada";

    
    public AnalizadorLexico(File file) throws FileNotFoundException {
    	
        Mtransicion = new Matriz();
        errores = new ArrayList<>();
        tokens= new ArrayList<>();
        fm = new FileManager(file);
        
        tablaSimbolos = new HashMap<>();
        equivalentes = new HashMap<>();
        llenarEquivalentes();
        
        palabras_reservadas = new HashMap<>();
        llenarPalabrasRes();
        
        cantLineas = 1; 
    }
    
    private void llenarPalabrasRes() {
    	
    	palabras_reservadas.put("id", 257);
    	palabras_reservadas.put("entero", 258);
    	palabras_reservadas.put("cadena", 259);
    	palabras_reservadas.put(":=", 260);
    	palabras_reservadas.put("<=", 261);
    	palabras_reservadas.put(">=", 262);
    	palabras_reservadas.put("==", 263);
    	palabras_reservadas.put("<>", 264);
    	
    	//Palabras reservadas
    	palabras_reservadas.put("if", 265);
    	palabras_reservadas.put("else", 266);
    	palabras_reservadas.put("end_if", 267);
    	palabras_reservadas.put("print", 268);
    	palabras_reservadas.put("int", 269);
    	palabras_reservadas.put("begin", 270);
    	palabras_reservadas.put("end", 271);
    	palabras_reservadas.put("foreach", 272);
    	palabras_reservadas.put("in", 273);
    	palabras_reservadas.put("ulong", 274);
    	
    }
    
    private void llenarEquivalentes() {
    	equivalentes.put("letra", 0);
    	equivalentes.put("digito", 1);
    	equivalentes.put("", 2);
    	equivalentes.put("", 3);
    	equivalentes.put("", 4);
    	equivalentes.put("", 5);
    	equivalentes.put("", 6);
    	equivalentes.put("", 7);
    	equivalentes.put("", 8);
    	equivalentes.put("", 9);
    	equivalentes.put("", 10);
    	equivalentes.put("", 11);
    	equivalentes.put("", 12);
    	equivalentes.put("", 13);
    	equivalentes.put("", 14);
    	equivalentes.put("", 15);
    	equivalentes.put("", 16);
    	equivalentes.put("", 17);
    	equivalentes.put("", 18);
    	equivalentes.put("", 19);
    	equivalentes.put("", 20);
    	equivalentes.put("", 21);
    	equivalentes.put("", 22);   	
    	
    }
    
    
    public Token getNextToken() throws IOException {
    	
    	 StringBuilder buffer = new StringBuilder(); //Donde voy guardando los chars que voy leyendo hasta formar un token 
    	 int estadoActual = 0;
         Token token = null;
         
         
         Character c = fm.readChar(); //Leo un caracter

         while (estadoActual != ESTADO_FINAL && c != null) { // y no sea fin de archivo
            
        	 int columna = this.getColumna(c); //HACER
             
             int estadoProx = Mtransicion.getEstado(estadoActual, columna);
             AccionSemantica as = Mtransicion.getAS(estadoActual, columna);
             
             if (as != null) //Ejecuto la AS
            	 token = as.ejecutar(buffer, c);
             
             
             if (( c == '\n') || (c == '\r')){ //Aumento la cantidad de lineas
            	 cantLineas++;
             }
             
             estadoActual = estadoProx;
             
             if (estadoActual != ESTADO_FINAL) 
                 c = fm.readChar();
        }
         
        if(token!=null) //si se formo un token
           AnalizadorLexico.tokens.add(token); 
         
        return token;
    }
    

    
	private int getColumna(Character c) {
		
		return 0;
	}

}
