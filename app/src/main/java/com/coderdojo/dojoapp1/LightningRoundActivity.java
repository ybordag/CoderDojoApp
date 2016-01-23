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

import com.coderdojo.dojoapp1.dataSources.KidsDataSource;

public class LightningRoundActivity extends Activity{
	private final String TAG = "LightningRoundActivity";

	private Boolean reconKidCheck;



	//****************************************************************************************
	// mCheckBoxListener
	//	Handles checkbox clicks.  Marks the Kid as having completed his talk
	private View.OnClickListener mCheckBoxListener = new View.OnClickListener() {
		public void onClick(View v) {
			//mark the row grey
			CheckBox checkbox = (CheckBox)v;

			TableRow clickedRow = (TableRow)v.getParent();
			int curRow = mKidsTable.indexOfChild(clickedRow);
			String name = mPresenters.get(curRow).getName();
			TextView nameText = (TextView)clickedRow.getChildAt(1); //na

			Kid currKid = mKidsData.getKid(name);

			currKid._is_checked = true;


			try {
				mKidsData.save();
			} catch (Exception e) {
				e.printStackTrace();
			}


			if (checkbox.isChecked()) {
				if (currKid._is_checked) {
					currKid._is_checked = false;

					try {
						mKidsData.save();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				mKidsTable.removeViewAt(curRow);
				mPresenters.remove(curRow);
				try {
					mKidsData.removeKid(name);
				} catch (Exception e) {
					e.printStackTrace();
				}


			Kid replacedKid = new Kid(name);
			try {


				mKidsData.addKid(replacedKid);

				//add kid to the presenters list
				mPresenters.add(replacedKid);

				//display kid as new row in table
				appendKidToTable(name);
			} catch (Exception e) {
				e.printStackTrace();
			}


				TableRow newCheckbox = (TableRow) mKidsTable.getChildAt(mPresenters.size() - 1);

				checkbox = (CheckBox)newCheckbox.getChildAt(2);



				checkbox.setChecked(true);
			}





		}
	};

	//****************************************************************************************
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


				//update array
				Kid movedKid = mPresenters.get(curRow);
				mPresenters.remove(curRow);
				mPresenters.add(newRow, movedKid);

			}
		}
	};


	private KidsDataSource mKidsData;

	private ArrayList<Kid> mPresenters;

	private TableLayout mKidsTable;
	private EditText mNewNameTextBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lightning_round);

		//get data for table
		mKidsData = ApplicationFactory.getInstance().getKidsDataSource();
		//populate view
		mNewNameTextBox = (EditText)findViewById(R.id.lightningRoundAddNameText);
		mKidsTable = (TableLayout)findViewById(R.id.lightningRoundTable);
		populateKidsTable();


	}
	
	private void populateKidsTable(){
		mPresenters = mKidsData.getKids();
		for (int i=0; i<mPresenters.size(); i++){


			String name = mPresenters.get(i).getName();
			Kid reconKid = mKidsData.getKid(name);
			reconKidCheck = reconKid._is_checked;
			addKidToTable(i, name, reconKidCheck);
		}


	}

	private void addKidToTable(int i, String name, Boolean check) {
		//add row to table
		TableRow nextRow = new TableRow(this);

		TableRow.LayoutParams rowLayout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
		nextRow.setLayoutParams(rowLayout);
		if (i%2==0) {
			nextRow.setBackgroundColor(Color.LTGRAY);
		}
		else{
			nextRow.setBackgroundColor(Color.argb(50,255,0,0));
		}
		RectShape rectangle = new RectShape();
		ShapeDrawable background = new ShapeDrawable(rectangle);
		background.setPadding(4, 2, 16, 2);
		Paint painter = background.getPaint();
		painter.setColor(Color.argb(100,100,0,0));
		//nextRow.setBackground(background); --- cannot use unless restrict to higher API levels



		//show Kid name in text view in new row
		TextView nextText = new TextView(this);
		nextText.setText(name);
		nextText.setPadding(2, 2, 16, 2);

		//provide checkbox to indicate has already presented
		CheckBox nextCheckBox = new CheckBox(this);
		nextCheckBox.setPadding(2, 2, 2, 2);
		nextCheckBox.setOnClickListener(this.mCheckBoxListener);
		if (check == true) {
			nextCheckBox.setChecked(true);
		}

		//add up/down buttons
		Button nextUpButton = new Button(this);
		nextUpButton.setText("^");
		nextUpButton.setEms(4);
		nextUpButton.setOnClickListener(this.mUpDownListener);
		Button nextDownButton = new Button(this);
		nextDownButton.setEms(4);
		nextDownButton.setText("v");
		nextDownButton.setOnClickListener(this.mUpDownListener);

		//setup row with checkbox, name, up/down buttons
		nextRow.addView(nextUpButton);
		nextRow.addView(nextDownButton);
		nextRow.addView(nextCheckBox);
		nextRow.addView(nextText);
		
		mKidsTable.addView(nextRow, i);
	}



	private void onEditListener(View view){

	}

	private void appendKidToTable(String name) {
		int count = mKidsTable.getChildCount();
		addKidToTable(count,name, false);
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
			
			//add the kid to the database
			Kid newKid = new Kid(newName);
			mKidsData.addKid(newKid);

			//add kid to the presenters list
			mPresenters.add(newKid);

			//display kid as new row in table
			appendKidToTable(newName);
			
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
