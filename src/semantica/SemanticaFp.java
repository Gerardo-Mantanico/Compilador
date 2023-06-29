/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package semantica;

import compilerTools.Token;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTextArea;

/**
 *
 * @author HP
 */
public class SemanticaFp {

    private Map<String, ArrayList<Token>> bloqueMetodoFunciones;
    private Map<String, String> listVariable = new HashMap<>();
    private ArrayList<Token> listToken;
    private Map<String, String> lisfun = new HashMap<>();
    private int estado = 0;
    private String error="";

    public SemanticaFp(Map<String, ArrayList<Token>> bloqueMetodoFunciones, ArrayList<Token> listToken) {
        this.bloqueMetodoFunciones = bloqueMetodoFunciones;
        this.listToken = listToken;
        identificarTiposVariables();
        identificarFuncionesMetodos();
        verificacion();
    }
    
    //metodo para buscar funcions o metodos en el codigo principal
    public void identificarFuncionesMetodos() {
        String lexema = "";
        String nombre= "";
        for (Token token : listToken) {
            switch (estado) {
                case 0:
                    if (token.getLexicalComp().equals("IDENTIFICADOR")) {
                        lexema = token.getLexeme();
                        nombre=token.getLexeme();
                        estado = 1;
                    } else {
                        estado = 0;
                    }
                    break;
                case 1:
                    if (token.getLexicalComp().equals("PARENTESIS_DE_APERTURA")) {
                        lexema = lexema + token.getLexeme();
                        estado = 2;
                    } else {
                        estado = 0;
                    }
                    break;

                case 2:
                    if (token.getLexicalComp().equals("PARENTESIS_DE_CIERRE")) {
                        estado = 3;
                        lexema = lexema + token.getLexeme();

                    } else if (token.getLexicalComp().equals("IDENTIFICADOR")) {
                        estado = 2;
                        lexema = lexema + " " + listVariable.get(token.getLexeme());
                    } else if (token.getLexicalComp().equals("ENTERO")) {
                        lexema = lexema + " " + token.getLexicalComp();
                          estado = 2;

                    } else if (token.getLexicalComp().equals("FLOTANTE")) {
                        lexema = lexema + " " + token.getLexicalComp();
                          estado = 2;
                    }
                    break;
                case 3:
                    if (token.getLexicalComp().equals("PUNTO_COMA")) {
                        estado = 0;
                        this.lisfun.put(nombre, lexema);
                    } else {
                        estado = 0;
                    }
                    break;
                default:
                    throw new AssertionError();
            }
        }

    }

     //metodo para buscar variables y identificar que tipo es 
    public void identificarTiposVariables() {
        String tipo="";
        String var="";
        for (Token token : listToken) {
            switch (estado) {
                case 0:
                    if (token.getLexicalComp().equals("TIPO_DE_VARIABLE")) {
                        tipo = token.getLexeme();
                        estado = 1;
                    }
                    break;
                case 1:
                    if (token.getLexicalComp().equals("IDENTIFICADOR")) {
                        var =token.getLexeme();
                        listVariable.put(var, tipo);
                        estado = 0;
                    } else {
                        estado = 0;
                    }
                    break;
                default:
                    throw new AssertionError();
            }
        }

    }

    
    //metodo para buscar la funcion en el codigo general 
    public String buscarFp(String tipo) {
        String lexema = "";
        for (Token token : this.bloqueMetodoFunciones.get(tipo)) {
            switch (estado) {
                case 0:
                    if (token.getLexicalComp().equals("IDENTIFICADOR")) {
                        lexema = token.getLexeme();
                        estado = 1;
                    } else {
                        estado = 0;
                    }
                    break;
                case 1:
                    if (token.getLexicalComp().equals("PARENTESIS_DE_APERTURA")) {
                        lexema = lexema + token.getLexeme();
                        estado = 2;
                    } else {
                        estado = 0;
                    }
                    break;

                case 2:
                    if (token.getLexicalComp().equals("PARENTESIS_DE_CIERRE")) {
                        lexema = lexema + token.getLexeme();
                        estado = 0;
                        return lexema;
                    }
                    if (token.getLexicalComp().equals("TIPO_DE_VARIABLE")) {
                        lexema = lexema + " " + token.getLexeme();
                        estado = 2;
                    }
                    break;

                default:
                    throw new AssertionError();
            }

        }
        return lexema;
    }
    public void verificacion(){
        for (Map.Entry<String, String> entry : this.lisfun.entrySet()) {
            String nombre = entry.getKey();
            String lexema = entry.getValue();
            if(lexema.equals(buscarFp(nombre))){
            }
            else { 
                error="error de declaracion  "+ buscarFp(nombre);}
        }
    
    }

    public String getError() {
        return error;
    }
    
    
  
}
