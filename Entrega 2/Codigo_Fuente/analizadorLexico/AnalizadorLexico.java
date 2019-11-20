package analizadorLexico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    static boolean unreadNl; //Para saber si deslei un nl o no
    
    public static final String TIPO_ID = "identificador";
    public static final String TIPO_CTE = "constante";
    static final String TIPO_CADENA = "cadena";
    static final String TIPO_OPERADOR = "";
    static final String TIPO_COMPARADOR = "comparador";
    static final String TIPO_PALABRA_RESERVADA = "palabra reservada";
    
    public static final String TIPO_DATO_ENTERO = "int";
    public static final String TIPO_DATO_ULONG = "ulong";

    
    public AnalizadorLexico(File file, List<Error> listae) throws FileNotFoundException {
    	
        Mtransicion = new Matriz();
        errores = listae;
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
    	palabras_reservadas.put("cte", 258);
    	palabras_reservadas.put("cadena", 259);
    	palabras_reservadas.put("asign", 260);
    	palabras_reservadas.put("mayorigual", 261);
    	palabras_reservadas.put("menorigual", 262);
    	palabras_reservadas.put("igualigual", 263);
    	palabras_reservadas.put("distinto", 264);
    	
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
    	palabras_reservadas.put("to_ulong", 275);
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

         while (estadoActual != ESTADO_FINAL) {
            
        	 int columna = this.getColumna(c); 
             
             Integer estadoProx = Mtransicion.getEstado(estadoActual, columna);
             AccionSemantica as = Mtransicion.getAS(estadoActual, columna);
             
             if (as != null) //Ejecuto la AS
            	 token = as.ejecutar(buffer, c);
             
             if ((c != null) && (c == '\n') ){ //si no es fin de archivo y es un nl
            	 
            	 if (unreadNl == false){ //No deslei nl
                	 cantLineas++;
                	 
                 }else { //si deslei un nl
					unreadNl = false; 
				}
             }
          
             estadoActual = estadoProx;

             if (estadoActual != ESTADO_FINAL) //Si no llegue al EF
                 c = fm.readChar(); //leo el siguiente

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
			out.append(token.getTipoDeToken());
			out.append(" ");
			out.append(token.getLexema());
			out.append("\n");	
		}
		
		return out.toString();
	}
	
	
	
	
	public String tdeStoString() {
		
		StringBuilder out = new StringBuilder();
		
		for (Map.Entry entrada : tablaSimbolos.entrySet()) { 
            String lexema = (String) entrada.getKey();
            Token token = (Token) entrada.getValue();
  
            out.append("Lexema: ");
			out.append(lexema);
			out.append(" , Tipo de token: ");
			out.append(token.getTipoDeToken());
			out.append(" , Tipo de dato: ");
			out.append(token.getTipoDeDato());
			out.append(" , cantidad de de veces que es referenciado: ");
			out.append(token.getContadorDeReferencias());
			out.append(" , Uso: ");
			out.append(token.getUso());
			out.append(" , Tamanio: ");
			out.append(token.getTamanio());
			
			int tamanio = token.getCantidadValoresIniciales();
			if (tamanio > 0) {
				out.append(" , Valores Iniciales: ");
				for (int i=0; i < tamanio-1;i++) { 
					out.append(token.getValorInicial(i) + ", ");				
				}
				out.append(token.getValorInicial(tamanio-1) + " ");	
			}
			
			out.append("\n");
        } 
		return out.toString();
	}
	
	
	

	


}
