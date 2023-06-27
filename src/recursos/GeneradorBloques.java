/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package recursos;

import compilerTools.Token;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author HP
 */
public class GeneradorBloques {

    private ArrayList<Token> lis;
    private Map<String, ArrayList<Token>> bloqueMetodoFunciones;
    private ArrayList<Token> listPrincipal = new ArrayList();
 
    public GeneradorBloques(ArrayList<Token> lis) {
        this.lis = lis;
        this.bloqueMetodoFunciones = new HashMap<>();
    }

    public void bloques() {
     bloqueMetodoFunciones.clear();
     listPrincipal.clear();
        int estado = 0;
        int parentesis_a = 0;
        String tipo="";
        ArrayList<Token> listfun = null;
        for (Token token : lis) {
           
            switch (estado) {
                case 0:
                    if (token.getLexeme().equals("PRINCIPAL")) {
                        estado = 1;
                        listPrincipal.add(token);
                    } else if (token.getLexeme().equals("FUNCION")) {
                        listfun = new ArrayList<>();
                        estado = 2; 
                        listfun.add(token);
                        
                    }
                    else if (token.getLexeme().equals("PROCEDIMIENTO")) {
                        estado = 3;
                        listfun = new ArrayList<>();
                        listfun.add(token);
                    }
                    
                    break;
                case 1:
                    if (token.getLexeme().equals("{")) {
                        listPrincipal.add(token);
                        parentesis_a++;
                        estado = 1;
                    } else if (token.getLexeme().equals("}")) {
                        listPrincipal.add(token);
                        parentesis_a--;
                        estado = 1;
                    } else if (parentesis_a == 0) {
                        estado=0;
                        parentesis_a=0;
                        
                    } else {
                        listPrincipal.add(token);
                        estado = 1;
                    }
                    break;

                case 2:
                    if(token.getLexeme().equals("ENTERO") || token.getLexeme().equals("FLOTANTE") || token.getLexeme().equals("CADENA")){
                        listfun.add(token);
                        estado=3;
                    }
                    break;
                    
                case 3:
                      tipo=token.getLexeme();
                      listfun.add(token);
                      estado=4;
                    break;
                      
                    
                case 4: 
                        if (token.getLexeme().equals("{")) {
                            listfun.add(token);
                            parentesis_a++;
                            estado = 5;
                        }
                        else {
                            listfun.add(token);
                            estado=4;
                        }
                                        
                    break;
                    
                case 5:
                 
                     if (token.getLexeme().equals("{")) {
                            listfun.add(token);
                            parentesis_a++;
                            estado = 5;
                        }
                     else if (token.getLexeme().equals("}")) {
                        listfun.add(token);
                        parentesis_a--;
                        estado = 5;
                    }
                    
                    
                    else if (parentesis_a == 0) {
                        estado=0;
                        parentesis_a=0;
                        if( bloqueMetodoFunciones.put(tipo, listfun)==null){
                             bloqueMetodoFunciones.put(tipo, listfun);
                        }
                        else {System.out.println(" ya existe este metodo: "+tipo);}
                        
                    }else {
                        listfun.add(token);
                        estado = 5;
                    }
                    
                    break;
                default:
            }
        }
    }

    public ArrayList<Token> getLis() {
        return lis;
    }

    public void setLis(ArrayList<Token> lis) {
        this.lis = lis;
    }

    public Map<String, ArrayList<Token>> getBloqueMetodoFunciones() {
        return bloqueMetodoFunciones;
    }

    public void setBloqueMetodoFunciones(Map<String, ArrayList<Token>> bloqueMetodoFunciones) {
        this.bloqueMetodoFunciones = bloqueMetodoFunciones;
    }

    public ArrayList<Token> getListPrincipal() {
        return listPrincipal;
    }

    public void setListPrincipal(ArrayList<Token> list) {
        this.listPrincipal = list;
    }
    
    
  public void read(){
   System.out.println("*** "+bloqueMetodoFunciones.toString());
  }
}
