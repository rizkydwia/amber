package com.amber.rizkydwi.amber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.phone);

        findViewById(R.id.buttonlanjut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = phone.getText().toString().trim();

                if (mobile.isEmpty() || mobile.length() < 8){
                    phone.setError("Enter a valid number");
                    phone.requestFocus();
                    return;
                }

                Intent intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });
    }
}
