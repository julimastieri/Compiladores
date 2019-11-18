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
import java.util.Arrays;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.Token;
import analizadorLexico.Error;

//#line 31 "Parser.java"




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
    7,    7,    2,    2,    2,    2,    2,    8,    8,    9,
    9,    9,    9,   10,   10,   15,   15,   15,   15,   16,
   17,   18,   18,   18,   18,   18,   14,   14,   14,   14,
   14,   20,   20,   20,   20,   20,   20,   11,   11,   11,
   11,   12,   12,   12,   19,   19,   19,   21,   21,   21,
   22,   22,   22,   22,   22,   22,   23,   23,   13,   13,
   13,   13,   13,   13,
};
final static short yylen[] = {                            2,
    2,    1,    1,    1,    2,    3,    2,    2,    1,    1,
    1,    4,    4,    3,    6,    6,    3,    3,    3,    3,
    3,    1,    3,    3,    3,    3,    2,    2,    2,    2,
    1,    2,    3,    2,    2,    2,    1,    1,    2,    1,
    1,    1,    1,    3,    2,    3,    5,    2,    4,    1,
    1,    1,    3,    2,    2,    2,    5,    4,    4,    4,
    2,    1,    1,    1,    1,    1,    1,    6,    5,    5,
    5,    5,    4,    4,    3,    3,    1,    3,    3,    1,
    1,    1,    2,    4,    4,    3,    1,    1,    4,    7,
    3,    6,    6,    6,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    9,    0,    0,   10,    0,    0,
    0,    3,    0,    0,    0,    0,   40,   41,   42,   43,
    0,    8,   87,   88,    0,    0,    0,    0,    0,    0,
   34,    0,    0,    0,    0,   82,   62,   63,   64,   65,
    0,    0,   61,   66,   67,    0,    0,    0,    0,    1,
    5,    0,    0,   35,   39,    0,    0,    0,   45,    0,
   50,   91,    0,    0,    0,    0,   44,    0,    0,   33,
    0,    0,    0,    0,    0,    0,   83,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    6,   54,    0,   55,    0,    0,   48,   89,    0,    0,
    0,    0,   73,   74,    0,    0,    0,    0,    0,    0,
    0,   86,   59,    0,   58,   75,   76,   78,   79,   17,
   32,    0,   21,    0,   14,   31,    0,   30,    0,   19,
    0,   28,    0,   53,    0,   51,   46,    0,    0,    0,
    0,   72,   70,    0,    0,   71,   69,   84,   85,   57,
    0,    0,   26,    0,   25,    0,    0,   49,   94,   93,
    0,   92,   68,    0,    0,   47,   90,   16,   15,
};
final static short yydgoto[] = {                         10,
   11,   12,   13,   14,   53,  132,  133,   57,   58,   17,
   18,   19,   20,   21,   59,   60,  135,   61,   46,   47,
   48,   49,   27,
};
final static short yysindex[] = {                       -27,
  -43,  -81,  -15,  -32,    0,  175, -245,    0,  -38,    0,
  210,    0, -168, -228, -208,  258,    0,    0,    0,    0,
  211,    0,    0,    0,  -36,  -75,  -19,  211,   26, -190,
    0, -196, -220, -181,  -13,    0,    0,    0,    0,    0,
  -40, -172,    0,    0,    0,  273,   91,   -7,   -3,    0,
    0,  -18,   28,    0,    0,  232, -182,  258,    0,  -53,
    0,    0,   38, -153,  -79, -152,    0,   51,    6,    0,
  211,  242,  211, -209,   91,   75,    0,  -26,   77,   91,
   91,   91,   91,   27, -139,    5, -228,  -12,   32,  -10,
    0,    0, -145,    0,  211,   68,    0,    0,   91,   91,
 -132,   91,    0,    0,   70,   71,  169,   73,   76,   44,
   98,    0,    0,   99,    0,    0,    0,    0,    0,    0,
    0,   49,    0,   53,    0,    0,   39,    0,  -10,    0,
   39,    0,  -10,    0,  -57,    0,    0,   88,   89,   91,
   92,    0,    0,  -81,   93,    0,    0,    0,    0,    0,
  106,  114,    0,  -10,    0,  -10,  100,    0,    0,    0,
  102,    0,    0, -228, -228,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  157,    0,  133,    0,  164,   40,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  165,    0,    0,  145,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  193,  371,    0,
    0,   72,  123,    0,    0,    0,    0,  -55,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   -2,    0,    0,    0,   11,    0,    0,
    0,    0,   -5,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  252,    0,    0,    0,    0,    0,    0,
    0,    1,    0,   82,    0,    0,    0,    0,   21,    0,
    0,    0,   31,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   74,    0,    0,    0,    0,    0,    0,
  103,  113,    0,   52,    0,   62,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  153,  158,    0,    0,  -70,  -22,   29,  138,  485,    0,
    0,    0,    0,  167,  140,    0,    0,  -16,  480,  125,
  -24,    0,   -6,
};
final static int YYTABLESIZE=643;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         75,
   18,  158,   43,   52,   42,   97,   42,   30,   42,   26,
   22,   33,    9,  101,  113,   22,  125,   64,   42,   65,
   29,   44,   62,   45,    9,   87,   85,   34,   52,   89,
   27,  127,   85,  131,   85,   80,   71,   81,   82,   38,
   18,   31,   31,   83,   31,   31,  105,   23,   24,   85,
   22,   24,   72,   56,  106,  108,  109,  118,  119,   18,
   29,   23,   54,  124,  104,  128,   68,  110,   69,   22,
   27,   11,   86,   66,   70,   73,   88,   74,  136,   29,
   90,   20,   88,   85,   88,   77,   91,    1,   94,   27,
  145,   24,   31,  168,  169,   31,   98,  123,   38,   88,
    5,   23,   13,   22,  153,    8,   99,  102,  155,  103,
   24,   11,   12,   29,   90,  112,  129,  115,  121,  120,
   23,   20,    7,   27,  130,  134,  137,  140,  142,  143,
   11,  146,    4,   88,  147,   42,  148,   15,  149,  150,
   20,  151,   13,   32,   24,  152,  159,  160,   15,  164,
  162,  163,   12,   55,   23,  154,    2,  165,  166,  156,
  167,   13,    7,   37,   36,   51,   87,   67,   50,   28,
   78,   12,    4,    0,    0,   23,   24,    0,   25,    0,
  100,   23,   24,    0,   81,   81,   81,   81,    0,   81,
    0,   81,    0,   93,    0,   55,    0,    0,    0,    0,
    0,    0,    0,   81,   81,    0,   81,    0,    9,  157,
   52,   52,   95,   96,    9,   38,   35,   36,   35,   36,
   35,   36,   37,   38,   39,   40,   29,    0,    1,    2,
   35,   36,   77,   77,   41,    0,   41,    3,   41,   84,
    4,    5,    6,    0,    7,  126,    8,  126,   41,    9,
    9,   77,   77,    0,   77,   31,   18,   18,   31,   26,
   56,   56,  122,    0,    0,   18,   22,   22,   18,   18,
   18,    9,   18,    0,   18,   22,   29,   29,   22,   22,
   22,    9,   22,    0,   22,   29,   27,   27,   29,   29,
   29,   60,   29,    0,   29,   27,  126,    9,   27,   27,
   27,    0,   27,    0,   27,   38,   38,   24,   24,    0,
   38,    0,    0,    0,    0,    0,   24,   23,   23,   24,
   24,   24,    0,   24,    0,   24,   23,   11,   11,   23,
   23,   23,   44,   23,   45,   23,   11,   20,   20,   11,
   11,   11,    0,   11,    0,   11,   20,   35,   36,   20,
   20,   20,    0,   20,    0,   20,    0,    0,   13,   13,
    0,    0,    0,    0,    0,   41,    0,   13,   12,   12,
   13,   13,   13,    0,   13,    0,   13,   12,    7,    7,
   12,   12,   12,    0,   12,    0,   12,    7,    0,    4,
    7,    7,    7,    0,    7,    0,    7,    4,    0,    0,
    4,   81,    4,    0,    4,   81,   81,   81,   81,   81,
   80,   80,   81,   80,   81,   80,   81,    0,    0,    0,
    0,    0,    0,    0,    0,  144,   24,    0,   25,   80,
   80,    2,   80,    3,    0,    0,    4,    0,   56,    3,
    7,    0,    4,    0,    0,   31,    7,    0,    0,   77,
    0,    0,    0,   77,   77,   77,   77,   77,    0,    0,
   77,    0,   77,    0,   77,    0,    2,    2,    0,    0,
    0,    0,    0,    0,    3,    3,    0,    4,    4,    6,
   56,    7,    7,    0,   16,    0,    0,    0,    2,    0,
   16,    0,    0,    0,    0,   16,    3,    0,  107,    4,
   16,    0,   92,    7,   63,    0,    3,    0,   60,    4,
    0,   56,    0,    7,    2,    0,   60,    0,    0,   60,
   76,   60,    3,   60,    0,    4,   79,    0,    0,    7,
    0,    0,    0,   37,   38,   39,   40,    0,    0,    0,
   16,    0,   16,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  111,    0,    0,  114,    0,  116,
  117,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  138,  139,
    0,  141,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  161,
    0,    0,    0,    0,    0,    0,    0,   80,    0,    0,
    0,   80,   80,   80,   80,   80,    0,    0,   80,    0,
   80,    0,   80,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   59,   41,   59,   45,   59,   45,   40,   45,   91,
    0,  257,   40,   93,   41,   59,   87,   93,   45,   26,
    0,   60,   59,   62,   40,   44,   45,  273,  257,   52,
    0,   44,   45,   44,   45,   43,  257,   45,   42,    0,
   40,   44,   45,   47,   44,   45,   41,  257,  258,   45,
   40,    0,  273,   59,   71,   72,   73,   82,   83,   59,
   40,    0,  271,   86,   59,   88,   41,   74,  259,   59,
   40,    0,   91,   93,  271,  257,   95,   91,   95,   59,
   52,    0,   95,   45,   95,  258,   59,  256,  271,   59,
  107,   40,   95,  164,  165,   95,   59,   93,   59,   95,
  269,   40,    0,   93,  127,  274,  260,  260,  131,   59,
   59,   40,    0,   93,   86,   41,   88,   41,  258,   93,
   59,   40,    0,   93,   93,  271,   59,  260,   59,   59,
   59,   59,    0,   95,   59,   45,   93,    0,   41,   41,
   59,   93,   40,    6,   93,   93,   59,   59,   11,   44,
   59,   59,   40,   16,   93,  127,    0,   44,   59,  131,
   59,   59,   40,    0,    0,   13,   93,   28,   11,    3,
   46,   59,   40,   -1,   -1,  257,  258,   -1,  260,   -1,
  260,  257,  258,   -1,   40,   41,   42,   43,   -1,   45,
   -1,   47,   -1,   56,   -1,   58,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   59,   60,   -1,   62,   -1,   40,  267,
  266,  267,  266,  267,   40,  271,  257,  258,  257,  258,
  257,  258,  261,  262,  263,  264,  259,   -1,  256,  257,
  257,  258,   40,   41,  275,   -1,  275,  265,  275,  258,
  268,  269,  270,   -1,  272,  258,  274,  258,  275,   40,
   40,   59,   60,   -1,   62,  258,  256,  257,  258,   91,
  266,  267,  258,   -1,   -1,  265,  256,  257,  268,  269,
  270,   40,  272,   -1,  274,  265,  256,  257,  268,  269,
  270,   40,  272,   -1,  274,  265,  256,  257,  268,  269,
  270,   40,  272,   -1,  274,  265,  258,   40,  268,  269,
  270,   -1,  272,   -1,  274,  266,  267,  256,  257,   -1,
  271,   -1,   -1,   -1,   -1,   -1,  265,  256,  257,  268,
  269,  270,   -1,  272,   -1,  274,  265,  256,  257,  268,
  269,  270,   60,  272,   62,  274,  265,  256,  257,  268,
  269,  270,   -1,  272,   -1,  274,  265,  257,  258,  268,
  269,  270,   -1,  272,   -1,  274,   -1,   -1,  256,  257,
   -1,   -1,   -1,   -1,   -1,  275,   -1,  265,  256,  257,
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
   -1,   -1,   -1,   -1,   75,   -1,   -1,   78,   -1,   80,
   81,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   99,  100,
   -1,  102,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  140,
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
"lista_de_valores_iniciales : cte ',' cte",
"lista_de_valores_iniciales : '_' ',' cte",
"lista_de_valores_iniciales : cte ',' lista_de_valores_iniciales",
"lista_de_valores_iniciales : '_' ',' lista_de_valores_iniciales",
"lista_de_valores_iniciales : cte cte",
"lista_de_valores_iniciales : cte lista_de_valores_iniciales",
"lista_de_valores_iniciales : '_' cte",
"lista_de_valores_iniciales : '_' lista_de_valores_iniciales",
"cte : CTE",
"cte : '-' CTE",
"sentencias_ejecutables : BEGIN lista_de_sentencias END",
"sentencias_ejecutables : BEGIN END",
"sentencias_ejecutables : lista_de_sentencias END",
"sentencias_ejecutables : BEGIN lista_de_sentencias",
"sentencias_ejecutables : lista_de_sentencias",
"lista_de_sentencias : sentencia_ejecutable",
"lista_de_sentencias : sentencia_ejecutable lista_de_sentencias",
"sentencia_ejecutable : seleccion",
"sentencia_ejecutable : sentencia_foreach",
"sentencia_ejecutable : sentencia_print",
"sentencia_ejecutable : asignacion",
"seleccion : IF condicion cuerpo_if",
"seleccion : condicion cuerpo_if",
"cuerpo_if : bloque_de_sentencias_if END_IF ';'",
"cuerpo_if : bloque_de_sentencias_if ELSE bloque_de_sentencias_else END_IF ';'",
"cuerpo_if : bloque_de_sentencias_if ';'",
"cuerpo_if : bloque_de_sentencias_if ELSE bloque_de_sentencias_else ';'",
"bloque_de_sentencias_if : bloque_de_sentencias",
"bloque_de_sentencias_else : bloque_de_sentencias",
"bloque_de_sentencias : sentencia_ejecutable",
"bloque_de_sentencias : BEGIN lista_de_sentencias END",
"bloque_de_sentencias : BEGIN END",
"bloque_de_sentencias : lista_de_sentencias END",
"bloque_de_sentencias : BEGIN lista_de_sentencias",
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

