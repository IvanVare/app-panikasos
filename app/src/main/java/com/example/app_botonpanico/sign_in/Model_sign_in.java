package com.example.app_botonpanico.sign_in;

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
import com.example.app_botonpanico.qa_main_menu;
import com.example.app_botonpanico.utils.PanicButtomConfig;

import java.util.HashMap;
import java.util.Map;

public class Model_sign_in {
    private Context context;
    private PanicButtomConfig panicButtomConfig;

    public Model_sign_in(Context context) {
        this.context = context;
        this.panicButtomConfig = new PanicButtomConfig();

    }
    public void validateUser(String phoneNumber, String password) {
        String Url = panicButtomConfig.getServerPanicButtom()+"/ServidorPhp/user/validate_user.php";

        StringRequest request = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.isEmpty()) {
                    Intent intent = new Intent(context, qa_main_menu.class);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "No hay respuesta", Toast.LENGTH_SHORT).show();
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
                params.put("phone_number_user", phoneNumber);
                params.put("password_user", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}
