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
    0,    1,    1,    1,    1,    2,    2,    4,    4,    5,
    5,    6,    6,    6,    6,    6,    6,    6,    6,    6,
    6,    6,    7,    7,    7,    7,    7,    7,    7,    7,
    8,    8,    3,    3,    3,    3,    3,    9,    9,   11,
   11,   11,   11,   11,   10,   10,   10,   10,   12,   12,
   12,   12,   12,   12,   16,   16,   16,   16,   16,   18,
   18,   18,   18,   18,   18,   13,   13,   13,   13,   14,
   14,   14,   17,   17,   17,   19,   19,   19,   20,   20,
   20,   20,   20,   20,   21,   21,   15,   15,   15,   15,
   15,   15,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    2,    1,    2,    3,    2,    1,
    1,    1,    4,    4,    3,    6,    6,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    2,    2,    2,    2,
    1,    1,    3,    2,    2,    2,    1,    1,    2,    1,
    3,    2,    2,    2,    1,    1,    1,    1,    5,    7,
    6,    4,    6,    4,    5,    4,    4,    4,    2,    1,
    1,    1,    1,    1,    1,    6,    5,    5,    5,    5,
    4,    4,    3,    3,    1,    3,    3,    1,    1,    1,
    2,    4,    4,    3,    1,    1,    4,    7,    3,    6,
    6,    6,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   10,    0,    0,   11,    0,    0,
    1,    0,    4,    0,    0,    0,    0,   45,   46,   47,
   48,    0,    5,   85,   86,    0,    0,    0,    0,    0,
    0,   34,    0,    0,    0,    0,   80,   60,   61,   62,
   63,    0,    0,   59,   64,   65,    0,    0,    0,    0,
    2,    7,    0,    0,   35,   39,    0,    0,    0,    0,
   89,    0,    0,    0,    0,    0,    0,    0,   33,    0,
    0,    0,    0,    0,    0,   81,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    8,   42,    0,
   43,    0,    0,   87,    0,    0,    0,    0,    0,    0,
   54,   71,   72,    0,    0,    0,    0,    0,    0,    0,
   84,   57,    0,   56,   73,   74,   76,   77,    0,   18,
    0,    0,   28,   27,    0,   22,    0,   15,    0,   30,
   29,   20,   41,    0,   52,    0,    0,    0,    0,    0,
   49,   70,   68,    0,    0,   69,   67,   82,   83,   55,
   25,   23,    0,    0,   26,   24,    0,   92,   91,    0,
   90,    0,   53,   66,    0,    0,   51,   88,   50,   17,
   16,
};
final static short yydgoto[] = {                         10,
   11,   12,   13,   14,   15,   54,  123,  124,   58,   59,
   60,   18,   19,   20,   21,   22,   47,   48,   49,   50,
   28,
};
final static short yysindex[] = {                       -27,
  -23,  -75,   -1,  -32,    0,  136, -240,    0,  -38,    0,
    0,  -11,    0, -225, -207, -219,  176,    0,    0,    0,
    0,  141,    0,    0,    0,  -36,  -67,  -37,  141,   25,
 -197,    0, -203, -230, -185,  -12,    0,    0,    0,    0,
    0,  -40, -166,    0,    0,    0,  202,   95,    3,   27,
    0,    0,  -10,   39,    0,    0,  153, -171,  176, -208,
    0,   43, -157,  -91, -155,  -55,   51,  -21,    0,  141,
  158,  141, -174,   95,   70,    0,  -26,   71,   95,   95,
   95,   95,  -30,  -63, -207,   -7,   22,    0,    0, -152,
    0,  141,   61,    0,   95,   95, -137,   95,  141,   66,
    0,    0,    0,   68,   69,   -5,   73,   74,   36,   89,
    0,    0,   93,    0,    0,    0,    0,    0,   -2,    0,
  -85,   -7,    0,    0,   29,    0,   44,    0,  -85,    0,
    0,    0,    0, -131,    0,   79,   80,   95,   83,  -34,
    0,    0,    0,  -75,   90,    0,    0,    0,    0,    0,
    0,    0,  107,  113,    0,    0,  100,    0,    0,  105,
    0,  111,    0,    0, -207, -207,    0,    0,    0,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  166,    0,   91,    0,  171,   40,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  172,    0,    0,  103,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  127,  115,
    0,    0,   21,   77,    0,    0,    0,    0,   42,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -53,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  170,    0,    0,    0,    0,    0,    1,    0,
    0,   11,    0,    0,   31,    0,   47,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   85,    0,    0,    0,    0,    0,    0,
    0,    0,   57,   67,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    0,  165,  168,    0,    0,  -57,   -8,   32,  135,  437,
  -17,    0,    0,    0,    0,  181,  403,  149,   14,    0,
   -9,
};
final static int YYTABLESIZE=541;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         74,
   31,   97,   44,  101,   43,   44,   43,   31,   43,  122,
   32,   66,    9,  121,  112,   27,   34,   64,   43,  104,
   12,   45,   61,   46,  163,   63,   70,  128,    9,  126,
   19,   86,   35,   85,    9,   23,  129,  103,    9,   38,
   31,  121,   71,    5,   87,   79,   21,   80,    8,   53,
   32,   55,  105,  107,  108,   65,   14,   92,   93,   31,
   12,   68,  120,  109,  122,   67,   13,   69,   81,   32,
   19,   72,  121,   82,  134,  127,    9,  130,   73,   12,
   84,  140,   24,   25,   86,   27,   21,  122,  145,   19,
    6,   76,  122,   31,  117,  118,   14,   88,   38,   91,
   40,   94,   95,   32,   98,   21,   13,  170,  171,  102,
  111,  114,  151,  130,  132,   14,    9,  131,  133,  135,
  155,  153,  138,  122,  141,   13,  142,  143,  148,  149,
    6,  146,  147,  150,   16,  157,  154,  158,  159,   43,
   33,  161,   79,   79,   79,   79,   16,   79,  164,   79,
  165,   56,  152,  131,   78,   78,  166,   78,  167,   78,
  156,   79,   79,  168,   79,    3,   75,   75,   96,  169,
   37,   36,  119,   78,   78,    9,   78,   85,   52,   51,
    9,   24,   25,   29,   26,   75,   75,    0,   75,   24,
   25,   90,    9,   56,  125,   77,    0,    9,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   58,
   99,  100,   44,   44,    0,    9,   36,   37,   36,   37,
   36,   37,   38,   39,   40,   41,   30,  119,    1,    2,
   36,   37,  162,    0,   42,    0,   42,    3,   42,    0,
    4,    5,    6,    0,    7,    2,    8,   83,   42,    0,
  119,  144,   25,    3,   26,  119,    4,   31,    6,    3,
    7,   45,    4,   46,   57,   31,    7,   32,   31,   31,
   31,    0,   31,    0,   31,   32,    0,   12,   32,   32,
   32,    0,   32,    0,   32,   12,  119,   19,   12,   12,
   12,    0,   12,    0,   12,   19,    0,    0,   19,   19,
   19,    0,   19,   21,   19,   38,   38,   40,   40,    0,
   38,   21,   38,   14,   21,   21,   21,    0,   21,    0,
   21,   14,    0,   13,   14,   14,   14,    0,   14,    0,
   14,   13,    0,    9,   13,   13,   13,    0,   13,    0,
   13,    9,    0,    0,    9,    9,    9,    6,    9,    0,
    9,   36,   37,    0,    0,    6,    0,    0,    6,   79,
    6,    0,    6,   79,   79,   79,   79,   79,    0,   42,
   79,   78,   79,    0,   79,   78,   78,   78,   78,   78,
    0,    0,   78,   75,   78,    0,   78,   75,   75,   75,
   75,   75,    2,    0,   75,    0,   75,    2,   75,    0,
    3,    0,    0,    4,    0,    3,   32,    7,    4,    2,
   57,    0,    7,    0,  106,    0,    0,    3,    0,    0,
    4,    0,    3,   89,    7,    4,   58,   57,   62,    7,
    0,    0,    2,    0,   58,    0,   17,   58,    0,   58,
    3,   58,   17,    4,   75,    0,    0,    7,   17,    0,
   78,    0,    0,   17,    0,    0,    0,    0,    0,    0,
    0,    0,   38,   39,   40,   41,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  110,    0,    0,  113,
    0,  115,  116,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   17,    0,   17,    0,  136,  137,    0,
  139,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  160,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   93,   41,   59,   45,   59,   45,   40,   45,   95,
    0,   29,   40,   44,   41,   91,  257,   27,   45,   41,
    0,   60,   59,   62,   59,   93,  257,   85,   40,   93,
    0,   95,  273,   44,   40,   59,   44,   59,   40,    0,
   40,   44,  273,  269,   53,   43,    0,   45,  274,  257,
   40,  271,   70,   71,   72,   93,    0,  266,  267,   59,
   40,  259,   93,   73,   95,   41,    0,  271,   42,   59,
   40,  257,   44,   47,   92,   84,    0,   86,   91,   59,
   91,   99,  257,  258,   95,   91,   40,   95,  106,   59,
    0,  258,   95,   93,   81,   82,   40,   59,   59,  271,
   59,   59,  260,   93,  260,   59,   40,  165,  166,   59,
   41,   41,  121,  122,   93,   59,   40,   86,  271,   59,
  129,   93,  260,   95,   59,   59,   59,   59,   93,   41,
   40,   59,   59,   41,    0,  267,   93,   59,   59,   45,
    6,   59,   40,   41,   42,   43,   12,   45,   59,   47,
   44,   17,  121,  122,   40,   41,   44,   43,   59,   45,
  129,   59,   60,   59,   62,    0,   40,   41,  260,   59,
    0,    0,  258,   59,   60,   40,   62,   93,   14,   12,
   40,  257,  258,    3,  260,   59,   60,   -1,   62,  257,
  258,   57,   40,   59,  258,   47,   -1,   40,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   40,
  266,  267,  266,  267,   -1,   40,  257,  258,  257,  258,
  257,  258,  261,  262,  263,  264,  259,  258,  256,  257,
  257,  258,  267,   -1,  275,   -1,  275,  265,  275,   -1,
  268,  269,  270,   -1,  272,  257,  274,  258,  275,   -1,
  258,  257,  258,  265,  260,  258,  268,  257,  270,  265,
  272,   60,  268,   62,  270,  265,  272,  257,  268,  269,
  270,   -1,  272,   -1,  274,  265,   -1,  257,  268,  269,
  270,   -1,  272,   -1,  274,  265,  258,  257,  268,  269,
  270,   -1,  272,   -1,  274,  265,   -1,   -1,  268,  269,
  270,   -1,  272,  257,  274,  266,  267,  266,  267,   -1,
  271,  265,  271,  257,  268,  269,  270,   -1,  272,   -1,
  274,  265,   -1,  257,  268,  269,  270,   -1,  272,   -1,
  274,  265,   -1,  257,  268,  269,  270,   -1,  272,   -1,
  274,  265,   -1,   -1,  268,  269,  270,  257,  272,   -1,
  274,  257,  258,   -1,   -1,  265,   -1,   -1,  268,  257,
  270,   -1,  272,  261,  262,  263,  264,  265,   -1,  275,
  268,  257,  270,   -1,  272,  261,  262,  263,  264,  265,
   -1,   -1,  268,  257,  270,   -1,  272,  261,  262,  263,
  264,  265,  257,   -1,  268,   -1,  270,  257,  272,   -1,
  265,   -1,   -1,  268,   -1,  265,  271,  272,  268,  257,
  270,   -1,  272,   -1,  257,   -1,   -1,  265,   -1,   -1,
  268,   -1,  265,  271,  272,  268,  257,  270,   26,  272,
   -1,   -1,  257,   -1,  265,   -1,    0,  268,   -1,  270,
  265,  272,    6,  268,   42,   -1,   -1,  272,   12,   -1,
   48,   -1,   -1,   17,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  261,  262,  263,  264,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   74,   -1,   -1,   77,
   -1,   79,   80,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   57,   -1,   59,   -1,   95,   96,   -1,
   98,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  138,
};
}
final static short YYFINAL=10;
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
"programa : conjunto_de_sentencias",
"conjunto_de_sentencias : sentencias_declarativas sentencias_ejecutables",
"conjunto_de_sentencias : sentencias_declarativas",
"conjunto_de_sentencias : sentencias_ejecutables",
"conjunto_de_sentencias : error ';'",
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
"lista_de_variables : ID '[' lista_de_valores_iniciales ']' ',' lista_de_variables",
"lista_de_variables : ID '[' CTE ']' ',' lista_de_variables",
"lista_de_variables : ID CTE ']'",
"lista_de_variables : ID '[' CTE",
"lista_de_variables : ID lista_de_valores_iniciales ']'",
"lista_de_variables : ID '[' lista_de_valores_iniciales",
"lista_de_variables : ID '[' ']'",
"lista_de_valores_iniciales : CTE ',' elem_lista",
"lista_de_valores_iniciales : '_' ',' elem_lista",
"lista_de_valores_iniciales : CTE ',' lista_de_valores_iniciales",
"lista_de_valores_iniciales : '_' ',' lista_de_valores_iniciales",
"lista_de_valores_iniciales : CTE elem_lista",
"lista_de_valores_iniciales : CTE lista_de_valores_iniciales",
"lista_de_valores_iniciales : '_' elem_lista",
"lista_de_valores_iniciales : '_' lista_de_valores_iniciales",
"elem_lista : CTE",
"elem_lista : '_'",
"sentencias_ejecutables : BEGIN lista_de_sentencias END",
"sentencias_ejecutables : BEGIN END",
"sentencias_ejecutables : lista_de_sentencias END",
"sentencias_ejecutables : BEGIN lista_de_sentencias",
"sentencias_ejecutables : lista_de_sentencias",
"lista_de_sentencias : sentencia_ejecutable",
"lista_de_sentencias : sentencia_ejecutable lista_de_sentencias",
"bloque_de_sentencias : sentencia_ejecutable",
"bloque_de_sentencias : BEGIN lista_de_sentencias END",
"bloque_de_sentencias : BEGIN END",
"bloque_de_sentencias : lista_de_sentencias END",
"bloque_de_sentencias : BEGIN lista_de_sentencias",
"sentencia_ejecutable : sentencia_if",
"sentencia_ejecutable : sentencia_foreach",
"sentencia_ejecutable : sentencia_print",
"sentencia_ejecutable : asignacion",
"sentencia_if : IF condicion bloque_de_sentencias END_IF ';'",
"sentencia_if : IF condicion bloque_de_sentencias ELSE bloque_de_sentencias END_IF ';'",
"sentencia_if : condicion bloque_de_sentencias ELSE bloque_de_sentencias END_IF ';'",
"sentencia_if : condicion bloque_de_sentencias END_IF ';'",
"sentencia_if : IF condicion bloque_de_sentencias ELSE bloque_de_sentencias ';'",
"sentencia_if : IF condicion bloque_de_sentencias ';'",
"condicion : '(' expresion comparador expresion ')'",
"condicion : '(' comparador expresion ')'",
"condicion : '(' expresion comparador ')'",
"condicion : '(' expresion comparador expresion",
"condicion : '(' ')'",
"comparador : MAYORIGUAL",
"comparador : MENORIGUAL",
"comparador : IGUALIGUAL",
"comparador : DISTINTO",
"comparador : '<'",
"comparador : '>'",
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

