/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Generador_codiigo;

import recursos.Variable;
import compilerTools.CodeBlock;
import compilerTools.Token;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import recursos.GeneradorBloques;
import semantica.Semantico;

/**
 *
 * @author HP
 */
public class EjecutorCodigo {

    private ArrayList<Variable> lista_variable;
    private JTextArea textArea;
    private Semantico  analizador_semantico;
    private Map<String, ArrayList<Token>> bloqueMetodoFunciones;
    public EjecutorCodigo(JTextArea textArea) {
        this.textArea = textArea;
        this.lista_variable = new ArrayList<>();
        analizador_semantico = new Semantico();
    }

    public void ejecutarCodigo(ArrayList<Token> tokens) {
        this.lista_variable.clear();
        CodeBlock codeBlock = compilerTools.Functions.splitCodeInCodeBlocks(tokens, "{", "}", ";");
        ArrayList<String> blocksOfCode = codeBlock.getBlocksOfCodeInOrderOfExec();
        System.out.println(" CodeBloc: " + blocksOfCode);
       // analizador_semantico.anali(tokens,textArea);
        //analizador_semantico.imprimi();
        executeCode(blocksOfCode, 1);
    }

    private void executeCode(ArrayList<String> blocksOfCode, int repeats) {
        boolean estado_si=false;
        String auxi="";
        int auxi_siglo_mientras=1;
        for (int j = 1; j <= repeats; j++) {
            int repeatCode = -1;
            for (int i = 0; i < blocksOfCode.size(); i++) {
                String blockOfCode = blocksOfCode.get(i);
              // System.out.println("block    ---->"+ blocksOfCode.get(i));
                if (repeatCode != -1) {
                    int[] posicionMarcador = CodeBlock.getPositionOfBothMarkers(blocksOfCode, blockOfCode);
                    executeCode(new ArrayList<>(blocksOfCode.subList(posicionMarcador[0], posicionMarcador[1])), repeatCode);
                    repeatCode = -1;
                    i = posicionMarcador[1];
                } else {
                    String[] sentences = blockOfCode.split(";");
                    // System.out.println("block    ---->"+blockOfCode);
                    for (String sentence : sentences) {
                         sentence = sentence.trim();
                                            
                         //CODIGO PARAR ESCRIBIR
                         if (sentence.startsWith("ESCRIBIR")) {
                            String parametro;
                            String[] identComp = sentence.split(" ");
                            if (identComp.length == 4) {
                                if(lista_variable.size()!=0){
                                    for (Variable var : lista_variable) {
                                        if (var.getNombre().equals(identComp[2])) {
                                            textArea.append("-->" + var.getValor() + " \n ");
                                        }
                                    }
                                }
                                else {
                                    this.textArea.append("no se a teclarado una variable");
                                }
                                
                            } else {
                                parametro = sentence.substring(12, sentence.length() - 4);
                                textArea.append("-->" + parametro + " \n ");
                            }
                        }
                        
                        //Codigo para leer
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
                        // pasando parametros
                        else if(sentence.startsWith("#")){
                           if(pasar_parametros(sentence)==false){return;}
                        }

                        //DECLARACION DE VARIABLE
                        else if (sentence.startsWith("ENTERO")) {
                           if(declarar_variable(sentence)==true){return;}
                        } 
                        else if (sentence.startsWith("FLOTANTE")){
                             if(declarar_variable(sentence)==true){return;}
                        }
                        else if (sentence.startsWith("CADENA")){
                            if( declarar_cadena(sentence)==true){return;};
                        }
                        
                        //SICLO SI 
                        else if (sentence.startsWith("SI")) {
                            String[] cadena = sentence.split(" ");
                            System.out.println(cadena[0]);
                            if(cadena[0].equals("SI")){
                                int  valor1 = Convertir(cadena[2]);
                                int  valor2 = Convertir(cadena[4]);
                                if(valor1==-1 || valor2==-1){ return;}
                                //si la condicion es falsa 
                                estado_si=condicion(valor1, valor2, cadena[3]);
                                if(condicion(valor1, valor2, cadena[3])==false){repeatCode = repeatCode + 1;}
                           }
                            //SINO
                            else if (cadena[0].equals("SINO") && estado_si==true){
                                repeatCode=repeatCode+1;
                            }
                        }
                        
                        //SICLO PARA
                        else if (sentence.startsWith("PARA")) {
                            int result = -1;
                            if (sentence.contains("$")) {
                                //parametro = identificadores.get(sentence.substring(10, sentence.length() - 2));
                            } else {
                                String[] identComp = sentence.split(" ");
                                int valor1 = Convertir(identComp[2]);
                                int valor2 = Convertir(identComp[3]);
                                if(valor1==-1 || valor2==-1){ return;}
                                if (valor1 > valor2) {
                                    result = valor1 - valor2;
                                } else {
                                    result = valor2 - valor1;
                                }
                            }
                            repeatCode = result;
                        }
                        
                        // siglo while
                        else if(sentence.startsWith("MIENTRAS")){
                            boolean estado=true;
                            String  []campos= sentence.split(" ");
                            int  valor1 = Convertir(campos[2]);
                            int  valor2 = Convertir(campos[4]);
                            if(condicion(valor1,valor2,campos[3])==true){
                                    blockOfCode = blocksOfCode.get(i+1);
                                    int[] posicionMarcador = CodeBlock.getPositionOfBothMarkers(blocksOfCode, blockOfCode);
                                    while(estado!=false){
                                    executeCode(new ArrayList<>(blocksOfCode.subList(posicionMarcador[0], posicionMarcador[1])), 1);
                                   estado=  condicion(Convertir(campos[2]), Convertir(campos[4]),campos[3]);
                                    repeatCode = -1;
                                    i = posicionMarcador[1];}
                            }
                            else {
                                repeatCode++;
                            }
                        }
                        
                        //SWICH
                        else if(sentence.startsWith("CUANDO")){
                           String  []campos= sentence.split(" ");
                           if(retor_variable(campos[2]).equals("")){return;}
                            try{
                                float f= Float.valueOf(retor_variable(campos[2]));
                                int v= (int) f;
                                auxi= String.valueOf(v);
                            }catch(NumberFormatException ex){ auxi=retor_variable(campos[2]);}
                        }
                        //case
                        else if (sentence.startsWith("CASO")){
                            String  []campos= sentence.split(" ");
                            if(campos.length>4){
                                if(auxi.equals(campos[2])){}
                                else{repeatCode=repeatCode+1;}
                            }
                            else{
                                if(auxi.equals(campos[1])){}
                                else{repeatCode=repeatCode+1;}
                            }
                        }                   
                      //funciones
                        else if(sentence.startsWith("FUNCION")){
                        }
                        
                        //procedimiento 
                         else if(sentence.startsWith("PROCEDIMIENTO")){
                          String [] campo= sentence.split(" ");
                          System.out.println("procedimiento"+campo);
                         }
                         
                         
                        else { 
                            String [] campos=sentence.split(" ");
                           
                            if(campos.length>2){
                                 System.out.println("-*-*-* "+campos[0]);
                                if(bloqueMetodoFunciones.get(campos[0])!=null){
                                    CodigoFunciones codigo = new CodigoFunciones(textArea,lista_variable,campos);
                                    codigo.ejecutarCodigo(bloqueMetodoFunciones.get(campos[0]));
                                    ejecutarCodigo(bloqueMetodoFunciones.get(campos[0])); 
                                    
                                }
                                else {
                                    textArea.append("la funcion no esta declarada:" +campos[0] +"\n"); 
                                    textArea.setForeground(Color.red);
                                }
                                
                            }
                        }
                    }
                }
            }
        }
    }

