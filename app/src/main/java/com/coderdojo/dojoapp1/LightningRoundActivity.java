package com.coderdojo.dojoapp1;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LightningRoundActivity extends Activity{
	private final String TAG = "LightningRoundActivity";




	//****************************************************************************************
	// mCheckBoxListener
	//	Handles checkbox clicks.  Marks the Kid as having completed his talk
	private View.OnClickListener mCheckBoxListener = new View.OnClickListener() {
		public void onClick(View v) {
			CheckBox checkbox = (CheckBox)v;

			//update the Kid's status
			TableRow clickedRow = (TableRow)v.getParent();
			int curRow = mKidsTable.indexOfChild(clickedRow);
			Kid currKid = mPresenters.get(curRow);
			String name = currKid.getName();
			currKid.setChecked(checkbox.isChecked());

			//checked -> move Kid to bottom of list
			if (checkbox.isChecked()) {
				mKidsTable.removeViewAt(curRow);
				mPresenters.remove(curRow);

				mPresenters.add(currKid);

				//display kid in new row at bottom of table
				appendKidToTable(currKid);

				TableRow newCheckbox = (TableRow) mKidsTable.getChildAt(mPresenters.size() - 1);

				checkbox = (CheckBox)newCheckbox.getChildAt(2);
				checkbox.setChecked(true);
			}

			//refresh alternating row colors
			refreshRowColors();
		}
	};

	//****************************************************************************************
	// Remove presenters
    private View.OnClickListener mRemovePresenterListener = new View.OnClickListener() {
        public void onClick(View v) {
            Log.d(TAG, "ClearButton Clicked");
            TableRow clickedRow = (TableRow)v.getParent();
			int curRow = mKidsTable.indexOfChild(clickedRow);
			Kid kid = mPresenters.get(curRow);
			mPresenters.remove(curRow);
			mKidsTable.removeViewAt(curRow);
			try{
				mPresenters.remove(kid);
			} catch (Exception e) {
				e.printStackTrace();
			}

			//refresh alternating row colors
			refreshRowColors();

        }

    } ;


	// mUpDownListener
	//	handles clicks of Up/Down buttons.  Moves Kid's name up or down in the list by one slot
	private View.OnClickListener mUpDownListener = new View.OnClickListener() {
		public void onClick(View v) {
			Log.d(TAG, "UpDownButton Clicked");
			//determine if up or down button
			int changeBy = -1; // change by moving up 1 == decrement index
			Button clickedButton = (Button)(v);

			if (clickedButton.getText().charAt(0) == 'v') {
				//	move down 1 ==> increment index
				changeBy = 1;
			}

			//get current position in table
			TableRow clickedRow = (TableRow)v.getParent();
			int curRow = mKidsTable.indexOfChild(clickedRow);
			int newRow = curRow + changeBy;

			//check if move is valid
			if ((newRow >= 0) && (newRow < mPresenters.size())) {
				//move table row as requested
				mKidsTable.removeViewAt(curRow);
				mKidsTable.addView(clickedRow, newRow);

				//refresh alternating row colors
				refreshRowColors();

				//update array
				Kid movedKid = mPresenters.get(curRow);
				mPresenters.remove(curRow);
				mPresenters.add(newRow, movedKid);

			}
		}
	};




	//List of Kids who have signed up to present
	protected ArrayList<Kid> mPresenters;

	private TableLayout mKidsTable;
	private EditText mNewNameTextBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lightning_round);

	}

	@Override
	protected void onStart(){
		super.onStart();

		//get data for table
		mPresenters = MainActivity.getKidsData();

		//populate view
		mNewNameTextBox = (EditText)findViewById(R.id.lightningRoundAddNameText);
		mKidsTable = (TableLayout)findViewById(R.id.lightningRoundTable);
		populateKidsTable();

	}
	
	private void populateKidsTable(){
		for (int i=0; i<mPresenters.size(); i++){

			Kid reconKid = mPresenters.get(i);
			addKidToTable(i, reconKid);
		}


	}

	private void addKidToTable(int i, Kid kid) {
		//add row to table
		TableRow nextRow = new TableRow(this);

		TableRow.LayoutParams rowLayout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
		nextRow.setLayoutParams(rowLayout);
		setRowColor(i, nextRow);
		RectShape rectangle = new RectShape();
		ShapeDrawable background = new ShapeDrawable(rectangle);
		background.setPadding(4, 2, 16, 2);
		Paint painter = background.getPaint();
		painter.setColor(Color.argb(100,100,0,0));
		//nextRow.setBackground(background); --- cannot use unless restrict to higher API levels



		//show Kid name in text view in new row
		TextView nextText = new TextView(this);
		nextText.setText(kid.getName());
		nextText.setPadding(2, 2, 16, 2);

		//provide checkbox to indicate has already presented
		CheckBox nextCheckBox = new CheckBox(this);
		nextCheckBox.setPadding(2, 2, 2, 2);
		nextCheckBox.setOnClickListener(this.mCheckBoxListener);
		nextCheckBox.setChecked(kid.isChecked());

		//add up/down buttons
		Button nextUpButton = new Button(this);
		nextUpButton.setText("^");
		nextUpButton.setEms(4);
		nextUpButton.setOnClickListener(this.mUpDownListener);
		Button nextDownButton = new Button(this);
		nextDownButton.setEms(4);
		nextDownButton.setText("v");
		nextDownButton.setOnClickListener(this.mUpDownListener);
        Button nextRemoveButton = new Button(this);
        nextRemoveButton.setText("Clear");
        nextRemoveButton.setEms(4);
        nextRemoveButton.setOnClickListener(this.mRemovePresenterListener);

		//setup row with checkbox, name, up/down buttons
		nextRow.addView(nextUpButton);
		nextRow.addView(nextDownButton);
		nextRow.addView(nextCheckBox);
		nextRow.addView(nextText);
        nextRow.addView(nextRemoveButton);
		
		mKidsTable.addView(nextRow, i);



	}

	private void refreshRowColors(){
		for (int i=0; i<mKidsTable.getChildCount(); i++){
			TableRow nextRow = (TableRow)mKidsTable.getChildAt(i);
			setRowColor(i,nextRow);
		}
	}

	private void setRowColor(int index, TableRow nextRow) {
		if (index %2==0) {
			nextRow.setBackgroundColor(Color.LTGRAY);
		}
		else{
			nextRow.setBackgroundColor(Color.argb(50,255,0,0));
		}
	}


	private void onEditListener(View view){

	}

	private void appendKidToTable(Kid kid) {
		int count = mKidsTable.getChildCount();
		addKidToTable(count, kid);
	}
	
	
	public void onClickAdd(View view){
		try{
			String newName = mNewNameTextBox.getText().toString();
			//remove leading/trailing spaces
			newName = newName.trim();
			
			//check name is not empty
			if(newName.isEmpty()){
				//ignore click
				//clear the edit box
				mNewNameTextBox.setText("");
				return;
			}
			
			//check name is not already in presentation list
			Kid found = null;
			for (int i=0; i<mPresenters.size(); i++){
				if (mPresenters.get(i).getName().equalsIgnoreCase(newName)){
					found = mPresenters.get(i);
					break;
				}
			}

			if (found != null){
				showMessage("This Kid is already in the Lightning Round");
				//clear the edit box
				mNewNameTextBox.setText("");
 				return;
			}

			//add kid to the presenters list
			Kid newKid = new Kid(newName);
			mPresenters.add(newKid);

			//display kid as new row in table
			appendKidToTable(newKid);
			
			//clear the edit box
			mNewNameTextBox.setText("");
		}
		catch(Exception ex){
			showMessage("Unable to add:  " + ex.getMessage());
		}
		
	}



    private void showMessage(String message){
		ApplicationUtilities.showMessage(this, message);
    }

}
