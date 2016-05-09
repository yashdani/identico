package m.workshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import app.AppController;

public class MainActivity extends AppCompatActivity {

private EditText etnum;
private Button btsubmit;
private TextInputLayout inputLayout;




private static String TAG = MainActivity.class.getSimpleName();

private ProgressDialog pDialog;

private String jsonResponse;



@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
setSupportActionBar(toolbar);

etnum = (EditText) findViewById(R.id.input_number);
btsubmit = (Button) findViewById(R.id.btn_submit);
inputLayout = (TextInputLayout) findViewById(R.id.input_layout_number);



pDialog = new ProgressDialog(this);
pDialog.setMessage("Please wait...");
pDialog.setCancelable(false);







etnum.addTextChangedListener(new MyTextWatcher(etnum));
btsubmit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Pattern p = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]");
        Matcher m = p.matcher(etnum.getText().toString());


        if((etnum.getText().toString().length() == 10)) {

            if(m.matches()){
               makeJsonObjectRequestPan();


            }
            else {
                makeJsonObjectRequestVoter();
                Intent i = new Intent(getApplicationContext(),Display.class);
                i.putExtra("json",jsonResponse);
                startActivity(i);
            }



        }else if ((etnum.getText().toString().length() == 15||(etnum.getText().toString().length() == 16))){
            makeJsonObjectRequestDl();
        }

        else {
            Toast.makeText(getApplicationContext(), "Check the number!", Toast.LENGTH_SHORT).show();

        }
    }
});

}
private void makeJsonObjectRequestDl(){

Uri.Builder builder = new Uri.Builder();
builder.scheme("http")
        .authority("identi.co.in")
        .appendPath("api")
        .appendPath("smart")
        .appendPath(etnum.getText().toString());
String myUrl = builder.build().toString();


showpDialog();

JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
        myUrl, null, new Response.Listener<JSONObject>() {

    @Override
    public void onResponse(JSONObject response) {
        Log.d(TAG, response.toString());

        try {
            // Parsing json object response
            // response will be a json object
            String valid = response.getString("valid");
            String type = response.getString("type");
            JSONObject data = response.getJSONObject("data");
            String status = data.getString("active");
            String dateofissue = data.getString("doi");
            String validtill = data.getString("vt");






            jsonResponse = "";
            jsonResponse +="VALID :  " + valid +"\n\n";
            jsonResponse +="TYPE  :   " + type +"\n\n";
            jsonResponse += "STATUS :   " + status + "\n\n";
            jsonResponse += "DATE OF ISSUE  :   " + dateofissue + "\n\n";
            jsonResponse += "VALID TILL   :   " + validtill+ "\n\n";




        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        hidepDialog();
        Intent i = new Intent(MainActivity.this,Display.class);
        i.putExtra("json", jsonResponse);
        startActivity(i);

    }

}, new Response.ErrorListener() {

    @Override
    public void onErrorResponse(VolleyError error) {
        VolleyLog.d(TAG, "Error: " + error.getMessage());
        Toast.makeText(getApplicationContext(),
                error.getMessage(), Toast.LENGTH_SHORT).show();
        // hide the progress dialog
        hidepDialog();
    }
});

// Adding request to request queue
AppController.getInstance().addToRequestQueue(jsonObjReq);

}
private void makeJsonObjectRequestVoter(){

Uri.Builder builder = new Uri.Builder();
builder.scheme("http")
        .authority("identi.co.in")
        .appendPath("api")
        .appendPath("smart")
        .appendPath(etnum.getText().toString());
String myUrl = builder.build().toString();


showpDialog();

JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
        myUrl, null, new Response.Listener<JSONObject>() {

    @Override
    public void onResponse(JSONObject response) {
        Log.d(TAG, response.toString());

        try {
            // Parsing json object response
            // response will be a json object
            String valid = response.getString("valid");
            String type = response.getString("type");
            JSONObject data = response.getJSONObject("data");
            String Name = data.getString("name");
            String Gender = data.getString("gender");
            String DateOfBirth = data.getString("dob");
            String LastUpdated = data.getString("last_updated");
            String Age = data.getString("age");






            jsonResponse = "";
            jsonResponse +="VALID  :  " + valid +"\n\n";
            jsonResponse +="TYPE  :  " + type +"\n\n";
            jsonResponse += "NAME  :   " + Name + "\n\n";
            jsonResponse += "DATE OF BIRTH  :  " + DateOfBirth + "\n\n";
            jsonResponse += "AGE  :  " + Age+ "\n\n";
            jsonResponse += "LAST UPDATED  :  " + LastUpdated+ "\n\n";
            jsonResponse += "GENDER  :  " + Gender+ "\n\n";




        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        hidepDialog();
        Intent i = new Intent(MainActivity.this,Display.class);
        i.putExtra("json", jsonResponse);
        startActivity(i);
    }
}, new Response.ErrorListener() {

    @Override
    public void onErrorResponse(VolleyError error) {
        VolleyLog.d(TAG, "Error: " + error.getMessage());
        Toast.makeText(getApplicationContext(),
                error.getMessage(), Toast.LENGTH_SHORT).show();
        // hide the progress dialog
        hidepDialog();

    }
});

jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
        15000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Adding request to request queue
AppController.getInstance().addToRequestQueue(jsonObjReq);

}



