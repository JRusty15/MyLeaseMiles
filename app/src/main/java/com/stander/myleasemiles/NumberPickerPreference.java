package com.stander.myleasemiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;


/**
 * Created by jeffrey.stander on 8/21/2015.
 */
public class NumberPickerPreference extends DialogPreference
{
    private int m_Value;
    private int m_DefaultValue = 0;
    private NumberPicker m_NumberPicker;

    public NumberPickerPreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        setDialogLayoutResource(R.layout.numberpicker_dialog);
        setPositiveButtonText("OK");
        setNegativeButtonText("Cancel");

        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View view)
    {
        super.onBindDialogView(view);

        m_NumberPicker = (NumberPicker)view.findViewById(R.id.numberPicker);
        m_NumberPicker.setMinValue(0);
        m_NumberPicker.setMaxValue(60);
        m_NumberPicker.setValue(m_Value);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult)
    {
        if(positiveResult)
        {
            m_Value = m_NumberPicker.getValue();
            persistInt(m_Value);
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue)
    {
        if(restorePersistedValue)
        {
            m_Value = this.getPersistedInt(m_DefaultValue);
        }
        else
        {
            m_Value = (Integer)defaultValue;
            persistInt(m_Value);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index)
    {
        int retVal = a.getInteger(index, m_DefaultValue);
        return retVal;
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
        myState.value = m_Value;
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
        m_NumberPicker.setValue(m_Value);
    }

    private static class SavedState extends BaseSavedState
    {
        // Member that holds the setting's value
        // Change this data type to match the type saved by your Preference
        int value;

        public SavedState(Parcelable superState)
        {
            super(superState);
        }

        public SavedState(Parcel source)
        {
            super(source);
            // Get the current preference's value
            value = source.readInt();  // Change this to read the appropriate data type
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            super.writeToParcel(dest, flags);
            // Write the preference's value
            dest.writeInt(value);  // Change this to write the appropriate data type
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
