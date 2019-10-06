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
    0,    1,    1,    3,    3,    4,    4,    5,    5,    5,
    5,    5,    5,    6,    6,    6,    6,    7,    7,    2,
    2,    2,    8,    8,   10,   10,   10,   10,    9,    9,
    9,    9,    9,   11,   11,   15,   15,   15,   17,   17,
   17,   17,   17,   17,   17,   12,   12,   12,   12,   13,
   13,   13,   16,   16,   16,   18,   18,   18,   19,   19,
   19,   19,   19,   19,   20,   20,   14,   14,   14,   14,
   14,   14,
};
final static short yylen[] = {                            2,
    2,    1,    2,    3,    2,    1,    1,    1,    4,    4,
    3,    3,    3,    3,    3,    3,    3,    1,    1,    3,
    2,    2,    1,    2,    1,    3,    2,    2,    1,    1,
    1,    1,    2,    6,    8,    3,    2,    2,    1,    1,
    1,    1,    1,    1,    2,    6,    5,    5,    5,    5,
    4,    4,    3,    3,    1,    3,    3,    1,    1,    1,
    2,    4,    4,    3,    1,    1,    4,    7,    3,    6,
    6,    6,
};
final static short yydefred[] = {                         0,
    6,    7,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,    0,    0,   29,   30,   31,   32,    3,
    0,    0,   33,   65,   66,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   21,   24,    0,    0,    0,    0,
    0,    4,    0,   60,    0,    0,   69,    0,    0,    0,
    0,    0,    0,    0,   39,   40,   41,   42,   43,   44,
    0,    0,    0,    0,    0,   20,    0,    0,    0,    0,
    0,    0,   11,    0,   12,    0,    0,    0,   61,   67,
    0,    0,    0,    0,    0,    0,    0,    0,   45,    0,
    0,   37,   51,   52,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   16,   14,   10,    9,   17,   15,
    0,    0,   64,   53,   54,   56,   57,    0,    0,    0,
    0,    0,   36,   50,    0,   27,   48,    0,    0,   49,
   47,   62,   63,   72,   71,    0,   70,    0,   34,   26,
   46,   68,    0,   35,
};
final static short yydgoto[] = {                          3,
    4,   13,    5,    6,   22,   41,  106,   97,   98,   99,
   16,   17,   18,   19,   61,   48,   63,   49,   50,   28,
};
final static short yysindex[] = {                      -197,
    0,    0,    0, -181, -197, -245,    6,  -75,  -10,  -31,
   54, -223,    0, -198,   54,    0,    0,    0,    0,    0,
  -29,   20,    0,    0,    0,  -34,  -79,   -5,  -42,   55,
 -162, -172, -204, -156,    0,    0,   58,  -71, -245,   66,
   19,    0,   22,    0,  -40, -142,    0,   60,   18,   38,
 -139,  -85, -137,   68,    0,    0,    0,    0,    0,    0,
   84,  -56,  -32,   71,    8,    0, -150, -118, -150,  -69,
   -3,   40,    0,  -69,    0, -187,  -32,   90,    0,    0,
  -32,  -32,  -32,  -32,  -32,  -32, -125,  -32,    0, -150,
  -32,    0,    0,    0,   77,   54, -126,   54,   92,  -84,
   94,   96,   58,   66,    0,    0,    0,    0,    0,    0,
   63,  117,    0,    0,    0,    0,    0,  100,  102,  -32,
  104, -185,    0,    0, -106,    0,    0,  -75,  107,    0,
    0,    0,    0,    0,    0,  108,    0, -150,    0,    0,
    0,    0,  -99,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0, -108,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    1,    0,    0,    0,    0,    0,
   34, -128,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  169,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -14,    0,    0,    0,    0,    0,   -4,  -24,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   44,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  129,    0,    0,    0,    0,    0,    0,   -2,    0,    0,
    0,    0,    5,   24,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -57,    0,    0,   78,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  172,    0,    0,    0,  141,    4,  116,   28,   36,  -46,
    0,    0,    0,    0,    0,   23,  130,   11,    0,  -17,
};
final static int YYTABLESIZE=326;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         77,
   23,   28,   46,   59,   46,   60,   27,   87,   31,   52,
   46,   21,   46,   51,   39,   27,   58,   59,   58,   60,
   58,  101,  102,   40,   47,  104,   59,   59,   59,   29,
   59,   14,   59,   33,   58,   58,   55,   58,   32,   15,
   70,   72,   36,  122,   59,   59,   15,   59,   95,   34,
   15,   62,   67,  129,   55,   55,   25,   55,  111,   23,
   81,   38,   82,   18,   23,   40,   94,   78,   68,   24,
   25,    1,   35,  105,    7,    8,    2,  109,   42,   83,
  138,  139,   19,    9,   84,   92,   10,   53,   11,  107,
   12,  143,    8,  116,  117,   64,   65,   18,   66,  112,
   69,   70,   13,  114,  115,    7,    8,  118,  119,   74,
  121,   75,   76,  123,    9,   79,   19,   10,   80,   96,
   85,   12,   88,  125,   90,   36,   89,    5,    5,   93,
  113,   15,  108,   15,  120,  124,    5,    7,  100,    5,
    5,    5,  136,    5,  126,    5,    9,    2,    2,   10,
  127,   96,  130,   12,  131,  132,    2,  133,  134,    2,
  135,    2,  137,    2,  140,  141,  142,  144,   22,   38,
   65,    7,  128,   25,   86,   26,   20,   24,   25,   73,
    9,   24,   25,   10,   26,   96,   71,   12,  103,  110,
    0,   91,    0,    0,    0,    0,    0,    0,    0,   54,
    0,    0,    0,    0,   55,   56,   57,   58,   28,   28,
    0,    0,    0,   54,   43,   44,   43,   44,   55,   56,
   57,   58,   43,   44,   43,   44,    0,   30,   37,    0,
    0,   58,   45,    0,   45,    0,   58,   58,   58,   58,
   45,   59,   45,    0,    0,    0,   59,   59,   59,   59,
    0,   55,    0,    0,    0,    0,   55,   55,   55,   55,
   18,   18,    0,   25,   25,    0,   23,   23,   23,   18,
    0,   23,   18,   18,   18,    0,   18,    0,   18,   19,
   19,    0,    0,    0,    0,    0,    0,    0,   19,    8,
    8,   19,   19,   19,    0,   19,    0,   19,    8,   13,
   13,    8,    8,    8,    0,    8,    0,    8,   13,    7,
    8,   13,   13,   13,    0,   13,    0,   13,    9,    0,
    0,   10,    0,    0,    0,   12,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   59,   45,   60,   45,   62,   91,   93,   40,   27,
   45,  257,   45,   93,   44,   91,   41,   60,   43,   62,
   45,   68,   69,   95,   59,   95,   41,   42,   43,   40,
   45,    4,   47,  257,   59,   60,   41,   62,   11,    4,
   44,   38,   15,   90,   59,   60,   11,   62,   41,  273,
   15,   29,  257,  100,   59,   60,   59,   62,   76,   59,
   43,   91,   45,   59,   59,   95,   59,   45,  273,  257,
  258,  269,  271,   70,  256,  257,  274,   74,   59,   42,
  266,  267,   59,  265,   47,   63,  268,   93,  270,   93,
  272,  138,   59,   83,   84,   41,  259,   93,  271,   77,
  257,   44,   59,   81,   82,  256,  257,   85,   86,   44,
   88,   93,   91,   91,  265,  258,   93,  268,   59,  270,
  260,  272,  260,   96,   41,   98,   59,  256,  257,   59,
   41,   96,   93,   98,  260,   59,  265,  256,  257,  268,
  269,  270,  120,  272,  271,  274,  265,  256,  257,  268,
   59,  270,   59,  272,   59,   93,  265,   41,   59,  268,
   59,  270,   59,  272,  271,   59,   59,  267,    0,   41,
   93,  256,  257,  258,  260,  260,    5,  257,  258,   39,
  265,  257,  258,  268,  260,  270,  258,  272,  258,   74,
   -1,   62,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,
   -1,   -1,   -1,   -1,  261,  262,  263,  264,  266,  267,
   -1,   -1,   -1,  256,  257,  258,  257,  258,  261,  262,
  263,  264,  257,  258,  257,  258,   -1,  259,  258,   -1,
   -1,  256,  275,   -1,  275,   -1,  261,  262,  263,  264,
  275,  256,  275,   -1,   -1,   -1,  261,  262,  263,  264,
   -1,  256,   -1,   -1,   -1,   -1,  261,  262,  263,  264,
  256,  257,   -1,  266,  267,   -1,  266,  267,  271,  265,
   -1,  271,  268,  269,  270,   -1,  272,   -1,  274,  256,
  257,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  265,  256,
  257,  268,  269,  270,   -1,  272,   -1,  274,  265,  256,
  257,  268,  269,  270,   -1,  272,   -1,  274,  265,  256,
  257,  268,  269,  270,   -1,  272,   -1,  274,  265,   -1,
   -1,  268,   -1,   -1,   -1,  272,
};
}
final static short YYFINAL=3;
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
"sentencias_declarativas : sentencia_declarativa",
"sentencias_declarativas : sentencia_declarativa sentencias_declarativas",
"sentencia_declarativa : tipo lista_de_variables ';'",
"sentencia_declarativa : tipo lista_de_variables",
"tipo : INT",
"tipo : ULONG",
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
"sentencia_ejecutable : error ';'",
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

