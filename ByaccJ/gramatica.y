%{
package analizadorSintactico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.Token;

%}

%token ID CTE CADENA ASIGN MAYORIGUAL MENORIGUAL IGUALIGUAL DISTINTO IF ELSE END_IF PRINT INT BEGIN END FOREACH IN ULONG TO_ULONG

%right '+' '-'
%right '*' '/'

%start programa


%%
programa : sentencias_declarativas sentencias_ejecutables { raiz = new NodoArbol("PROGRAMA", null , $2);
			}
		 |sentencias_declarativas { raiz = new NodoArbol("PROGRAMA", null , null);
			}
		 |sentencias_ejecutables { raiz = new NodoArbol("PROGRAMA", $1, null);
			}	
;

sentencias_declarativas : sentencia_declarativa
						| sentencia_declarativa sentencias_declarativas
;							

sentencia_declarativa : tipo lista_de_variables';' { agregarTipoTS($1.sval, $2.sval); //$2.sval tiene el string? o hay que crearlo desde abajo?
												     
												    }
					  | tipo lista_de_variables { errores.add(new analizadorLexico.Error("ERROR", "Declaracion incorrecta. Se esperaba ';'.", AnalizadorLexico.cantLineas)); }
					  | error';'{ errores.add(new analizadorLexico.Error("ERROR", "Sentencia invalida.", AnalizadorLexico.cantLineas)); }
;
				  		   

tipo :INT
	 |ULONG
;

lista_de_variables :ID { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion" + "\n"); 
					     agregarUsoTS($1.sval, "Variable"); 
					    }
				   |ID '['lista_de_valores_iniciales']' { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion" + "\n"); 
				   										  agregarUsoTS($1.sval, "Nombre de coleccion");
				   										  inferirTamanio($1.sval, $3.sval);
														}
				   |ID '['CTE']' { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion" + "\n"); 
				   				   agregarUsoTS($1.sval, "Nombre de coleccion");
				   				   agregarTamanio($1.sval, $3.sval);
								 }
				   |ID ',' lista_de_variables { agregarUsoTS($1.sval, "Variable"); 

				   							  }
				   |ID '['lista_de_valores_iniciales']'',' lista_de_variables { agregarUsoTS($1.sval, "Nombre de coleccion");
				   																inferirTamanio($1.sval, $3.sval);
				   															   }
				   |ID '['CTE']' ',' lista_de_variables { agregarUsoTS($1.sval, "Nombre de coleccion");
				   										  agregarTamanio($1.sval, $3.sval);
				   										}


				   |ID     CTE ']' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '[' para definir tamaño de la coleccion. ", AnalizadorLexico.cantLineas)); }
				   |ID '[' CTE     { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ']' para definir tamaño de la coleccion. ", AnalizadorLexico.cantLineas)); }
				   |ID     lista_de_valores_iniciales ']' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '[' para definir los valores iniciales de la coleccion. ", AnalizadorLexico.cantLineas)); }
				   |ID '[' lista_de_valores_iniciales     { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ']' para definir los valores iniciales de la coleccion. ", AnalizadorLexico.cantLineas)); }
				   |ID '['                            ']' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba valor entre '[' ']' para definir a la coleccion. ", AnalizadorLexico.cantLineas)); }
;				   

lista_de_valores_iniciales :'_'
						   |CTE ',' CTE 
						   |'_' ',' CTE 
						   |CTE ',' lista_de_valores_iniciales 
						   |'_' ',' lista_de_valores_iniciales

						   |CTE CTE { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); }
						   |CTE lista_de_valores_iniciales { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); }
						   |'_' CTE { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); }
						   |'_' lista_de_valores_iniciales { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); }

;

