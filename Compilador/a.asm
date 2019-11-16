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
cadena1 DB "mensaje mas largo",0
_a DW ?
cadena0 DB "mensaje mas largo",0
cadena2 DB "cadena ........ _ bien definida < > , ; ? &@!",0
.code
start:
invoke MessageBox, NULL, addr cadena0, addr TituloCadena, MB_OK
invoke MessageBox, NULL, addr cadena1, addr TituloCadena, MB_OK
invoke MessageBox, NULL, addr cadena2, addr TituloCadena, MB_OK
LabelError:
invoke ExitProcess, 0
end start
