package not_an_example.com.freelancerworld.Fragments;

import android.content.Context;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import not_an_example.com.freelancerworld.Contants.FilterConstants;
import not_an_example.com.freelancerworld.Enum.PaymentMethod;
import not_an_example.com.freelancerworld.Enum.SeekType;
import not_an_example.com.freelancerworld.R;
import not_an_example.com.freelancerworld.Utils.Filters;

public class JobFiltersFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private SeekBar mYourDistance, mHomeDistance, mMinPayment, mMaxPayment, mMinTime, mMaxTime;
    private TextView mYourDistanceText, mHomeDistanceText, mMinPaymentText, mMaxPaymentText, mMinTimeText, mMaxTimeText;
    private Spinner mPaymentMethodSpinner;

    public JobFiltersFragment() {
        // Required empty public constructor
    }


    public static JobFiltersFragment newInstance(String param1, String param2) {
        JobFiltersFragment fragment = new JobFiltersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_filters, container, false);

        mYourDistance = (SeekBar) view.findViewById(R.id.job_filters_distance_your_seek);
        mHomeDistance = (SeekBar) view.findViewById(R.id.job_filters_distance_home_seek);
        mMinPayment = (SeekBar) view.findViewById(R.id.job_filters_payment_min_seek);
        mMaxPayment = (SeekBar) view.findViewById(R.id.job_filters_payment_max_seek);
        mMinTime = (SeekBar) view.findViewById(R.id.job_filters_time_min_seek);
        mMaxTime = (SeekBar) view.findViewById(R.id.job_filters_time_max_seek);

        mYourDistanceText = (TextView) view.findViewById(R.id.job_filters_distance_your_text_value);
        mHomeDistanceText = (TextView) view.findViewById(R.id.job_filters_distance_home_text_value);
        mMinPaymentText = (TextView) view.findViewById(R.id.job_filters_payment_min_text_value);
        mMaxPaymentText = (TextView) view.findViewById(R.id.job_filters_payment_max_text_value);
        mMinTimeText = (TextView) view.findViewById(R.id.job_filters_time_min_text_value);
        mMaxTimeText = (TextView) view.findViewById(R.id.job_filters_time_max_text_value);

        mPaymentMethodSpinner = (Spinner) view.findViewById(R.id.job_filters_payment_method_spinner);

        setListeners();
        assignInitialValues();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        saveValues();
        mListener = null;
    }

    private void assignInitialValues() {
        mYourDistance.setProgress((int) (Filters.getRangeFromYou() / FilterConstants.DISTANCE_CONVERSION));
        mHomeDistance.setProgress((int) (Filters.getRangeFromOffice() / FilterConstants.DISTANCE_CONVERSION));
        mMinPayment.setProgress((int) (Filters.getMinPayment() / FilterConstants.MONEY_CONVERSION));
        mMaxPayment.setProgress((int) (Filters.getMaxPayment() / FilterConstants.MONEY_CONVERSION));
        mMinTime.setProgress((int) (Filters.getMinTime() / FilterConstants.TIME_CONVERSION));
        mMaxTime.setProgress((int) (Filters.getMaxTime() / FilterConstants.TIME_CONVERSION));

        mPaymentMethodSpinner.setSelection(Filters.getPaymentMethod().ordinal());

        mYourDistanceText.setText((Filters.getRangeFromYou() / FilterConstants.DISTANCE_CONVERSION) + " " + FilterConstants.DISTANCE_UNIT);
        mHomeDistanceText.setText((Filters.getRangeFromOffice() / FilterConstants.DISTANCE_CONVERSION) + " " + FilterConstants.DISTANCE_UNIT);
        mMinPaymentText.setText((Filters.getMinPayment() / FilterConstants.MONEY_CONVERSION) + " " + FilterConstants.MONEY_UNIT);
        mMaxPaymentText.setText((Filters.getMaxPayment() / FilterConstants.MONEY_CONVERSION) + " " + FilterConstants.MONEY_UNIT);
        mMinTimeText.setText((Filters.getMinTime() / FilterConstants.TIME_CONVERSION) + " " + FilterConstants.TIME_UNIT);
        mMaxTimeText.setText((Filters.getMaxTime() / FilterConstants.TIME_CONVERSION) + " " + FilterConstants.TIME_UNIT);
    }

    private void saveValues() {
        Filters.setRangeFromYou(mYourDistance.getProgress());
        Filters.setRangeFromOffice(mHomeDistance.getProgress());
        Filters.setMinPayment(mMinPayment.getProgress());
        Filters.setMaxPayment(mMaxPayment.getProgress());
        Filters.setMinTime(mMinTime.getProgress());
        Filters.setMaxTime(mMaxTime.getProgress());
        Filters.setPaymentMethod(PaymentMethod.fromString((String) mPaymentMethodSpinner.getSelectedItem()));
    }

    private void setListeners() {
        mYourDistance.setOnSeekBarChangeListener(new JobSeekBarListener(
                null, SeekType.INDEPENDENT, mYourDistanceText,
                FilterConstants.DISTANCE_CONVERSION, FilterConstants.DISTANCE_MIN_VALUE, FilterConstants.DISTANCE_UNIT
        ));
        mHomeDistance.setOnSeekBarChangeListener(new JobSeekBarListener(
                null, SeekType.INDEPENDENT, mHomeDistanceText,
                FilterConstants.DISTANCE_CONVERSION, FilterConstants.DISTANCE_MIN_VALUE, FilterConstants.DISTANCE_UNIT
        ));
        mMinPayment.setOnSeekBarChangeListener(new JobSeekBarListener(
                mMaxPayment, SeekType.MIN, mMinPaymentText,
                FilterConstants.MONEY_CONVERSION, FilterConstants.PAYMENT_MIN_VALUE, FilterConstants.MONEY_UNIT
        ));
        mMaxPayment.setOnSeekBarChangeListener(new JobSeekBarListener(
                mMinPayment, SeekType.MAX, mMaxPaymentText,
                FilterConstants.MONEY_CONVERSION, FilterConstants.PAYMENT_MIN_VALUE, FilterConstants.MONEY_UNIT
        ));
        mMinTime.setOnSeekBarChangeListener(new JobSeekBarListener(
                mMaxTime, SeekType.MIN, mMinTimeText,
                FilterConstants.TIME_CONVERSION, FilterConstants.TIME_MIN_VALUE, FilterConstants.TIME_UNIT
        ));
        mMaxTime.setOnSeekBarChangeListener(new JobSeekBarListener(
                mMinTime, SeekType.MAX, mMaxTimeText,
                FilterConstants.TIME_CONVERSION, FilterConstants.TIME_MIN_VALUE, FilterConstants.TIME_UNIT
        ));

        ArrayList<String> paymentMethods = new ArrayList<>();
        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            paymentMethods.add(paymentMethod.toString());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, paymentMethods);
        mPaymentMethodSpinner.setAdapter(spinnerAdapter);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class JobSeekBarListener implements SeekBar.OnSeekBarChangeListener {

        private SeekBar mRelatedSeekBar;
        private SeekType mSeekType;
        private TextView mRelatedTextView;
        private float mConversionMultiplier;
        private int mProgressMinValue;
        private String mSeekBarUnit;

        JobSeekBarListener() {
            this(null, SeekType.INDEPENDENT, null, 1.0f, 1, "");
        }

        JobSeekBarListener(SeekBar relatedSeekBar, SeekType seekType) {
            this(relatedSeekBar, seekType, null, 1.0f, 1, "");
        }

        JobSeekBarListener(SeekBar relatedSeekBar, SeekType seekType, TextView relatedTextView, String seekBarUnit) {
            this(relatedSeekBar, seekType, relatedTextView, 1.0f, 1, seekBarUnit);
        }

        JobSeekBarListener(SeekBar relatedSeekBar, SeekType seekType, TextView relatedTextView, float conversionMultiplier, int progressMinValue, String seekBarUnit) {
            mRelatedSeekBar = relatedSeekBar;
            mSeekType = seekType;
            mRelatedTextView = relatedTextView;
            mConversionMultiplier = conversionMultiplier;
            mProgressMinValue = progressMinValue;
            mSeekBarUnit = seekBarUnit;
        }

        float getmConversionMultiplier() {
            return mConversionMultiplier;
        }

        int getmProgressMinValue() {
            return mProgressMinValue;
        }

        String getmSeekBarUnit() {
            return mSeekBarUnit;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (mRelatedTextView != null) {
                String progressString = (progress * mConversionMultiplier) + " " + mSeekBarUnit;
                if (progress == seekBar.getMax()) {
                    progressString = String.valueOf((--progress * mConversionMultiplier)) + "+ " + mSeekBarUnit;
                }
                mRelatedTextView.setText(progressString);
            }

            if (progress < mProgressMinValue) seekBar.setProgress(mProgressMinValue);

            if (mRelatedSeekBar != null) {
                if (mSeekType == SeekType.MIN && (progress > mRelatedSeekBar.getProgress())) {
                    mRelatedSeekBar.setProgress(progress);
                } else if (mSeekType == SeekType.MAX && (progress < mRelatedSeekBar.getProgress())) {
                    mRelatedSeekBar.setProgress(progress);
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
