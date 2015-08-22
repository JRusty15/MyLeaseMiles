package com.stander.myleasemiles;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{
    private TextView tvOutput;

    public MainActivityFragment()
    {
    }

    @Override
    public void onResume()
    {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sDate = prefs.getString("date", "");
        int iMonths = prefs.getInt("months", 36);
        int iMiles = prefs.getInt("miles", 36) * 1000;

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = null;
        try
        {
            startDate = sdf.parse(sDate);
        }
        catch (Exception ex)
        {
            tvOutput.setText("Please enter your information in the settings screen");
            return;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);

        Calendar endCalendar = (Calendar)startCalendar.clone();
        endCalendar.add(Calendar.MONTH, iMonths);

        Calendar todayCalendar = new GregorianCalendar();

        long todaySeconds = todayCalendar.getTimeInMillis();
        long startSeconds = startCalendar.getTimeInMillis();
        long daysFromStart = TimeUnit.MILLISECONDS.toDays(Math.abs(todaySeconds - startSeconds));

        long endSeconds = endCalendar.getTimeInMillis();
        long totalDays = TimeUnit.MILLISECONDS.toDays(Math.abs(endSeconds - startSeconds));

        double milesPerDay = (iMiles * 1.0) / totalDays;

        long milesToCurrent = Math.round(milesPerDay * daysFromStart);

        tvOutput.setText("You should be at " + NumberFormat.getInstance().format(milesToCurrent) + " miles");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v =  inflater.inflate(R.layout.fragment_main, container, false);
        tvOutput = (TextView)v.findViewById(R.id.tvOutput);

        return v;
    }
}
