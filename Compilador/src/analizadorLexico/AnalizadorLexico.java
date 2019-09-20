package analizadorLexico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AnalizadorLexico {
	
	private static final int ESTADO_FINAL = -1;
	
	private Matriz Mtransicion;
    public static HashMap <String, Token> tablaSimbolos;
    public static List<Error> errores;
    public static List<Token> tokens; //Lista de tokens para dsp pasarlos a un archivo
    
    private static FileManager fm;
    public static int cantLineas;
    private String ultCharLeido = "0"; //0 es invalido, 1 es valido
    
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
         //boolean estadoFantasma = false;
         Token token = null;
         //Token tokenAnterior = new Token("", 0, "");
         Character c;
         
         if ( ultCharLeido.charAt(0) == '0')  //Si el ultimo caracter fue invalido
             c = fm.readChar();
         else //Si el ultimo era valido 
             c = ultCharLeido.substring(1).charAt(0); //Lo recupero
         
         
         while (estadoActual != ESTADO_FINAL && c != null) { // y no sea fin de archivo
            
        	 int columna = this.getColumna(c);
             
             int estadoProx = Mtransicion.getEstado(estadoActual, columna);
             AccionSemantica as = Mtransicion.getAS(estadoActual, columna);
             token = as.ejecutar(buffer, c);
             
             //aumentarCantSaltosLinea(c, verificaRepetidosEnDiferentesIteraciones(buffer, tokenAnterior));
             
             //estadoFantasma = verificaEstadoFantasma(estadoActual, estadoProx, c); //si paso por el estado fantasma no tiene que decrementar el cursor (son para los estados que tienen un solo caracter)
             estadoActual = estadoProx;
             
             if (estadoActual != ESTADO_FINAL ) { // || estadoFantasma
                 c = fm.readChar();
                 ultCharLeido = "1" + c;
             }
         }
         
         if(token!=null) //si se formo un token
         	AnalizadorLexico.tokens.add(token); 
         
         return token;
    }

    /* VER PARA QUE ES EL "verificaRepetidosEnDiferentesIteraciones"
    
    private boolean verificaRepetidosEnDiferentesIteraciones(StringBuilder buffer, Token tokenAnterior) {
        if (tokenAnterior == null)
            return false;
        if (tokenAnterior.lexema.equals(buffer.toString())) {
            tokenAnterior.lexema = buffer.toString();// ?????
            return false;
        }
        return true;
    }
    
    

    private void aumentarCantSaltosLinea(char c, boolean repetidos) {
        if ((c == '\n' || c=='\r')&& !repetidos) {
            cantLineas ++;
        }
    }
    
    
    private boolean verificaEstadoFantasma(int ea, int ef, char c) {
        if (ef == -1) {
            if (ea == 18 && c == 39) //diferente de comilla simple
                return true;
            if (ea == 8 && c == 'l')
                return true;
            if (ea == 0)
                return true;
            if (ea == 12 && c == '=')
                return true;
            if (ea == 13 && c == '=')
                return true;
            if (ea == 14 && c == '=')
                return true;
            if (ea == 15 && c == '=')
                return true;
            if (ea == 17 && c == '=')
                return true;
        }
        return false;
    }
    
    */
    
	private int getColumna(Character c) {
		
		// TODO Auto-generated method stub
		return 0;
	}

}
