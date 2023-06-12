/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codigo;

import compilerTools.CodeBlock;
import compilerTools.Token;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author HP
 */
public class EjecutorCodigo {

    private ArrayList<Variable> lista_variable;
    private JTextArea textArea;

    public EjecutorCodigo(JTextArea textArea) {
        this.textArea = textArea;
        this.lista_variable = new ArrayList<>();

    }

    public void ejecutarCodigo(ArrayList<Token> tokens) {
        this.lista_variable.clear();
        CodeBlock codeBlock = compilerTools.Functions.splitCodeInCodeBlocks(tokens, "{", "}", ";");
        ArrayList<String> blocksOfCode = codeBlock.getBlocksOfCodeInOrderOfExec();
        System.out.println(" CodeBloc: " + blocksOfCode);
        executeCode(blocksOfCode, 1);
    }

    private void executeCode(ArrayList<String> blocksOfCode, int repeats) {
        boolean estado_si=false;
        for (int j = 1; j <= repeats; j++) {
            int repeatCode = -1;
            for (int i = 0; i < blocksOfCode.size(); i++) {
                String blockOfCode = blocksOfCode.get(i);
                if (repeatCode != -1) {
                    int[] posicionMarcador = CodeBlock.getPositionOfBothMarkers(blocksOfCode, blockOfCode);
                    executeCode(new ArrayList<>(blocksOfCode.subList(posicionMarcador[0], posicionMarcador[1])), repeatCode);
                    repeatCode = -1;
                    i = posicionMarcador[1];
                } else {
                    String[] sentences = blockOfCode.split(";");
                    for (String sentence : sentences) {
                        sentence = sentence.trim();
                        
                        //CODIGO PARAR ESCRIBIR
                        if (sentence.startsWith("ESCRIBIR")) {
                            String parametro;
                            String[] identComp = sentence.split(" ");
                            if (identComp.length == 4) {
                                for (Variable var : lista_variable) {
                                    if (var.getNombre().equals(identComp[2])) {
                                        textArea.append("-->" + var.getValor() + " \n ");
                                    }
                                    /*else {
                                        this.textArea.append("--> null \n ");
                                     */
                                }
                            } else {
                                parametro = sentence.substring(12, sentence.length() - 4);
                                textArea.append("-->" + parametro + " \n ");
                            }

                        }
                        else if(sentence.startsWith("LEER")){
                           String var = sentence.substring(7,sentence.length()-2);
                           for(Variable variable: lista_variable){
                               if(variable.getNombre().equals(var)){
                                   String respuesta = JOptionPane.showInputDialog(null, "Ingrese el valor de entrada", "Entrada", JOptionPane.QUESTION_MESSAGE);
                                   variable.setValor(respuesta);
                               }
                               else {
                                   System.out.println("variable no declarada");
                               }
                           }
                            
                        }
                        else if(sentence.startsWith("#")){
                            pasar_parametros(sentence);
                        }

                        //DECLARACION DE VARIABLE
                        else if (sentence.startsWith("ENTERO")) {
                            declarar_variable(sentence);
                        } 
                        
                        else if (sentence.startsWith("FLOTANTE")){
                            declarar_variable(sentence);
                        }
                        else if (sentence.startsWith("CADENA")){
                            declarar_cadena(sentence);
                        }
                        
                        //SICLO SI 
                        else if (sentence.startsWith("SI")) {
                            String[] cadena = sentence.split(" ");
                            System.out.println(cadena[0]);
                            if(cadena[0].equals("SI")){
                                int  valor1 = Convertir(cadena[2]);
                                int  valor2 = Convertir(cadena[4]);
                            switch (cadena[3]) {
                                case ">":
                                    if(valor1>valor2){
                                       estado_si=true;
                                    }
                                    else {
                                        repeatCode=repeatCode+1;
                                        estado_si=false;
                                       // estado_escribir=false; 
                                    }
                                break;
                                case "<":
                                     if(valor1<valor2){
                                       
                                    }
                                    else { repeatCode=repeatCode+1;}

                                    break;
                                case "==":
                                         if(valor1==valor2){
                                        
                                    }
                                    else { repeatCode=repeatCode+1;}
                                    break;
                                case ">=":
                                     if(valor1>=valor2){
                                       
                                    }
                                    else { repeatCode=repeatCode+1;}

                                    break;
                                case "<=":
                                     if(valor1<=valor2){
                                      
                                    }
                                    else { repeatCode=repeatCode+1;}
                                    break;
                                case "!=":
                                     if(valor1!=valor2){

                                    }
                                    else { repeatCode=repeatCode+1;}
                                    break;
                                default:
                                    throw new AssertionError();
                            }
                           
                           }
                            else if (cadena[0].equals("SINO") && estado_si==true){
                                repeatCode=repeatCode+1;
                            }
                        }
                        //SINO
                        
                        //SICLO PARA
                        else if (sentence.startsWith("PARA")) {
                            int result = -1;
                            if (sentence.contains("$")) {
                                //parametro = identificadores.get(sentence.substring(10, sentence.length() - 2));
                            } else {
                                String[] identComp = sentence.split(" ");
                                int valor1 = Convertir(identComp[2]);
                                int valor2 = Convertir(identComp[3]);
                                if (valor1 > valor2) {
                                    result = valor1 - valor2;
                                } else {
                                    result = valor2 - valor1;
                                }
                            }
                            repeatCode = result;
                        }
                    }
                }
            }
        }
    }

