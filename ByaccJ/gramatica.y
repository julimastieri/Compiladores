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
import analizadorLexico.Error;

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

sentencia_declarativa : tipo lista_de_variables';' { agregarTipoTS($1.sval, $2.obj);}

					  | tipo lista_de_variables { errores.add(new Error("ERROR", "Declaracion incorrecta. Se esperaba ';'.", AnalizadorLexico.cantLineas)); }
					  | error';'{ errores.add(new Error("ERROR", "Sentencia invalida.", AnalizadorLexico.cantLineas)); $$=new NodoArbol("ERROR SINTACTICO", null, null);}
;
				  		   

tipo :INT
	 |ULONG
;

lista_de_variables :ID { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion" + "\n"); 
					     agregarUsoTS($1.sval, Token.USO_VARIABLE); 

					     ArrayList<String> listaDeVariables = new ArrayList<String>();
					     listaDeVariables.add(0, $1.sval);
					     $$.obj = listaDeVariables;
					    }

				   |ID '['lista_de_valores_iniciales']' { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion" + "\n"); 
				   										  agregarUsoTS($1.sval, Token.USO_COLECCION);
				   										  if ($3 != null)
				   										  	inferirTamanio($1.sval, $3.obj);
				   										  
				   										  ArrayList<String> listaDeVariables = new ArrayList<String>();
				   										  listaDeVariables.add(0, $1.sval);
				   										  $$.obj = listaDeVariables;
														}
				   |ID '['CTE']' { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion" + "\n"); 
				   				   agregarUsoTS($1.sval, Token.USO_COLECCION);
				   				   agregarTamanio($1.sval, $3.sval);

				   				   ArrayList<String> listaDeVariables = new ArrayList<String>();
				   				   listaDeVariables.add(0, $1.sval);
				   				   $$.obj = listaDeVariables;
								 }

				   |ID ',' lista_de_variables { agregarUsoTS($1.sval, Token.USO_VARIABLE); 

				   								((ArrayList<String>)$3.obj).add(0, $1.sval);
				   								$$.obj = $3.obj;
				   							  }

				   |ID '['lista_de_valores_iniciales']'',' lista_de_variables { agregarUsoTS($1.sval, Token.USO_COLECCION);
				   																if ($3 != null)
				   																	inferirTamanio($1.sval, $3.obj);

				   																((ArrayList<String>)$6.obj).add(0, $1.sval);
				   																$$.obj = $6.obj;
				   															   }

				   |ID '['CTE']' ',' lista_de_variables { agregarUsoTS($1.sval, Token.USO_COLECCION);
				   										  agregarTamanio($1.sval, $3.sval);

				   										  ((ArrayList<String>)$6.obj).add(0, $1.sval);
				   										  $$.obj = $6.obj;
				   										}


				   |ID     CTE ']' { errores.add(new Error("ERROR", "Se esperaba '[' para definir tamaño de la coleccion. ", AnalizadorLexico.cantLineas)); }
				   |ID '[' CTE     { errores.add(new Error("ERROR", "Se esperaba ']' para definir tamaño de la coleccion. ", AnalizadorLexico.cantLineas)); }
				   |ID     lista_de_valores_iniciales ']' { errores.add(new Error("ERROR", "Se esperaba '[' para definir los valores iniciales de la coleccion. ", AnalizadorLexico.cantLineas)); }
				   |ID '[' lista_de_valores_iniciales     { errores.add(new Error("ERROR", "Se esperaba ']' para definir los valores iniciales de la coleccion. ", AnalizadorLexico.cantLineas)); }
				   |ID '['                            ']' { errores.add(new Error("ERROR", "Se esperaba valor entre '[' ']' para definir a la coleccion. ", AnalizadorLexico.cantLineas)); }
;				   

