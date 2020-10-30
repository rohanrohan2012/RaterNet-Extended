package com.example.raternet_isp_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class IspRatingsActivity extends AppCompatActivity {

    public RadioGroup RdoGrpType;
    public RadioButton RdoBtnType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isp_ratings);

        RdoGrpType=findViewById(R.id.RdoGrpType);
    }

    public void checkType(View v)
    {
        int radioId=RdoGrpType.getCheckedRadioButtonId();

        RdoBtnType=findViewById(radioId);

        Toast.makeText(this, "Selected "+RdoBtnType.getText(), Toast.LENGTH_SHORT).show();
    }
}