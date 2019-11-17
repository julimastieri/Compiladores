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
MsgError db "Error en ejecucion",0
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
MOV EAX,8
IMUL EAX,0
ADD EAX, offset _h
MOV EBX,40200
MOV [EAX],EBX

MOV EAX,8
IMUL EAX,0
ADD EAX, offset _h

MOV BX,[EAX]
MOV CX,40200
CMP BX,CX

JNE LabelElse0
invoke MessageBox, NULL, addr cadena0, addr TituloCadena, MB_OK
JMP LabelSiguiente0
LabelElse0:
LabelSiguiente0:
LabelError:
invoke ExitProcess, 0
end start