lista_de_valores_iniciales :'_' { ArrayList<String> listaDeVariables = new ArrayList<String>();
					    		  listaDeVariables.add(0, $1.sval);
					     		  $$.obj = listaDeVariables;
					     		}

						   |cte ',' cte {ArrayList<String> listaDeVariables = new ArrayList<String>();
					    		  		 listaDeVariables.add(0, $3.sval);
					    		  		 listaDeVariables.add(0, $1.sval);
					     		  		 $$.obj = listaDeVariables;
						   				}
						   |'_' ',' cte {ArrayList<String> listaDeVariables = new ArrayList<String>();
					    		  		 listaDeVariables.add(0, $3.sval);
					    		  		 listaDeVariables.add(0, $1.sval);
					     		  		 $$.obj = listaDeVariables;
						   				}
						   |cte ',' lista_de_valores_iniciales {
						   										((ArrayList<String>)$3.obj).add(0, $1.sval);
				   												$$.obj = $3.obj;
						   									   }
						   |'_' ',' lista_de_valores_iniciales{
						   										((ArrayList<String>)$3.obj).add(0, $1.sval);
				   												$$.obj = $3.obj;
						   									   }

						   |cte cte { errores.add(new Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); $$=null;}
						   |cte lista_de_valores_iniciales { errores.add(new Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); $$=null;}
						   |'_' cte { errores.add(new Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); $$=null;}
						   |'_' lista_de_valores_iniciales { errores.add(new Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); $$=null;}

;

cte : CTE 
	| '-'CTE {$$.sval = modificarContadorDeReferencias($2.sval);}
;

sentencias_ejecutables :BEGIN lista_de_sentencias END {$$ = $2;}
					   |BEGIN END {$$=null;}

					   |lista_de_sentencias END { errores.add(new Error("ERROR", "Se esperaba 'begin' al comienzo. ", AnalizadorLexico.cantLineas)); $$=null;}
					   |BEGIN lista_de_sentencias { errores.add(new Error("ERROR", "Se esperaba 'end' al final. ", AnalizadorLexico.cantLineas)); $$=null;}
					   |lista_de_sentencias { errores.add(new Error("ERROR", "Ausencia de begin y end de sentencias ejecutables.", AnalizadorLexico.cantLineas)); $$=null;}
;

lista_de_sentencias :sentencia_ejecutable { $$ = $1 ; }
					|sentencia_ejecutable lista_de_sentencias { $$ = new NodoArbol("Sentencia Ejecutable", $1, $2); }
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
									NodoArbol aux = (NodoArbol) $2;
									aux.setNroIdentificador(contadorDeIf);

									aux = (NodoArbol) $3;
									aux.setNroIdentificador(contadorDeIf);

									aux = (NodoArbol) $3;
									aux = aux.getNodoIzq();
									aux.setNroIdentificador(contadorDeIf);

									contadorDeIf++;
			                       }

		  |condicion cuerpo_if { errores.add(new Error("ERROR", "Ausencia de palabra reservada 'if'. ", AnalizadorLexico.cantLineas)); $$=new NodoArbol("ERROR SINTACTICO", null, null);}

;

cuerpo_if : bloque_de_sentencias_if END_IF ';' {  
												  NodoArbol aux = new NodoArbol("CUERPO", $1, null);
												  aux.setNroIdentificador(contadorDeIf);
												  $$ = aux;

			                                   }	

		  | bloque_de_sentencias_if ELSE bloque_de_sentencias_else END_IF';' { 
		  																		$$ = new NodoArbol("CUERPO", $1, $3);
		                                                                     }

		  | bloque_de_sentencias_if ';'  { errores.add(new Error("ERROR", "Ausencia de palabra reservada 'end_if'. ", AnalizadorLexico.cantLineas)); $$=null;}
		  | bloque_de_sentencias_if ELSE bloque_de_sentencias_else ';' { errores.add(new Error("ERROR", "Ausencia de palabra reservada 'end_if'. ", AnalizadorLexico.cantLineas)); $$=null;}

//THEN
bloque_de_sentencias_if : bloque_de_sentencias {  
												NodoArbol aux = new NodoArbol("THEN", $1, null);
												aux.setNroIdentificador(contadorDeIf);
												$$ = aux;
							                   }
;


bloque_de_sentencias_else : bloque_de_sentencias { $$ = new NodoArbol("ELSE", $1, null);
					                             }
