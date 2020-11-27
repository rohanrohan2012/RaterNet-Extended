package com.example.raternet_isp_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.raternet_isp_app.models.Constants;
import com.example.raternet_isp_app.models.Discussion;
import com.example.raternet_isp_app.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class CreateDiscussionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    public Spinner spinnerIssueType;
    public EditText edtMyLocality;
    public EditText edtMyISP;
    public EditText edtMyIssueTitle;
    public EditText edtMyIssueDetails;
    public Button btnSubmitIssue;

    private ProgressDialog progressDialog;

    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_discussion);

        spinnerIssueType=findViewById(R.id.spinnerIssueType);
        ArrayAdapter<CharSequence> issueAdapter=ArrayAdapter.createFromResource(this,R.array.issueTypes, R.layout.spinner_item);
        issueAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerIssueType.setAdapter(issueAdapter);

        spinnerIssueType.setOnItemSelectedListener(this);

        edtMyISP=findViewById(R.id.edtMyISP);
        edtMyLocality=findViewById(R.id.edtMyLocality);
        edtMyIssueTitle=findViewById(R.id.edtMyIssueTitle);
        edtMyIssueDetails=findViewById(R.id.edtMyIssueDetails);

        //Setting locality and ISP
        edtMyISP.setText(Constants.ISP_Name);
        edtMyLocality.setText(Constants.locality);

        btnSubmitIssue=findViewById(R.id.btnSubmitIssue);

        btnSubmitIssue.setOnClickListener(this);

        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this,R.id.edtMyIssueTitle, RegexTemplate.NOT_EMPTY,R.string.issueTitle_error);
        awesomeValidation.addValidation(this,R.id.txtFeedback, RegexTemplate.NOT_EMPTY,R.string.issueDetails_error);

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnSubmitIssue:
                boolean validated=awesomeValidation.validate();
                if(validated)
                {
                    Constants.issueTitle=edtMyIssueTitle.getText().toString().trim();
                    Constants.issueDetails=edtMyIssueDetails.getText().toString().trim();

                    //Enable Progress Dialog
                    progressDialog = new ProgressDialog(CreateDiscussionActivity.this);
                    progressDialog.setMessage("Submitting your Issue...");
                    progressDialog.show();

                    //Enter Discussion here
                    final String UserEmail = Constants.UserEmail;
                    final String ISP_Name = Constants.ISP_Name;
                    final String locality=Constants.locality;
                    final String issueType=Constants.issueType;
                    final String issueTitle=Constants.issueTitle;
                    final String issueDetails=Constants.issueDetails;

                    final Discussion new_discussion=new Discussion(UserEmail,ISP_Name,locality,issueType,issueTitle,issueDetails);

                    FirebaseDatabase.getInstance().getReference("Discussions").push().setValue(new_discussion)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        //Disable ProgressDialog
                                        progressDialog.dismiss();
                                        Toast.makeText(CreateDiscussionActivity.this, "Successfully Added Issue",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(CreateDiscussionActivity.this,MainActivity2.class));
                                        finish();
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(CreateDiscussionActivity.this, "Adding Issue Failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(this, "Unsuccessful Submission", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String issueType=adapterView.getItemAtPosition(i).toString();
        Constants.issueType=issueType;
        Toast.makeText(adapterView.getContext(), "Selected: "+issueType, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed ()
    {
        startActivity(new Intent(CreateDiscussionActivity.this,MainActivity.class));
    }
}