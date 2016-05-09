package m.workshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class Display extends AppCompatActivity {

    private TextView txtResponse;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtResponse = (TextView) findViewById(R.id.txtResponse);
        Intent i = getIntent();
        String  str = i.getStringExtra("json");
        txtResponse.setText(str);







    }

}
