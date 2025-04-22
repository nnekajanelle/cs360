package com.app.weightloss.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.weightloss.R;

public class SMSActivity extends AppCompatActivity {

    private static final int REQUEST_SMS_PERMISSION = 100;
    private TextView tvStatus;
    private Button btnRequestPermission, btnSendSms;
    private EditText etPhoneNumber,etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsactivity);

        tvStatus = findViewById(R.id.tvStatus);
        btnRequestPermission = findViewById(R.id.btnRequestPermission);
        btnSendSms = findViewById(R.id.btnSendSms);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etMessage = findViewById(R.id.etMessage);

        checkPermission();

        btnRequestPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestSmsPermission();
            }
        });

        btnSendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSms();
            }
        });
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            tvStatus.setText("SMS permission already granted ✅");
            btnSendSms.setEnabled(true);
            etMessage.setEnabled(true);
            etPhoneNumber.setEnabled(true);
        } else {
            tvStatus.setText("Permission required to send SMS.");
            btnSendSms.setEnabled(false);
            etMessage.setEnabled(false);
            etPhoneNumber.setEnabled(false);
        }
    }

    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS},
                REQUEST_SMS_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tvStatus.setText("SMS permission granted ✅");
                btnSendSms.setEnabled(true);
                etMessage.setEnabled(true);
                etPhoneNumber.setEnabled(true);
            } else {
                tvStatus.setText("Permission denied. SMS notifications disabled ❌");
                btnSendSms.setEnabled(false);
                etMessage.setEnabled(false);
                etPhoneNumber.setEnabled(false);
            }
        }
    }

    private void sendSms() {
        String number = etPhoneNumber.getText().toString().trim();
        String message = etMessage.getText().toString().trim();

        if (number.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Please enter phone number and message", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, null, null);
            Toast.makeText(this, "SMS sent to " + number, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "SMS failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