//#line 455 ".\gramatica.y"

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
//#line 653 "Parser.java"
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
case 1:
//#line 26 ".\gramatica.y"
{ raiz = new NodoArbol("PROGRAMA", null , val_peek(0));
			}
break;
case 2:
//#line 28 ".\gramatica.y"
{ raiz = new NodoArbol("PROGRAMA", null , null);
			}
break;
case 3:
//#line 30 ".\gramatica.y"
{ raiz = new NodoArbol("PROGRAMA", val_peek(0), null);
			}
break;
case 6:
//#line 38 ".\gramatica.y"
{ agregarTipoTS(val_peek(2).sval, val_peek(1).obj);}
break;
case 7:
//#line 40 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Declaracion incorrecta. Se esperaba ';'.", AnalizadorLexico.cantLineas)); }
break;
case 8:
//#line 41 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Sentencia invalida.", AnalizadorLexico.cantLineas)); yyval=new NodoArbol("ERROR SINTACTICO", null, null);}
break;
case 11:
//#line 49 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion" + "\n"); 
					     agregarUsoTS(val_peek(0).sval, Token.USO_VARIABLE); 

					     ArrayList<String> listaDeVariables = new ArrayList<String>();
					     listaDeVariables.add(0, val_peek(0).sval);
					     yyval.obj = listaDeVariables;
					    }
