/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package recursos;

/**
 *
 * @author HP
 */
public class Parametros extends Variable{
    
    private boolean prioridad = false;
    
    public Parametros(String nombre, String valor, String tipo , boolean prioridad) {
        super(nombre, valor, tipo);
        this.prioridad=prioridad;
    }

    public boolean isPrioridad() {
        return prioridad;
    }

    public void setPrioridad(boolean prioridad) {
        this.prioridad = prioridad;
    }
    
    
}
