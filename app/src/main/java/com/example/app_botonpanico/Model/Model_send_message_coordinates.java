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

public class Model_send_message_coordinates {
    private Context context;
    private PanicButtomConfig panicButtomConfig;
    String first_name,last_name,email,myfirst_name,mylast_name,myemail,myphone_number,myubication;


    public Model_send_message_coordinates(Context context) {
        this.context = context;
        this.panicButtomConfig = new PanicButtomConfig();
    }

    public Model_send_message_coordinates(String first_name, String last_name,String email, String myfirst_name,String mylast_name, String myemail, String myphone_number, String myubication, Context context) {
        this.first_name= first_name;
        this.last_name=last_name;
        this.email = email;
        this.myemail = myemail;
        this.myfirst_name = myfirst_name;
        this.mylast_name = mylast_name;
        this.myphone_number=myphone_number;
        this.myubication = myubication;
        this.context = context;
        this.panicButtomConfig = new PanicButtomConfig();
    }

    public void sendCoordinate(String first_name, String last_name,String email, String myfirst_name,String mylast_name, String myemail, String myphone_number, String myubication){
        String Url = panicButtomConfig.getServerPanicButtom()+"/ServidorPhp/user/send_mail.php";
        StringRequest request = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.toString().trim().equalsIgnoreCase("Exito")) {
                    Toast.makeText(context, "Panika SOS Activado", Toast.LENGTH_SHORT).show();
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
                params.put("first_name_user",myfirst_name);
                params.put("last_name_user", mylast_name);
                params.put("email_user",myemail);
                params.put("phone_number_user", myphone_number);
                params.put("ubication_user",myubication);
                params.put("contact_firstname_user", first_name);
                params.put("contact_lastname_user",last_name);
                params.put("contact_email_user", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}