break;
case 12:
//#line 57 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion" + "\n"); 
				   										  agregarUsoTS(val_peek(3).sval, Token.USO_COLECCION);
				   										  if (val_peek(1) != null)
				   										  	inferirTamanio(val_peek(3).sval, val_peek(1).obj);
				   										  
				   										  ArrayList<String> listaDeVariables = new ArrayList<String>();
				   										  listaDeVariables.add(0, val_peek(3).sval);
				   										  yyval.obj = listaDeVariables;
														}
break;
case 13:
//#line 66 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Declaracion" + "\n"); 
				   				   agregarUsoTS(val_peek(3).sval, Token.USO_COLECCION);
				   				   agregarTamanio(val_peek(3).sval, val_peek(1).sval);

				   				   ArrayList<String> listaDeVariables = new ArrayList<String>();
				   				   listaDeVariables.add(0, val_peek(3).sval);
				   				   yyval.obj = listaDeVariables;
								 }
break;
case 14:
//#line 75 ".\gramatica.y"
{ agregarUsoTS(val_peek(2).sval, Token.USO_VARIABLE); 

				   								((ArrayList<String>)val_peek(0).obj).add(0, val_peek(2).sval);
				   								yyval.obj = val_peek(0).obj;
				   							  }
break;
case 15:
//#line 81 ".\gramatica.y"
{ agregarUsoTS(val_peek(5).sval, Token.USO_COLECCION);
				   																if (val_peek(3) != null)
				   																	inferirTamanio(val_peek(5).sval, val_peek(3).obj);

				   																((ArrayList<String>)val_peek(0).obj).add(0, val_peek(5).sval);
				   																yyval.obj = val_peek(0).obj;
				   															   }