//#line 172 "gramatica.y"

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

//#line 467 "Parser.java"
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
case 5:
//#line 33 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Declaracion incorrecta. Se esperaba ';' ", AnalizadorLexico.cantLineas)); }
break;
case 8:
//#line 41 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion de variable" + "\n"); }
break;
case 9:
//#line 42 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion de coleccion" + "\n"); }
break;
case 10:
//#line 43 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion de coleccion" + "\n"); }
break;
case 12:
//#line 46 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '[' para definir los valores iniciales de la coleccion ", AnalizadorLexico.cantLineas)); }
break;
case 13:
//#line 47 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ']' para definir los valores iniciales de la coleccion ", AnalizadorLexico.cantLineas)); }
break;
case 21:
//#line 66 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'BEGIN' al comienzo ", AnalizadorLexico.cantLineas)); }
break;
case 22:
//#line 67 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'END' al final ", AnalizadorLexico.cantLineas)); }
break;
case 27:
//#line 78 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'BEGIN' al comienzo ", AnalizadorLexico.cantLineas));  }
break;
case 28:
//#line 79 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'END' al final ", AnalizadorLexico.cantLineas));  }
break;
case 29:
//#line 82 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia if " + "\n");}
break;
case 30:
//#line 83 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia foreach " + "\n");}
break;
case 31:
//#line 84 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia print " + "\n");}
break;
case 32:
//#line 85 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Asignacion " + "\n");}
break;
case 33:
//#line 86 "gramatica.y"
{ Parser.errores.add(new analizadorLexico.Error("ERROR", "Sentencia ejecutable invalida ", AnalizadorLexico.cantLineas)); }
break;
case 36:
//#line 104 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Condicion " + "\n"); }
break;
case 37:
//#line 106 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion del lado derecho para comparar ", AnalizadorLexico.cantLineas)); }
break;
case 38:
//#line 107 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion del lado izquierdo para comparar ", AnalizadorLexico.cantLineas)); }
break;
case 45:
//#line 116 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba un comparador valido ", AnalizadorLexico.cantLineas)); }
break;
case 47:
//#line 121 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba el nombre de la variable para iterar ", AnalizadorLexico.cantLineas)); }
break;
case 48:
//#line 122 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'in' y se encontró el nombre de la coleccion ", AnalizadorLexico.cantLineas));  }
break;
case 49:
//#line 123 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba el nombre de la coleccion y se encontraron sentencias ", AnalizadorLexico.cantLineas));  }
break;
case 51:
//#line 130 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '(' y se encontro una cadena ", AnalizadorLexico.cantLineas));  }
break;
case 52:
//#line 131 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ')' y se encontro ';' ", AnalizadorLexico.cantLineas));  }
break;
case 53:
//#line 134 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Suma " + "\n"); }
break;
case 54:
//#line 135 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Resta " + "\n"); }
break;
case 56:
//#line 139 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Multiplicacion " + "\n"); }
break;
case 57:
//#line 140 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Division " + "\n"); }
break;
case 59:
//#line 144 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor ID " + "\n"); }
break;
case 60:
//#line 145 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE " + "\n"); }
break;
case 61:
//#line 146 "gramatica.y"
{  estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE negativa " + "\n");
	   			  modificarContadorDeReferencias(val_peek(0).sval);
	   					 }
break;
case 62:
//#line 150 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor coleccion " + "\n"); }
break;
case 63:
//#line 151 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor conversion " + "\n"); }
break;
case 64:
//#line 153 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '(' para realizar la conversion ", AnalizadorLexico.cantLineas)); }
break;
case 69:
//#line 165 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion para realizar la asignacion ", AnalizadorLexico.cantLineas));  }
break;
case 70:
//#line 166 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '[' para indicar la posicion de la coleccion ", AnalizadorLexico.cantLineas));  }
break;
case 71:
//#line 167 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ']' para indicar la posicion de la coleccion ", AnalizadorLexico.cantLineas));  }
break;
case 72:
//#line 168 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba un subindice para realizar la asignacion ", AnalizadorLexico.cantLineas));  }
break;
//#line 774 "Parser.java"
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