;

//S
bloque_de_sentencias :sentencia_ejecutable { $$ = $1;						                   }

					 |BEGIN lista_de_sentencias END { $$ = $2; }

					 |BEGIN END { $$ = new NodoArbol("Sentencia Ejecutable", null, null);
					            }

					 |lista_de_sentencias END { errores.add(new Error("ERROR", "Se esperaba 'begin' al comienzo del bloque de sentencias. ", AnalizadorLexico.cantLineas));  $$=new NodoArbol("ERROR SINTACTICO", null, null);}
					 |BEGIN lista_de_sentencias { errores.add(new Error("ERROR", "Se esperaba 'end' al final del bloque de sentencias. ", AnalizadorLexico.cantLineas));  $$=new NodoArbol("ERROR SINTACTICO", null, null);}
;


condicion :'(' expresion comparador expresion ')' { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Condicion " + "\n"); 
													NodoArbol nodo_cond = new NodoArbol($3.sval, $2, $4);
													nodo_cond.setNroIdentificador(contadorDeIf);
													nodo_cond.setTipoDeDato((NodoArbol)$2, (NodoArbol) $4);

													$$ = new NodoArbol("CONDICION", nodo_cond, null);
												  }

		  |'(' comparador expresion ')'{ errores.add(new Error("ERROR", "Se esperaba una expresion del lado derecho para comparar. ", AnalizadorLexico.cantLineas)); $$=null;}
		  |'(' expresion comparador ')' { errores.add(new Error("ERROR", "Se esperaba una expresion del lado izquierdo para comparar. ", AnalizadorLexico.cantLineas)); $$=null;}
		  | '(' expresion comparador expresion { errores.add(new Error("ERROR", "Se esperaba ')' que cierre condicion. ", AnalizadorLexico.cantLineas)); $$=null;}
		  | '(' ')' { errores.add(new Error("ERROR", "Se esperaba una condicion entre '(' ')'. ", AnalizadorLexico.cantLineas)); $$=null;}
;		  

comparador : MAYORIGUAL 
		   | MENORIGUAL 
		   | IGUALIGUAL 
		   | DISTINTO
		   | '<'
		   | '>'
;		   

sentencia_foreach :FOREACH ID IN ID bloque_de_sentencias';' { 
															  Boolean variable_declarada = estaDeclarada($2.sval);
															  Boolean coleccion_declarada = estaDeclarada($4.sval);
															  if ( variable_declarada && coleccion_declarada ){
																Token token_variable = AnalizadorLexico.tablaSimbolos.get($2.sval);
																Token token_coleccion = AnalizadorLexico.tablaSimbolos.get($4.sval);
																
																checkearUsoCorrecto(token_variable.getUso(), Token.USO_VARIABLE);
																checkearUsoCorrecto(token_coleccion.getUso(), Token.USO_COLECCION);

																if(!(token_variable.getTipoDeDato().equals(token_coleccion.getTipoDeDato()))) {
																	errores.add(new Error("ERROR", "Tipo de dato de "+ $2.sval + " es " + token_variable.getTipoDeDato() + " no es compatible con el tipo de dato de "+ $4.sval + " que es " + token_coleccion.getTipoDeDato(), AnalizadorLexico.cantLineas));
																}

															  }

															  String lexema = "@itForeach" + contadorDeForeach;
										 			          Integer id = AnalizadorLexico.palabras_reservadas.get("id");
															  Token token = new Token(lexema, AnalizadorLexico.TIPO_ID, id);
															  token.setTipoDeDato(AnalizadorLexico.TIPO_DATO_ENTERO);
															  token.setUso(Token.USO_VARIABLE_AUX);
															  AnalizadorLexico.tablaSimbolos.put(lexema, token);

															  NodoArbol nodo_variable = new NodoArbol($2.sval, null, null);
															  NodoArbol nodo_coleccion = new NodoArbol($4.sval, null, null);
															  NodoArbol nodo_condicion = new NodoArbol("CONDICION_FOREACH", nodo_variable, nodo_coleccion);
															  nodo_condicion.setNroIdentificador(contadorDeForeach);
															  NodoArbol nodo_cuerpo_foreach = new NodoArbol("CUERPO_FOREACH", $5, null);
															  nodo_cuerpo_foreach.setNroIdentificador(contadorDeForeach);
															  contadorDeForeach++;
															  $$ = new NodoArbol("FOREACH",nodo_condicion ,nodo_cuerpo_foreach);
					                                        }

				  |FOREACH IN ID bloque_de_sentencias';' { errores.add(new Error("ERROR", "Se esperaba el nombre de la variable para iterar. ", AnalizadorLexico.cantLineas)); $$=new NodoArbol("ERROR SINTACTICO", null, null);} 
				  |FOREACH ID ID bloque_de_sentencias';' { errores.add(new Error("ERROR", "Se esperaba 'in' y se encontró el nombre de la coleccion. ", AnalizadorLexico.cantLineas));  $$=new NodoArbol("ERROR SINTACTICO", null, null);} 
				  |FOREACH ID IN bloque_de_sentencias';' { errores.add(new Error("ERROR", "Se esperaba el nombre de la coleccion y se encontraron sentencias. ", AnalizadorLexico.cantLineas)); $$=new NodoArbol("ERROR SINTACTICO", null, null);} 
