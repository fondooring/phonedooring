package com.fonekey.mainpage;
import com.fonekey.searchpage.CSearchActivity;
import com.fonekey.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CSearchFragment  extends Fragment {

    private Button m_btnNumberPerson;
    private Button m_btnDate;
    private Button m_btnTown;

    String m_dateBegin = "20-10-2020 12:00:00";
    String m_dateEnd = "20-10-2020 12:00:23";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        m_btnTown = view.findViewById(R.id.btnTown);
        m_btnDate = view.findViewById(R.id.btnDate);
        m_btnNumberPerson = view.findViewById(R.id.btnNumberPerson);

        m_btnTown.setOnClickListener(OpenBottomSheetDialogTown);
        m_btnDate.setOnClickListener(OpenBottomSheetDialogDate);
        m_btnNumberPerson.setOnClickListener(OpenBottomSheetDialogNumberPerson);

        Button btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    m_dateBegin = formatter.parse(m_dateBegin).getTime() + "";
                    m_dateEnd = formatter.parse(m_dateEnd).getTime() + "";
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getActivity(), CSearchActivity.class);
                intent.putExtra("town", "111");
                intent.putExtra("number_person", m_btnNumberPerson.getText().toString());
                intent.putExtra("data_begin", m_dateBegin);
                intent.putExtra("data_end", m_dateEnd);

                startActivity(intent);
            }
        });

        return view;
    }

    View.OnClickListener OpenBottomSheetDialogNumberPerson = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);

            int layout = R.layout.layout_numberperson;
            LinearLayout linearLayout = v.findViewById(R.id.llNumberPerson);
            View bottomSheetView = LayoutInflater.from(getContext()).inflate(layout, linearLayout);

            final EditText txtPerson = bottomSheetView.findViewById(R.id.txtNumberPersonSearch);

            bottomSheetView.findViewById(R.id.btnSaveNumberPerson).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    m_btnNumberPerson.setText(txtPerson.getText().toString());
                    bottomSheetDialog.dismiss();
                }
            });

            bottomSheetView.findViewById(R.id.btnPlus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int count = Integer.parseInt(txtPerson.getText().toString());
                    count++;
                    txtPerson.setText(String.valueOf(count));
                }
            });

            bottomSheetView.findViewById(R.id.btnMinus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int count = Integer.parseInt(txtPerson.getText().toString());
                    if(count > 1)
                        count--;
                    txtPerson.setText(String.valueOf(count));
                }
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        }
    };

    View.OnClickListener OpenBottomSheetDialogDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);

            int layout = R.layout.layout_date;
            LinearLayout linearLayout = v.findViewById(R.id.llDate);
            View bottomSheetView = LayoutInflater.from(getContext()).inflate(layout, linearLayout);

            final TextView txtDateViewBegin = bottomSheetView.findViewById(R.id.txtDateViewBegin);
            final TextView txtDateViewEnd = bottomSheetView.findViewById(R.id.txtDateViewEnd);

            final NumberPicker dd_begin = bottomSheetView.findViewById(R.id.numberPickerDayBegin);
            dd_begin.setMinValue(1);
            dd_begin.setMaxValue(31);
            dd_begin.setValue(1);

            final NumberPicker mm_begin = bottomSheetView.findViewById(R.id.numberPickerMonthBegin);
            mm_begin.setMinValue(1);
            mm_begin.setMaxValue(12);
            mm_begin.setValue(1);

            final NumberPicker yy_begin = bottomSheetView.findViewById(R.id.numberPickerYearBegin);
            yy_begin.setMinValue(1970);
            yy_begin.setMaxValue(2050);
            yy_begin.setValue(2020);

            final NumberPicker dd_end = bottomSheetView.findViewById(R.id.numberPickerDayEnd);
            dd_end.setMinValue(1);
            dd_end.setMaxValue(31);
            dd_end.setValue(31);

            final NumberPicker mm_end = bottomSheetView.findViewById(R.id.numberPickerMonthEnd);
            mm_end.setMinValue(1);
            mm_end.setMaxValue(12);
            mm_end.setValue(12);

            final NumberPicker yy_end = bottomSheetView.findViewById(R.id.numberPickerYearEnd);
            yy_end.setMinValue(1970);
            yy_end.setMaxValue(2050);
            yy_end.setValue(2020);

            dd_begin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                    ByteArrayOutputStream data_begin = new ByteArrayOutputStream();
                    ByteArrayOutputStream data_end = new ByteArrayOutputStream();

                    try {
                        data_begin.write(String.valueOf(newVal).getBytes());
                        data_begin.write('.');
                        data_begin.write(String.valueOf(mm_begin.getValue()).getBytes());
                        data_begin.write('.');
                        data_begin.write(String.valueOf(yy_begin.getValue()).getBytes());

                        data_end.write(String.valueOf(dd_end.getValue()).getBytes());
                        data_end.write('.');
                        data_end.write(String.valueOf(mm_end.getValue()).getBytes());
                        data_end.write('.');
                        data_end.write(String.valueOf(yy_end.getValue()).getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    txtDateViewBegin.setText(data_begin.toString());
                    txtDateViewEnd.setText(data_end.toString());
                }
            });

            mm_begin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                    ByteArrayOutputStream data_begin = new ByteArrayOutputStream();
                    ByteArrayOutputStream data_end = new ByteArrayOutputStream();

                    try {
                        data_begin.write(String.valueOf(dd_begin.getValue()).getBytes());
                        data_begin.write('.');
                        data_begin.write(String.valueOf(newVal).getBytes());
                        data_begin.write('.');
                        data_begin.write(String.valueOf(yy_begin.getValue()).getBytes());

                        data_end.write(String.valueOf(dd_end.getValue()).getBytes());
                        data_end.write('.');
                        data_end.write(String.valueOf(mm_end.getValue()).getBytes());
                        data_end.write('.');
                        data_end.write(String.valueOf(yy_end.getValue()).getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    txtDateViewBegin.setText(data_begin.toString());
                    txtDateViewEnd.setText(data_end.toString());
                }
            });

            yy_begin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                    ByteArrayOutputStream data_begin = new ByteArrayOutputStream();
                    ByteArrayOutputStream data_end = new ByteArrayOutputStream();

                    try {
                        data_begin.write(String.valueOf(dd_begin.getValue()).getBytes());
                        data_begin.write('.');
                        data_begin.write(String.valueOf(mm_begin.getValue()).getBytes());
                        data_begin.write('.');
                        data_begin.write(String.valueOf(newVal).getBytes());

                        data_end.write(String.valueOf(dd_end.getValue()).getBytes());
                        data_end.write('.');
                        data_end.write(String.valueOf(mm_end.getValue()).getBytes());
                        data_end.write('.');
                        data_end.write(String.valueOf(yy_end.getValue()).getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    txtDateViewBegin.setText(data_begin.toString());
                    txtDateViewEnd.setText(data_end.toString());
                }
            });

            dd_end.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                    ByteArrayOutputStream data_begin = new ByteArrayOutputStream();
                    ByteArrayOutputStream data_end = new ByteArrayOutputStream();

                    try {
                        data_begin.write(String.valueOf(dd_begin.getValue()).getBytes());
                        data_begin.write('.');
                        data_begin.write(String.valueOf(mm_begin.getValue()).getBytes());
                        data_begin.write('.');
                        data_begin.write(String.valueOf(yy_begin.getValue()).getBytes());

                        data_end.write(String.valueOf(newVal).getBytes());
                        data_end.write('.');
                        data_end.write(String.valueOf(mm_end.getValue()).getBytes());
                        data_end.write('.');
                        data_end.write(String.valueOf(yy_end.getValue()).getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    txtDateViewBegin.setText(data_begin.toString());
                    txtDateViewEnd.setText(data_end.toString());
                }
            });

            mm_end.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                    ByteArrayOutputStream data_begin = new ByteArrayOutputStream();
                    ByteArrayOutputStream data_end = new ByteArrayOutputStream();

                    try {
                        data_begin.write(String.valueOf(dd_begin.getValue()).getBytes());
                        data_begin.write('.');
                        data_begin.write(String.valueOf(mm_begin.getValue()).getBytes());
                        data_begin.write('.');
                        data_begin.write(String.valueOf(yy_begin.getValue()).getBytes());

                        data_end.write(String.valueOf(dd_end.getValue()).getBytes());
                        data_end.write('.');
                        data_end.write(String.valueOf(newVal).getBytes());
                        data_end.write('.');
                        data_end.write(String.valueOf(yy_end.getValue()).getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    txtDateViewBegin.setText(data_begin.toString());
                    txtDateViewEnd.setText(data_end.toString());
                }
            });

            yy_end.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                    ByteArrayOutputStream data_begin = new ByteArrayOutputStream();
                    ByteArrayOutputStream data_end = new ByteArrayOutputStream();

                    try {
                        data_begin.write(String.valueOf(dd_begin.getValue()).getBytes());
                        data_begin.write('.');
                        data_begin.write(String.valueOf(mm_begin.getValue()).getBytes());
                        data_begin.write('.');
                        data_begin.write(String.valueOf(yy_begin.getValue()).getBytes());

                        data_end.write(String.valueOf(dd_end.getValue()).getBytes());
                        data_end.write('.');
                        data_end.write(String.valueOf(mm_end.getValue()).getBytes());
                        data_end.write('.');
                        data_end.write(String.valueOf(newVal).getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    txtDateViewBegin.setText(data_begin.toString());
                    txtDateViewEnd.setText(data_end.toString());
                }
            });

            bottomSheetView.findViewById(R.id.btnSaveDate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    m_dateBegin = txtDateViewBegin.getText().toString();
                    m_dateEnd = txtDateViewEnd.getText().toString();
                    m_btnDate.setText(m_dateBegin + " - " + m_dateEnd);
                    bottomSheetDialog.dismiss();
                }
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        }
    };

    View.OnClickListener OpenBottomSheetDialogTown = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);

            int layout = R.layout.layout_town;
            LinearLayout linearLayout = v.findViewById(R.id.llTown);
            View bottomSheetView = LayoutInflater.from(getContext()).inflate(layout, linearLayout);

            final EditText txtTown = bottomSheetView.findViewById(R.id.txtTownSearch);

            bottomSheetView.findViewById(R.id.btnSaveTown).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    m_btnTown.setText(txtTown.getText().toString());
                    bottomSheetDialog.dismiss();
                }
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        }
    };
}