/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package recursos;

import compilerTools.Token;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author HP
 */
public class TablaFuncionProcesos {
    private String tipo;
    private String tipo_variable ;
    private String nombre;
    private String valor;
    private Map<String, ArrayList<Parametros>> lista_variables;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo_variable() {
        return tipo_variable;
    }

    public void setTipo_variable(String tipo_variable) {
        this.tipo_variable = tipo_variable;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Map<String, ArrayList<Parametros>> getLista_variables() {
        return lista_variables;
    }

    public void setLista_variables(Map<String, ArrayList<Parametros>> lista_variables) {
        this.lista_variables = lista_variables;
    }

  
 
    
    
    
    
    
}