break;
case 16:
//#line 89 ".\gramatica.y"
{ agregarUsoTS(val_peek(5).sval, Token.USO_COLECCION);
				   										  agregarTamanio(val_peek(5).sval, val_peek(3).sval);

				   										  ((ArrayList<String>)val_peek(0).obj).add(0, val_peek(5).sval);
				   										  yyval.obj = val_peek(0).obj;
				   										}
break;
case 17:
//#line 97 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba '[' para definir tamaño de la coleccion. ", AnalizadorLexico.cantLineas)); }
break;
case 18:
//#line 98 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba ']' para definir tamaño de la coleccion. ", AnalizadorLexico.cantLineas)); }
break;
case 19:
//#line 99 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba '[' para definir los valores iniciales de la coleccion. ", AnalizadorLexico.cantLineas)); }
break;
case 20:
//#line 100 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba ']' para definir los valores iniciales de la coleccion. ", AnalizadorLexico.cantLineas)); }
break;
case 21:
//#line 101 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba valor entre '[' ']' para definir a la coleccion. ", AnalizadorLexico.cantLineas)); }
break;
case 22:
//#line 104 ".\gramatica.y"
{ ArrayList<String> listaDeVariables = new ArrayList<String>();
					    		  listaDeVariables.add(0, val_peek(0).sval);
					     		  yyval.obj = listaDeVariables;
					     		}
