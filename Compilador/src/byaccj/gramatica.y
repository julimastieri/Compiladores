%{
package , imports de parser ...
%}

%token ID CTE CADENA ASIGN MAYORIGUAL MENORIGUAL IGUALIGUAL DISTINTO IF ELSE END_IF PRINT INT BEGIN END FOREACH IN ULONG TO_ULONG

%right '+' '-'
%right '*' '/'

%start programa


%%
programa : sentencias_declarativas sentencias_ejecutables
;

sentencias_declarativas : tipo lista_de_variables ';'
						| tipo ID '[' lista_de_valores_iniciales ']' ';'
;

tipo : INT
	 | ULONG
;

lista_de_variables : ID 
				   | ID ',' lista_de_variables

lista_de_valores_iniciales : CTE
						   | '_' 
						   | CTE ',' lista_de_valores_iniciales 
						   | '_' ',' lista_de_valores_iniciales
;

sentencias_ejecutables : BEGIN lista_de_sentencias END

/*bloque_de_sentencias : BEGIN lista_de_sentencias END
;*/

lista_de_sentencias : sentencia_ejecutable 
					| sentencia_ejecutable lista_de_sentencias
;

bloque_de_sentencias : sentencia_ejecutable
					 | BEGIN lista_de_sentencias END
;

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

sentencia_foreach : FOREACH ID IN ID bloque_de_sentencias ';'
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

factor : ID 
	   | CTE
	   | ID '[' subindice ']'
;	   

subindice : ID 
		  | CTE

asignacion : ID ASIGN expresion ';' 
		   | ID ASIGN conversion '(' expresion ')' ';'
;

conversion : TO_ULONG
;

/*
problema con sentencias_ejecutables, lista_de_sentencias, bloque_de_sentencias
problema: multiples cosas se definen como ID y CTE
*/