sentencias_ejecutables :BEGIN lista_de_sentencias END { $$ = $2;
						}
					   |BEGIN END

					   |lista_de_sentencias END { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'begin' al comienzo. ", AnalizadorLexico.cantLineas)); }
					   |BEGIN lista_de_sentencias { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'end' al final. ", AnalizadorLexico.cantLineas)); }
					   |lista_de_sentencias { errores.add(new analizadorLexico.Error("ERROR", "Ausencia de begin y end de sentencias ejecutables.", AnalizadorLexico.cantLineas)); }
;

lista_de_sentencias :sentencia_ejecutable { $$ = $1 ; }
					|sentencia_ejecutable lista_de_sentencias { $$ = new NodoArbol("S", $1, $2); }
;


sentencia_ejecutable :seleccion { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia if. " + "\n"); 
								  $$ = $1;
						        }
					 |sentencia_foreach { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia foreach. " + "\n");
					 			          $$ = $1;
					                    }
					 |sentencia_print { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia print. " + "\n");
					 			        $$ = $1;
					                  }
					 |asignacion { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Asignacion. " + "\n");
					 			   $$ = $1;
					             }
;	

seleccion : IF condicion cuerpo_if { $$ = new NodoArbol("IF", $2, $3);
			                       }

		  |condicion cuerpo_if { errores.add(new analizadorLexico.Error("ERROR", "Ausencia de palabra reservada 'if'. ", AnalizadorLexico.cantLineas)); }

;

cuerpo_if : bloque_de_sentencias_if END_IF ';' {  
												  $$ = new NodoArbol("CUERPO", $1, null);
			                                   }	

		  | bloque_de_sentencias_if ELSE bloque_de_sentencias_else END_IF';' { 
		  																		$$ = new NodoArbol("CUERPO", $1, $3);
		                                                                     }

		  | bloque_de_sentencias_if ';'  { errores.add(new analizadorLexico.Error("ERROR", "Ausencia de palabra reservada 'end_if'. ", AnalizadorLexico.cantLineas)); }
		  | bloque_de_sentencias_if ELSE bloque_de_sentencias_else ';' { errores.add(new analizadorLexico.Error("ERROR", "Ausencia de palabra reservada 'end_if'. ", AnalizadorLexico.cantLineas)); }

//THEN
bloque_de_sentencias_if : bloque_de_sentencias {  $$ = new NodoArbol("THEN", $1, null);
							                   }
;


bloque_de_sentencias_else : bloque_de_sentencias { $$ = new NodoArbol("ELSE", $1, null);
					                             }
;

//S
bloque_de_sentencias :sentencia_ejecutable { $$ = new NodoArbol("S", $1, null);
						                   }

					 |BEGIN lista_de_sentencias END { $$ = $2; }

					 |BEGIN END { $$ = new NodoArbol("S", null, null);
					            }

					 |lista_de_sentencias END { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'begin' al comienzo del bloque de sentencias. ", AnalizadorLexico.cantLineas));  }
					 |BEGIN lista_de_sentencias { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'end' al final del bloque de sentencias. ", AnalizadorLexico.cantLineas));  }
;


condicion :'(' expresion comparador expresion ')' { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Condicion " + "\n"); 
													NodoArbol nodo_cond = new NodoArbol($3.sval, $2, $4);
													$$ = new NodoArbol("CONDICION", nodo_cond, null);
												  }

		  |'(' comparador expresion ')'{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion del lado derecho para comparar. ", AnalizadorLexico.cantLineas)); }
		  |'(' expresion comparador ')' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion del lado izquierdo para comparar. ", AnalizadorLexico.cantLineas)); }
		  | '(' expresion comparador expresion { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ')' que cierre condicion. ", AnalizadorLexico.cantLineas)); }
		  | '(' ')' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una condicion entre '(' ')'. ", AnalizadorLexico.cantLineas)); }
;		  

comparador : MAYORIGUAL 
		   | MENORIGUAL 
		   | IGUALIGUAL 
		   | DISTINTO
		   | '<'
		   | '>'
;		   

sentencia_foreach :FOREACH ID IN ID bloque_de_sentencias';' { String tipo_variable, tipo_coleccion;
															  if ( (estaDeclarada($2.sval)) && (estaDeclarada($4.sval)) ){
																Token token_variable = AnalizadorLexico.tablaSimbolos.get(val_peek(4).sval);
																Token token_coleccion = AnalizadorLexico.tablaSimbolos.get(val_peek(2).sval);
																
																tipo_variable = token_variable.getTipoDeDato();
																tipo_coleccion = token_coleccion.getTipoDeDato();
																String uso_variable = token_variable.getUso();
																String uso_coleccion = token_coleccion.getUso();

																if(uso_variable != "Variable"){
																	errores.add(new analizadorLexico.Error("ERROR",$2.sval + " debe ser de tipo 'variable'" , AnalizadorLexico.cantLineas));
																	
																}

																if(uso_coleccion != "Nombre de coleccion"){
																	errores.add(new analizadorLexico.Error("ERROR",$4.sval + " debe ser de tipo 'coleccion'" , AnalizadorLexico.cantLineas));
																}

																if(tipo_variable != tipo_coleccion){
																	errores.add(new analizadorLexico.Error("ERROR",$2.sval + " debe ser del mismo tipo de la coleccion", AnalizadorLexico.cantLineas));
																	
																}

															  }
															  NodoArbol nodo_condicion = new NodoArbol("condicion_foreach", $2, $4);
															  $$ = new NodoArbol("FOREACH",nodo_condicion ,$5);
					                                        }

				  |FOREACH IN ID bloque_de_sentencias';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba el nombre de la variable para iterar. ", AnalizadorLexico.cantLineas)); } 
				  |FOREACH ID ID bloque_de_sentencias';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'in' y se encontró el nombre de la coleccion. ", AnalizadorLexico.cantLineas));  } 
				  |FOREACH ID IN bloque_de_sentencias';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba el nombre de la coleccion y se encontraron sentencias. ", AnalizadorLexico.cantLineas));  } 
;


sentencia_print :PRINT '(' CADENA ')' ';' { NodoArbol nodo_cadena = new NodoArbol("Cadena", null, null);
											$$ = new NodoArbol("PRINT", nodo_cadena, null);
				 						  }
 
				|PRINT CADENA ')' ';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '(' y se encontro una cadena. ", AnalizadorLexico.cantLineas));  } 
				|PRINT '(' CADENA ';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ')' y se encontro ';'. ", AnalizadorLexico.cantLineas));  } 


;


expresion :termino'+'expresion { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Suma. " + "\n"); 
								NodoArbol aux = new NodoArbol("+", $1, $3);
								aux.setTipoDeDato((NodoArbol)$1, (NodoArbol) $3);
								$$ = aux; 
								}
		  |termino'-'expresion { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Resta. " + "\n"); 
		  						NodoArbol aux = new NodoArbol("-", $1, $3);
		  						aux.setTipoDeDato((NodoArbol) $1, (NodoArbol) $3); 
		  						$$ = aux;
		                       }
		  |termino { $$ = $1;
		           }
;		  

termino :factor'*'termino { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Multiplicacion. " + "\n"); 
							NodoArbol aux = new NodoArbol("*", $1, $3);
							aux.setTipoDeDato((NodoArbol)$1, (NodoArbol) $3); //adentro se chequea que los tipos sean iguales
							$$ = aux;
						  }

		|factor'/'termino { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Division. " + "\n"); 
							NodoArbol aux = new NodoArbol("/", $1, $3);
							aux.setTipoDeDato((NodoArbol)$1, (NodoArbol)$3); //chequear que los tipos sean igules
							$$ = aux;
						  }

		|factor { $$ = $1;
		        }
;		

factor :ID { estaDeclarada($1.sval);
			 if (AnalizadorLexico.tablaSimbolos.get($1.sval).getUso() == "Nombre de coleccion") {
			 	errores.add(new analizadorLexico.Error("ERROR", "No se puede utilizar una coleccion como factor. ", AnalizadorLexico.cantLineas));
			 }
			 estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor ID. " + "\n"); 
			 NodoArbol aux = new NodoArbol($1.sval, null, null);
			 aux.setTipoDeDato(AnalizadorLexico.tablaSimbolos.get($1.sval).getTipoDeDato());
			 $$ = aux;
		   }

	   |CTE { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE. " + "\n"); 
	   		  NodoArbol aux = new NodoArbol($1.sval, null, null);
			  aux.setTipoDeDato(AnalizadorLexico.tablaSimbolos.get($1.sval).getTipoDeDato());
			  $$ = aux;
			}

	   |'-'CTE {  estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE Negativa. " + "\n");
	   			  modificarContadorDeReferencias($2.sval);	 
	   			  NodoArbol aux = new NodoArbol($1.sval, null, null);
			      aux.setTipoDeDato(AnalizadorLexico.tablaSimbolos.get($1.sval).getTipoDeDato());
			      $$ = aux;

	   			}

	   |ID'['subindice']' { estaDeclarada($1.sval);
	   						estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor coleccion. " + "\n");
	   						NodoArbol nodo_id = new NodoArbol($1.sval, null, null);
							nodo_id.setTipoDeDato(AnalizadorLexico.tablaSimbolos.get($1.sval).getTipoDeDato());
		   					

	   						NodoArbol aux = new NodoArbol("ELEM_COLEC", nodo_id, $3); 
	   						aux.setTipoDeDato(nodo_id.getTipoDeDato()); 
	   						$$ = aux;

						  }

	   |TO_ULONG'('expresion')' { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor conversion. " + "\n"); 
	   							  NodoArbol aux = new NodoArbol("CONVERSION", $3, null); 
	   							  aux.setTipoDeDato(AnalizadorLexico.TIPO_DATO_ULONG);
	   							  $$ = aux;
								}

	   |TO_ULONG expresion')' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '(' para realizar la conversion. ", AnalizadorLexico.cantLineas)); }
;	   

subindice :ID {	if (estaDeclarada($1.sval)){
					if( AnalizadorLexico.tablaSimbolos.get($1.sval).getTipoDeDato() != "int"){
		  				errores.add(new analizadorLexico.Error("ERROR", "El tipo de dato del subindice debe ser entero. ", AnalizadorLexico.cantLineas));
		  		 	}
		  			if( AnalizadorLexico.tablaSimbolos.get($1.sval).getUso() != "Variable"){
		  				errores.add(new analizadorLexico.Error("ERROR", "El identificador usado como subindice no es una variable. ", AnalizadorLexico.cantLineas));
		  		 	}
				}
				
				NodoArbol aux = new NodoArbol($1.sval, null, null);
				aux.setTipoDeDato(AnalizadorLexico.tablaSimbolos.get($1.sval).getTipoDeDato());
				$$ = aux;

			  }
		  |CTE { if( AnalizadorLexico.tablaSimbolos.get($1.sval).getTipoDeDato() != "int"){
		  			errores.add(new analizadorLexico.Error("ERROR", "El tipo de dato del subindice debe ser entero. ", AnalizadorLexico.cantLineas));
		  		 }
		  		 NodoArbol aux = new NodoArbol($1.sval, null, null);
				 aux.setTipoDeDato(AnalizadorLexico.tablaSimbolos.get($1.sval).getTipoDeDato());
				 $$ = aux;

			   }
;		  

asignacion :ID ASIGN expresion ';' {  estaDeclarada($1.sval);
									  NodoArbol nodo_id = new NodoArbol($1.sval, null, null);//crear nodo con ID y ponerle el tipo
									  nodo_id.setTipoDeDato(AnalizadorLexico.tablaSimbolos.get($1.sval).getTipoDeDato());
									  NodoArbol aux = new NodoArbol(":=", nodo_id , $3);
									  aux.setTipoDeDato(nodo_id.getTipoDeDato()); 	
									  $$ = aux;

			                       }
			                       					
		   |ID'['subindice']'ASIGN expresion ';' {  estaDeclarada($1.sval);
		   										    NodoArbol nodo_id = new NodoArbol($1.sval, null, null);
													nodo_id.setTipoDeDato(AnalizadorLexico.tablaSimbolos.get($1.sval).getTipoDeDato());
	   												NodoArbol nodo_colec = new NodoArbol("ELEM_COLEC", nodo_id, $3); 
	   												nodo_colec.setTipoDeDato(nodo_id.getTipoDeDato()); 

		   											NodoArbol aux = new NodoArbol(":=", nodo_colec, $6);
		   											aux.setTipoDeDato(nodo_id.getTipoDeDato());
		   											$$ = aux;

		                                         }
			
		   |ID ASIGN ';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion para realizar la asignacion. ", AnalizadorLexico.cantLineas));  }
		   |ID subindice']'ASIGN expresion ';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '[' para indicar la posicion de la coleccion. ", AnalizadorLexico.cantLineas));  }
		   |ID'['subindice ASIGN expresion ';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ']' para indicar la posicion de la coleccion. ", AnalizadorLexico.cantLineas));  }
		   |ID'[' ']'ASIGN expresion ';' { errores.add(new analizadorLexico.Error("ERROR", "Se esperaba un subindice para realizar la asignacion. ", AnalizadorLexico.cantLineas));  }
;

%%

public AnalizadorLexico aLexico;
public static List<analizadorLexico.Error> errores;
public static List<String> estructuras;
public NodoArbol raiz;
public StringBuilder arbolString = new StringBuilder();


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

		if (Long.parseLong(lexema) <= AnalizadorLexico.MAX_INT) {
			if (t != null){ //ya esta en TS
				t.incrementarContadorDeReferencias();
			}
			else{
				Integer id = AnalizadorLexico.palabras_reservadas.get("cte");
				t = new Token(negativo, AnalizadorLexico.TIPO_CTE, id);
				t.setTipoDeDato(AnalizadorLexico.TIPO_DATO_ENTERO);
				AnalizadorLexico.tablaSimbolos.put(negativo, t);
			}
		}
		else{ //generar error
			errores.add(new analizadorLexico.Error("ERROR", "Constante negativa fuera de rango. Fue reemplazado por el valor limite permitido del rango", AnalizadorLexico.cantLineas));	
			negativo = "-" + AnalizadorLexico.MAX_INT;
			t = AnalizadorLexico.tablaSimbolos.get(negativo);
			if (t != null) {//si ya esta
				t.incrementarContadorDeReferencias();
			}
			else {
				Integer id = AnalizadorLexico.palabras_reservadas.get("cte");
				t = new Token(negativo, AnalizadorLexico.TIPO_CTE, id);
				t.setTipoDeDato(AnalizadorLexico.TIPO_DATO_ENTERO);
				AnalizadorLexico.tablaSimbolos.put(negativo, t);
			}
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

public int parse() throws IOException{
	return yyparse();
}


public void agregarTipoTS (String tipo, String listaVariables){
	Token t;
	String v;
	List<String> variables = new ArrayList<String> (Arrays.asList(listaVariables.split(",")));
	for (int i=0; i<variables.size(); i++){
		v = variables.get(i);
		t = AnalizadorLexico.tablaSimbolos.get(v);
		t.setTipoDeDato(tipo);
	}
}


public void agregarUsoTS (String variable , String uso){
	AnalizadorLexico.tablaSimbolos.get(variable).setUso(uso);
}

public void agregarTamanio (String variable , String tamanio){
	AnalizadorLexico.tablaSimbolos.get(variable).setTamanio(Integer.parseInt(tamanio));
}


public void inferirTamanio (String variable , String lista_de_valores_iniciales){ //aca tambien seteamos los valores iniciales
	ArrayList<String> valores_iniciales = new ArrayList<String> (Arrays.asList(lista_de_valores_iniciales.split(",")));
	Token t = AnalizadorLexico.tablaSimbolos.get(variable);
	t.setTamanio(valores_iniciales.size());
	t.setValoresIniciales(valores_iniciales);
}

public boolean estaDeclarada(String lexema){

	if (AnalizadorLexico.tablaSimbolos.containsKey(lexema)){
		return true;
	} else{
		errores.add(new analizadorLexico.Error("ERROR", "Variable " + lexema + " no declarada" , AnalizadorLexico.cantLineas));		
		return false;
	}

}


public void imprimirArbol(NodoArbol nodo, String tabs) {
    	 
	arbolString.append(tabs + nodo.getNombre() + "\n");  //raiz
	
	if(nodo.nodoIzq!=null)
		imprimirArbol(nodo.getNodoIzq(), tabs + "\t");
        
    if(nodo.nodoDer!=null)
        imprimirArbol(nodo.getNodoDer(), tabs + "\t");
 
}
