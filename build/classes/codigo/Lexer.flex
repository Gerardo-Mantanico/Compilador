package codigo;
import compilerTools.Token;

%%
%class Lexer
%type Token
%line
%column
%{
     public String lexeme;
    private Token token(String lexeme, String lexicalComp, int line, int column){
        return new Token(lexeme, lexicalComp, line+1, column+1);
    }
%}

L=[a-zA-Z]+
D=[0-9]+
espacio=[ ,\t,\r]+

%%

/* Espacios en blanco */
{espacio} {/*Ignore*/}

/* Comentarios */
( "//"(.)* ) {/*Ignore*/}

/* Salto de linea */
( "\n" ) { return token(yytext(), "SALTO_DE_LINEA", yyline, yycolumn); }

/* Comillas */
( "\"" ) { return token(yytext(), "COMILLA", yyline, yycolumn); }

/* Tipos de varibles */
( ENTERO | CADENA | FLOTANTE ) { return token(yytext(), "TIPO_DE_VARIABLE", yyline, yycolumn); }

/* Palabra reservada Escribir */
( ESCRIBIR ) { return token(yytext(), "PALABRA_RESERVADA", yyline, yycolumn); }

/* Palabra reservada Escribir */
( LEER ) { return token(yytext(), "PALABRA_RESERVADA", yyline, yycolumn); }

/* Palabra reservada If */
( SI ) { return token(yytext(), "PALABR_RESERVADA", yyline, yycolumn); }

/* Palabra reservada Else */
( SINO ) { return token(yytext(), "PALABRA_RESERVADA", yyline, yycolumn); }


/* Palabra reservada While */
( MIENTRAS ) { return token(yytext(), "PALABRA_RESERVADA", yyline, yycolumn); }

/* Palabra reservada For */
( PARA ) { return token(yytext(), "PALABRA_RESERVADA", yyline, yycolumn); }

/* Operador Igual */
( "=" ) { return token(yytext(), "SIMBOLO_MATEMATICOS", yyline, yycolumn); }

/* Operador Suma */
( "+" ) { return token(yytext(), "SIMBOLO_MATEMATICOS", yyline, yycolumn); }

/* Operador Resta */
( "-" ) { return token(yytext(), "SIMBOLO_MATEMATICOS", yyline, yycolumn); }

/* Operador Multiplicacion */
( "*" ) { return token(yytext(), "SIMBOLO_MATEMATICOS", yyline, yycolumn); }

/* Operador Division */
( "/" ) { return token(yytext(), "SIMBOLO_MATEMATICOS", yyline, yycolumn); }

/* operador Asignacion*/
( ":" ) { return token(yytext(), "OPERADOR_DE_ASIGNACION", yyline, yycolumn); }

/*Operadores Relacionales */
( ">" | "<" | "==" | "!=" | ">=" | "<=" | "<<" | ">>" ) { return token(yytext(), "OPERADORES_RELACIONALES", yyline, yycolumn); }

/* Parentesis de apertura */
( "(" ) { return token(yytext(), "PARENTESIS_DE_APERTURA", yyline, yycolumn); }

/* Parentesis de cierre */
( ")" ) { return token(yytext(), "PARENTESIS_DE_CIERRE", yyline, yycolumn); }

/* Llave de apertura */
( "{" ) { return token(yytext(), "LLAVE_DE_APERTURA", yyline, yycolumn); }

/* Llave de cierre */
( "}" ) { return token(yytext(), "LLAVE_DE_CIERRE", yyline, yycolumn); }

/* Punto y coma */
( ";" ) { return token(yytext(), "PUNTO_COMA", yyline, yycolumn); }

/* Identificador */
{L}({L}|{D})* { return token(yytext(), "IDENTIFICADOR", yyline, yycolumn); }

/* Numero entero */
("(-"{D}+")")|{D}+ { return token(yytext(), "ENTERO", yyline, yycolumn); }

/* Numero flotante*/
{D}+"."{D}+ { return token(yytext(), "FLOTANTE", yyline, yycolumn); }

/* Error de analisis */
 . { return token(yytext(), "ERROR", yyline, yycolumn); }




/*{espacio} {/*Ignore*/}
{Identificador} {return token(yytext(), "IDENTIFICADOR", yyline, yycolumn);}
{Digito} {return token(yytext(), "NUMERO_ENTERO", yyline, yycolumn);}*/


