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






//#line 2 ".\gramatica.y"
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
    0,    0,    0,    1,    1,    3,    3,    3,    4,    4,
    5,    5,    5,    5,    5,    5,    5,    5,    5,    5,
    5,    6,    6,    6,    6,    6,    6,    6,    6,    6,
    2,    2,    2,    2,    2,    7,    7,    9,    9,    9,
    9,    9,    8,    8,    8,    8,   10,   10,   10,   10,
   10,   10,   14,   14,   14,   14,   14,   16,   16,   16,
   16,   16,   16,   11,   11,   11,   11,   12,   12,   12,
   15,   15,   15,   17,   17,   17,   18,   18,   18,   18,
   18,   18,   19,   19,   13,   13,   13,   13,   13,   13,
};
final static short yylen[] = {                            2,
    2,    1,    1,    1,    2,    3,    2,    2,    1,    1,
    1,    4,    4,    3,    6,    6,    3,    3,    3,    3,
    3,    1,    3,    3,    3,    3,    2,    2,    2,    2,
    3,    2,    2,    2,    1,    1,    2,    1,    3,    2,
    2,    2,    1,    1,    1,    1,    5,    7,    6,    4,
    6,    4,    5,    4,    4,    4,    2,    1,    1,    1,
    1,    1,    1,    6,    5,    5,    5,    5,    4,    4,
    3,    3,    1,    3,    3,    1,    1,    1,    2,    4,
    4,    3,    1,    1,    4,    7,    3,    6,    6,    6,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    9,    0,    0,   10,    0,    0,
    0,    3,    0,    0,    0,    0,   43,   44,   45,   46,
    0,    8,   83,   84,    0,    0,    0,    0,    0,    0,
   32,    0,    0,    0,    0,   78,   58,   59,   60,   61,
    0,    0,   57,   62,   63,    0,    0,    0,    0,    1,
    5,    0,    0,   33,   37,    0,    0,    0,    0,   87,
    0,    0,    0,    0,    0,    0,    0,   31,    0,    0,
    0,    0,    0,    0,   79,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    6,   40,    0,   41,
    0,    0,   85,    0,    0,    0,    0,    0,    0,   52,
   69,   70,    0,    0,    0,    0,    0,    0,    0,   82,
   55,    0,   54,   71,   72,   74,   75,    0,   17,    0,
   28,    0,   21,    0,   14,    0,    0,   30,   19,   39,
    0,   50,    0,    0,    0,    0,    0,   47,   68,   66,
    0,    0,   67,   65,   80,   81,   53,    0,   25,    0,
    0,    0,   26,    0,   90,   89,    0,   88,    0,   51,
   64,    0,    0,   49,   86,   48,   16,   15,
};
final static short yydgoto[] = {                         10,
   11,   12,   13,   14,   53,  121,   57,   58,   59,   17,
   18,   19,   20,   21,   46,   47,   48,   49,   27,
};
final static short yysindex[] = {                       -27,
  -22,  -81,  -13,  -32,    0,  175, -228,    0,  -38,    0,
  210,    0, -231, -209, -221,  258,    0,    0,    0,    0,
  211,    0,    0,    0,  -36,  -76,  -35,  211,   27, -190,
    0, -198, -218, -181,   -7,    0,    0,    0,    0,    0,
  -40, -173,    0,    0,    0,  273,   91,   -1,   17,    0,
    0,  -42,   28,    0,    0,  232, -185,  258, -220,    0,
   29, -171,  -77, -169,  -55,   34,   -5,    0,  211,  242,
  211, -201,   91,   54,    0,  -26,   57,   91,   91,   91,
   91,  -30,  -60, -209,  -14,    7,    0,    0, -170,    0,
  211,   47,    0,   91,   91, -145,   91,  211,   58,    0,
    0,    0,   59,   60,  169,   61,   66,   23,   85,    0,
    0,   86,    0,    0,    0,    0,    0,  -12,    0,  -83,
    0,  -18,    0,   35,    0,  -12,  -61,    0,    0,    0,
 -138,    0,   73,   75,   91,   76,  -31,    0,    0,    0,
  -81,   79,    0,    0,    0,    0,    0,  -12,    0,   95,
  102,  -12,    0,   81,    0,    0,   88,    0,   89,    0,
    0, -209, -209,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  151,    0,  133,    0,  152,   40,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  154,    0,    0,  145,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  193,  371,    0,
    0,   62,  123,    0,    0,    0,    0,   96,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    1,    0,    0,    0,  -53,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  252,    0,    0,    0,    0,    0,   11,    0,    0,
    0,   72,    0,   82,    0,   21,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   64,    0,    0,    0,    0,    0,    0,   31,    0,  103,
  113,   52,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  146,  147,    0,    0,  -66,   22,  150,  485,   39,    0,
    0,    0,    0,  157,  480,  118,   -2,    0,   -6,
};
final static int YYTABLESIZE=643;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         73,
   22,   84,   43,  100,   42,   42,   42,   30,   42,   26,
   27,   85,    9,  120,  111,   96,   62,  125,   42,   63,
   29,   44,   60,   45,    1,  120,    9,  160,   33,  127,
   23,  120,  123,   85,   85,  103,   22,    5,   69,   36,
   22,   78,    8,   79,   34,   91,   92,   52,   83,   54,
   27,   24,   85,  102,   70,   23,   24,   64,   80,   22,
   29,   11,  119,   81,   85,  108,   65,   66,   67,   27,
   23,   18,   68,   86,  150,   71,   85,  116,  117,   29,
   85,   20,   85,   72,   75,   90,   87,   93,   94,   23,
   97,   24,  101,   22,  110,  167,  168,  113,   36,  129,
  130,   11,   13,   27,  124,  132,  128,  104,  106,  107,
   24,   18,   12,   29,  135,  145,  138,  139,  140,  143,
   11,   20,    7,   23,  144,  146,  147,  151,  154,  131,
   18,  155,    4,  156,  158,   42,  137,  161,  162,  164,
   20,  149,   13,  142,   24,  163,  165,  166,  153,   15,
    2,   35,   12,   34,   38,   32,   83,   50,   51,   28,
   15,   13,    7,   76,    0,   55,    0,    0,    0,    0,
    0,   12,    4,    0,  148,   23,   24,    0,   25,    0,
   23,   24,   95,    0,   77,   77,   77,   77,    0,   77,
    0,   77,    0,    0,    0,    0,  152,  122,    0,    0,
    0,    0,    0,   77,   77,   89,   77,   55,    9,    0,
   98,   99,   42,   42,    9,   82,   35,   36,   35,   36,
   35,   36,   37,   38,   39,   40,   29,  118,    1,    2,
   35,   36,   73,   73,   41,  159,   41,    3,   41,  118,
    4,    5,    6,  126,    7,  118,    8,    0,   41,    9,
    9,   73,   73,    0,   73,    0,   22,   22,    0,   26,
    0,    0,    0,    0,    0,   22,   27,   27,   22,   22,
   22,    9,   22,    0,   22,   27,   29,   29,   27,   27,
   27,    9,   27,    0,   27,   29,   23,   23,   29,   29,
   29,   56,   29,    0,   29,   23,    0,    9,   23,   23,
   23,    0,   23,    0,   23,   36,   36,   24,   24,    0,
   36,    0,    0,    0,    0,    0,   24,   11,   11,   24,
   24,   24,    0,   24,    0,   24,   11,   18,   18,   11,
   11,   11,   44,   11,   45,   11,   18,   20,   20,   18,
   18,   18,    0,   18,    0,   18,   20,   35,   36,   20,
   20,   20,    0,   20,    0,   20,    0,    0,   13,   13,
    0,   38,   38,    0,    0,   41,   36,   13,   12,   12,
   13,   13,   13,    0,   13,    0,   13,   12,    7,    7,
   12,   12,   12,    0,   12,    0,   12,    7,    0,    4,
    7,    7,    7,    0,    7,    0,    7,    4,    0,    0,
    4,   77,    4,    0,    4,   77,   77,   77,   77,   77,
   76,   76,   77,   76,   77,   76,   77,    0,    0,    0,
    0,    0,    0,    0,    0,  141,   24,    0,   25,   76,
   76,    2,   76,    3,    0,    0,    4,    0,   56,    3,
    7,    0,    4,    0,    0,   31,    7,    0,    0,   73,
    0,    0,    0,   73,   73,   73,   73,   73,    0,    0,
   73,    0,   73,    0,   73,    0,    2,    2,    0,    0,
    0,    0,    0,    0,    3,    3,    0,    4,    4,    6,
   56,    7,    7,    0,   16,    0,    0,    0,    2,    0,
   16,    0,    0,    0,    0,   16,    3,    0,  105,    4,
   16,    0,   88,    7,   61,    0,    3,    0,   56,    4,
    0,   56,    0,    7,    2,    0,   56,    0,    0,   56,
   74,   56,    3,   56,    0,    4,   77,    0,    0,    7,
    0,    0,    0,   37,   38,   39,   40,    0,    0,    0,
   16,    0,   16,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  109,    0,    0,  112,    0,  114,  115,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  133,  134,    0,  136,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  157,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   76,    0,    0,
    0,   76,   76,   76,   76,   76,    0,    0,   76,    0,
   76,    0,   76,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   44,   41,   59,   45,   59,   45,   40,   45,   91,
    0,   95,   40,   44,   41,   93,   93,   84,   45,   26,
    0,   60,   59,   62,  256,   44,   40,   59,  257,   44,
    0,   44,   93,   95,   95,   41,   59,  269,  257,    0,
   40,   43,  274,   45,  273,  266,  267,  257,   91,  271,
   40,    0,   95,   59,  273,  257,  258,   93,   42,   59,
   40,    0,   93,   47,   95,   72,   28,   41,  259,   59,
   40,    0,  271,   52,   93,  257,   95,   80,   81,   59,
   95,    0,   95,   91,  258,  271,   59,   59,  260,   59,
  260,   40,   59,   93,   41,  162,  163,   41,   59,   93,
  271,   40,    0,   93,   83,   59,   85,   69,   70,   71,
   59,   40,    0,   93,  260,   93,   59,   59,   59,   59,
   59,   40,    0,   93,   59,   41,   41,   93,  267,   91,
   59,   59,    0,   59,   59,   45,   98,   59,   44,   59,
   59,  120,   40,  105,   93,   44,   59,   59,  127,    0,
    0,    0,   40,    0,   59,    6,   93,   11,   13,    3,
   11,   59,   40,   46,   -1,   16,   -1,   -1,   -1,   -1,
   -1,   59,   40,   -1,  258,  257,  258,   -1,  260,   -1,
  257,  258,  260,   -1,   40,   41,   42,   43,   -1,   45,
   -1,   47,   -1,   -1,   -1,   -1,  258,  258,   -1,   -1,
   -1,   -1,   -1,   59,   60,   56,   62,   58,   40,   -1,
  266,  267,  266,  267,   40,  258,  257,  258,  257,  258,
  257,  258,  261,  262,  263,  264,  259,  258,  256,  257,
  257,  258,   40,   41,  275,  267,  275,  265,  275,  258,
  268,  269,  270,  258,  272,  258,  274,   -1,  275,   40,
   40,   59,   60,   -1,   62,   -1,  256,  257,   -1,   91,
   -1,   -1,   -1,   -1,   -1,  265,  256,  257,  268,  269,
  270,   40,  272,   -1,  274,  265,  256,  257,  268,  269,
  270,   40,  272,   -1,  274,  265,  256,  257,  268,  269,
  270,   40,  272,   -1,  274,  265,   -1,   40,  268,  269,
  270,   -1,  272,   -1,  274,  266,  267,  256,  257,   -1,
  271,   -1,   -1,   -1,   -1,   -1,  265,  256,  257,  268,
  269,  270,   -1,  272,   -1,  274,  265,  256,  257,  268,
  269,  270,   60,  272,   62,  274,  265,  256,  257,  268,
  269,  270,   -1,  272,   -1,  274,  265,  257,  258,  268,
  269,  270,   -1,  272,   -1,  274,   -1,   -1,  256,  257,
   -1,  266,  267,   -1,   -1,  275,  271,  265,  256,  257,
  268,  269,  270,   -1,  272,   -1,  274,  265,  256,  257,
  268,  269,  270,   -1,  272,   -1,  274,  265,   -1,  257,
  268,  269,  270,   -1,  272,   -1,  274,  265,   -1,   -1,
  268,  257,  270,   -1,  272,  261,  262,  263,  264,  265,
   40,   41,  268,   43,  270,   45,  272,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,  260,   59,
   60,  257,   62,  265,   -1,   -1,  268,   -1,  270,  265,
  272,   -1,  268,   -1,   -1,  271,  272,   -1,   -1,  257,
   -1,   -1,   -1,  261,  262,  263,  264,  265,   -1,   -1,
  268,   -1,  270,   -1,  272,   -1,  257,  257,   -1,   -1,
   -1,   -1,   -1,   -1,  265,  265,   -1,  268,  268,  270,
  270,  272,  272,   -1,    0,   -1,   -1,   -1,  257,   -1,
    6,   -1,   -1,   -1,   -1,   11,  265,   -1,  257,  268,
   16,   -1,  271,  272,   25,   -1,  265,   -1,  257,  268,
   -1,  270,   -1,  272,  257,   -1,  265,   -1,   -1,  268,
   41,  270,  265,  272,   -1,  268,   47,   -1,   -1,  272,
   -1,   -1,   -1,  261,  262,  263,  264,   -1,   -1,   -1,
   56,   -1,   58,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   73,   -1,   -1,   76,   -1,   78,   79,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   94,   95,   -1,   97,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  135,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,   -1,   -1,
   -1,  261,  262,  263,  264,  265,   -1,   -1,  268,   -1,
  270,   -1,  272,
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
"programa : sentencias_declarativas sentencias_ejecutables",
"programa : sentencias_declarativas",
"programa : sentencias_ejecutables",
"sentencias_declarativas : sentencia_declarativa",
"sentencias_declarativas : sentencia_declarativa sentencias_declarativas",
"sentencia_declarativa : tipo lista_de_variables ';'",
"sentencia_declarativa : tipo lista_de_variables",
"sentencia_declarativa : error ';'",
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
"lista_de_valores_iniciales : '_'",
"lista_de_valores_iniciales : CTE ',' CTE",
"lista_de_valores_iniciales : '_' ',' CTE",
"lista_de_valores_iniciales : CTE ',' lista_de_valores_iniciales",
"lista_de_valores_iniciales : '_' ',' lista_de_valores_iniciales",
"lista_de_valores_iniciales : CTE CTE",
"lista_de_valores_iniciales : CTE lista_de_valores_iniciales",
"lista_de_valores_iniciales : '_' CTE",
"lista_de_valores_iniciales : '_' lista_de_valores_iniciales",
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