    // metodo para declarar
    public void declarar_variable(String sentence) {
        String[] identComp = sentence.split(" ");
        boolean estado = true;
        if (identComp.length == 2) {
            this.lista_variable.add(new Variable(identComp[1], "0"));
        } else if (identComp.length == 4) {
            this.lista_variable.add(new Variable(identComp[1], "0"));
            for (Variable var : lista_variable) {
                estado = false;
                if (var.getNombre().equals(identComp[1])) {
                    var.setValor(identComp[3]);
                    estado = false;
                }
            }
        }
    }
    
    
    // metodo para declarar cadena
    public void declarar_cadena(String sentence) {
        String[] identComp = sentence.split(" ");
        boolean estado = true;
        if (identComp.length == 2) {
            this.lista_variable.add(new Variable(identComp[1], "null"));
        } else if (identComp.length == 6) {
            this.lista_variable.add(new Variable(identComp[1], "null"));
            for (Variable var : lista_variable) {
                estado = false;
                if (var.getNombre().equals(identComp[1])) {
                    var.setValor(identComp[4]);
                    estado = false;
                }
            }
        }
    }

// metodo parar ver si es una varibale o un entero 
    public int Convertir(String var) {
        int numero = 0;
        try {
            numero = Integer.valueOf(var);
        } catch (Exception e) {

            for (Variable vare : lista_variable) {
                if (vare.getNombre().equals(var)) {
                    try{
                         numero = Integer.valueOf(vare.getValor());
                    }catch(NumberFormatException ex){
                        float num = Float.parseFloat(vare.getValor());
                        numero=(int) num;
                    }   
                }
                
            }
        }
        return numero;
                                

                
    }
    
    //metodo para pasar parametros
    public void pasar_parametros(String sentence){
        String[] identComp = sentence.split(" ");
        
        System.out.println(identComp.length);
        if(identComp.length==4){
            for(Variable var: lista_variable){
            if(var.getNombre().equals(identComp[1])){
              var.setValor(identComp[3]);
            }
        }}
        else if (identComp.length==6){
            for(Variable var:lista_variable){
              if(var.getNombre().equals(identComp[1])){
              var.setValor( operaciones_basicas(Convertir_float(identComp[3]), Convertir_float(identComp[5]), identComp[4]));
              }
            } 
        }
        else{
         for(Variable var: lista_variable){
            if(var.getNombre().equals(identComp[1])){
              var.setValor(identComp[4]);
            }}
        }
        
    }
//meto para realizar operaciones matematicas
    public String operaciones_basicas(float var1, float var2, String signo) {
        String resultado=" ";
        float resul=0;
        switch (signo) {
            case "+":
                resul = var1+var2;
                break;

            case "-":
                resul= var1-var2;
                break;
            case "*":
                resul= var1*var2;
                break;
            case "/":
                if(var2!=0){
                    resul=var1-var2;
                }
                break;
            default:
                throw new AssertionError();
        }
        return resultado = String.valueOf(resul);
    }
    
    
    // metodo parar ver si es una varibale o un entero 
    public float Convertir_float(String var) {
        float numero = 0;
        try {
            numero = Float.parseFloat(var);
        } catch (Exception e) {

            for (Variable vare : lista_variable) {
                if (vare.getNombre().equals(var)) {
                    numero = Float.parseFloat(vare.getValor());
                }
            }
        }
        return numero;
    }
    
    
}
