package com.bootcamp.gattani.mytipcalculator;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class TipCalculatorActivity extends Activity {
	private DecimalFormat df = new DecimalFormat("#.##");
	private TextView tvTipAmountValue;
	private TextView tvTotalAmountValue;
	private EditText etBillAmount;
	private EditText etCustomTipPct;
	private RadioGroup rgTipPercentages;
	private RadioButton rbtnCustomPct;
	private ToggleButton tBtnRound;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);
        mapActivityElements();
        addRadioButtonGroupListener();
        addBillAmountListener();
        addCustomTipListener();
        addRoundingListener();
    }

    private void mapActivityElements(){
        this.tvTipAmountValue = (TextView)findViewById(R.id.tvTipAmountValue);
        this.tvTotalAmountValue = (TextView)findViewById(R.id.tvTotalAmountValue);
        this.etBillAmount = (EditText)findViewById(R.id.etBillAmount);
        this.etCustomTipPct = (EditText)findViewById(R.id.etCustomTipPct);
        this.rgTipPercentages = (RadioGroup)findViewById(R.id.rgTipPercentages);
        this.rbtnCustomPct = (RadioButton)findViewById(R.id.rbtnCustomPct);
        this.tBtnRound = (ToggleButton)findViewById(R.id.tBtnRound);
    }
    
    private void addRadioButtonGroupListener(){
    	//listen for tip percentage change to update the Tip and Total
    	rgTipPercentages.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int id) {
				if(id != R.id.rbtnCustomPct){
					hideSoftKeyBoard();
				} else {
					etCustomTipPct.requestFocus();
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			    	imm.showSoftInput(etCustomTipPct, 0);
				}
				setValues();
				return;
			}			
		});
    }

    private void addBillAmountListener(){
    	//listen for bill amount change to update the Tip and Total
    	etBillAmount.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//nothing to do here;
				return;
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				//nothing to do here;
				return;				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				setValues();
				return;				
			}
		});
    }

    private void addCustomTipListener(){
    	//select custom tip percent radio buttom
    	etCustomTipPct.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rbtnCustomPct.setChecked(true);
			}
		});
    	
    	//listen for custom tip percentage change to update the Tip and Total
    	etCustomTipPct.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//nothing to do here;
				return;
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				//nothing to do here;
				return;				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				setValues();
				return;				
			}
		});
    }
    
    private void addRoundingListener(){
    	tBtnRound.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hideSoftKeyBoard();
				setValues();
			}
		});
    }

    private void setValues(){
		try {
			double tip = getTipAmount();
			double total = getTotalAmount();
			tvTipAmountValue.setText("$ ".concat(String.valueOf(tip)));
			tvTotalAmountValue.setText("$ ".concat(String.valueOf(total)));
		} catch (Exception e) {
			tvTipAmountValue.setText(R.string.empty_string);
			tvTotalAmountValue.setText(R.string.empty_string);
		}
    }
    
    private double getTipAmount() throws NumberFormatException {
		double billAmount = Double.parseDouble(etBillAmount.getText().toString());	
		billAmount = Double.valueOf(df.format(billAmount));
		double tipPct = getTipPercentage();
		double tip = ((billAmount * tipPct) / 100);
		tip = Double.valueOf(df.format(tip));
		if(doRound()){
			double total = billAmount + tip;
			total = Math.round(total);
			tip = total - billAmount;
		}
        return Double.valueOf(df.format(tip));
    }

    private double getTotalAmount() throws NumberFormatException {
		double billAmount = Double.parseDouble(etBillAmount.getText().toString());
		billAmount = Double.valueOf(df.format(billAmount));
		double tip = getTipAmount();
    	return Double.valueOf(df.format(billAmount + tip));
    }
    
    private double getTipPercentage() throws NumberFormatException {	
    	double tipPct = 0;
    	int id = rgTipPercentages.getCheckedRadioButtonId();
    	switch(id){
	    	case R.id.rbtn10Pct: tipPct = 10; break;
	    	case R.id.rbtn15Pct: tipPct = 15; break;
	    	case R.id.rbtn20Pct: tipPct = 20; break;
	    	case R.id.rbtnCustomPct: 
	    		String customTipPct = etCustomTipPct.getText().toString();
				tipPct = Double.parseDouble(customTipPct); 
	    		break;	    		
    	}
    	
    	return tipPct;
    }
    
    private boolean doRound(){
    	boolean doRound = true;
    	String toggleBtnText = tBtnRound.getText().toString();
    	if(getResources().getString(R.string.exact_label).equalsIgnoreCase(toggleBtnText)) {
    		doRound = false;
    	}
    	
    	return doRound;
    }
    private void hideSoftKeyBoard(){
    	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    	imm.hideSoftInputFromWindow(etCustomTipPct.getWindowToken(), 0);
    	imm.hideSoftInputFromWindow(etBillAmount.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tip_calculator, menu);
        return true;
    }
    
}