private void makeJsonObjectRequestPan() {
Uri.Builder builder = new Uri.Builder();
builder.scheme("http")
        .authority("identi.co.in")
        .appendPath("api")
        .appendPath("smart")
        .appendPath(etnum.getText().toString());
String myUrl = builder.build().toString();


showpDialog();

JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
        myUrl, null, new Response.Listener<JSONObject>() {

    @Override
    public void onResponse(JSONObject response) {
        Log.d(TAG, response.toString());

        try {
            // Parsing json object response
            // response will be a json object
            String valid = response.getString("valid");
            String type = response.getString("type");
            JSONObject data = response.getJSONObject("data");
            String surname = data.getString("Surname");
            String middlename = data.getString("Middle_Name");
            String firstname = data.getString("First_Name");
           String areacode = data.getString("Area_Code");





            jsonResponse = "";
            jsonResponse +="VALID  :   "   + valid +"\n\n";
            jsonResponse +="TYPE  :   " + type +"\n\n";
            jsonResponse += "SUR NAME :   " + surname + "\n\n";
            jsonResponse += "MIDDLE NAME  :   " + middlename + "\n\n";
            jsonResponse += "FIRST NAME  :  " + firstname + "\n\n";
            jsonResponse += "AREA CODE :   " + areacode + "\n\n";



        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        hidepDialog();
        Intent i = new Intent(MainActivity.this,Display.class);
        i.putExtra("json", jsonResponse);
        startActivity(i);
    }
}, new Response.ErrorListener() {

    @Override
    public void onErrorResponse(VolleyError error) {
        VolleyLog.d(TAG, "Error: " + error.getMessage());
        Toast.makeText(getApplicationContext(),
                error.getMessage(), Toast.LENGTH_SHORT).show();
        // hide the progress dialog
        hidepDialog();
    }
});

jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
        15000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


// Adding request to request queue
AppController.getInstance().addToRequestQueue(jsonObjReq);
}




private void showpDialog() {
if (!pDialog.isShowing())
    pDialog.show();
}

private void hidepDialog() {
if (pDialog.isShowing())
    pDialog.dismiss();
}








private boolean validateNumber() {
if ((etnum.getText().toString().trim().isEmpty())){
    inputLayout.setError(getString(R.string.err_msg_number));
    requestFocus(etnum);
    return false;
}

else {
    inputLayout.setErrorEnabled(false);
}

return true;
}

private void requestFocus(View view) {
if (view.requestFocus()) {
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
}
}

private class MyTextWatcher implements TextWatcher {

private View view;

private MyTextWatcher(View view) {
    this.view = view;
}

public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
}

public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
}

public void afterTextChanged(Editable editable) {
    switch (view.getId()) {
        case R.id.input_number:
            validateNumber();

            break;

    }
}
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
        return true;
    }

    return super.onOptionsItemSelected(item);
}


@Override
protected void onDestroy() {

super.onDestroy();

}



}

