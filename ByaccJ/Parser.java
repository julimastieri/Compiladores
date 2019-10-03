//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
package analizadorSintactico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.Token;

//#line 29 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CTE=258;
public final static short CADENA=259;
public final static short ASIGN=260;
public final static short MAYORIGUAL=261;
public final static short MENORIGUAL=262;
public final static short IGUALIGUAL=263;
public final static short DISTINTO=264;
public final static short IF=265;
public final static short ELSE=266;
public final static short END_IF=267;
public final static short PRINT=268;
public final static short INT=269;
public final static short BEGIN=270;
public final static short END=271;
public final static short FOREACH=272;
public final static short IN=273;
public final static short ULONG=274;
public final static short TO_ULONG=275;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    3,    3,    3,    4,    4,    4,
    4,    4,    4,    5,    5,    5,    5,    5,    6,    6,
    2,    2,    2,    7,    7,    9,    9,    9,    9,    8,
    8,    8,    8,   10,   10,   14,   14,   14,   16,   16,
   16,   16,   16,   16,   16,   11,   11,   11,   11,   12,
   12,   12,   15,   15,   15,   17,   17,   17,   18,   18,
   18,   18,   18,   18,   19,   19,   13,   13,   13,   13,
   13,   13,
};
final static short yylen[] = {                            2,
    2,    3,    2,    2,    1,    1,    2,    1,    4,    4,
    3,    3,    3,    3,    3,    3,    3,    2,    1,    1,
    3,    2,    2,    1,    2,    1,    3,    2,    2,    1,
    1,    1,    1,    6,    8,    3,    2,    2,    1,    1,
    1,    1,    1,    1,    2,    6,    5,    5,    5,    5,
    4,    4,    3,    3,    1,    3,    3,    1,    1,    1,
    2,    4,    4,    3,    1,    1,    4,    7,    3,    6,
    6,    6,
};
final static short yydefred[] = {                         0,
    0,    0,    5,    6,    0,    0,    0,    0,    7,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    1,    0,    0,   30,   31,   32,   33,    0,    3,   18,
    0,    0,    0,   11,    0,   12,   65,   66,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   22,   25,    2,
    0,    0,   16,   14,   10,    9,   17,   15,    0,   60,
    0,    0,   69,    0,    0,    0,    0,    0,    0,    0,
   39,   40,   41,   42,   43,   44,    0,    0,    0,    0,
    0,   21,    0,    0,    0,    0,    0,    0,   61,   67,
    0,    0,    0,    0,    0,    0,    0,    0,   45,    0,
    0,   37,   51,   52,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   64,   53,   54,   56,   57,    0,
    0,    0,    0,    0,   36,   50,    0,   28,   48,    0,
    0,   49,   47,   62,   63,   72,   71,    0,   70,    0,
   34,   27,   46,   68,    0,   35,
};
final static short yydgoto[] = {                          5,
    6,   21,    7,    8,   15,   54,  107,  108,  109,   24,
   25,   26,   27,   77,   64,   79,   65,   66,   41,
};
final static short yysindex[] = {                      -215,
  -36,  -29,    0,    0,    0, -165, -225,  -10,    0,   -6,
   32,  -69, -225,   40,   -7,  -75,   56,  -31, -137, -205,
    0, -150, -137,    0,    0,    0,    0,   47,    0,    0,
  -65,   -5,   15,    0,  -65,    0,    0,    0,  -34,  -79,
   45,  -42,   84, -122, -132, -182, -117,    0,    0,    0,
   32,   40,    0,    0,    0,    0,    0,    0,   50,    0,
  -40, -116,    0,   85,  -33,   76, -115,  -85, -114,   88,
    0,    0,    0,    0,    0,    0,  102,  -56,  -32,   89,
  -19,    0, -156, -155, -156, -163,  -32,  108,    0,    0,
  -32,  -32,  -32,  -32,  -32,  -32, -109,  -32,    0, -156,
  -32,    0,    0,    0,   94, -137, -113, -137,   95,  -84,
   96,   97,   64,  118,    0,    0,    0,    0,    0,  101,
  103,  -32,  104, -134,    0,    0, -110,    0,    0,  -75,
  105,    0,    0,    0,    0,    0,    0,  106,    0, -156,
    0,    0,    0,    0, -101,    0,
};
final static short yyrindex[] = {                         0,
    0,   22,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,    0,    0,    0,    0, -146,    0,    0,
    0,    0,   23,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  167,    0,    0,    0,    0,    0,
    5,    6,    0,    0,    0,    0,    0,    0,  -14,    0,
    0,    0,    0,    0,   -4,  -24,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  127,    0,    0,    0,    0,    0,    0,   -2,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -57,    0,    0,   77,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,   72,   58,  134,   28,   44,  -13,    0,
    0,    0,    0,    0,  -18,   93,   36,    0,    4,
};
final static int YYTABLESIZE=295;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         87,
   24,   29,   62,   75,   62,   76,   40,   97,   44,   91,
   62,   92,   62,   67,   13,   40,   58,   75,   58,   76,
   58,  105,    9,   78,   63,   14,   59,   59,   59,   52,
   59,    2,   59,   22,   58,   58,   55,   58,   31,  104,
    1,    2,   88,   68,   59,   59,   45,   59,   29,   23,
   49,   46,   30,    3,   55,   55,   26,   55,    4,   24,
  102,   12,   23,   19,   20,   14,   23,   47,  114,   33,
  111,  112,  116,  117,   83,   31,  120,  121,   28,  123,
    8,   13,  125,   35,   34,   36,  124,   55,   53,  113,
   84,   16,   57,   37,   38,   42,  131,   19,   20,   17,
   16,  110,   18,  138,   19,   50,   20,   56,   17,   17,
    4,   18,   18,  106,  106,   20,   20,   93,    4,   16,
   48,    4,   94,    4,   80,    4,  145,   17,  118,  119,
   18,  140,  141,  127,   20,   49,   81,   69,   82,   85,
   86,   89,  100,   90,   95,   98,   99,  103,  115,   23,
  122,   23,  126,  129,  132,  133,  134,  128,  135,  136,
  142,  137,  139,  143,  144,  146,   23,   38,   58,   65,
  101,    0,  130,   38,   96,   39,    0,   37,   38,    0,
   17,   37,   38,   18,   39,  106,   10,   20,   32,    0,
   10,    0,   51,    0,    0,    0,    0,    0,    0,   70,
    0,    0,    0,    0,   71,   72,   73,   74,   29,   29,
    0,    0,    0,   70,   59,   60,   59,   60,   71,   72,
   73,   74,   59,   60,   59,   60,   10,   43,   11,    0,
    0,   58,   61,    0,   61,    0,   58,   58,   58,   58,
   61,   59,   61,    0,    0,    0,   59,   59,   59,   59,
    0,   55,    0,    0,    0,    0,   55,   55,   55,   55,
    0,   19,   20,   26,   26,    0,   24,   24,   24,   19,
   20,   24,   19,   20,   19,   20,   19,   20,    8,   13,
    0,    0,    0,    0,    0,    0,    8,   13,    0,    8,
   13,    8,   13,    8,   13,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   59,   45,   60,   45,   62,   91,   93,   40,   43,
   45,   45,   45,   93,   44,   91,   41,   60,   43,   62,
   45,   41,   59,   42,   59,   95,   41,   42,   43,   95,
   45,  257,   47,    6,   59,   60,   41,   62,   44,   59,
  256,  257,   61,   40,   59,   60,   19,   62,   59,    6,
   23,  257,   59,  269,   59,   60,   59,   62,  274,   59,
   79,   91,   19,   59,   59,   95,   23,  273,   87,   12,
   84,   85,   91,   92,  257,   44,   95,   96,    7,   98,
   59,   59,  101,   44,   13,   93,  100,   93,   31,   86,
  273,  257,   35,  257,  258,   40,  110,   93,   93,  265,
  257,  257,  268,  122,  270,   59,  272,   93,  265,  265,
  257,  268,  268,  270,  270,  272,  272,   42,  265,  257,
  271,  268,   47,  270,   41,  272,  140,  265,   93,   94,
  268,  266,  267,  106,  272,  108,  259,   93,  271,  257,
   91,  258,   41,   59,  260,  260,   59,   59,   41,  106,
  260,  108,   59,   59,   59,   59,   93,  271,   41,   59,
  271,   59,   59,   59,   59,  267,    0,   41,   35,   93,
   78,   -1,  257,  258,  260,  260,   -1,  257,  258,   -1,
  265,  257,  258,  268,  260,  270,  256,  272,  258,   -1,
  256,   -1,  258,   -1,   -1,   -1,   -1,   -1,   -1,  256,
   -1,   -1,   -1,   -1,  261,  262,  263,  264,  266,  267,
   -1,   -1,   -1,  256,  257,  258,  257,  258,  261,  262,
  263,  264,  257,  258,  257,  258,  256,  259,  258,   -1,
   -1,  256,  275,   -1,  275,   -1,  261,  262,  263,  264,
  275,  256,  275,   -1,   -1,   -1,  261,  262,  263,  264,
   -1,  256,   -1,   -1,   -1,   -1,  261,  262,  263,  264,
   -1,  257,  257,  266,  267,   -1,  266,  267,  271,  265,
  265,  271,  268,  268,  270,  270,  272,  272,  257,  257,
   -1,   -1,   -1,   -1,   -1,   -1,  265,  265,   -1,  268,
  268,  270,  270,  272,  272,
};
}
final static short YYFINAL=5;
final static short YYMAXTOKEN=275;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'",null,"'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'",null,"'_'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","CTE","CADENA","ASIGN","MAYORIGUAL",
"MENORIGUAL","IGUALIGUAL","DISTINTO","IF","ELSE","END_IF","PRINT","INT","BEGIN",
"END","FOREACH","IN","ULONG","TO_ULONG",
};
final static String yyrule[] = {
"$accept : programa",
"programa : sentencias_declarativas sentencias_ejecutables",
"sentencias_declarativas : tipo lista_de_variables ';'",
"sentencias_declarativas : lista_de_variables ';'",
"sentencias_declarativas : tipo lista_de_variables",
"tipo : INT",
"tipo : ULONG",
"tipo : error ';'",
"lista_de_variables : ID",
"lista_de_variables : ID '[' lista_de_valores_iniciales ']'",
"lista_de_variables : ID '[' CTE ']'",
"lista_de_variables : ID ',' lista_de_variables",
"lista_de_variables : ID lista_de_valores_iniciales ']'",
"lista_de_variables : ID '[' lista_de_valores_iniciales",
"lista_de_valores_iniciales : CTE ',' elem_lista",
"lista_de_valores_iniciales : '_' ',' elem_lista",
"lista_de_valores_iniciales : CTE ',' lista_de_valores_iniciales",
"lista_de_valores_iniciales : '_' ',' lista_de_valores_iniciales",
"lista_de_valores_iniciales : error ';'",
"elem_lista : CTE",
"elem_lista : '_'",
"sentencias_ejecutables : BEGIN lista_de_sentencias END",
"sentencias_ejecutables : lista_de_sentencias END",
"sentencias_ejecutables : BEGIN lista_de_sentencias",
"lista_de_sentencias : sentencia_ejecutable",
"lista_de_sentencias : sentencia_ejecutable lista_de_sentencias",
"bloque_de_sentencias : sentencia_ejecutable",
"bloque_de_sentencias : BEGIN lista_de_sentencias END",
"bloque_de_sentencias : lista_de_sentencias END",
"bloque_de_sentencias : BEGIN lista_de_sentencias",
"sentencia_ejecutable : sentencia_if",
"sentencia_ejecutable : sentencia_foreach",
"sentencia_ejecutable : sentencia_print",
"sentencia_ejecutable : asignacion",
"sentencia_if : IF '(' condicion ')' bloque_de_sentencias END_IF",
"sentencia_if : IF '(' condicion ')' bloque_de_sentencias ELSE bloque_de_sentencias END_IF",
"condicion : expresion comparador expresion",
"condicion : comparador expresion",
"condicion : expresion comparador",
"comparador : MAYORIGUAL",
"comparador : MENORIGUAL",
"comparador : IGUALIGUAL",
"comparador : DISTINTO",
"comparador : '<'",
"comparador : '>'",
"comparador : error ';'",
"sentencia_foreach : FOREACH ID IN ID bloque_de_sentencias ';'",
"sentencia_foreach : FOREACH IN ID bloque_de_sentencias ';'",
"sentencia_foreach : FOREACH ID ID bloque_de_sentencias ';'",
"sentencia_foreach : FOREACH ID IN bloque_de_sentencias ';'",
"sentencia_print : PRINT '(' CADENA ')' ';'",
"sentencia_print : PRINT CADENA ')' ';'",
"sentencia_print : PRINT '(' CADENA ';'",
"expresion : termino '+' expresion",
"expresion : termino '-' expresion",
"expresion : termino",
"termino : factor '*' termino",
"termino : factor '/' termino",
"termino : factor",
"factor : ID",
"factor : CTE",
"factor : '-' CTE",
"factor : ID '[' subindice ']'",
"factor : TO_ULONG '(' expresion ')'",
"factor : TO_ULONG expresion ')'",
"subindice : ID",
"subindice : CTE",
"asignacion : ID ASIGN expresion ';'",
"asignacion : ID '[' subindice ']' ASIGN expresion ';'",
"asignacion : ID ASIGN ';'",
"asignacion : ID subindice ']' ASIGN expresion ';'",
"asignacion : ID '[' subindice ASIGN expresion ';'",
"asignacion : ID '[' ']' ASIGN expresion ';'",
};