    // metodo para declarar
    public boolean declarar_variable(String sentence) {
        String[] identComp = sentence.split(" ");
        boolean estado = false;
        if (identComp.length == 2) {
            for(Variable var: lista_variable){
                if(var.getNombre().equals(identComp[1])){
                    textArea.append("Variable ya fue declarada :" +var.getNombre()); 
                    textArea.setForeground(Color.red);
                    return true;
                }
            }
           this.lista_variable.add(new Variable(identComp[1], "0",""));
        }
        else if (identComp.length == 4) {
            for(Variable var: lista_variable){
                if(var.getNombre().equals(identComp[1])){
                    textArea.append("Variable ya fue declarada :" +var.getNombre()); 
                    textArea.setForeground(Color.red);
                    return true;
                }
            }
           this.lista_variable.add(new Variable(identComp[1], identComp[3],""));
        }
        return estado;
    }
    
    
    // metodo para declarar cadena
    public boolean declarar_cadena(String sentence) {
        String[] identComp = sentence.split(" ");
        boolean estado = false;
        if (identComp.length == 2) {
             for(Variable var: lista_variable){
                if(var.getNombre().equals(identComp[1])){
                    textArea.append("Variable ya fue declarada :" +var.getNombre()); 
                    textArea.setForeground(Color.red);
                    return true;
                }
            }
           this.lista_variable.add(new Variable(identComp[1], "null",""));
        } else if (identComp.length == 6) {
           
            for (Variable var : lista_variable) {
                if (var.getNombre().equals(identComp[1])) {
                    var.setValor(identComp[4]);
                     textArea.append("Variable ya fue declarada :" +var.getNombre()); 
                    textArea.setForeground(Color.red);
                    return true;
                }
            }
            this.lista_variable.add(new Variable(identComp[1], identComp[4],""));
        }
        return estado;
    }

// metodo parar ver si es una varibale o un entero 
    public int Convertir(String var) {
        int numero = -1;
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
        if(numero==-1){ 
            this.textArea.append("Variable no declarada :" +var); 
            textArea.setForeground(Color.red);
            return -1;
        }
         
        return numero;
    }
    
