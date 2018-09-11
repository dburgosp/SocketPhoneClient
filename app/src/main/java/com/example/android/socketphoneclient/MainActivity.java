package com.example.android.socketphoneclient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends Activity {
    TextView mResponseTextView;
    EditText mEditTextAddress, mEditTextPort;
    Button mButtonConnect;
    static SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextAddress = findViewById(R.id.addressEditText);
        mEditTextPort = findViewById(R.id.portEditText);
        mButtonConnect = findViewById(R.id.connectButton);
        mResponseTextView = findViewById(R.id.responseTextView);

        // Read preferences for server IP and port.
        mSharedPreferences = getDefaultSharedPreferences(this);
        mEditTextAddress.setText(mSharedPreferences.getString(
                getString(R.string.preference_server_ip), ""));
        mEditTextPort.setText(mSharedPreferences.getString(
                getString(R.string.preference_server_port), ""));

        mButtonConnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Client myClient = new Client(mEditTextAddress.getText().toString(),
                        Integer.parseInt(mEditTextPort.getText().toString()), mResponseTextView,
                        MainActivity.this);
                myClient.execute();

                // Update button state.
                if (myClient.isConnected()) {
                    mButtonConnect.setEnabled(false);
                }

                // Save preferences.
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(getString(R.string.preference_server_ip),
                        mEditTextAddress.getText().toString());
                editor.putString(getString(R.string.preference_server_port),
                        mEditTextPort.getText().toString());
                editor.apply();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("text", mResponseTextView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mResponseTextView.setText(savedInstanceState.getString("text"));
    }
}