break;
case 23:
//#line 109 ".\gramatica.y"
{ArrayList<String> listaDeVariables = new ArrayList<String>();
					    		  		 listaDeVariables.add(0, val_peek(0).sval);
					    		  		 listaDeVariables.add(0, val_peek(2).sval);
					     		  		 yyval.obj = listaDeVariables;
						   				}
break;
case 24:
//#line 114 ".\gramatica.y"
{ArrayList<String> listaDeVariables = new ArrayList<String>();
					    		  		 listaDeVariables.add(0, val_peek(0).sval);
					    		  		 listaDeVariables.add(0, val_peek(2).sval);
					     		  		 yyval.obj = listaDeVariables;
						   				}
break;
case 25:
//#line 119 ".\gramatica.y"
{
						   										((ArrayList<String>)val_peek(0).obj).add(0, val_peek(2).sval);
				   												yyval.obj = val_peek(0).obj;
						   									   }
break;
case 26:
//#line 123 ".\gramatica.y"
{
						   										((ArrayList<String>)val_peek(0).obj).add(0, val_peek(2).sval);
				   												yyval.obj = val_peek(0).obj;
						   									   }
break;
case 27:
//#line 128 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); yyval=null;}
break;
case 28:
//#line 129 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); yyval=null;}
break;
case 29:
//#line 130 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); yyval=null;}
break;
case 30:
//#line 131 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba caracter ',' de separacion de valores iniciales. ", AnalizadorLexico.cantLineas)); yyval=null;}
break;
case 32:
//#line 136 ".\gramatica.y"
{yyval.sval = modificarContadorDeReferencias(val_peek(0).sval);}
break;
case 33:
//#line 139 ".\gramatica.y"
{yyval = val_peek(1);}
break;
case 34:
//#line 140 ".\gramatica.y"
{yyval=null;}
break;
case 35:
//#line 142 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba 'begin' al comienzo. ", AnalizadorLexico.cantLineas)); yyval=null;}
break;
case 36:
//#line 143 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba 'end' al final. ", AnalizadorLexico.cantLineas)); yyval=null;}
break;
case 37:
//#line 144 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Ausencia de begin y end de sentencias ejecutables.", AnalizadorLexico.cantLineas)); yyval=null;}
break;
case 38:
//#line 147 ".\gramatica.y"
{ yyval = val_peek(0) ; }
break;
case 39:
//#line 148 ".\gramatica.y"
{ yyval = new NodoArbol("Sentencia Ejecutable", val_peek(1), val_peek(0)); }
break;
case 40:
//#line 152 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia if. " + "\n"); 
								  yyval = val_peek(0);
						        }
break;
case 41:
//#line 155 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia foreach. " + "\n");
					 			          yyval = val_peek(0);
					                    }
break;
case 42:
//#line 158 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Sentencia print. " + "\n");
					 			        yyval = val_peek(0);
					                  }
break;
case 43:
//#line 161 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Asignacion. " + "\n");
					 			   yyval = val_peek(0);
					             }
break;
case 44:
//#line 166 ".\gramatica.y"
{ yyval = new NodoArbol("IF", val_peek(1), val_peek(0));
									NodoArbol aux = (NodoArbol) val_peek(1);
									aux.setNroIdentificador(contadorDeIf);

									aux = (NodoArbol) val_peek(0);
									aux.setNroIdentificador(contadorDeIf);

									aux = (NodoArbol) val_peek(0);
									aux = aux.getNodoIzq();
									aux.setNroIdentificador(contadorDeIf);

									contadorDeIf++;
			                       }
break;
case 45:
//#line 180 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Ausencia de palabra reservada 'if'. ", AnalizadorLexico.cantLineas)); yyval=new NodoArbol("ERROR SINTACTICO", null, null);}
break;
case 46:
//#line 184 ".\gramatica.y"
{  
												  NodoArbol aux = new NodoArbol("CUERPO", val_peek(2), null);
												  aux.setNroIdentificador(contadorDeIf);
												  yyval = aux;

			                                   }
break;
case 47:
//#line 191 ".\gramatica.y"
{ 
		  																		yyval = new NodoArbol("CUERPO", val_peek(4), val_peek(2));
		                                                                     }
break;
case 48:
//#line 195 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Ausencia de palabra reservada 'end_if'. ", AnalizadorLexico.cantLineas)); yyval=null;}
break;
case 49:
//#line 196 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Ausencia de palabra reservada 'end_if'. ", AnalizadorLexico.cantLineas)); yyval=null;}
break;
case 50:
//#line 199 ".\gramatica.y"
{  
												NodoArbol aux = new NodoArbol("THEN", val_peek(0), null);
												aux.setNroIdentificador(contadorDeIf);
												yyval = aux;
							                   }
