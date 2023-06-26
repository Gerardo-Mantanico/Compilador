/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Generador_codiigo;

import compilerTools.CodeBlock;
import compilerTools.Token;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import recursos.Variable;
import semantica.Semantico;

/**
 *
 * @author HP
 */
public class CodigoFunciones {
    
    private ArrayList<Variable> lista_variable;
    private JTextArea textArea;
    private String  []campo;
    
    public CodigoFunciones (JTextArea textArea, ArrayList<Variable> lista_variable,String  []campo) {
        this.textArea = textArea;
        this.lista_variable = lista_variable;
        this.campo=campo;
    }
    
     public void ejecutarCodigo(ArrayList<Token> tokens) {
        CodeBlock codeBlock = compilerTools.Functions.splitCodeInCodeBlocks(tokens, "{", "}", ";");
        ArrayList<String> blocksOfCode = codeBlock.getBlocksOfCodeInOrderOfExec();
           System.out.println(" CodeBloc: " + blocksOfCode);
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
                         
                       //  System.out.println("funcion "+sentence);
                                            
                         //CODIGO PARAR ESCRIBIR
                    
                    }
                }
            }
        }
    }

}
