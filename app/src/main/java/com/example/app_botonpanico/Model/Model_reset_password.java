package com.example.app_botonpanico.Model;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_botonpanico.Interface.ResetPasswordCallback;
import com.example.app_botonpanico.utils.PanicButtomConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Model_reset_password {
    private Context context;
    private PanicButtomConfig panicButtomConfig;
    private ResetPasswordCallback resetPasswordCallback;
    String email,codeVerification;

    public Model_reset_password(Context context) {
        this.context = context;
        this.panicButtomConfig = new PanicButtomConfig();

    }

    public Model_reset_password(String email, Context context, ResetPasswordCallback resetPasswordCallback) {
        this.email = email;
        this.context = context;
        this.panicButtomConfig = new PanicButtomConfig();
        this.resetPasswordCallback = resetPasswordCallback;
    }

    public Model_reset_password(String email, String codeVerification, Context context, ResetPasswordCallback resetPasswordCallback) {
        this.codeVerification = codeVerification;
        this.email = email;
        this.context = context;
        this.panicButtomConfig = new PanicButtomConfig();
        this.resetPasswordCallback = resetPasswordCallback;
    }


    public void validateEmail(){
        String Url = panicButtomConfig.getServerPanicButtom()+"/ServidorPhp/user/validate_email.php";
        String email = String.valueOf(getEmail());
        StringRequest request = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String exito = jsonObject.getString("exito");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() == 0){
                        System.out.println("model_generalUser -> login -> datos vacío");
                        Toast.makeText(context, "Correo no registrado", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (exito.equals("1")){
                            JSONObject object = jsonArray.getJSONObject(0);
                            String first_name = object.getString("first_name_user");
                            String last_name = object.getString("last_name_user");
                            String email = object.getString("email_user");
                            String phone_number = object.getString("phone_number_user");
                            String age = object.getString("age_user");
                            String[] res = {first_name, last_name,email ,phone_number, age};
                            resetPasswordCallback.OnSuccess(email);
                        }
                    }
                }catch (JSONException jsonException){
                    System.out.println(jsonException);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                resetPasswordCallback.OnFailure("Conexión perdida");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email_user", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public void sendEmailCode(){
        String Url = panicButtomConfig.getServerPanicButtom()+"/ServidorPhp/user/mail_reset_password_user.php";
        String email = String.valueOf(getEmail());
        String codeVerification = String.valueOf(getCodeVerification());
        StringRequest request = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String exito = jsonObject.getString("exito");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() == 0){
                        System.out.println("model_generalUser -> sendCode -> datos vacío");}
                    else{
                        if (exito.equals("1")){
                            System.out.println("Datos existentes");
                        }
                    }
                }catch (JSONException jsonException){
                    System.out.println(jsonException);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email_user", email);
                params.put("codeverification",codeVerification);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public String getCodeVerification() {
        return codeVerification;
    }

    public void setCodeVerification(String codeVerification) {
        this.codeVerification = codeVerification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
