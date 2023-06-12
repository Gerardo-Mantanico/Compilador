package codigo;
import java_cup.runtime.Symbol;
%%
%class LexerCup
%type java_cup.runtime.Symbol
/* para regresar el analisis*/
%cup
/* retornar toda la cadena*/
%full
%line
%char
L=[a-zA-Z]+
D=[0-9]+
espacio=[ ,\t,\r,\n]+
%{
    private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline, yycolumn, value);
    }
    private Symbol symbol(int type){
        return new Symbol(type, yyline, yycolumn);
    }
%}
%%

/* Espacios en blanco */
{espacio} {/*Ignore*/}

/* Comentarios */
( "//"(.)* ) {/*Ignore*/}

/* Comillas */
( "\"" ) {return new Symbol(sym.Comillas, yychar, yyline, yytext());}


/* tipo de varible entero*/
(ENTERO) {return new Symbol(sym.Entero, yychar, yyline, yytext());}

/* tipo de varible String*/
(CADENA) {return new Symbol(sym.Cadena, yychar, yyline, yytext());}

/* tipo de varible flotante*/
(FLOTANTE) {return new Symbol(sym.Flotante, yychar, yyline, yytext());}

/* Palabra reservada Escribir */
( ESCRIBIR ) {return new Symbol(sym.Escribir, yychar, yyline, yytext());}

/* Palabra reservada Escribir */
( LEER ) {return new Symbol(sym.Leer, yychar, yyline, yytext());}

/* Palabra reservada If */
( SI ) {return new Symbol(sym.Si, yychar, yyline, yytext());}

/* Palabra reservada Else */
( SINO ) {return new Symbol(sym.Sino, yychar, yyline, yytext());}


/* Palabra reservada While */
( MIENTRAS ) {return new Symbol(sym.Mientras, yychar, yyline, yytext());}

/* Palabra reservada For */
( PARA ) {return new Symbol(sym.Para, yychar, yyline, yytext());}

/* Operador Igual */
( "=" ) {return new Symbol(sym.Igual, yychar, yyline, yytext());}

/* Operador Suma */
( "+" ) {return new Symbol(sym.Suma, yychar, yyline, yytext());}

/* Operador Resta */
( "-" ) {return new Symbol(sym.Resta, yychar, yyline, yytext());}

/* Operador Multiplicacion */
( "*" ) {return new Symbol(sym.Multiplicacion, yychar, yyline, yytext());}

/* Operador Division */
( "/" ) {return new Symbol(sym.Division, yychar, yyline, yytext());}

/* operador Asignacion*/
( ":" ) {return new Symbol(sym.Asignacion, yychar, yyline, yytext());}

/* coma*/
( "," ) {return new Symbol(sym.Coma, yychar, yyline, yytext());}

/* #*/
( "#" ) {return new Symbol(sym.Numeral, yychar, yyline, yytext());}

/*Operadores Relacionales */
( ">" | "<" | "==" | "!=" | ">=" | "<=" | "<<" | ">>" ) {return new Symbol(sym.Op_relacional, yychar, yyline, yytext());}

/* Parentesis de apertura */
( "(" ) {return new Symbol(sym.Parentesis_a, yychar, yyline, yytext());}

/* Parentesis de cierre */
( ")" ) {return new Symbol(sym.Parentesis_c, yychar, yyline, yytext());} 

/* Llave de apertura */
( "{" ) {return new Symbol(sym.Llave_a, yychar, yyline, yytext());} 

/* Llave de cierre */
( "}" )  {return new Symbol(sym.Llave_c, yychar, yyline, yytext());} 

/* Punto y coma */
( ";" ) {return new Symbol(sym.P_coma, yychar, yyline, yytext());} 

/* Identificador */
{L}({L}|{D})*  {return new Symbol(sym.Identificador, yychar, yyline, yytext());} 

/* Numero entero */
("(-"{D}+")")|{D}+ {return new Symbol(sym.Numero_entero, yychar, yyline, yytext());}

/* Numero flotante*/
{D}+"."{D}+ {return new Symbol(sym.Numero_flotante, yychar, yyline, yytext());}

/* Error de analisis */
 . {return new Symbol(sym.ERROR, yychar, yyline, yytext());}
