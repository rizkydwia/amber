package com.amber.rizkydwi.amber;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {
    private String mVerificationId;
    private EditText editTextCode;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private Button buttonsignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_verify_phone);

        mAuth = FirebaseAuth.getInstance();
        editTextCode = findViewById(R.id.editTextCode);
         buttonsignin = findViewById(R.id.buttonSignIn);

         buttonsignin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
               // startActivity(new Intent(VerifyPhoneActivity.this, MapActivity.class));


             }
         });

        Intent intent = getIntent();
        String mobile = intent.getStringExtra("mobile");
        sendVerivicationCode(mobile);

    }

    private void sendVerivicationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+62" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (null != code) {
                editTextCode.setText(code);
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            resendingToken = forceResendingToken;
    }

        private void  verifyVerificationCode (String otp) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
            signInWithPhoneAuthCredential(credential);
        }

        private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(VerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //verifi sukses lanjut ke map activity
                            Intent intent = new Intent(VerifyPhoneActivity.this, MapActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            String message = "Something is wrong";

                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                message = "invalid code";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent),message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                        }
                    });
        }
    };
}
