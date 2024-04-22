package com.example.app_botonpanico.data_register;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_botonpanico.sign_in.Controller_sign_in_user;
import com.example.app_botonpanico.utils.PanicButtomConfig;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Model_data_register {

    private Context context;
    private PanicButtomConfig panicButtomConfig;

    public Model_data_register(Context context) {
        this.context = context;
        this.panicButtomConfig = new PanicButtomConfig();
    }

    public void register_User(String firstName,String lastName,String phoneNumber,String age, String password){
        String Url = panicButtomConfig.getServerPanicButtom()+"/ServidorPhp/user/register_user.php";
        StringRequest request = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Registro exitoso")) {
                    Intent intent = new Intent(context, Controller_sign_in_user.class);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "No hay respuesta", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, volleyError.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Error: "+volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("Error: "+volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("first_name_user",firstName);
                params.put("last_name_user",lastName);
                params.put("phone_number_user", phoneNumber);
                params.put("age_user",age);
                params.put("password_user", password);
                params.put("status_user", "1");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    /*private String encryptPassword(String data,String password) throws Exception{
        SecretKeySpec secretKeySpec = generateKey(password);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptDataBytes = cipher.doFinal(data.getBytes());
        String encryptDataString = Base64.getEncoder().encodeToString(encryptDataBytes, Base64);
    }

    private SecretKeySpec generateKey(String password) throws Exception{
        MessageDigest sha= MessageDigest.getInstance("SHA-256");
        byte[] key = password.getBytes("UTF-8");
        key=sha.digest(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }*/
}