;


sentencia_print :PRINT '(' CADENA ')' ';' { 
											String lexema = "cadena"+contadorDeCadenas;
											contadorDeCadenas++;
											Token t = AnalizadorLexico.tablaSimbolos.get(lexema);

											agregarUsoTS(lexema, Token.USO_CADENA);
											NodoArbol nodo_cadena = new NodoArbol(lexema, null, null);
											$$ = new NodoArbol("PRINT", nodo_cadena, null);
				 						  }
 
				|PRINT CADENA ')' ';' { errores.add(new Error("ERROR", "Se esperaba '(' y se encontro una cadena. ", AnalizadorLexico.cantLineas)); $$=new NodoArbol("ERROR SINTACTICO", null, null);} 
				|PRINT '(' CADENA ';' { errores.add(new Error("ERROR", "Se esperaba ')' y se encontro ';'. ", AnalizadorLexico.cantLineas)); $$=new NodoArbol("ERROR SINTACTICO", null, null);} 


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
							aux.setTipoDeDato((NodoArbol)$1, (NodoArbol)$3); //chequear que los tipos sean iguales
							$$ = aux;
						  }

		|factor { $$ = $1;
		        }
;		

factor :ID { estaDeclarada($1.sval);

			 Token id = AnalizadorLexico.tablaSimbolos.get($1.sval);
			 checkearUsoCorrecto(id.getUso(), Token.USO_VARIABLE);

			 estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor ID. " + "\n"); 
			 NodoArbol aux = new NodoArbol($1.sval, null, null);
			 aux.setTipoDeDato(id.getTipoDeDato());
			 $$ = aux;
		   }

	   |CTE { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE. " + "\n"); 
	   		  NodoArbol aux = new NodoArbol($1.sval, null, null);
			  aux.setTipoDeDato(AnalizadorLexico.tablaSimbolos.get($1.sval).getTipoDeDato());
			  $$ = aux;
			}

	   |'-'CTE {  estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE Negativa. " + "\n");
	   			  String lexema = modificarContadorDeReferencias($2.sval);

	   			  Token t = AnalizadorLexico.tablaSimbolos.get(lexema);
	   			  t.setUso(Token.USO_CONSTANTE);
	   			  NodoArbol aux = new NodoArbol(lexema, null, null);
			      aux.setTipoDeDato(AnalizadorLexico.tablaSimbolos.get(lexema).getTipoDeDato());
			      $$ = aux;

	   			}

	   |ID'['subindice']' { estaDeclarada($1.sval);

	   						Token id = AnalizadorLexico.tablaSimbolos.get($1.sval);
	   						checkearUsoCorrecto(id.getUso(), Token.USO_COLECCION);

	   						estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor coleccion. " + "\n");
	   						NodoArbol nodo_id = new NodoArbol($1.sval, null, null);
							nodo_id.setTipoDeDato(id.getTipoDeDato());
		   					

	   						NodoArbol aux = new NodoArbol("ELEM_COLEC", nodo_id, $3); 
	   						aux.setTipoDeDato(nodo_id.getTipoDeDato()); 
	   						$$ = aux;
						  }

	   |TO_ULONG'('expresion')' { estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor conversion. " + "\n"); 

	   							  NodoArbol aux = new NodoArbol("CONVERSION", $3, null);
	   							  aux.setTipoDeDato(AnalizadorLexico.TIPO_DATO_ULONG);
	   							  $$ = aux;
								}

	   |TO_ULONG expresion')' { errores.add(new Error("ERROR", "Se esperaba '(' para realizar la conversion. ", AnalizadorLexico.cantLineas)); $$=new NodoArbol("ERROR SINTACTICO", null, null);}
