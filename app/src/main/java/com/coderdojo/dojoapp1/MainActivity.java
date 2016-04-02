package com.coderdojo.dojoapp1;

import java.util.ArrayList;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends Activity {
    private final String TAG = "MainActivity";

    private static ArrayList<Kid> mKidsData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            mKidsData = new ArrayList<Kid>();
        }
        catch(Exception ex){
            showMessage("Unable to open Kids Data:  " + ex.getMessage());
        }
    }

    protected void onPause(){
        //save any data
        super.onPause();
    }

    protected void onClose(){
        try {
            //mKidsData.close();
        }
        catch(Exception ex){
            ApplicationUtilities.showMessage(this,"Error in closing" + ex.getMessage());
        }
    }

    public void onClickLoad(View view){
        Log.d(TAG, "onClickLoad");
        try{
           // mKidsData.load();
        }
        catch(Exception ex){
            showMessage("Unable to load:  " + ex.getMessage());
        }

    }

    public void onClickSave(View view){
        Log.d(TAG, "onClickSave");
        try{
           // mKidsData.save();
        }
        catch(Exception ex){
            showMessage("Unable to save:  " + ex.getMessage());
        }

    }

    public void onClickClear(View view){
        Log.d(TAG, "onClickClear");
        try{
            mKidsData.clear();
        }
        catch(Exception ex){
            showMessage("Unable to clear:  " + ex.getMessage());
        }

    }

    protected void onDestroy(){
        //persist data

        //cleanup
        if (mKidsData != null){
            mKidsData = null;
            //make sure all files/data stores closed
        }

        super.onDestroy();
    }





    public void onClickLightningRound(View view){
        Log.d(TAG, "onClickLightningRound entered");

        Intent intent = new Intent(this, LightningRoundActivity.class);
        startActivity(intent);

    }

    public static ArrayList<Kid> getKidsData(){
        return mKidsData;
    }

    private void showMessage(String message){
        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this);
        dlgBuilder.setMessage(message);
        dlgBuilder.setCancelable(true);
//	    dlgBuilder.setPositiveButton("Yes",
//	            new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int id) {
//	            dialog.cancel();
//	        }
//	    });
//	    dlgBuilder.setNegativeButton("No",
//	            new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int id) {
//	            dialog.cancel();
//	        }
//	    });

        AlertDialog alert11 = dlgBuilder.create();
        alert11.show();
    }

}
