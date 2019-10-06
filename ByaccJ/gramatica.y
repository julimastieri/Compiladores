%{
package analizadorSintactico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.Token;

%}

%token ID CTE CADENA ASIGN MAYORIGUAL MENORIGUAL IGUALIGUAL DISTINTO IF ELSE END_IF PRINT INT BEGIN END FOREACH IN ULONG TO_ULONG

%right '+' '-'
%right '*' '/'

%start programa


%%
programa :sentencias_declarativas sentencias_ejecutables { System.out.println("Reconoce bien el programa"); }
;

sentencias_declarativas :tipo lista_de_variables';'
						|lista_de_variables';' { errores.add(new analizadorLexico.Error("ERROR", "Declaracion incorrecta. Se esperaba el tipo de la variable ", AnalizadorLexico.cantLineas)); }
						|tipo lista_de_variables { errores.add(new analizadorLexico.Error("ERROR", "Declaracion incorrecta. Se esperaba ';' ", AnalizadorLexico.cantLineas)); }
;

tipo :INT
	 |ULONG
	 |error';' { errores.add(new analizadorLexico.Error("ERROR", "Tipo inexistente ", AnalizadorLexico.cantLineas)); }
;

lista_de_variables :ID { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion de variable" + "\n"); }
				   |ID '[' lista_de_valores_iniciales ']' { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion de coleccion" + "\n"); }
				   |ID '[' CTE ']' { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion de coleccion" + "\n"); }
				   |ID ',' lista_de_variables

				   |ID     lista_de_valores_iniciales ']' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '[' para definir los valores iniciales de la coleccion ", AnalizadorLexico.cantLineas)); }
				   |ID '[' lista_de_valores_iniciales     { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ']' para definir los valores iniciales de la coleccion ", AnalizadorLexico.cantLineas)); }
;				   

lista_de_valores_iniciales :CTE ',' elem_lista 
						   |'_' ',' elem_lista 
						   |CTE ',' lista_de_valores_iniciales 
						   |'_' ',' lista_de_valores_iniciales
						   //| error ';' { errores.add(new analizadorLexico.Error("ERROR", "Formato de valores iniciales incorrectos ", AnalizadorLexico.cantLineas)); }
;

elem_lista :CTE
		   |'_'
;





sentencias_ejecutables :BEGIN lista_de_sentencias END
					   |lista_de_sentencias END { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'BEGIN' al comienzo ", AnalizadorLexico.cantLineas)); }
					   |BEGIN lista_de_sentencias     { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'END' al final ", AnalizadorLexico.cantLineas)); }
					   //| error ';' { Parser.errores.add(new Error("ERROR", "Sentencia ejecutable incorrecta ", AnalizadorLexico.cantLineas)); }
;

lista_de_sentencias :sentencia_ejecutable 
					|sentencia_ejecutable lista_de_sentencias
;

bloque_de_sentencias :sentencia_ejecutable
					 |BEGIN lista_de_sentencias END

					 |lista_de_sentencias END { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'BEGIN' al comienzo ", AnalizadorLexico.cantLineas));  }
					 |BEGIN lista_de_sentencias     { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'END' al final ", AnalizadorLexico.cantLineas));  }
;

sentencia_ejecutable :sentencia_if { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia if " + "\n");}
					 |sentencia_foreach { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia foreach " + "\n");}
					 |sentencia_print { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia print " + "\n");}
					 |asignacion { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Asignacion " + "\n");}
					 |error';' { Parser.errores.add(new analizadorLexico.Error("ERROR", "Sentencia ejecutable invalida ", AnalizadorLexico.cantLineas)); }
;					 

sentencia_if :IF'('condicion')'bloque_de_sentencias END_IF
			 |IF'('condicion')'bloque_de_sentencias ELSE bloque_de_sentencias END_IF

			 //| error ';' { Parser.errores.add(new Error("ERROR", "Error sintactico en sentencia if ", AnalizadorLexico.cantLineas)); }

			 //| IF '(' condicion ')' bloque_de_sentencias declaracion
			 //| IF '(' condicion ')' bloque_de_sentencias { Parser.errores.add(new Error("ERROR", "Se esperaba 'end_if' al final.", AnalizadorLexico.cantLineas));  }
			 //| IF '(' condicion ')' bloque_de_sentencias ELSE bloque_de_sentencias { Parser.errores.add(new Error("ERROR", "Se esperaba 'end_if' al final.", AnalizadorLexico.cantLineas));  }
			 //| IF     condicion ')' bloque_de_sentencias { Parser.errores.add(new Error("ERROR", "Se esperaba '(' antes de la condicion ", AnalizadorLexico.cantLineas));  }
			 //| IF '(' condicion     bloque_de_sentencias { Parser.errores.add(new Error("ERROR", "Se esperaba ')' luego de la condicion ", AnalizadorLexico.cantLineas));  }
			 //| IF     condicion     bloque_de_sentencias { Parser.errores.add(new Error("ERROR", "Se esperaban '(' y ')' para definir la condicion ", AnalizadorLexico.cantLineas));  }
			 //| IF '('           ')' bloque_de_sentencias { Parser.errores.add(new Error("ERROR", "Se esperaba una condicion ", AnalizadorLexico.cantLineas));  }
			
;

condicion :expresion comparador expresion { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Condicion " + "\n"); }

		  |comparador expresion { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion del lado derecho para comparar ", AnalizadorLexico.cantLineas)); }
		  |expresion comparador { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion del lado izquierdo para comparar ", AnalizadorLexico.cantLineas)); }
;		  

comparador :MAYORIGUAL  
		   |MENORIGUAL 
		   |IGUALIGUAL 
		   |DISTINTO
		   |'<'
		   |'>'
		   |error';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba un comparador valido ", AnalizadorLexico.cantLineas)); }
;		   

sentencia_foreach :FOREACH ID IN ID bloque_de_sentencias';'

				  |FOREACH IN ID bloque_de_sentencias';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba el nombre de la variable para iterar ", AnalizadorLexico.cantLineas)); } 
				  |FOREACH ID ID bloque_de_sentencias';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'in' y se encontró el nombre de la coleccion ", AnalizadorLexico.cantLineas));  } 
				  |FOREACH ID IN bloque_de_sentencias';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba el nombre de la coleccion y se encontraron sentencias ", AnalizadorLexico.cantLineas));  } 
				  //| FOREACH ID IN ID bloque_de_sentencias     { Parser.errores.add(new Error("ERROR", "Se esperaba ';' al final ", AnalizadorLexico.cantLineas));  } 
;

sentencia_print :PRINT'('CADENA')'';'

				//| PRINT '(' CADENA ')' { Parser.errores.add(new Error("ERROR", "Se esperaba ';' al final ", AnalizadorLexico.cantLineas)); } 
				|PRINT CADENA')'';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '(' y se encontro una cadena ", AnalizadorLexico.cantLineas));  } 
				|PRINT'('CADENA';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ')' y se encontro ';' ", AnalizadorLexico.cantLineas));  } 
;

expresion :termino'+'expresion { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Suma " + "\n"); }
		  |termino'-'expresion { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Resta " + "\n"); }
		  |termino
;		  

termino :factor'*'termino { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Multiplicacion " + "\n"); }
		|factor'/'termino { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Division " + "\n"); }
		|factor
;		

factor :ID { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor ID " + "\n"); }
	   |CTE { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE " + "\n"); }
	   |'-'CTE {  estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE negativa " + "\n");
	   			  modificarContadorDeReferencias($2.sval);
	   					 }

	   |ID'['subindice']' { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor coleccion " + "\n"); }
	   |TO_ULONG'('expresion')' { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor conversion " + "\n"); }

	   |TO_ULONG expresion')' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '(' para realizar la conversion ", AnalizadorLexico.cantLineas)); }
	   // | TO_ULONG '(' expresion { Parser.errores.add(new Error("ERROR", "Se esperaba ')' para realizar la conversion ", AnalizadorLexico.cantLineas)); }
;	   

subindice :ID 
		  |CTE

asignacion :ID ASIGN expresion';' 
		   |ID'['subindice']'ASIGN expresion';'
			
		   //| ID ASIGN expresion  { Parser.errores.add(new Error("ERROR", "Se esperaba ';' al final ", AnalizadorLexico.cantLineas));  } 
		   //|    ASIGN expresion ';' { Parser.errores.add(new Error("ERROR", "Se esperaba el nombre de la variable para realizar la asignacion ", AnalizadorLexico.cantLineas));  } 
		   |ID ASIGN';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion para realizar la asignacion ", AnalizadorLexico.cantLineas));  }
		   |ID subindice']'ASIGN expresion';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '[' para indicar la posicion de la coleccion ", AnalizadorLexico.cantLineas));  }
		   |ID'['subindice ASIGN expresion';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ']' para indicar la posicion de la coleccion ", AnalizadorLexico.cantLineas));  }
		   |ID'['']'ASIGN expresion';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba un subindice para realizar la asignacion ", AnalizadorLexico.cantLineas));  }
;

%%

public AnalizadorLexico aLexico;
public static List<analizadorLexico.Error> errores;
public static List<String> estructuras;


public int yylex() throws IOException{
	
	Token token = null;

	token = aLexico.getNextToken();

	if(token != null){ //si tengo un token
		yylval = new ParserVal(token.getLexema());
		return token.getId();
	}
	return 0;
}


public void yyerror ( String error){
	System.out.println(error);
}


public void modificarContadorDeReferencias(String lexema){
		Token t = AnalizadorLexico.tablaSimbolos.get(lexema);
		t.decrementarContadorDeReferencias();

		//si queda en 0 eliminarlo
		if (t.getContadorDeReferencias() <= 0)
			AnalizadorLexico.tablaSimbolos.remove(lexema);

		String negativo = "-" + lexema;
		t = AnalizadorLexico.tablaSimbolos.get(negativo);

		if (t != null){ //ya esta en TS
			t.incrementarContadorDeReferencias();
		}
		else{
			Integer id = AnalizadorLexico.palabras_reservadas.get("cte");
			t = new Token(negativo, AnalizadorLexico.TIPO_CTE, id);
			AnalizadorLexico.tablaSimbolos.put(negativo, t);
		}
}


public Parser (List<analizadorLexico.Error> erroresL, File file ) throws FileNotFoundException{
	this();
	errores = erroresL;
	aLexico = new AnalizadorLexico(file,erroresL);
	estructuras = new ArrayList<String>();
}

public String tokensToString(){
	return aLexico.tokensToString();
}

public String tDeStoString(){
	return aLexico.tdeStoString();
}

public String erroresToString(){

	StringBuilder out = new StringBuilder();
	analizadorLexico.Error error;
		
	for (int i=0 ; i<errores.size(); i++) {
		error = errores.get(i);
		out.append(error.toString());
	}
		
	return out.toString();
}


public String estructurasToString(){

	StringBuilder out = new StringBuilder();
		
	for (int i=0 ; i<estructuras.size(); i++) {
		out.append(estructuras.get(i));
	}
		
	return out.toString();
}

public void parse() throws IOException{
	int res = yyparse();
}

