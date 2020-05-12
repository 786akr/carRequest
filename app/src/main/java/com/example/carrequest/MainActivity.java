package com.example.carrequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onClick(View v)

    {
        if (edtDriverOrPassenger.getText().toString().equals("Driver") || edtDriverOrPassenger.getText().toString().equals("Passenger")) {
            if (ParseUser.getCurrentUser() == null) {
                ParseAnonymousUtils.logIn(new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null && user != null) {
                            FancyToast.makeText(MainActivity.this, "Logged in", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                            user.put("as", edtDriverOrPassenger.getText().toString());

                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        if (edtDriverOrPassenger.getText().toString().equals("Passenger")){
                                //            Passenger();
                                            Toast.makeText(MainActivity.this, "Passenger", Toast.LENGTH_SHORT).show();
                                        }
                                        if(edtDriverOrPassenger.getText().toString().equals("Driver")) {
                                  //          Driver();
                                            Toast.makeText(MainActivity.this, "Driver", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            });
                        }
                    }
                });
            }


        } else {
            FancyToast.makeText(MainActivity.this,
                    "are you a driver or passeneger",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.SUCCESS,
                    true).show();

        }
    }

    enum State {
        SIGNUP, LOGIN
    }

    private Button btnsign, btnoneTimeLogin;
    private RadioButton DriverRadioButton, PassengerRadioButton;
    private EditText edtUsername, edtPassword, edtDriverOrPassenger;
    private State state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseInstallation.getCurrentInstallation().saveInBackground();// build a connection with parse server

        btnsign = findViewById(R.id.btnsignUp);
        state = State.SIGNUP;
        edtPassword = findViewById(R.id.edtPassword);
        edtUsername = findViewById(R.id.edtUsername);
        btnoneTimeLogin = findViewById(R.id.OneTimeLogin);
        edtDriverOrPassenger = findViewById(R.id.edtDriverorPasseneger);
        DriverRadioButton = findViewById(R.id.rdoDriver);
        PassengerRadioButton = findViewById(R.id.rdoPassenger);
        btnoneTimeLogin.setOnClickListener(this);
        if (ParseUser.getCurrentUser() != null) {
            ParseUser.logOut();
        }

        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == State.SIGNUP) {
                    if (edtUsername.getText().toString().equals("") && edtPassword.getText().toString().equals("")) {
                        Toast.makeText(MainActivity.this, "Fill Email and Password", Toast.LENGTH_SHORT).show();
                    } else if (DriverRadioButton.isChecked() == false && PassengerRadioButton.isChecked() == false) {
                        Toast.makeText(MainActivity.this, "Please Describe Driver or Passenger ", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        ParseUser appUser = new ParseUser();
                        appUser.setUsername(edtUsername.getText().toString());
                        appUser.setPassword(edtPassword.getText().toString());
                        if (DriverRadioButton.isChecked()) {
                            appUser.put("as", "Driver");
                        } else if (PassengerRadioButton.isChecked()) {
                            appUser.put("as", "Passenger");
                        }
                        FancyToast.makeText(MainActivity.this, "Done", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                        appUser.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    FancyToast.makeText(MainActivity.this, "saved SignUP", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                    Toast.makeText(MainActivity.this, "Passenger", Toast.LENGTH_SHORT).show();
                                   // Passenger();
                                    if (PassengerRadioButton.isChecked()) {
                                     //   Passenger();
                                            startActivity(new Intent(MainActivity.this,PassengerActivity.class));
                                        Toast.makeText(MainActivity.this, "Passenger", Toast.LENGTH_SHORT).show();
                                    }
                                    if (DriverRadioButton.isChecked()) {
                                       // Driver();
                                        Toast.makeText(MainActivity.this, "Driver", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                }
                if (state == State.LOGIN) {
                    ParseUser.logInInBackground(edtUsername.getText().toString(), edtPassword.getText().toString(), (new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (e == null && user != null) {
                                FancyToast.makeText(MainActivity.this, "Logged in", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                if(user.get("as").equals("Driver")) {
                                   Toast.makeText(MainActivity.this, "Driver", Toast.LENGTH_SHORT).show();
                                }
                                if(user.get("as").equals("Passenger")){
                                    //Passenger();
                                    startActivity(new Intent(MainActivity.this,PassengerActivity.class));
                                    Toast.makeText(MainActivity.this, "Passenger", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }
                    }));

                }
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.LoginItem:
                if (state == State.SIGNUP) {
                    state = State.LOGIN;
                    item.setTitle("Sign Up");
                    btnsign.setText("Log In");
                } else if (state == State.LOGIN) {
                    state = State.SIGNUP;
                    item.setTitle("Log In");
                    btnsign.setText("Sign Up");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
