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
_a DW ?
_b DD ?
_c DW ?
_d DD ?
_e DW 3,2,5
_f DD 40600,50842
_g DW 4 DUP (?)
_h DD 3 DUP (?)
correcto1 DB "correcto 1",0
.code
start:
MOV AX,2
MOV _a,AX
CMP _a,2
JNE LabelElse
invoke MessageBox, NULL, addr correcto1, addr correcto1, MB_OK
JMP LabelSiguiente
LabelElse:
LabelSiguiente:
LabelError:
invoke ExitProcess, 0
end start
