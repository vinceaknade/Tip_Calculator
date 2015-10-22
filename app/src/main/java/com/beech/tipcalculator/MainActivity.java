package com.beech.tipcalculator;

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
    NumberFormat currency;
    NumberFormat percent;

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

        //initialize values and clear all fields
        setDefaults();
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
                tipPercent -= .01f;
                break;
            }
            case R.id.btnIncrease:
            {
                tipPercent += .01f;
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
        String strBillAmount = txtBillAmount.getText().toString();

        if(strBillAmount == ""){
            billAmount = 0f;
        } else {
                billAmount = Float.parseFloat(strBillAmount);
        }

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
        tipPercent = .2f;
        txtBillAmount.setText("0.00");
        updateUI(0f, 0f);
    }
}