break;
case 51:
//#line 207 ".\gramatica.y"
{ yyval = new NodoArbol("ELSE", val_peek(0), null);
					                             }
break;
case 52:
//#line 212 ".\gramatica.y"
{ yyval = val_peek(0);						                   }
break;
case 53:
//#line 214 ".\gramatica.y"
{ yyval = val_peek(1); }
break;
case 54:
//#line 216 ".\gramatica.y"
{ yyval = new NodoArbol("Sentencia Ejecutable", null, null);
					            }
break;
case 55:
//#line 219 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba 'begin' al comienzo del bloque de sentencias. ", AnalizadorLexico.cantLineas));  yyval=new NodoArbol("ERROR SINTACTICO", null, null);}
break;
case 56:
//#line 220 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba 'end' al final del bloque de sentencias. ", AnalizadorLexico.cantLineas));  yyval=new NodoArbol("ERROR SINTACTICO", null, null);}
break;
case 57:
//#line 224 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Condicion " + "\n"); 
													NodoArbol nodo_cond = new NodoArbol(val_peek(2).sval, val_peek(3), val_peek(1));
													nodo_cond.setNroIdentificador(contadorDeIf);
													yyval = new NodoArbol("CONDICION", nodo_cond, null);
												  }
break;
case 58:
//#line 230 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba una expresion del lado derecho para comparar. ", AnalizadorLexico.cantLineas)); yyval=null;}
break;
case 59:
//#line 231 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba una expresion del lado izquierdo para comparar. ", AnalizadorLexico.cantLineas)); yyval=null;}
break;
case 60:
//#line 232 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba ')' que cierre condicion. ", AnalizadorLexico.cantLineas)); yyval=null;}
break;
case 61:
//#line 233 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba una condicion entre '(' ')'. ", AnalizadorLexico.cantLineas)); yyval=null;}
break;
case 68:
//#line 244 ".\gramatica.y"
{ 
															  Boolean variable_declarada = estaDeclarada(val_peek(4).sval);
															  Boolean coleccion_declarada = estaDeclarada(val_peek(2).sval);
															  if ( variable_declarada && coleccion_declarada ){
																Token token_variable = AnalizadorLexico.tablaSimbolos.get(val_peek(4).sval);
																Token token_coleccion = AnalizadorLexico.tablaSimbolos.get(val_peek(2).sval);
																
																checkearUsoCorrecto(token_variable.getUso(), Token.USO_VARIABLE);
																checkearUsoCorrecto(token_coleccion.getUso(), Token.USO_COLECCION);

																if(!(token_variable.getTipoDeDato().equals(token_coleccion.getTipoDeDato()))) {
																	errores.add(new Error("ERROR", "Tipo de dato de "+ val_peek(4).sval + " es " + token_variable.getTipoDeDato() + " no es compatible con el tipo de dato de "+ val_peek(2).sval + " que es " + token_coleccion.getTipoDeDato(), AnalizadorLexico.cantLineas));
																}

															  }

															  String lexema = "@itForeach" + contadorDeForeach;
										 			          Integer id = AnalizadorLexico.palabras_reservadas.get("id");
															  Token token = new Token(lexema, AnalizadorLexico.TIPO_ID, id);
															  token.setTipoDeDato(AnalizadorLexico.TIPO_DATO_ENTERO);
															  token.setUso(Token.USO_VARIABLE_AUX);
															  AnalizadorLexico.tablaSimbolos.put(lexema, token);

															  NodoArbol nodo_variable = new NodoArbol(val_peek(4).sval, null, null);
															  NodoArbol nodo_coleccion = new NodoArbol(val_peek(2).sval, null, null);
															  NodoArbol nodo_condicion = new NodoArbol("CONDICION_FOREACH", nodo_variable, nodo_coleccion);
															  nodo_condicion.setNroIdentificador(contadorDeForeach);
															  NodoArbol nodo_cuerpo_foreach = new NodoArbol("CUERPO_FOREACH", val_peek(1), null);
															  nodo_cuerpo_foreach.setNroIdentificador(contadorDeForeach);
															  contadorDeForeach++;
															  yyval = new NodoArbol("FOREACH",nodo_condicion ,nodo_cuerpo_foreach);
					                                        }