;	   

subindice :ID {	String tipoDeDato = Token.UNDEFINED;
				if (estaDeclarada($1.sval)){

					Token id = AnalizadorLexico.tablaSimbolos.get($1.sval);
					tipoDeDato = id.getTipoDeDato();

					if(!tipoDeDato.equals(AnalizadorLexico.TIPO_DATO_ENTERO)){
		  				errores.add(new Error("ERROR", "El tipo de dato del subindice debe ser entero y no "+tipoDeDato, AnalizadorLexico.cantLineas));
		  		 	}

		  		 	checkearUsoCorrecto(id.getUso(), Token.USO_VARIABLE);
				}
				
				NodoArbol aux = new NodoArbol($1.sval, null, null);
				aux.setTipoDeDato(tipoDeDato);
				$$ = aux;

			  }
		  |CTE {
		  		Token cons = AnalizadorLexico.tablaSimbolos.get($1.sval);
		  		String tipoDeDato = cons.getTipoDeDato();

		  		if(!tipoDeDato.equals(AnalizadorLexico.TIPO_DATO_ENTERO)){
		  			errores.add(new Error("ERROR", "El tipo de dato del subindice debe ser entero y no "+tipoDeDato, AnalizadorLexico.cantLineas));
		  		 }

		  		 NodoArbol aux = new NodoArbol($1.sval, null, null);
				 aux.setTipoDeDato(tipoDeDato);
				 $$ = aux;

			   }
;		  

asignacion :ID ASIGN expresion ';' {  estaDeclarada($1.sval);

									Token id = AnalizadorLexico.tablaSimbolos.get($1.sval);
									
									checkearUsoCorrecto(id.getUso(), Token.USO_VARIABLE);

									NodoArbol nodo_id = new NodoArbol($1.sval, null, null);
									nodo_id.setTipoDeDato(id.getTipoDeDato());

									NodoArbol aux = new NodoArbol(":=", nodo_id , $3);
									aux.setTipoDeDato(nodo_id.getTipoDeDato(), ((NodoArbol)$3).getTipoDeDato()); 	
									$$ = aux;
			                       }
			                       					
		   |ID'['subindice']'ASIGN expresion ';' {  estaDeclarada($1.sval);

		   											Token id = AnalizadorLexico.tablaSimbolos.get($1.sval); 

													checkearUsoCorrecto(id.getUso(), Token.USO_COLECCION);

		   										    NodoArbol nodo_id = new NodoArbol($1.sval, null, null);
													nodo_id.setTipoDeDato(id.getTipoDeDato());

	   												NodoArbol nodo_colec = new NodoArbol("ELEM_COLEC", nodo_id, $3); 
	   												nodo_colec.setTipoDeDato(nodo_id.getTipoDeDato()); 

		   											NodoArbol aux = new NodoArbol(":=", nodo_colec, $6);
		   											aux.setTipoDeDato(nodo_colec.getTipoDeDato(), ((NodoArbol)$6).getTipoDeDato());
		   											$$ = aux;

		                                         }
			
		   |ID ASIGN ';' { errores.add(new Error("ERROR", "Se esperaba una expresion para realizar la asignacion. ", AnalizadorLexico.cantLineas)); $$=new NodoArbol("ERROR SINTACTICO", null, null);}
		   |ID subindice']'ASIGN expresion ';' { errores.add(new Error("ERROR", "Se esperaba '[' para indicar la posicion de la coleccion. ", AnalizadorLexico.cantLineas)); $$=new NodoArbol("ERROR SINTACTICO", null, null);}
		   |ID'['subindice ASIGN expresion ';' { errores.add(new Error("ERROR", "Se esperaba ']' para indicar la posicion de la coleccion. ", AnalizadorLexico.cantLineas)); $$=new NodoArbol("ERROR SINTACTICO", null, null);}
		   |ID'[' ']'ASIGN expresion ';' { errores.add(new Error("ERROR", "Se esperaba un subindice para realizar la asignacion. ", AnalizadorLexico.cantLineas)); $$=new NodoArbol("ERROR SINTACTICO", null, null);}
