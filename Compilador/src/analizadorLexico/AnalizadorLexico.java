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
    static boolean unreadNl;
    
    static final String TIPO_ID = "identificador";
    static final String TIPO_CTE_ENTERA = "constante entera";
    static final String TIPO_CTE_ULONG = "constante ulong";
    static final String TIPO_CADENA = "cadena";
    static final String TIPO_OPERADOR = "";
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
        unreadNl = false;
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
    	equivalentes.put("_", 2);
    	equivalentes.put("+", 3);
    	equivalentes.put("-", 4);
    	equivalentes.put("*", 5);
    	equivalentes.put("/", 6);
    	equivalentes.put(":", 7);
    	equivalentes.put("=", 8);
    	equivalentes.put("$", 9);
    	equivalentes.put("{", 10);
    	equivalentes.put("}", 11);
    	equivalentes.put("<", 12);
    	equivalentes.put(">", 13);
    	equivalentes.put("(", 14);
    	equivalentes.put(")", 15);
    	equivalentes.put(",", 16);
    	equivalentes.put(";", 17);
    	equivalentes.put("[", 18);
    	equivalentes.put("]", 19);
    	equivalentes.put("blanco/tab", 20);
    	equivalentes.put("nl", 21);
    	equivalentes.put("#", 22);  
    	
    }
    
    
    public Token getNextToken() throws IOException {
    	
    	 StringBuilder buffer = new StringBuilder(); //Donde voy guardando los chars que voy leyendo hasta formar un token 
    	 int estadoActual = 0;
         Token token = null;
         
         
         Character c = fm.readChar(); //Leo un caracter

         while (estadoActual != ESTADO_FINAL) { //&& c != null) { // y no sea fin de archivo
            
        	 int columna = this.getColumna(c); 
             
             Integer estadoProx = Mtransicion.getEstado(estadoActual, columna);
             AccionSemantica as = Mtransicion.getAS(estadoActual, columna);
             
             if (as != null) //Ejecuto la AS
            	 token = as.ejecutar(buffer, c);
             
             if ((c != null) && (c == '\n') ){
            	 if (unreadNl == false){ //No deslei nl
                	 cantLineas++;
                 }else { //si deslei un ln
					unreadNl = false; 
				}
             }
             
             if (estadoProx != -2)//hay estado
            	 estadoActual = estadoProx;
             else {
            	 estadoActual = 0; //Descarto el token
            	 Error error = new Error("ERROR", "Caracter invalido", cantLineas);
            	 errores.add(error);
             }
             

             if (estadoActual != ESTADO_FINAL) 
                 c = fm.readChar();

        }
         
        if(token!=null) //si se formo un token
           AnalizadorLexico.tokens.add(token); 
         
        return token;
    }
    

    
	private int getColumna(Character c) {
		
		if (c == null) //fin de archivo
	        return equivalentes.get("$");
        
        if ((c == 32)||(c == 9)) //espacio
            return equivalentes.get("blanco/tab");
        
        if ((c == 10 )|| (c == 13))  // /n o /r
            return equivalentes.get("nl");
        
        if ((c >= 48) && (c <= 57))  // digito
            return equivalentes.get("digito");
                                                               
        if (((c >= 65) && (c <= 90)) || ((c >= 97) && (c <= 122)))
            return equivalentes.get("letra");
        
     

        String caracter = "" + c;
        Integer indice = equivalentes.get(caracter);
        
        if (indice == null)
            return 23;//Otro
        
        return indice;
	}
	
	public String tokensToString() {
		
		StringBuilder out = new StringBuilder();
		Token token;
		
		for (int i=0 ; i<tokens.size(); i++) {
			token = tokens.get(i);
			out.append(token.getTipo());
			out.append(" ");
			out.append(token.getLexema());
			out.append("\r\n");	
		}
		
		return out.toString();
	}
	
	
	
	public String erroresToString() {
		
		StringBuilder out = new StringBuilder();
		Error error;
		
		for (int i=0 ; i<errores.size(); i++) {
			error = errores.get(i);
			out.append(error.toString());
		}
		
		return out.toString();
	}
	

	


}
