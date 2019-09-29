%{
package , imports de parser ...
%}

%token ID CTE CADENA ASIGN MAYORIGUAL MENORIGUAL IGUALIGUAL DISTINTO IF ELSE END_IF PRINT INT BEGIN END FOREACH IN ULONG TO_ULONG

%right '+' '-'
%right '*' '/'

%start programa


%%
programa : sentencias_declarativas bloque_de_sentencias//sentencias_ejecutables
;

sentencias_declarativas : tipo lista_de_variables ';'
						| tipo nombre_coleccion '[' tamanio ']' ';' 
						| tipo nombre_coleccion '[' lista_de_valores_iniciales ']' ';'
;

tipo : INT
	 | ULONG
;

lista_de_variables : nombre_variable 
				   | nombre_variable ',' lista_de_variables

nombre_variable : ID
;

nombre_coleccion : ID
;

tamanio : CTE
;

lista_de_valores_iniciales : CTE
						   | '_' 
						   | CTE ',' lista_de_valores_iniciales 
						   | '_' ',' lista_de_valores_iniciales
;

//sentencias_ejecutables : BEGIN lista_de_sentencias END
bloque_de_sentencias : BEGIN lista_de_sentencias END
;

lista_de_sentencias : sentencia_ejecutable 
					| sentencia_ejecutable lista_de_sentencias
;

/*bloque_de_sentencias : sentencia_ejecutable
					 | BEGIN lista_de_sentencias END
;*/

sentencia_ejecutable : sentencia_if 
					 | sentencia_foreach 
					 | sentencia_print
					 | asignacion
;					 

sentencia_if : IF '(' condicion ')' bloque_de_sentencias END_IF 
			 | IF '(' condicion ')' bloque_de_sentencias ELSE bloque_de_sentencias END_IF
;

condicion : expresion comparador expresion
		  | conversion '(' expresion ')' comparador conversion '('expresion ')'
		  | expresion comparador conversion '(' expresion ')' 
		  | conversion '(' expresion ')' comparador expresion
;		  

comparador : MAYORIGUAL  
		   | MENORIGUAL 
		   | IGUALIGUAL 
		   | DISTINTO
		   | '<'
		   | '>'
;		   

sentencia_foreach : FOREACH nombre_variable IN nombre_coleccion bloque_de_sentencias ';'
;

sentencia_print : PRINT '(' '{' CADENA '}' ')' ';'
;

expresion : termino '+' expresion 
		  | termino '-' expresion
		  | termino
;		  

termino : factor '*' termino 
		| factor '/' termino 
		| factor
;		

factor : nombre_variable 
	   | CTE
	   | nombre_coleccion '[' subindice ']'
;	   

subindice : ID 
		  | CTE

asignacion : nombre_variable ASIGN expresion ';' 
		   | nombre_variable ASIGN conversion '(' expresion ')' ';'
;

conversion : TO_ULONG
;