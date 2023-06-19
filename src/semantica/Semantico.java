/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package semantica;

import compilerTools.CodeBlock;
import compilerTools.Token;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JTextArea;
import recursos.Tipo_variable;
import recursos.Variable;

/**
 *
 * @author HP
 */
public class Semantico {
    private ArrayList<Variable> lista_variable;
    public Semantico(){
        this.lista_variable= new ArrayList<>();
    }
    /*public void analizador(ArrayList<String> blocksOfCode, int repeats) {
        boolean estado_si=false;
        for (int j = 1; j <= repeats; j++) {
            int repeatCode = -1;
            for (int i = 0; i < blocksOfCode.size(); i++) {
                String blockOfCode = blocksOfCode.get(i);
                if (repeatCode != -1) {
                    int[] posicionMarcador = CodeBlock.getPositionOfBothMarkers(blocksOfCode, blockOfCode);
                    analizador(new ArrayList<>(blocksOfCode.subList(posicionMarcador[0], posicionMarcador[1])), repeatCode);
                    repeatCode = -1;
                    i = posicionMarcador[1];
                } else {
                    String[] sentences = blockOfCode.split(";");
                    for (String sentence : sentences) {
                        declararVariable(sentence);
                    }
                }
            }
        }
    }
    
    
    
    //metodo para declarar variable
    public void declararVariable(String codigo){
        entero(codigo);
    }
    
    
    public void entero(String codigo){
        if(codigo.startsWith("ENTERO")){
            String [] s = codigo.split(" ");
              System.out.println(" analizador semantico  "+s[0]+ " " + s[1] +" TAMA;O  "+s.length);
              
        }
        
    }*/
    
    
    public void anali(ArrayList<Token> tokens ,JTextArea textArea){
        int estado=0;
        String variable="";
        lista_variable.clear();
        String tipo_var="";
        for(Token token: tokens){
            switch (estado) {
                case 0:
                    if(token.getLexeme().equals(Tipo_variable.ENTERO.name())){
                        estado=1;
                        tipo_var=Tipo_variable.ENTERO.name();
                    }
                    else if(token.getLexeme().equals(Tipo_variable.FLOTANTE.name())){
                        estado=1;
                        tipo_var=Tipo_variable.FLOTANTE.name();
                    }
                    else{
                        estado=0;
                    }
                    break;
                case 1:
                    variable=token.getLexeme();
                    estado=2;
                    break;
                case 2:
                    if(token.getLexeme().equals(";")){    
                        if(buscar_variable(variable)==false){
                            lista_variable.add(new Variable(variable,"0",tipo_var));
                            estado=0;
                            variable="";
                        }
                        else{
                            textArea.setText("Error en la declaracion "+variable +" Linea: "+token.getLine()+" Columna: "+token.getColumn());
                            textArea.setForeground(Color.red);
                            return;
                        }   
                    }
                    else {estado=3;}
                    break;
                case 3:
                    if (token.getLexeme().equals(";")){
                        estado=0;
                    }
                    
                    else if (buscar_variable(variable)==false){
                            lista_variable.add(new Variable(variable, token.getLexeme(),tipo_var));
                            System.out.println(" la variable es "+variable);
                            estado=3;
                            variable="";
                    }
                    else{
                            textArea.setText("Error en la declaracion "+variable +" Linea: "+token.getLine()+" Columna: "+token.getColumn());
                            textArea.setForeground(Color.red);
                            return;
                    }   
                    break;    
                   
                default:
                  
            }
        }
    
    }
    boolean estado=true;
    public boolean buscar_variable(String var){
        System.out.println(lista_variable.size());
        if(lista_variable.isEmpty()){estado=false;}
        else{
            for(Variable variable: lista_variable ){
            if(variable.getNombre().equals(var)){
                estado=true;
            }
            else {
                estado=false;
            }
        }
        }
        return estado;
    }
    
    public void imprimi(){
        for(Variable var: lista_variable){
            System.out.println("tokenes .....|"+var.getNombre()+" valor "+var.getValor() +"TIPO DE VAR "+var.getTipo());
        }
    }
   
}
