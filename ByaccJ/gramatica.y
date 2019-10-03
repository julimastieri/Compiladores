
%token ID CTE CADENA ASIGN MAYORIGUAL MENORIGUAL IGUALIGUAL DISTINTO IF ELSE END_IF PRINT INT BEGIN END FOREACH IN ULONG TO_ULONG

%right '+' '-'
%right '*' '/'

%start programa


%%
programa : sentencias_declarativas sentencias_ejecutables { print("Reconoce bien el programa"); }
;

sentencias_declarativas : tipo lista_de_variables ';'
						|      lista_de_variables ';' { Parser.errores.add(new Error("ERROR", "Declaracion incorrecta. Se esperaba el tipo de la variable ", AnalizadorLexico.cantLineas)); }
						| tipo lista_de_variables { Parser.errores.add(new Error("ERROR", "Declaracion incorrecta. Se esperaba ';' ", AnalizadorLexico.cantLineas)); }
;

tipo : INT
	 | ULONG
	 | error ';' { Parser.errores.add(new Error("ERROR", "Tipo inexistente ", AnalizadorLexico.cantLineas)); }
;

lista_de_variables : ID { Parser.estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion de variable" + "\n"); }
				   | ID '[' lista_de_valores_iniciales ']' { Parser.estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion de coleccion" + "\n"); }
				   | ID '[' CTE ']' { Parser.estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion de coleccion" + "\n"); }
				   | ID ',' lista_de_variables

				   | ID     lista_de_valores_iniciales ']' { Parser.errores.add(new Error("ERROR", "Se esperaba '[' para definir los valores iniciales de la coleccion ", AnalizadorLexico.cantLineas)); }
				   | ID '[' lista_de_valores_iniciales     { Parser.errores.add(new Error("ERROR", "Se esperaba ']' para definir los valores iniciales de la coleccion ", AnalizadorLexico.cantLineas)); }
;				   

lista_de_valores_iniciales : CTE ',' elem_lista 
						   | '_' ',' elem_lista 
						   | CTE ',' lista_de_valores_iniciales 
						   | '_' ',' lista_de_valores_iniciales
						   | error ';' { Parser.errores.add(new Error("ERROR", "Formato de valores iniciales incorrectos ", AnalizadorLexico.cantLineas)); }
;

elem_lista : CTE
		   | '_'
;





sentencias_ejecutables : BEGIN lista_de_sentencias END
					   | 	   lista_de_sentencias END { Parser.errores.add(new Error("ERROR", "Se esperaba 'BEGIN' al comienzo ", AnalizadorLexico.cantLineas)); }
					   | BEGIN lista_de_sentencias     { Parser.errores.add(new Error("ERROR", "Se esperaba 'END' al final ", AnalizadorLexico.cantLineas)); }
					   //| error ';' { Parser.errores.add(new Error("ERROR", "Sentencia ejecutable incorrecta ", AnalizadorLexico.cantLineas)); }
;

lista_de_sentencias : sentencia_ejecutable 
					| sentencia_ejecutable lista_de_sentencias
;

bloque_de_sentencias : sentencia_ejecutable
					 | BEGIN lista_de_sentencias END

					 | 		 lista_de_sentencias END { Parser.errores.add(new Error("ERROR", "Se esperaba 'BEGIN' al comienzo ", AnalizadorLexico.cantLineas));  }
					 | BEGIN lista_de_sentencias     { Parser.errores.add(new Error("ERROR", "Se esperaba 'END' al final ", AnalizadorLexico.cantLineas));  }
;

sentencia_ejecutable : sentencia_if { Parser.estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia if " + "\n");}
					 | sentencia_foreach { Parser.estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia foreach " + "\n");}
					 | sentencia_print { Parser.estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia print " + "\n");}
					 | asignacion { Parser.estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Asignacion " + "\n");}
					 //| error ';' { Parser.errores.add(new Error("ERROR", "Sentencia ejecutable invalida ", AnalizadorLexico.cantLineas)); }
;					 

sentencia_if : IF '(' condicion ')' bloque_de_sentencias END_IF 
			 | IF '(' condicion ')' bloque_de_sentencias ELSE bloque_de_sentencias END_IF

			 //| error ';' { Parser.errores.add(new Error("ERROR", "Sentencia ejecutable invalida ", AnalizadorLexico.cantLineas)); }
			 
			/*
			   IF '(' condicion ')' bloque_de_sentencias declaracion
			 | IF '(' condicion ')' bloque_de_sentencias { Parser.errores.add(new Error("ERROR", "Se esperaba 'end_if' al final.", AnalizadorLexico.cantLineas));  }
			 | IF '(' condicion ')' bloque_de_sentencias ELSE bloque_de_sentencias { Parser.errores.add(new Error("ERROR", "Se esperaba 'end_if' al final.", AnalizadorLexico.cantLineas));  }
			 | IF     condicion ')' bloque_de_sentencias { Parser.errores.add(new Error("ERROR", "Se esperaba '(' antes de la condicion ", AnalizadorLexico.cantLineas));  }
			 | IF '(' condicion     bloque_de_sentencias { Parser.errores.add(new Error("ERROR", "Se esperaba ')' luego de la condicion ", AnalizadorLexico.cantLineas));  }
			 | IF     condicion     bloque_de_sentencias { Parser.errores.add(new Error("ERROR", "Se esperaban '(' y ')' para definir la condicion ", AnalizadorLexico.cantLineas));  }
			 | IF '('           ')' bloque_de_sentencias { Parser.errores.add(new Error("ERROR", "Se esperaba una condicion ", AnalizadorLexico.cantLineas));  }
			*/
;

condicion : expresion comparador expresion { Parser.estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Condicion " + "\n"); }

		  |           comparador expresion { Parser.errores.add(new Error("ERROR", "Se esperaba una expresion del lado derecho para comparar ", AnalizadorLexico.cantLineas)); }
		  | expresion comparador           { Parser.errores.add(new Error("ERROR", "Se esperaba una expresion del lado izquierdo para comparar ", AnalizadorLexico.cantLineas)); }
;		  

comparador : MAYORIGUAL  
		   | MENORIGUAL 
		   | IGUALIGUAL 
		   | DISTINTO
		   | '<'
		   | '>'
		   | error ';' { Parser.errores.add(new Error("ERROR", "Se esperaba un comparador valido ", AnalizadorLexico.cantLineas)); }
;		   

sentencia_foreach : FOREACH ID IN ID bloque_de_sentencias ';'

				  | FOREACH    IN ID bloque_de_sentencias ';' { Parser.errores.add(new Error("ERROR", "Se esperaba el nombre de la variable para iterar ", AnalizadorLexico.cantLineas)); } 
				  | FOREACH ID    ID bloque_de_sentencias ';' { Parser.errores.add(new Error("ERROR", "Se esperaba 'in' y se encontró el nombre de la coleccion ", AnalizadorLexico.cantLineas));  } 
				  | FOREACH ID IN    bloque_de_sentencias ';' { Parser.errores.add(new Error("ERROR", "Se esperaba el nombre de la coleccion y se encontraron sentencias ", AnalizadorLexico.cantLineas));  } 
				 // | FOREACH ID IN ID bloque_de_sentencias     { Parser.errores.add(new Error("ERROR", "Se esperaba ';' al final ", AnalizadorLexico.cantLineas));  } 
;

sentencia_print : PRINT '(' CADENA ')' ';'

				//| PRINT '(' CADENA ')' { Parser.errores.add(new Error("ERROR", "Se esperaba ';' al final ", AnalizadorLexico.cantLineas)); } 
				| PRINT CADENA ')' ';' { Parser.errores.add(new Error("ERROR", "Se esperaba '(' y se encontro una cadena ", AnalizadorLexico.cantLineas));  } 
				| PRINT '(' CADENA ';' { Parser.errores.add(new Error("ERROR", "Se esperaba ')' y se encontro ';' ", AnalizadorLexico.cantLineas));  } 
;

expresion : termino '+' expresion { Parser.estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Suma " + "\n"); }
		  | termino '-' expresion { Parser.estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Resta " + "\n"); }
		  | termino
;		  

termino : factor '*' termino { Parser.estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Multiplicacion " + "\n"); }
		| factor '/' termino { Parser.estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Division " + "\n"); }
		| factor
;		

factor : ID { Parser.estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor ID " + "\n"); }
	   | CTE { Parser.estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE " + "\n"); }
	   | '-' CTE { Parser.estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE negativa " + "\n"); }
	   | ID '[' subindice ']' { Parser.estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor coleccion " + "\n"); }
	   | TO_ULONG '(' expresion ')' { Parser.estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor conversion " + "\n"); }

	   | TO_ULONG  expresion ')' { Parser.errores.add(new Error("ERROR", "Se esperaba '(' para realizar la conversion ", AnalizadorLexico.cantLineas)); }
	   // | TO_ULONG '(' expresion { Parser.errores.add(new Error("ERROR", "Se esperaba ')' para realizar la conversion ", AnalizadorLexico.cantLineas)); }
;	   

subindice : ID 
		  | CTE

asignacion : ID ASIGN expresion ';' 
			
		   //| ID ASIGN expresion  { Parser.errores.add(new Error("ERROR", "Se esperaba ';' al final ", AnalizadorLexico.cantLineas));  } 
		   //|    ASIGN expresion ';' { Parser.errores.add(new Error("ERROR", "Se esperaba el nombre de la variable para realizar la asignacion ", AnalizadorLexico.cantLineas));  } 
		   | ID ASIGN           ';' { Parser.errores.add(new Error("ERROR", "Se esperaba una expresion para realizar la asignacion ", AnalizadorLexico.cantLineas));  }
;