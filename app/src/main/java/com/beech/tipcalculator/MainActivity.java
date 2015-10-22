package com.beech.tipcalculator;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


import java.text.NumberFormat;
import java.text.ParseException;

/****************************NOTE**************************/
    //alt+enter will bring up a solution menu for bugs related to imports (Perhaps more)
/****************************NOTE**************************/

public class MainActivity extends AppCompatActivity
        implements OnEditorActionListener, View.OnClickListener {

    private static final float defaultTipPercent = .2f;
    private static final float adjustTip = .01f;

    //Instance Variables (Declaration) for controls and fields
    private EditText txtBillAmount;

    private TextView txtTipPercent;
    private TextView txtTip;
    private TextView txtTotal;

    private Button btnIncrease;
    private Button btnDecrease;
    private Button btnClear;

    //Instance Variables (Declaration) for calculations
    private float tipPercent;

    //Instance variables (Declaration) for formatting
    private NumberFormat currency;
    private NumberFormat percent;

    private String strBillAmount;

    //define shared preference object
    private SharedPreferences savedValues;

    //First function call when an app is opened
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize controls and fields
        txtBillAmount = (EditText)findViewById(R.id.txtBillAmount);

        txtTipPercent = (TextView) findViewById(R.id.txtTipPercent);
        txtTip = (TextView) findViewById(R.id.txtTip);
        txtTotal = (TextView) findViewById(R.id.txtTotal);

        btnIncrease = (Button) findViewById(R.id.btnIncrease);
        btnDecrease = (Button) findViewById(R.id.btnDecrease);
        btnClear = (Button) findViewById(R.id.btnClear);

        //Create Listeners
        txtBillAmount.setOnEditorActionListener(this);

        btnIncrease.setOnClickListener(this);
        btnDecrease.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        currency = NumberFormat.getCurrencyInstance();
        percent = NumberFormat.getPercentInstance();

        // get shared Preferences object
        savedValues = getSharedPreferences("savedValues", MODE_PRIVATE);

        //initialize values and clear all fields
        setDefaults();
    }

    //(Alt+Fn+Insert) opens a create method option
    @Override
    protected void onPause() {

        //save instance variables
        //access file editor
        Editor editor = savedValues.edit();

        //write to file
        editor.putString("billAmountString", strBillAmount);
        editor.putFloat("tipPercent",tipPercent);

        //commit changes
        editor.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //retrieve instance variables //TODO test toString() here
        strBillAmount = savedValues.getString("billAmountString","");
        tipPercent = savedValues.getFloat("tipPercent", defaultTipPercent);

        //apply bill amount to field
        txtBillAmount.setText(strBillAmount);

        //update
        calculateAndDisplay();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if(actionId == EditorInfo.IME_ACTION_DONE
                || actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
            calculateAndDisplay();
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.btnDecrease:
            {
                tipPercent -= adjustTip;
                break;
            }
            case R.id.btnIncrease:
            {
                tipPercent += adjustTip;
                break;
            }
            case R.id.btnClear:
            {
                setDefaults();
            }
        }
        calculateAndDisplay();
    }

    public void calculateAndDisplay(){
        float billAmount;
        float tipAmount;
        float total;

        //get bill amount
        strBillAmount = txtBillAmount.getText().toString();

        if( strBillAmount.equals("")){
            billAmount = 0f;
        } else {
                billAmount = Float.parseFloat(strBillAmount);
        }

        /*try {
            billAmount = Float.parseFloat(strBillAmount);
        }
        catch(Exception e)
        {
            billAmount = 0f;
        }*/

        //calculate total
        tipAmount = billAmount * tipPercent;
        total = billAmount + tipAmount;

        //update UI
        updateUI(tipAmount, total);
    }

    private void updateUI(float tipAmount, float total) {

        txtTip.setText(currency.format(tipAmount));
        txtTotal.setText(currency.format(total));
        txtTipPercent.setText(percent.format(tipPercent));
    }

    public void setDefaults(){
        tipPercent = defaultTipPercent;
        txtBillAmount.setText("");
        updateUI(0f, 0f);

        /*//save instance variables //TODO this seems wrong
        //access file editor
        Editor editor = savedValues.edit();

        //write to file
        editor.putString("billAmountString", "");
        editor.putFloat("tipPercent", 0f);

        //commit changes
        editor.commit();*/
    }
}