;

%%

public AnalizadorLexico aLexico;
public static List<Error> errores;
public static List<String> estructuras;
public NodoArbol raiz;
public StringBuilder arbolString = new StringBuilder();
private int contadorDeCadenas=0;
private int contadorDeForeach=0;
private int contadorDeIf=0;

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


public String modificarContadorDeReferencias(String lexema){
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
			errores.add(new Error("ERROR", "Constante negativa fuera de rango. Fue reemplazado por el valor limite permitido del rango", AnalizadorLexico.cantLineas));	
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
		return negativo;
	}


public Parser (List<Error> erroresL, File file ) throws FileNotFoundException{
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
	Error error;
		
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


public void agregarTipoTS (String tipo, Object listaVars){
	Token token;
	String variable;
	ArrayList<String> listaVariables = (ArrayList<String>) listaVars;

	for (int i=0; i<listaVariables.size(); i++){
		variable = listaVariables.get(i);
		token = AnalizadorLexico.tablaSimbolos.get(variable);

		if (token.getTipoDeDato() == Token.UNDEFINED) //si el tipo no esta definido
			token.setTipoDeDato(tipo);
		else
			errores.add(new Error("ERROR", "Redeclaracion de la variable " + token.getLexema(), AnalizadorLexico.cantLineas));
	}
}

public void agregarUsoTS (String variable , String uso){
	AnalizadorLexico.tablaSimbolos.get(variable).setUso(uso);
}

public void agregarTamanio (String variable , String tamanio){
	AnalizadorLexico.tablaSimbolos.get(variable).setTamanio(Integer.parseInt(tamanio));
}


public void inferirTamanio (String coleccion , Object listaValoresIniciales){ //aca tambien seteamos los valores iniciales
		ArrayList<String> listaVal = (ArrayList<String>) listaValoresIniciales;
		Token t = AnalizadorLexico.tablaSimbolos.get(coleccion);
		t.setTamanio(listaVal.size());
		t.setValoresIniciales(listaVal);
}

public boolean estaDeclarada(String lexema){

	if (AnalizadorLexico.tablaSimbolos.get(lexema).getTipoDeDato() != Token.UNDEFINED){
		return true;
	} else{
		errores.add(new Error("ERROR", "Variable " + lexema + " no declarada" , AnalizadorLexico.cantLineas));		
		return false;
	}

}


public void imprimirArbol(NodoArbol nodo, String tabs) {
    	 
	arbolString.append(tabs + nodo.getNombre() + "\n");  //raiz
	
	if(nodo.getNodoIzq()!=null)
		imprimirArbol(nodo.getNodoIzq(), tabs + "\t");
        
    if(nodo.getNodoDer()!=null)
        imprimirArbol(nodo.getNodoDer(), tabs + "\t");
 
}

public void checkearUsoCorrecto(String lexema, String uso){

	Token id = AnalizadorLexico.tablaSimbolos.get(lexema);
		if(!lexema.equals(uso)){
			errores.add(new Error("ERROR", lexema + " es usada como " + uso , AnalizadorLexico.cantLineas));
		}
}
