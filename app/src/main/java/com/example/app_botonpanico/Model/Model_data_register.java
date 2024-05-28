package com.example.app_botonpanico.Model;

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
import com.example.app_botonpanico.Controller.Controller_sign_in_user;
import com.example.app_botonpanico.Interface.DataRegisterCallback;
import com.example.app_botonpanico.Interface.ResetPasswordCallback;
import com.example.app_botonpanico.utils.PanicButtomConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Model_data_register {

    private Context context;
    private PanicButtomConfig panicButtomConfig;
    private DataRegisterCallback dataRegisterCallback;

    String email,phoneNumber;

    public Model_data_register(Context context) {
        this.context = context;
        this.panicButtomConfig = new PanicButtomConfig();
    }

    public Model_data_register(String email,String phoneNumber,Context context ,DataRegisterCallback dataRegisterCallback) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.context = context;
        this.panicButtomConfig = new PanicButtomConfig();
        this.dataRegisterCallback = dataRegisterCallback;
    }

    public void registerUser(String firstName, String lastName,String email, String phoneNumber, String age, String password){
        String Url = panicButtomConfig.getServerPanicButtom()+"/ServidorPhp/user/register_user.php";
        StringRequest request = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Registro exitoso")) {
                    Toast.makeText(context, "Registro hecho", Toast.LENGTH_SHORT).show();
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
                params.put("email_user",email);
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


    public void validateEmail(){
        String Url = panicButtomConfig.getServerPanicButtom()+"/ServidorPhp/user/validate_phone_number_and_email_uesr.php";
        String email = String.valueOf(getEmail());
        String phoneNumber = String.valueOf(getPhoneNumber());
        StringRequest request = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String exito = jsonObject.getString("exito");

                    System.out.println(exito);
                    if (exito.equals("1")){
                        dataRegisterCallback.OnSuccess();
                    }else {
                        Toast.makeText(context, "Correo o número telefonico ya registrado", Toast.LENGTH_SHORT).show();
                    }

                }catch (JSONException jsonException){
                    System.out.println(jsonException);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dataRegisterCallback.OnFailure("Conexión perdida");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email_user", email);
                params.put("phone_number_user", phoneNumber);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

