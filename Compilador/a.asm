.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
TituloCadena db "Cadena",0
@aux1 DD ?
_a DW ?
_b DW ?
_c DD ?
_d DW 1,2,3
@itForeach0 DW ?
@itForeach2 DW ?
@itForeach1 DW ?
@itForeach3 DW ?
.code
start:
MOV AX,6
MOV _b,AX
MOV AX,2
MOV _a,AX
MOV @itForeach2,0
LabelCondForech2:
CMP @itForeach2,3
JGE LabelSiguienteForeach2
MOV AX,@itForeach2
MUL AX,8
ADD AX, offset _d
MOV AX,[AX]
MOV _a,AX
ADD @itForeach2,1
MOV @itForeach1,0
LabelCondForech1:
CMP @itForeach1,3
JGE LabelSiguienteForeach1
MOV AX,@itForeach1
MUL AX,8
ADD AX, offset _d
MOV AX,[AX]
MOV _a,AX
ADD @itForeach1,1
MOV @itForeach0,0
LabelCondForech0:
CMP @itForeach0,3
JGE LabelSiguienteForeach0
MOV AX,@itForeach0
MUL AX,8
ADD AX, offset _d
MOV AX,[AX]
MOV _a,AX
ADD @itForeach0,1
MOV AX,100
ADD AX,_a
MOV _b,AX
JMP LabelCondForech0
LabelSiguienteForeach0:
JMP LabelCondForech1
LabelSiguienteForeach1:
JMP LabelCondForech2
LabelSiguienteForeach2:
MOV @itForeach3,0
LabelCondForech3:
CMP @itForeach3,3
JGE LabelSiguienteForeach3
MOV AX,@itForeach3
MUL AX,8
ADD AX, offset _d
MOV AX,[AX]
MOV _a,AX
ADD @itForeach3,1
MOV AX,100
ADD AX,_a
MOV _b,AX
JMP LabelCondForech3
LabelSiguienteForeach3:
LabelError:
invoke ExitProcess, 0
end start
