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
mensaje DB "mensaje",0
.code
start:
MOV AX,@data
MOV ds,AX
invoke MessageBox, NULL, addr mensaje, addr TituloCadena, MB_OK
LabelError:
invoke ExitProcess, 0
end start
