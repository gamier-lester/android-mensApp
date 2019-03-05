package com.thesis.group.periodecalcalendar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

public class editprofile extends AppCompatActivity implements View.OnClickListener{
    EditText _tfPic, _tfHeight, _tfWeight;
    ImageView _imgLeft, _imgRight, _imgUp;
    Spinner _spnrWorking, _spnrSports;
    SessionManager session;
    DatabaseHelper db;
    HashMap<String, String> user, regProfileMap;
    ArrayAdapter<CharSequence> adapter1, adapter2;
    String sid, sheight, sweight, spnrWorking, spnrSports;
    int iSpnrWorking, iSpnrSports;
    double height, weight;
    private static final int READ_REQUEST_CODE = 42;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        session = new SessionManager(getApplicationContext());
        user = session.getUserDetails();
        sid = user.get(SessionManager.KEY_ID);

        db = new DatabaseHelper(this);

        ScrollView _scrlvParent = findViewById(R.id.scrlvParentProfile);
        _tfPic = findViewById(R.id.tfPcitureProfile);
        _tfHeight = findViewById(R.id.tfHeightProfile);
        _tfWeight = findViewById(R.id.tfWeightProfile);
        _spnrWorking = findViewById(R.id.spnrWorkingProfile);
        _spnrSports = findViewById(R.id.spnrSportsActiveProfile);
        _imgLeft = findViewById(R.id.imgLeftProfile);
        _imgRight = findViewById(R.id.imgRightProfile);
        _imgUp = findViewById(R.id.imgUpProfile);
        _scrlvParent.setFocusable(true);
        _scrlvParent.setFocusableInTouchMode(true);
        _scrlvParent.requestFocus();
        _tfPic.setShowSoftInputOnFocus(false);

        adapter1 = ArrayAdapter.createFromResource(this, R.array.working_reg, R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(R.layout.dropdown_spinner_item);
        _spnrWorking.setAdapter(adapter1);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.sports_active_reg, R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(R.layout.dropdown_spinner_item);
        _spnrSports.setAdapter(adapter2);

        setInputsValue();

        _tfPic.setOnClickListener(this);
        findViewById(R.id.btnUpdateProfile).setOnClickListener(this);
        _imgLeft.setOnClickListener(this);
        _imgRight.setOnClickListener(this);
        _imgUp.setOnClickListener(this);

    }

    @Override
    protected void onResume() {

        super.onResume();
        _imgLeft.setClickable(true);
        _imgRight.setClickable(true);
        _imgUp.setClickable(true);
    }

    private void setInputsValue()
    {
        String imagePath = null;
        boolean checkPic = db.checkPicture(sid);
        if (checkPic)
        {
            imagePath = db.getPicture(sid);
        }

        String imageUrl;
        if (imagePath == null || isEmpty(imagePath))
        {
            imageUrl = "";
        }
        else
        {
            imageUrl = imagePath;
        }

        regProfileMap = db.getRegProfile(sid);
        String height = regProfileMap.get(DatabaseHelper.KEY_height);
        String weight = regProfileMap.get(DatabaseHelper.KEY_weight);
        String check1 = regProfileMap.get(DatabaseHelper.KEY_work);
        String check2 = regProfileMap.get(DatabaseHelper.KEY_sports);
        assert check1 != null;
        String work = convertSpnrValueReverse(check1);
        assert check2 != null;
        String sports = convertSpnrValueReverse(check2);

        _tfPic.setText(imageUrl);
        _tfHeight.setText(height);
        _tfWeight.setText(weight);

        int spnrPosition1 = adapter1.getPosition(work);
        _spnrWorking.setSelection(spnrPosition1);

        int spnrPosition2 = adapter2.getPosition(sports);
        _spnrSports.setSelection(spnrPosition2);
    }

    private int convertSpnrValue(String spnrValue)
    {
        if (spnrValue.equals("yes"))
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    private String convertSpnrValueReverse(String spnrValue)
    {
        if (spnrValue.equals("1"))
        {
            return "yes";
        }
        else
        {
            return "no";
        }
    }

    private boolean isEmpty(String str)
    {
        return TextUtils.isEmpty(str);
    }

    private boolean checkDataEntered(String imagePathlc, String heightlc, String weightlc, String workinglc, String sportslc)
    {
        boolean bol = true;

        if (isEmpty(imagePathlc))
        {
            bol = false;
        }

        if (isEmpty(weightlc))
        {
            bol = false;
        }

        if (isEmpty(heightlc))
        {
            bol = false;
        }

        if (isEmpty(workinglc))
        {
            bol = false;
        }

        if (isEmpty(sportslc))
        {
            bol = false;
        }

        return bol;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    public void saveProfile(String imgPath, double heightlc, double weightlc, int worklc, int sportlc)
    {
        boolean check = db.checkPicture(sid);
        if(check)
        {
            boolean check1 = db.updatePicture(imgPath, sid);
            boolean check2 = db.updateProfile(heightlc, weightlc, worklc, sportlc, sid);
            if (check1 && check2)
            {
                Toast.makeText(getApplicationContext(), "Successfully updated profile!", Toast.LENGTH_LONG).show();
                Intent loginIntent = new Intent(editprofile.this, welcomeuser.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                loginIntent.putExtra("EXIT", true);
                startActivity(loginIntent);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Failed to update profile!!", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            boolean check1 = db.setPicture(imgPath, sid);
            boolean check2 = db.updateProfile(heightlc, weightlc, worklc, sportlc, sid);
            if (check1 && check2)
            {
                Toast.makeText(getApplicationContext(), "Successfully updated profile!", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Failed to update profile!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData)
    {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            Uri uri;
            if (resultData != null)
            {
                uri = resultData.getData();
                assert uri != null;
                String imgPath = uri.toString();
                _tfPic.setText(imgPath);
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v)
    {



        if(v.getId()==R.id.tfPcitureProfile)
        {
            openImageChooser();
        }
        else  if (v.getId()==R.id.imgLeftProfile)
        {
            _imgLeft.setClickable(false);
            Intent intent = new Intent(editprofile.this, reminder.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
        }
        else  if (v.getId()==R.id.imgRightProfile)
        {
            _imgRight.setClickable(false);
            Intent intent = new Intent(editprofile.this, settings.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
        }
        else  if (v.getId()==R.id.imgUpProfile)
        {
            _imgUp.setClickable(false);
            Intent intent = new Intent(editprofile.this, welcomeuser.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
        }
        else
        {
            sheight = _tfHeight.getText().toString();
            try
            {
                height = Double.parseDouble(sheight);
            }
            catch (NumberFormatException e)
            {
                height = 0;
            }

            sweight = _tfWeight.getText().toString();
            try
            {
                weight = Double.parseDouble(sweight);
            }
            catch (NumberFormatException e)
            {
                weight = 0;
            }

            spnrWorking = _spnrWorking.getSelectedItem().toString();
            spnrSports = _spnrSports.getSelectedItem().toString();
            iSpnrWorking = convertSpnrValue(spnrWorking);
            iSpnrSports = convertSpnrValue(spnrSports);

            String imagePathlc = _tfPic.getText().toString();

            if (checkDataEntered(imagePathlc, sheight, sweight, spnrWorking, spnrSports))
            {
                saveProfile(imagePathlc, height, weight, iSpnrWorking, iSpnrSports);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Please fill up all the fields!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
