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
package , imports de parser ...
//#line 19 "Parser.java"




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
    0,    0,    1,    1,    1,    3,    3,    4,    4,    8,
    5,    6,    7,    7,    7,    7,    2,    9,    9,   11,
   11,   10,   10,   10,   10,   12,   12,   16,   16,   16,
   16,   18,   18,   18,   18,   18,   18,   13,   14,   17,
   17,   17,   20,   20,   20,   21,   21,   21,   22,   22,
   15,   15,   19,
};
final static short yylen[] = {                            2,
    1,    1,    3,    6,    6,    1,    1,    1,    3,    1,
    1,    1,    1,    1,    3,    3,    3,    1,    3,    1,
    2,    1,    1,    1,    1,    6,    8,    3,    9,    6,
    6,    1,    1,    1,    1,    1,    1,    6,    7,    3,
    3,    1,    3,    3,    1,    1,    1,    4,    1,    1,
    4,    7,    1,
};
final static short yydefred[] = {                         0,
    6,    0,    7,    0,    1,    2,    0,   10,    0,    0,
    0,    0,    0,    0,   18,   22,   23,   24,   25,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   17,
    3,    0,    0,   47,   53,    0,   46,    0,    0,    0,
    0,    0,    0,   21,   19,    0,    0,    0,    0,    0,
    0,    0,    9,    0,    0,   32,   33,   34,   35,   36,
   37,    0,    0,    0,    0,    0,    0,    0,   11,    0,
   51,    0,    0,    0,    0,    0,   49,   50,    0,    0,
   28,    0,    0,   40,   41,   43,   44,    0,    0,    0,
    0,   15,   16,    4,    5,   48,    0,   26,    0,    0,
    0,   38,    0,    0,    0,    0,   39,   52,   27,   30,
   31,    0,    0,    0,   29,
};
final static short yydgoto[] = {                          4,
    5,    6,    7,   21,   36,   51,   52,   37,   14,   15,
   27,   16,   17,   18,   19,   38,   39,   62,   40,   41,
   42,   79,
};
final static short yysindex[] = {                      -203,
    0, -224,    0,    0,    0,    0, -241,    0,    5,   12,
 -200, -188, -196, -194,    0,    0,    0,    0,    0,    0,
   22,   -9,   39, -220,  -39, -200, -186, -187, -220,    0,
    0,  -87, -188,    0,    0,   -4,    0,   47,  -49,   49,
  -23,   11, -169,    0,    0, -165,   35,   55,   52,   54,
    6,    7,    0, -218, -224,    0,    0,    0,    0,    0,
    0, -220, -215, -215, -215, -215, -215,  -24,    0, -224,
    0, -215,  -83,  -83,   43,   44,    0,    0,   14, -193,
    0,   65,   67,    0,    0,    0,    0,   68,   51,   71,
   52,    0,    0,    0,    0,    0, -224,    0, -215,  -49,
   56,    0,   57, -161,   72, -220,    0,    0,    0,    0,
    0,   74, -215,   76,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -41,
    0,    0,   59,    0,    0, -152,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -31,  -36,    0,    0,    0,    0,    0,    0,   28,   29,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   30,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,   91,    8,    0,    2,   23,  -38,   25,
   99,    0,    0,    0,    0,    0,   -2,   26,  -15,   13,
    0,    0,
};
final static int YYTABLESIZE=233;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         10,
   10,   10,   10,   10,   45,   10,   45,   50,   45,   42,
   60,   50,   61,   48,   22,   20,   80,   10,   10,   64,
   10,   65,   45,   45,   13,   45,   47,   42,   42,   23,
   42,   89,    8,   13,   28,   26,   20,   34,   77,   78,
    9,   20,   34,   10,   24,   11,   82,   12,   13,   11,
   26,   25,   66,   70,   35,   23,    8,   67,  104,   81,
   83,   84,   85,   29,    9,    1,    2,   10,    8,   90,
    3,   12,   97,   98,   92,   93,   30,   13,   86,   87,
   31,   32,   33,   43,   45,   46,   54,   55,   63,   68,
  112,   69,   13,   71,   72,   73,  105,   74,   75,   76,
   88,   94,   95,  111,   99,  109,   96,  100,  101,  102,
  114,  103,  110,  113,  107,  108,  115,    8,   20,   13,
   12,   14,   13,   53,   44,  106,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   49,    0,    0,    0,   91,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   56,   57,   58,   59,    0,    0,    0,    0,   10,
   10,   10,   10,    0,   45,   45,   45,   45,    0,   42,
   42,   42,   42,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   41,   47,   43,   95,   45,   41,
   60,   95,   62,   29,    7,  257,   55,   59,   60,   43,
   62,   45,   59,   60,    2,   62,   29,   59,   60,    7,
   62,   70,  257,   11,   12,   11,  257,  258,  257,  258,
  265,  257,  258,  268,   40,  270,   62,  272,   26,   91,
   26,   40,   42,   46,  275,   33,  257,   47,   97,   62,
   63,   64,   65,  260,  265,  269,  270,  268,  257,   72,
  274,  272,  266,  267,   73,   74,  271,   55,   66,   67,
   59,   91,   44,  123,  271,  273,   91,   41,   40,  259,
  106,  257,   70,   59,   40,   44,   99,   44,   93,   93,
  125,   59,   59,  106,   40,  267,   93,   41,   41,   59,
  113,   41,   41,   40,   59,   59,   41,   59,  271,   97,
   93,   93,   93,   33,   26,  100,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  258,   -1,   -1,   -1,  258,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  261,  262,  263,  264,   -1,   -1,   -1,   -1,  261,
  262,  263,  264,   -1,  261,  262,  263,  264,   -1,  261,
  262,  263,  264,
};
}
final static short YYFINAL=4;
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
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"ID","CTE","CADENA","ASIGN",
"MAYORIGUAL","MENORIGUAL","IGUALIGUAL","DISTINTO","IF","ELSE","END_IF","PRINT",
"INT","BEGIN","END","FOREACH","IN","ULONG","TO_ULONG",
};
final static String yyrule[] = {
"$accept : programa",
"programa : sentencias_declarativas",
"programa : sentencias_ejecutables",
"sentencias_declarativas : tipo lista_de_variables ';'",
"sentencias_declarativas : tipo nombre_coleccion '[' tamanio ']' ';'",
"sentencias_declarativas : tipo nombre_coleccion '[' lista_de_valores_iniciales ']' ';'",
"tipo : INT",
"tipo : ULONG",
"lista_de_variables : nombre_variable",
"lista_de_variables : nombre_variable ',' lista_de_variables",
"nombre_variable : ID",
"nombre_coleccion : ID",
"tamanio : CTE",
"lista_de_valores_iniciales : CTE",
"lista_de_valores_iniciales : '_'",
"lista_de_valores_iniciales : CTE ',' lista_de_valores_iniciales",
"lista_de_valores_iniciales : '_' ',' lista_de_valores_iniciales",
"sentencias_ejecutables : BEGIN bloque_de_sentencias END",
"bloque_de_sentencias : sentencia_ejecutable",
"bloque_de_sentencias : BEGIN lista_de_sentencias END",
"lista_de_sentencias : sentencia_ejecutable",
"lista_de_sentencias : sentencia_ejecutable lista_de_sentencias",
"sentencia_ejecutable : sentencia_if",
"sentencia_ejecutable : sentencia_foreach",
"sentencia_ejecutable : sentencia_print",
"sentencia_ejecutable : asignacion",
"sentencia_if : IF '(' condicion ')' bloque_de_sentencias END_IF",
"sentencia_if : IF '(' condicion ')' bloque_de_sentencias ELSE bloque_de_sentencias END_IF",
"condicion : expresion comparador expresion",
"condicion : conversion '(' expresion ')' comparador conversion '(' expresion ')'",
"condicion : expresion comparador conversion '(' expresion ')'",
"condicion : conversion '(' expresion ')' comparador expresion",
"comparador : MAYORIGUAL",
"comparador : MENORIGUAL",
"comparador : IGUALIGUAL",
"comparador : DISTINTO",
"comparador : '<'",
"comparador : '>'",
"sentencia_foreach : FOREACH nombre_variable IN nombre_coleccion bloque_de_sentencias ';'",
"sentencia_print : PRINT '(' '{' CADENA '}' ')' ';'",
"expresion : termino '+' expresion",
"expresion : termino '-' expresion",
"expresion : termino",
"termino : factor '*' termino",
"termino : factor '/' termino",
"termino : factor",
"factor : nombre_variable",
"factor : CTE",
"factor : nombre_coleccion '[' subindice ']'",
"subindice : ID",
"subindice : CTE",
"asignacion : nombre_variable ASIGN expresion ';'",
"asignacion : nombre_variable ASIGN conversion '(' expresion ')' ';'",
"conversion : TO_ULONG",
};

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