//#line 174 "gramatica.y"

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

		if (Long.parseLong(lexema) <= AnalizadorLexico.MAX_INT) {
			if (t != null){ //ya esta en TS
				t.incrementarContadorDeReferencias();
			}
			else{
				Integer id = AnalizadorLexico.palabras_reservadas.get("cte");
				t = new Token(negativo, AnalizadorLexico.TIPO_CTE, id);
				AnalizadorLexico.tablaSimbolos.put(negativo, t);
			}
		}
		else{ //generar error
			errores.add(new analizadorLexico.Error("ERROR", "Constante negativa fuera de rango. Fue reemplazado por el valor limite permitido del rango", AnalizadorLexico.cantLineas));	
			if (t != null) {//si ya esta
				t.incrementarContadorDeReferencias();
			}
			else {
				negativo = "-" + AnalizadorLexico.MAX_INT;
				Integer id = AnalizadorLexico.palabras_reservadas.get("cte");
				t = new Token(negativo, AnalizadorLexico.TIPO_CTE, id);
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
	return yyparse(); // si devuelve 0 -> sintaxis correcta
}

//#line 560 "Parser.java"
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
int yyparse() throws IOException
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
        yyerror("Programa sintacticamente incorrecto.");
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
case 5:
//#line 31 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Sentencia invalida.", AnalizadorLexico.cantLineas)); }
break;
case 9:
//#line 39 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Declaracion incorrecta. Se esperaba ';'.", AnalizadorLexico.cantLineas)); }
break;
case 12:
//#line 46 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion" + "\n"); }
break;
case 13:
//#line 47 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion" + "\n"); }
break;
case 14:
//#line 48 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion" + "\n"); }
break;
case 18:
//#line 54 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '[' para definir tamaño de la coleccion. ", AnalizadorLexico.cantLineas)); }
break;
case 19:
//#line 55 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ']' para definir tamaño de la coleccion. ", AnalizadorLexico.cantLineas)); }
break;
case 20:
//#line 56 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '[' para definir los valores iniciales de la coleccion. ", AnalizadorLexico.cantLineas)); }
break;
case 21:
//#line 57 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ']' para definir los valores iniciales de la coleccion. ", AnalizadorLexico.cantLineas)); }
break;
case 22:
//#line 58 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba valor entre '[' ']' para definir a la coleccion. ", AnalizadorLexico.cantLineas)); }
break;
case 27:
//#line 66 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); }
break;
case 28:
//#line 67 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); }
break;
case 29:
//#line 68 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); }
break;
case 30:
//#line 69 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); }
break;
case 35:
//#line 80 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'begin' al comienzo. ", AnalizadorLexico.cantLineas)); }
break;
case 36:
//#line 81 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'end' al final. ", AnalizadorLexico.cantLineas)); }
break;
case 37:
//#line 82 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Ausencia de begin y end de sentencias ejecutables.", AnalizadorLexico.cantLineas)); }
break;
case 43:
//#line 93 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'begin' al comienzo del bloque de sentencias. ", AnalizadorLexico.cantLineas));  }
break;
case 44:
//#line 94 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'end' al final del bloque de sentencias. ", AnalizadorLexico.cantLineas));  }
break;
case 45:
//#line 97 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia if. " + "\n");}
break;
case 46:
//#line 98 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia foreach. " + "\n");}
break;
case 47:
//#line 99 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia print. " + "\n");}
break;
case 48:
//#line 100 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Asignacion. " + "\n");}
break;
case 51:
//#line 105 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Ausencia de palabra reservada 'if'. ", AnalizadorLexico.cantLineas)); }
break;
case 52:
//#line 106 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Ausencia de palabra reservada 'if'. ", AnalizadorLexico.cantLineas)); }
break;
case 53:
//#line 107 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Ausencia de palabra reservada 'end_if'. ", AnalizadorLexico.cantLineas)); }
break;
case 54:
//#line 108 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Ausencia de palabra reservada 'end_if'. ", AnalizadorLexico.cantLineas)); }
break;
case 55:
//#line 111 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Condicion " + "\n"); }
break;
case 56:
//#line 113 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion del lado derecho para comparar. ", AnalizadorLexico.cantLineas)); }
break;
case 57:
//#line 114 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion del lado izquierdo para comparar. ", AnalizadorLexico.cantLineas)); }
break;
case 58:
//#line 115 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ')' que cierre condicion. ", AnalizadorLexico.cantLineas)); }
break;
case 59:
//#line 116 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una condicion entre '(' ')'. ", AnalizadorLexico.cantLineas)); }
break;
case 67:
//#line 129 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba el nombre de la variable para iterar. ", AnalizadorLexico.cantLineas)); }
break;
case 68:
//#line 130 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'in' y se encontró el nombre de la coleccion. ", AnalizadorLexico.cantLineas));  }
break;
case 69:
//#line 131 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba el nombre de la coleccion y se encontraron sentencias. ", AnalizadorLexico.cantLineas));  }
break;
case 71:
//#line 136 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '(' y se encontro una cadena. ", AnalizadorLexico.cantLineas));  }
break;
case 72:
//#line 137 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ')' y se encontro ';'. ", AnalizadorLexico.cantLineas));  }
break;
case 73:
//#line 140 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Suma. " + "\n"); }
break;
case 74:
//#line 141 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Resta. " + "\n"); }
break;
case 76:
//#line 145 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Multiplicacion. " + "\n"); }
break;
case 77:
//#line 146 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Division. " + "\n"); }
break;
case 79:
//#line 150 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor ID. " + "\n"); }
break;
case 80:
//#line 151 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE. " + "\n"); }
break;
case 81:
//#line 152 "gramatica.y"
{  estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE Negativa. " + "\n");
	   			  modificarContadorDeReferencias(val_peek(0).sval);	 }
break;
case 82:
//#line 154 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor coleccion. " + "\n"); }
break;
case 83:
//#line 155 "gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor conversion. " + "\n"); }
break;
case 84:
//#line 157 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '(' para realizar la conversion. ", AnalizadorLexico.cantLineas)); }
break;
case 89:
//#line 167 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion para realizar la asignacion. ", AnalizadorLexico.cantLineas));  }
break;
case 90:
//#line 168 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '[' para indicar la posicion de la coleccion. ", AnalizadorLexico.cantLineas));  }
break;
case 91:
//#line 169 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ']' para indicar la posicion de la coleccion. ", AnalizadorLexico.cantLineas));  }
break;
case 92:
//#line 170 "gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba un subindice para realizar la asignacion. ", AnalizadorLexico.cantLineas));  }
break;
//#line 914 "Parser.java"
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
 * @throws IOException 
 */
public void run() throws IOException
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