break;
case 69:
//#line 277 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba el nombre de la variable para iterar. ", AnalizadorLexico.cantLineas)); yyval=new NodoArbol("ERROR SINTACTICO", null, null);}
break;
case 70:
//#line 278 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba 'in' y se encontró el nombre de la coleccion. ", AnalizadorLexico.cantLineas));  yyval=new NodoArbol("ERROR SINTACTICO", null, null);}
break;
case 71:
//#line 279 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba el nombre de la coleccion y se encontraron sentencias. ", AnalizadorLexico.cantLineas)); yyval=new NodoArbol("ERROR SINTACTICO", null, null);}
break;
case 72:
//#line 283 ".\gramatica.y"
{ 
											String lexema = "cadena"+contadorDeCadenas;
											contadorDeCadenas++;
											Token t = AnalizadorLexico.tablaSimbolos.get(lexema);

											agregarUsoTS(lexema, Token.USO_CADENA);
											NodoArbol nodo_cadena = new NodoArbol(lexema, null, null);
											yyval = new NodoArbol("PRINT", nodo_cadena, null);
				 						  }
break;
case 73:
//#line 293 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba '(' y se encontro una cadena. ", AnalizadorLexico.cantLineas)); yyval=new NodoArbol("ERROR SINTACTICO", null, null);}
break;
case 74:
//#line 294 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba ')' y se encontro ';'. ", AnalizadorLexico.cantLineas)); yyval=new NodoArbol("ERROR SINTACTICO", null, null);}
break;
case 75:
//#line 300 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Suma. " + "\n"); 
								NodoArbol aux = new NodoArbol("+", val_peek(2), val_peek(0));
								aux.setTipoDeDato((NodoArbol)val_peek(2), (NodoArbol) val_peek(0));
								yyval = aux; 
								}
break;
case 76:
//#line 305 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Resta. " + "\n"); 
		  						NodoArbol aux = new NodoArbol("-", val_peek(2), val_peek(0));
		  						aux.setTipoDeDato((NodoArbol) val_peek(2), (NodoArbol) val_peek(0)); 
		  						yyval = aux;
		                       }
break;
case 77:
//#line 310 ".\gramatica.y"
{ yyval = val_peek(0);
		           }
break;
case 78:
//#line 314 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Multiplicacion. " + "\n"); 
							NodoArbol aux = new NodoArbol("*", val_peek(2), val_peek(0));
							aux.setTipoDeDato((NodoArbol)val_peek(2), (NodoArbol) val_peek(0)); /*adentro se chequea que los tipos sean iguales*/
							yyval = aux;
						  }
break;
case 79:
//#line 320 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Division. " + "\n"); 
							NodoArbol aux = new NodoArbol("/", val_peek(2), val_peek(0));
							aux.setTipoDeDato((NodoArbol)val_peek(2), (NodoArbol)val_peek(0)); /*chequear que los tipos sean iguales*/
							yyval = aux;
						  }
break;
case 80:
//#line 326 ".\gramatica.y"
{ yyval = val_peek(0);
		        }
break;
case 81:
//#line 330 ".\gramatica.y"
{ estaDeclarada(val_peek(0).sval);

			 Token id = AnalizadorLexico.tablaSimbolos.get(val_peek(0).sval);
			 checkearUsoCorrecto(id.getUso(), Token.USO_VARIABLE);

			 estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor ID. " + "\n"); 
			 NodoArbol aux = new NodoArbol(val_peek(0).sval, null, null);
			 aux.setTipoDeDato(id.getTipoDeDato());
			 yyval = aux;
		   }
break;
case 82:
//#line 341 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE. " + "\n"); 
	   		  NodoArbol aux = new NodoArbol(val_peek(0).sval, null, null);
			  aux.setTipoDeDato(AnalizadorLexico.tablaSimbolos.get(val_peek(0).sval).getTipoDeDato());
			  yyval = aux;
			}
break;
case 83:
//#line 347 ".\gramatica.y"
{  estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor CTE Negativa. " + "\n");
	   			  String lexema = modificarContadorDeReferencias(val_peek(0).sval);

	   			  Token t = AnalizadorLexico.tablaSimbolos.get(lexema);
	   			  t.setUso(Token.USO_CONSTANTE);
	   			  NodoArbol aux = new NodoArbol(lexema, null, null);
			      aux.setTipoDeDato(AnalizadorLexico.tablaSimbolos.get(lexema).getTipoDeDato());
			      yyval = aux;

	   			}
break;
case 84:
//#line 358 ".\gramatica.y"
{ estaDeclarada(val_peek(3).sval);

	   						Token id = AnalizadorLexico.tablaSimbolos.get(val_peek(3).sval);
	   						checkearUsoCorrecto(id.getUso(), Token.USO_COLECCION);

	   						estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor coleccion. " + "\n");
	   						NodoArbol nodo_id = new NodoArbol(val_peek(3).sval, null, null);
							nodo_id.setTipoDeDato(id.getTipoDeDato());
		   					

	   						NodoArbol aux = new NodoArbol("ELEM_COLEC", nodo_id, val_peek(1)); 
	   						aux.setTipoDeDato(nodo_id.getTipoDeDato()); 
	   						yyval = aux;
						  }