    //metodo para pasar parametros
    public boolean pasar_parametros(String sentence){
        boolean estado=false;
        String[] identComp = sentence.split(" ");
        System.out.println(identComp.length);
        if(identComp.length==4){
            for(Variable var: lista_variable){
            if(var.getNombre().equals(identComp[1])){
              var.setValor(identComp[3]);
              estado=true;
            }
        }}
        else if (identComp.length==6){
            for(Variable var:lista_variable){
              if(var.getNombre().equals(identComp[1])){
                var.setValor( operaciones_basicas(Convertir_float(identComp[3]), Convertir_float(identComp[5]), identComp[4]));
                estado=true;
              }
            } 
        }
        else{
         for(Variable var: lista_variable){
            if(var.getNombre().equals(identComp[1])){
              var.setValor(identComp[4]);
              estado=true;
            }}
        }
        if(estado==false){
          this.textArea.append("Variable no declarada :" +sentence); 
            textArea.setForeground(Color.red);
        }
        return estado;
    }
//meto para realizar operaciones matematicas
    public String operaciones_basicas(float var1, float var2, String signo) {
        String resultado=" ";
        float resul=0;
        switch (signo) {
            case "+":  resul = var1+var2;
                break;

            case "-":  resul= var1-var2;
                break;
            
            case "*":  resul= var1*var2;
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
        float numero = -1;
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
    
    //condicion
    public boolean condicion(int var1, int var2, String menu) {
        boolean estado_condicion =false;
        switch (menu) {
            case ">":
                if (var1 > var2) { estado_condicion = true; } 
                else { estado_condicion = false; }
                break;
                
            case "<":
                if (var1 < var2) {  estado_condicion=true;}
                else { estado_condicion=false; }
                break;
                
            case "==":
                if (var1 == var2) { estado_condicion=true;} 
                else {  estado_condicion=false; }
                break;
                
            case ">=":
                if (var1 >= var2) { return estado_condicion=true; } 
                else {     estado_condicion=false; }
                break;
                
            case "<=":
                if (var1 <= var2) { return estado_condicion=true; } 
                else {   estado_condicion=false; }
                break;
                
            case "!=":
                if (var1 != var2) { return estado_condicion=true; } 
                 else {  estado_condicion=false; }
                break;
            default:
                throw new AssertionError();
        }
        return estado_condicion;
    }
    
    //CASSE
    public void caso(String condicion){
        try {
            int var = Integer.valueOf(condicion);
        }
        catch(NumberFormatException ex){
            
        }
    }
    
    //meto para retornar valor de variable
    public String retor_variable(String variable){
        String auxi="";
        for(Variable var: lista_variable){
            if(var.getNombre().equals(variable)){
                auxi=var.getValor();
            }
        }
        if(auxi.equals("")){
          this.textArea.append("Variable no declarada :" +auxi); 
            textArea.setForeground(Color.red);
        }
        return auxi;    
    }

    public void setBloqueMetodoFunciones(Map<String, ArrayList<Token>> bloqueMetodoFunciones) {
        this.bloqueMetodoFunciones = bloqueMetodoFunciones;
    }
    
}
