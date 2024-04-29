package com.example.app_botonpanico.reset_password;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_botonpanico.sign_in.CheckData;
import com.example.app_botonpanico.utils.PanicButtomConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Model_reset_password {
    private Context context;
    private PanicButtomConfig panicButtomConfig;
    private CheckDataRP checkDataRP;
    String phone_number;

    public Model_reset_password(Context context) {
        this.context = context;
        this.panicButtomConfig = new PanicButtomConfig();

    }
    public Model_reset_password(String phone_number,Context context,CheckDataRP checkDataRP) {
        this.phone_number = phone_number;
        this.context = context;
        this.panicButtomConfig = new PanicButtomConfig();
        this.checkDataRP = checkDataRP;
    }

    public void validatePhoneNumber(){
        String Url = panicButtomConfig.getServerPanicButtom()+"/ServidorPhp/user/validate_phone_number_user.php";
        String phoneNumber = String.valueOf(getPhone_number());
        StringRequest request = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String exito = jsonObject.getString("exito");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() == 0){
                        System.out.println("model_generalUser -> login -> datos vacío");}
                    else{
                        if (exito.equals("1")){
                            JSONObject object = jsonArray.getJSONObject(0);
                            String first_name = object.getString("first_name_user");
                            String last_name = object.getString("last_name_user");
                            String phone_number = object.getString("phone_number_user");
                            String age = object.getString("age_user");
                            String[] res = {first_name, last_name, phone_number, age};
                            checkDataRP.CheckPhoneNumber(phone_number);
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
                params.put("phone_number_user", phoneNumber);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