//#line 169 "gramatica.y"

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

//#line 457 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 24 "gramatica.y"
{ System.out.println("Reconoce bien el programa"); }
break;
case 3:
//#line 28 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Declaracion incorrecta. Se esperaba el tipo de la variable ", AnalizadorLexico.cantLineas)); }
break;
case 4:
//#line 29 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Declaracion incorrecta. Se esperaba ';' ", AnalizadorLexico.cantLineas)); }
break;
case 7:
//#line 34 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Tipo inexistente ", AnalizadorLexico.cantLineas)); }
break;
case 8:
//#line 37 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion de variable" + "\n"); }
break;
case 9:
//#line 38 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion de coleccion" + "\n"); }
break;
case 10:
//#line 39 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion de coleccion" + "\n"); }
break;
case 12:
//#line 42 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '[' para definir los valores iniciales de la coleccion ", AnalizadorLexico.cantLineas)); }
break;
case 13:
//#line 43 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ']' para definir los valores iniciales de la coleccion ", AnalizadorLexico.cantLineas)); }
break;
case 18:
//#line 50 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Formato de valores iniciales incorrectos ", AnalizadorLexico.cantLineas)); }
break;
case 22:
//#line 62 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'BEGIN' al comienzo ", AnalizadorLexico.cantLineas)); }
break;
case 23:
//#line 63 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'END' al final ", AnalizadorLexico.cantLineas)); }
break;
case 28:
//#line 74 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'BEGIN' al comienzo ", AnalizadorLexico.cantLineas));  }
break;
case 29:
//#line 75 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'END' al final ", AnalizadorLexico.cantLineas));  }
break;
case 30:
//#line 78 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia if " + "\n");}
break;
case 31:
//#line 79 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia foreach " + "\n");}
break;
case 32:
//#line 80 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia print " + "\n");}
break;
case 33:
//#line 81 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Asignacion " + "\n");}
break;
case 36:
//#line 101 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Condicion " + "\n"); }
break;
case 37:
//#line 103 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion del lado derecho para comparar ", AnalizadorLexico.cantLineas)); }
break;
case 38:
//#line 104 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion del lado izquierdo para comparar ", AnalizadorLexico.cantLineas)); }
break;
case 45:
//#line 113 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba un comparador valido ", AnalizadorLexico.cantLineas)); }
break;
case 47:
//#line 118 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba el nombre de la variable para iterar ", AnalizadorLexico.cantLineas)); }
break;
case 48:
//#line 119 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'in' y se encontró el nombre de la coleccion ", AnalizadorLexico.cantLineas));  }
break;
case 49:
//#line 120 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba el nombre de la coleccion y se encontraron sentencias ", AnalizadorLexico.cantLineas));  }
break;
case 51:
//#line 127 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '(' y se encontro una cadena ", AnalizadorLexico.cantLineas));  }
break;
case 52:
//#line 128 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ')' y se encontro ';' ", AnalizadorLexico.cantLineas));  }
break;
case 53:
//#line 131 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Suma " + "\n"); }
break;
case 54:
//#line 132 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Resta " + "\n"); }
break;
case 56:
//#line 136 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Multiplicacion " + "\n"); }
break;
case 57:
//#line 137 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Division " + "\n"); }
break;
case 59:
//#line 141 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor ID " + "\n"); }
break;
case 60:
//#line 142 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE " + "\n"); }
break;
case 61:
//#line 143 "gramatica.y"
{  estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE negativa " + "\n");
	   				modificarContadorDeReferencias(val_peek(0).sval);
	   					 }
break;
case 62:
//#line 147 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor coleccion " + "\n"); }
break;
case 63:
//#line 148 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor conversion " + "\n"); }
break;
case 64:
//#line 150 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '(' para realizar la conversion ", AnalizadorLexico.cantLineas)); }
break;
case 69:
//#line 162 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion para realizar la asignacion ", AnalizadorLexico.cantLineas));  }
break;
case 70:
//#line 163 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '[' para indicar la posicion de la coleccion ", AnalizadorLexico.cantLineas));  }
break;
case 71:
//#line 164 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ']' para indicar la posicion de la coleccion ", AnalizadorLexico.cantLineas));  }
break;
case 72:
//#line 165 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba un subindice para realizar la asignacion ", AnalizadorLexico.cantLineas));  }
break;
//#line 772 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
