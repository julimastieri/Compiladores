package analizadorLexico;

//Concatena y busca en las palabras_reservadas 
//Para ASIGNACION y comparadores

public class AccionSemantica5 implements AccionSemantica {

	public Token ejecutar(StringBuilder buffer, Character c) {
		
		buffer.append(c);
		String lexema = buffer.toString();
		String key = "";
	
		switch (lexema) {
        case ":=": key = "asign";
        break;
        
        case ">=": key = "mayorigual";
        break;
        
        case "<=": key = "menorigual";
        break;
      
        case "==": key = "igualigual";
        break;
        
        case "<>": key = "distinto";
        break;
		}
		
		int id = AnalizadorLexico.palabras_reservadas.get(key);
		Token token = new Token(lexema, AnalizadorLexico.TIPO_OPERADOR, id);
		
		return token;

	}

}
