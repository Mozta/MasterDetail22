package com.example.mozta.masterdetail2;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mozta on 23/05/18.
 */

public class PatientModel {
    String Area, CURP, Empresa, Identificacion, Nacimiento, Nombre, NumeroSS, Ocupacion, RFC, Residencia, Sangre, sexo, patient_image;

    public PatientModel() {
    }

    public PatientModel(String area, String CURP, String empresa, String identificacion, String nacimiento, String nombre, String numeroSS, String ocupacion, String RFC, String residencia, String sangre, String sexo, String patient_image) {
        Area = area;
        this.CURP = CURP;
        Empresa = empresa;
        Identificacion = identificacion;
        Nacimiento = nacimiento;
        Nombre = nombre;
        NumeroSS = numeroSS;
        Ocupacion = ocupacion;
        this.RFC = RFC;
        Residencia = residencia;
        Sangre = sangre;
        this.sexo = sexo;
        this.patient_image = patient_image;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("Area", Area);
        result.put("CURP", CURP);
        result.put("Empresa", Empresa);
        result.put("NumeroSS", NumeroSS);
        result.put("Ocupacion", Ocupacion);
        result.put("RFC", RFC);
        result.put("Residencia", Residencia);
        result.put("Sangre", Sangre);
        result.put("patient_image", patient_image);
        return result;
    }
}
