package analizadorLexico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Token;


public class AnalizadorLexico {
	
	private static final int ESTADO_FINAL = -1;
	public static final long MAX_LONG = 4294967295l;
	public static final long MAX_INT = 32768;
	
	private Matriz Mtransicion;
    public static HashMap <String, Token> tablaSimbolos;
    public static List<Error> errores;
    public static List<Token> tokens; //Lista de tokens para dsp pasarlos a un archivo
    private HashMap<String, Integer> equivalentes;
    public static HashMap<String, Integer> id_tokens;
    
    private static FileManager fm;
    public static int cantLineas;
    private String ultCharLeido = "0"; //0 es invalido, 1 es valido
    
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
        
        AnalizadorLexico.tablaSimbolos = new HashMap<>();
        llenarTablaDeSimbolos();
        llenarEquivalentes();
        llenarIdTokens();
        
        cantLineas = 1; 
    }
    
    private void llenarIdTokens() {
    	
    	id_tokens.put("id", 257);
    	id_tokens.put("cte", 258);
    	id_tokens.put("cadena", 259);
    	id_tokens.put(":=", 260);
    	id_tokens.put("<=", 261);
    	id_tokens.put(">=", 262);
    	id_tokens.put("==", 263);
    	id_tokens.put("<>", 264);
    	id_tokens.put("if", 265);
    	id_tokens.put("else", 266);
    	id_tokens.put("end_if", 267);
    	id_tokens.put("print", 268);
    	id_tokens.put("int", 269);
    	id_tokens.put("begin", 270);
    	id_tokens.put("end", 271);
    	id_tokens.put("foreach", 272);
    	id_tokens.put("in", 273);
    	id_tokens.put("ulong", 274);
    	
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
    
    private void llenarTablaDeSimbolos() {

            //Asignacion
            Token token = new Token(":=",TIPO_ASIGNACION, ASIGNACION);
            tablaSimbolos.put(":=", token);
            
            //Comparadores
            token = new Token("<=",TIPO_COMPARADOR, MENOR_IGUAL);
            tablaSimbolos.put("<=", token);
            
            token = new Token(">=",TIPO_COMPARADOR, MAYOR_IGUAL);
            tablaSimbolos.put(">=", token);
            
            token = new Token("==",TIPO_COMPARADOR, IGUAL_IGUAL);
            tablaSimbolos.put("==", token);
            
            token = new Token("<>",TIPO_COMPARADOR, DISTINTO);
            tablaSimbolos.put("<>", token);
        
            //Palabras reservadas
            token = new Token("if",TIPO_PALABRA_RESERVADA, IF);
            tablaSimbolos.put("if", token);
            
            token = new Token("else",TIPO_PALABRA_RESERVADA, ELSE);
            tablaSimbolos.put("else", token);
            
            token = new Token("end_if",TIPO_PALABRA_RESERVADA, END_IF);
            tablaSimbolos.put("end_if", token);
            
            token = new Token("print",TIPO_PALABRA_RESERVADA, PRINT);
            tablaSimbolos.put("print", token);
            
            token = new Token("int",TIPO_PALABRA_RESERVADA, INT);
            tablaSimbolos.put("int", token);
            
            token = new Token("begin",TIPO_PALABRA_RESERVADA, BEGIN);
            tablaSimbolos.put("begin", token);
    	
            token = new Token("end",TIPO_PALABRA_RESERVADA, END);
            tablaSimbolos.put("end", token);
            
            token = new Token("foreach",TIPO_PALABRA_RESERVADA, FOREACH);
            tablaSimbolos.put("foreach", token);
            
            token = new Token("in",TIPO_PALABRA_RESERVADA, IN);
            tablaSimbolos.put("in", token);
            
            token = new Token("ulong",TIPO_PALABRA_RESERVADA, ULONG);
            tablaSimbolos.put("ulong", token);
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