//#line 167 ".\gramatica.y"

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

//#line 574 "Parser.java"
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
case 7:
//#line 34 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Declaracion incorrecta. Se esperaba ';'.", AnalizadorLexico.cantLineas)); }
break;
case 8:
//#line 35 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Sentencia invalida.", AnalizadorLexico.cantLineas)); }
break;
case 11:
//#line 43 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion" + "\n"); }
break;
case 12:
//#line 44 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion" + "\n"); }
break;
case 13:
//#line 45 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion" + "\n"); }
break;
case 17:
//#line 51 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '[' para definir tamaño de la coleccion. ", AnalizadorLexico.cantLineas)); }
break;
case 18:
//#line 52 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ']' para definir tamaño de la coleccion. ", AnalizadorLexico.cantLineas)); }
break;
case 19:
//#line 53 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '[' para definir los valores iniciales de la coleccion. ", AnalizadorLexico.cantLineas)); }
break;
case 20:
//#line 54 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ']' para definir los valores iniciales de la coleccion. ", AnalizadorLexico.cantLineas)); }
break;
case 21:
//#line 55 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba valor entre '[' ']' para definir a la coleccion. ", AnalizadorLexico.cantLineas)); }
break;
case 27:
//#line 64 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); }
break;
case 28:
//#line 65 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); }
break;
case 29:
//#line 66 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); }
break;
case 30:
//#line 67 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); }
break;
case 33:
//#line 73 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'begin' al comienzo. ", AnalizadorLexico.cantLineas)); }
break;
case 34:
//#line 74 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'end' al final. ", AnalizadorLexico.cantLineas)); }
break;
case 35:
//#line 75 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Ausencia de begin y end de sentencias ejecutables.", AnalizadorLexico.cantLineas)); }
break;
case 41:
//#line 86 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'begin' al comienzo del bloque de sentencias. ", AnalizadorLexico.cantLineas));  }
break;
case 42:
//#line 87 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'end' al final del bloque de sentencias. ", AnalizadorLexico.cantLineas));  }
break;
case 43:
//#line 90 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia if. " + "\n");}
break;
case 44:
//#line 91 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia foreach. " + "\n");}
break;
case 45:
//#line 92 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia print. " + "\n");}
break;
case 46:
//#line 93 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Asignacion. " + "\n");}
break;
case 49:
//#line 98 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Ausencia de palabra reservada 'if'. ", AnalizadorLexico.cantLineas)); }
break;
case 50:
//#line 99 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Ausencia de palabra reservada 'if'. ", AnalizadorLexico.cantLineas)); }
break;
case 51:
//#line 100 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Ausencia de palabra reservada 'end_if'. ", AnalizadorLexico.cantLineas)); }
break;
case 52:
//#line 101 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Ausencia de palabra reservada 'end_if'. ", AnalizadorLexico.cantLineas)); }
break;
case 53:
//#line 104 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Condicion " + "\n"); }
break;
case 54:
//#line 106 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion del lado derecho para comparar. ", AnalizadorLexico.cantLineas)); }
break;
case 55:
//#line 107 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion del lado izquierdo para comparar. ", AnalizadorLexico.cantLineas)); }
break;
case 56:
//#line 108 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ')' que cierre condicion. ", AnalizadorLexico.cantLineas)); }
break;
case 57:
//#line 109 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una condicion entre '(' ')'. ", AnalizadorLexico.cantLineas)); }
break;
case 65:
//#line 122 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba el nombre de la variable para iterar. ", AnalizadorLexico.cantLineas)); }
break;
case 66:
//#line 123 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba 'in' y se encontró el nombre de la coleccion. ", AnalizadorLexico.cantLineas));  }
break;
case 67:
//#line 124 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba el nombre de la coleccion y se encontraron sentencias. ", AnalizadorLexico.cantLineas));  }
break;
case 69:
//#line 129 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '(' y se encontro una cadena. ", AnalizadorLexico.cantLineas));  }
break;
case 70:
//#line 130 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ')' y se encontro ';'. ", AnalizadorLexico.cantLineas));  }
break;
case 71:
//#line 133 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Suma. " + "\n"); }
break;
case 72:
//#line 134 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Resta. " + "\n"); }
break;
case 74:
//#line 138 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Multiplicacion. " + "\n"); }
break;
case 75:
//#line 139 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Division. " + "\n"); }
break;
case 77:
//#line 143 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor ID. " + "\n"); }
break;
case 78:
//#line 144 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE. " + "\n"); }
break;
case 79:
//#line 145 ".\gramatica.y"
{  estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE Negativa. " + "\n");
	   			  modificarContadorDeReferencias(val_peek(0).sval);	 }
break;
case 80:
//#line 147 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor coleccion. " + "\n"); }
break;
case 81:
//#line 148 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor conversion. " + "\n"); }
break;
case 82:
//#line 150 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '(' para realizar la conversion. ", AnalizadorLexico.cantLineas)); }
break;
case 87:
//#line 160 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba una expresion para realizar la asignacion. ", AnalizadorLexico.cantLineas));  }
break;
case 88:
//#line 161 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba '[' para indicar la posicion de la coleccion. ", AnalizadorLexico.cantLineas));  }
break;
case 89:
//#line 162 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba ']' para indicar la posicion de la coleccion. ", AnalizadorLexico.cantLineas));  }
break;
case 90:
//#line 163 ".\gramatica.y"
{ errores.add(new analizadorLexico.Error("ERROR", "Se esperaba un subindice para realizar la asignacion. ", AnalizadorLexico.cantLineas));  }
break;
//#line 928 "Parser.java"
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
