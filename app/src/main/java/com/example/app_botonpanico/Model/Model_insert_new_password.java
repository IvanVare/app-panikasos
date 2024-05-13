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
import com.example.app_botonpanico.utils.PanicButtomConfig;

import java.util.HashMap;
import java.util.Map;

public class Model_insert_new_password {

    private Context context;
    private PanicButtomConfig panicButtomConfig;
    public Model_insert_new_password(Context context) {
        this.context = context;
        this.panicButtomConfig = new PanicButtomConfig();
    }

    public void resetPassword(String email,String password){
        String Url = panicButtomConfig.getServerPanicButtom()+"/ServidorPhp/user/insert_new_password_user.php";
        StringRequest request = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response.toString());
                if (response.toString().trim().equalsIgnoreCase("modificacion exitosa")) {
                    Toast.makeText(context, "Modificación hecha", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "No hay respuesta", Toast.LENGTH_SHORT).show();
                    //Revisar pq da esto aunque si se cambie
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
                params.put("email_user",email);
                params.put("password_user", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }


}
