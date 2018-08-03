package com.example.mozta.masterdetail2;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mozta on 23/05/18.
 */

public class PatientsModel {
    String FechaRegistro, Folio, Paciente, nombre;

    public String key;

    public PatientsModel() {
    }

    public PatientsModel(String FechaRegistro, String Folio, String Paciente, String nombre, String key) {
        this.FechaRegistro = FechaRegistro;
        this.Folio = Folio;
        this.Paciente = Paciente;
        this.nombre = nombre;
        this.key = key;
    }



    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUidPaciente() {
        return Paciente;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("FechaRegistro", FechaRegistro);
        result.put("Folio", Folio);
        result.put("Paciente", Paciente);
        result.put("Nombre", nombre);
        result.put("key", key);
        return result;
    }
}
