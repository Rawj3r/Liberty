package com.equidais.mybeacon.controller.register;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.equidais.mybeacon.R;
import com.equidais.mybeacon.controller.JSONParser;
import com.equidais.mybeacon.controller.common.BaseActivity;
import com.equidais.mybeacon.controller.login.LoginActivity;
import com.equidais.mybeacon.controller.service.ServiceHandler;
import com.equidais.mybeacon.model.Company;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Register extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private EditText user_mail, fnme, lname, empno, user_pass, confirm_pass;
    private ProgressDialog progressDialog;
    private String companyID;

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
        fnme = (EditText) findViewById(R.id.edit_fname);
        lname = (EditText) findViewById(R.id.edit_lname);
        user_mail = (EditText) findViewById(R.id.edit_email);
        user_pass = (EditText) findViewById(R.id.edit_password);
        confirm_pass = (EditText) findViewById(R.id.edit_password_con);

        spinner = (Spinner) findViewById(R.id.select_com);
        spinner.setOnItemSelectedListener(this);
        findViewById(R.id.btn_signup).setOnClickListener(this);
        companies = new ArrayList<>();
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
            final String lname_ = lname.getText().toString();
            final String fname_ = fnme.getText().toString();
            final String pass = user_pass.getText().toString();
            final String cpass = confirm_pass.getText().toString();
            final String active = "1";

            if (lname_.equals("")){
                Toast.makeText(getApplicationContext(), "Please input last name", Toast.LENGTH_SHORT).show();
                return;
            }

//            if (emp_.equals("")){
//                Toast.makeText(getApplicationContext(), "Please input employee number", Toast.LENGTH_SHORT).show();
//                return;
//            }

            if (mail.equals("")){
                Toast.makeText(Register.this, "Please input email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (fname_.equals("")){
                Toast.makeText(getApplicationContext(), "Please input first name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (pass.equals("")){
                Toast.makeText(Register.this, "Please input password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (cpass.equals("")){
                Toast.makeText(Register.this, "Please confirm password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(cpass)){
                Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (companyID.equals("") || companyID.equals("Select Company")){
                Toast.makeText(getApplicationContext(), "Please select your company", Toast.LENGTH_SHORT).show();
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
                        hashMap.put("fusermail", mail);
                        hashMap.put("fpassword", pass);
                        hashMap.put("company", companyID);
                        hashMap.put("fname", fname_);
                        hashMap.put("lname", lname_);
                        hashMap.put("empno", "null");
                        hashMap.put("active", active);

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


                        try {
                            Toast.makeText(Register.this, jsonObject.getString(TAG_MESSAGE).toString(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        companyID = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class GetCompanies extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Register.this);
            progressDialog.setMessage("Fetching companies");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ServiceHandler handler = new  ServiceHandler();
            String s = handler.makeServiceCall(URL_COMPANIES, ServiceHandler.GET);
            Log.e("Response: ", "" + s);
            if (s != null){
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject != null){
                        JSONArray comp = jsonObject.getJSONArray("companies");
                        for (int i = 0; i < comp.length(); i++){
                            JSONObject comOb = (JSONObject) comp.get(i);
                            Company company = new Company(comOb.getInt("company_ID"), comOb.getString("company_Name"));
                            companies.add(company);
                        }
                    }
                }catch (JSONException sex){
                    sex.printStackTrace();
                }
            }else {
                Log.e("Error: ","Couldn't get companies");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
                populateSpinner();
            }
        }
    }

    private void populateSpinner() {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i< companies.size(); i++){
            stringList.add(companies.get(i).getCname());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

}
