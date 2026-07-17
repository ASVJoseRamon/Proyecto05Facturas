package com.api.gestion.api_gestion_facturas.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class FacturaUtils {
    private FacturaUtils(){

    }

    public static ResponseEntity<String> getResponseEntity(String message, HttpStatus httpStatus){
        return new ResponseEntity<String>("Mensaje: "+message,httpStatus);
    }

    public static String getUUID(){
        Date date = new Date();
        long time = date.getTime();
        return "FACTURA-"+time;
    }

    public static JSONArray getJsonArrayFromString(String data) {
        JSONArray jsonArray = new JSONArray();
        return jsonArray;
    }

    public static Map<String, Object> getMapFromJson(String data){
        if(!Strings.isNullOrEmpty(data)){
            return new Gson().fromJson(data, new TypeToken<Map<String,Object>>() {
             }.getType());
        }
        return new HashMap<>();
    }

} 