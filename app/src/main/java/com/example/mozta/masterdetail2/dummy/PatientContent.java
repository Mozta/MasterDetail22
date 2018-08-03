package com.example.mozta.masterdetail2.dummy;

import com.example.mozta.masterdetail2.PatientsModel;

import java.util.ArrayList;

public class PatientContent {
    //Lista que almacenará las personas
    private static ArrayList<PatientsModel> personList = new ArrayList();

    public static ArrayList getPersonList() {
        return personList;
    }

    /**
     * Carga en la lista personList una serie de personas con datos ficticios
     */
    public static void loadPersonList() {
        PatientsModel person;

        person = new PatientsModel();
        person.setNombre("AMADOR");
        personList.add(person);

        person = new PatientsModel();
        person.setNombre("BALTASAR");
        personList.add(person);

        person = new PatientsModel();
        person.setNombre("JESUS");
        personList.add(person);
    }

}