break;
case 85:
//#line 373 ".\gramatica.y"
{ estructuras.add("Linea: " + AnalizadorLexico.cantLineas + ". Factor conversion. " + "\n"); 

	   							  NodoArbol aux = new NodoArbol("CONVERSION", val_peek(1), null);
	   							  aux.setTipoDeDato(AnalizadorLexico.TIPO_DATO_ULONG);
	   							  yyval = aux;
								}
break;
case 86:
//#line 380 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba '(' para realizar la conversion. ", AnalizadorLexico.cantLineas)); yyval=new NodoArbol("ERROR SINTACTICO", null, null);}
break;
case 87:
//#line 383 ".\gramatica.y"
{	String tipoDeDato = Token.UNDEFINED;
				if (estaDeclarada(val_peek(0).sval)){

					Token id = AnalizadorLexico.tablaSimbolos.get(val_peek(0).sval);
					tipoDeDato = id.getTipoDeDato();

					if(!tipoDeDato.equals(AnalizadorLexico.TIPO_DATO_ENTERO)){
		  				errores.add(new Error("ERROR", "El tipo de dato del subindice debe ser entero y no "+tipoDeDato, AnalizadorLexico.cantLineas));
		  		 	}

		  		 	checkearUsoCorrecto(id.getUso(), Token.USO_VARIABLE);
				}
				
				NodoArbol aux = new NodoArbol(val_peek(0).sval, null, null);
				aux.setTipoDeDato(tipoDeDato);
				yyval = aux;

			  }
break;
case 88:
//#line 401 ".\gramatica.y"
{
		  		Token cons = AnalizadorLexico.tablaSimbolos.get(val_peek(0).sval);
		  		String tipoDeDato = cons.getTipoDeDato();

		  		if(!tipoDeDato.equals(AnalizadorLexico.TIPO_DATO_ENTERO)){
		  			errores.add(new Error("ERROR", "El tipo de dato del subindice debe ser entero y no "+tipoDeDato, AnalizadorLexico.cantLineas));
		  		 }

		  		 NodoArbol aux = new NodoArbol(val_peek(0).sval, null, null);
				 aux.setTipoDeDato(tipoDeDato);
				 yyval = aux;

			   }
break;
case 89:
//#line 416 ".\gramatica.y"
{  estaDeclarada(val_peek(3).sval);

									Token id = AnalizadorLexico.tablaSimbolos.get(val_peek(3).sval);
									
									checkearUsoCorrecto(id.getUso(), Token.USO_VARIABLE);

									NodoArbol nodo_id = new NodoArbol(val_peek(3).sval, null, null);
									nodo_id.setTipoDeDato(id.getTipoDeDato());

									NodoArbol aux = new NodoArbol(":=", nodo_id , val_peek(1));
									aux.setTipoDeDato(nodo_id.getTipoDeDato(), ((NodoArbol)val_peek(1)).getTipoDeDato()); 	
									yyval = aux;
			                       }
break;
case 90:
//#line 430 ".\gramatica.y"
{  estaDeclarada(val_peek(6).sval);

		   											Token id = AnalizadorLexico.tablaSimbolos.get(val_peek(6).sval); 

													checkearUsoCorrecto(id.getUso(), Token.USO_COLECCION);

		   										    NodoArbol nodo_id = new NodoArbol(val_peek(6).sval, null, null);
													nodo_id.setTipoDeDato(id.getTipoDeDato());

	   												NodoArbol nodo_colec = new NodoArbol("ELEM_COLEC", nodo_id, val_peek(4)); 
	   												nodo_colec.setTipoDeDato(nodo_id.getTipoDeDato()); 

		   											NodoArbol aux = new NodoArbol(":=", nodo_colec, val_peek(1));
		   											aux.setTipoDeDato(nodo_colec.getTipoDeDato(), ((NodoArbol)val_peek(1)).getTipoDeDato());
		   											yyval = aux;

		                                         }
break;
case 91:
//#line 448 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba una expresion para realizar la asignacion. ", AnalizadorLexico.cantLineas)); yyval=new NodoArbol("ERROR SINTACTICO", null, null);}
break;
case 92:
//#line 449 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba '[' para indicar la posicion de la coleccion. ", AnalizadorLexico.cantLineas)); yyval=new NodoArbol("ERROR SINTACTICO", null, null);}
break;
case 93:
//#line 450 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba ']' para indicar la posicion de la coleccion. ", AnalizadorLexico.cantLineas)); yyval=new NodoArbol("ERROR SINTACTICO", null, null);}
break;
case 94:
//#line 451 ".\gramatica.y"
{ errores.add(new Error("ERROR", "Se esperaba un subindice para realizar la asignacion. ", AnalizadorLexico.cantLineas)); yyval=new NodoArbol("ERROR SINTACTICO", null, null);}
break;
//#line 1381 "Parser.java"
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
