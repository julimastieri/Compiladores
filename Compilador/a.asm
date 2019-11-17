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
cadena0 DB "correcto",0
_b DD ?
_c DW ?
_d DD ?
_e DW 3,2,5
_f DD 40600,50842
_g DW 4 DUP (?)
_h DD 3 DUP (?)
.code
start:
MOV EAX,4
IMUL EAX,0
ADD EAX, offset _e
MOV @aux1, EAX
MOV EAX, @aux1
MOV AX,[EAX]
MOV _a,AX
CMP _a,3
JNE LabelElse0
invoke MessageBox, NULL, addr cadena0, addr TituloCadena, MB_OK
JMP LabelSiguiente0
LabelElse0:
LabelSiguiente0:
LabelError:
invoke ExitProcess, 0
end start
