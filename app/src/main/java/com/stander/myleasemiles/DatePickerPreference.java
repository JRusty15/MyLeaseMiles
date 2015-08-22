package com.stander.myleasemiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by jeffrey.stander on 8/21/2015.
 */
public class DatePickerPreference extends DialogPreference
{
    private String m_DateValue;
    private String m_DefaultValue;
    private DatePicker m_DatePicker;
    private Calendar m_CalendarDate;

    public DatePickerPreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        setDialogLayoutResource(R.layout.datepicker_dialog);
        setPositiveButtonText("OK");
        setNegativeButtonText("Cancel");

        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View view)
    {
        super.onBindDialogView(view);

        m_DatePicker = (DatePicker)view.findViewById(R.id.datePicker);
        if(m_DateValue == null || m_DateValue.isEmpty())
        {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            m_DateValue = (new StringBuilder().append(month + 1).append("/").append(day).append("/").append(year)).toString();
        }
        m_CalendarDate = GetCalendarFromString(m_DateValue);
        m_DatePicker.init(m_CalendarDate.get(Calendar.YEAR), m_CalendarDate.get(Calendar.MONTH),
                m_CalendarDate.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar c = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        m_DateValue = (new StringBuilder().append(monthOfYear + 1).append("/").append(dayOfMonth).append("/").append(year)).toString();
                    }
                });
    }

    private Calendar GetCalendarFromString(String value)
    {
        Calendar startCalendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = null;
        try
        {
            startDate = sdf.parse(value);
        }
        catch (Exception ex)
        {
            return null;
        }

        startCalendar.setTime(startDate);
        return startCalendar;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult)
    {
        if(positiveResult)
        {
            persistString(m_DateValue);
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue)
    {
        if(restorePersistedValue)
        {
            m_DateValue = this.getPersistedString(m_DefaultValue);
        }
        else
        {
            m_DateValue = (String)defaultValue;
            persistString(m_DateValue);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index)
    {
        return a.getString(index);
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        final Parcelable superState = super.onSaveInstanceState();
        if(isPersistent())
        {
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.value = m_DateValue;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if(state == null || !state.getClass().equals(SavedState.class))
        {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState)state;
        super.onRestoreInstanceState(myState.getSuperState());

        //Set date picker value
        m_CalendarDate = GetCalendarFromString(m_DateValue);
        m_DatePicker.updateDate(m_CalendarDate.get(Calendar.YEAR), m_CalendarDate.get(Calendar.MONTH),
                                m_CalendarDate.get(Calendar.DAY_OF_MONTH));
    }

    private static class SavedState extends BaseSavedState
    {
        // Member that holds the setting's value
        // Change this data type to match the type saved by your Preference
        String value;

        public SavedState(Parcelable superState)
        {
            super(superState);
        }

        public SavedState(Parcel source)
        {
            super(source);
            // Get the current preference's value
            value = source.readString();  // Change this to read the appropriate data type
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            super.writeToParcel(dest, flags);
            // Write the preference's value
            dest.writeString(value);  // Change this to write the appropriate data type
        }

        // Standard creator object using an instance of this class
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>()
                {
                    public SavedState createFromParcel(Parcel in)
                    {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size)
                    {
                        return new SavedState[size];
                    }
                };
    }
}
