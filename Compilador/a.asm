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
@aux2 DW ?
_a DW ?
cadena0 DB "wama",0
_b DW ?
_c DD ?
_d DW 1,2,3
_w DD ?
_z DD 40000,40001,40002
.code
start:
MOV EDX,1
CMP EDX,3
JGE LabelError
CMP EDX,0 
JL LabelError
MOV EAX,4
MUL EDX
ADD EAX, offset _z
MOV EBX,[EAX]
MOV _c,EBX
MOV EAX,_c
MOV EBX,40001
CMP EAX,EBX
JNE LabelElse0
invoke MessageBox, NULL, addr cadena0, addr TituloCadena, MB_OK
JMP LabelSiguiente0
LabelElse0:
LabelSiguiente0:
LabelError:
invoke ExitProcess, 0
end start
