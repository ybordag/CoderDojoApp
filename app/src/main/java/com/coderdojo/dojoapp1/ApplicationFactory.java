package com.coderdojo.dojoapp1;

import android.util.Log;

import com.coderdojo.dojoapp1.dataSources.KidsDataSource;
import com.coderdojo.dojoapp1.dataSources.XmlKidsDataSource;

/**
 * Created by LouisaSeever on 7/27/2015.
 */


public class ApplicationFactory {
    private final String TAG = "ApplicationFactory";
    private static ApplicationFactory mFactory = new ApplicationFactory();

    public static ApplicationFactory getInstance() {
        return mFactory;
    }

    private static KidsDataSource mKidsData;

    private ApplicationFactory() {
    }

    public  KidsDataSource getKidsDataSource(){
        if (mKidsData == null){
              //load any existing data
            try {
                //.mKidsData = new InMemoryKidsDataSource();
                mKidsData = new XmlKidsDataSource();
                //mKidsData.clear();
                //mKidsData.load();
            }
            catch(Exception ex){
                Log.d(TAG, "Cannot load Kids file: " + ex.getMessage());
            }
        }
        return mKidsData;
    }



}
