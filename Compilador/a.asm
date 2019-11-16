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
.code
start:
MOV AX,6
MOV _b,AX
MOV AX,2
MOV _a,AX
CMP _a,_b
JL LabelElse1
CMP _a,_b
JNE LabelElse0
MOV AX,2
MOV _a,AX
MOV AX,12
MOV _b,AX
JMP LabelSiguiente0
LabelElse0:
LabelSiguiente0:
JMP LabelSiguiente1
LabelElse1:
LabelSiguiente1:
CMP _a,_b
JNE LabelElse2
MOV AX,2
MOV _a,AX
MOV AX,12
MOV _b,AX
JMP LabelSiguiente2
LabelElse2:
LabelSiguiente2:
LabelError:
invoke ExitProcess, 0
end start
