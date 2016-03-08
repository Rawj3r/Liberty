package com.equidais.mybeacon.controller.register;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.equidais.mybeacon.R;
import com.equidais.mybeacon.controller.JSONParser;
import com.equidais.mybeacon.controller.common.BaseActivity;
import com.equidais.mybeacon.controller.login.LoginActivity;
import com.equidais.mybeacon.model.Company;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Register extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private EditText user_mail, user_name, user_pass, confirm_pass;
    private ProgressDialog progressDialog;

    private Spinner spinner;
    private ArrayList<Company> companies;
    private String URL_COMPANIES = "http://masscash.empirestate.co.za/GenyaApi/X/firm.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        link();
    }

    public void link(){
        user_mail = (EditText) findViewById(R.id.edit_email);
        user_name = (EditText) findViewById(R.id.edit_name_user);
        user_pass = (EditText) findViewById(R.id.edit_password);
        confirm_pass = (EditText) findViewById(R.id.edit_password_con);
        findViewById(R.id.btn_signup).setOnClickListener(this);
        spinner = (Spinner) findViewById(R.id.select_com);
        spinner.setOnItemClickListener(this);
        companies = new ArrayList<Company>();
        new GetCompanies().execute();
    }

    public void goLogin(View view){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_signup){
            final String mail = user_mail.getText().toString();
            final String uname = user_name.getText().toString();
            final String pass = user_pass.getText().toString();
            final String cpass =confirm_pass.getText().toString();

            if (mail.equals("")){
                Toast.makeText(Register.this, "Please fill in email field", Toast.LENGTH_SHORT).show();
                return;
            }

            if (uname.equals("")){
                Toast.makeText(Register.this, "Please fill in username field", Toast.LENGTH_SHORT).show();
                return;
            }

            if (pass.equals("")){
                Toast.makeText(Register.this, "Please fill in password field", Toast.LENGTH_SHORT).show();
                return;
            }

            if (cpass.equals("")){
                Toast.makeText(Register.this, "Please fill in password confirm field", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(cpass)){
                Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            class AddUser extends AsyncTask<String,String, JSONObject>{

                JSONParser jsonParser = new JSONParser();

                private static final String TAG_SUCCESS = "success";
                private static final String TAG_MESSAGE = "message";
                private final String REG_URL = "http://masscash.empirestate.co.za/GenyaApi/X/register.php";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = new ProgressDialog(Register.this);
                    progressDialog.setMessage("Registering new user");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                }

                @Override
                protected JSONObject doInBackground(String... params) {
                    try {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("usermail", mail);
                        hashMap.put("password", pass);
                        hashMap.put("username", uname);

                        Log.d("request", "sending request");

                        JSONObject jsonObject = jsonParser.makeHttpRequest(REG_URL, "POST", hashMap);

                        if (jsonObject != null){
                            Log.d("JSON result", jsonObject.toString());
                            return jsonObject;
                        }

                    }catch (Exception sex){
                        sex.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(JSONObject jsonObject) {
                    super.onPostExecute(jsonObject);

                    int success = 0;
                    String message = "";

                    if (progressDialog != null){
                        progressDialog.dismiss();
                    }

                    if (jsonObject != null){
                        Toast.makeText(Register.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();

                        try {
                            success = jsonObject.getInt(TAG_SUCCESS);
                            message = jsonObject.getString(TAG_MESSAGE);
                        }catch (JSONException je){
                            je.printStackTrace();
                        }
                    }

                    if (success == 1){
                        Log.d("Success", message);
                        Intent intent = new Intent(Register.this, LoginActivity.class);
                        startActivity(intent);
                    }else {
                        Log.d("Failure", message);
                    }
                }
            }

            new AddUser().execute();

        }

    }

    private class GetCompanies extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
