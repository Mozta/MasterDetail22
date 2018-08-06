package com.example.mozta.masterdetail2;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mozta on 23/05/18.
 */

public class HistoryModel {
    String Timestamp, Capturista;
    double latitud, longitud;

    public String key;

    public HistoryModel() {
    }

    public HistoryModel(String timestamp, String capturista, String key) {
        Timestamp = timestamp;
        Capturista = capturista;
        this.key = key;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("Timestamp", Timestamp);
        result.put("Capturista", Capturista);

        result.put("key", key);
        return result;
    }


}
