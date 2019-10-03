package com.syarif48.tgrid;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormat;
    private EditText txtTanggalMasuk;
    private EditText txtTanggalKeluar;
    private EditText txtTahunLulus;
    private Button btnHitungTGRID;
    private Spinner spinnerPendidikan;
    private TextView tvTGR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        txtTanggalMasuk = (EditText) findViewById(R.id.edtTanggalMasuk);
        txtTanggalKeluar = (EditText) findViewById(R.id.edtTanggalKeluar);
        txtTahunLulus = (EditText) findViewById(R.id.edtTahunLulus);
        btnHitungTGRID = (Button) findViewById(R.id.btnHitungTGR);
        spinnerPendidikan = (Spinner)findViewById(R.id.spinnerPendidikan);
        tvTGR = (TextView)findViewById(R.id.tvTGR);

        txtTanggalMasuk.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDateDialog(txtTanggalMasuk);
                }
            }
        });

        txtTanggalMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(txtTanggalMasuk);
            }
        });

        txtTanggalKeluar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showDateDialog(txtTanggalKeluar);
                }
            }
        });

        txtTanggalKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(txtTanggalKeluar);
            }
        });

        btnHitungTGRID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitungTgrId();
            }
        });
    }

    private void showDateDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year,month,dayOfMonth);
                //txtTanggalMasuk.setText(dateFormat.format(newDate.getTime()));
                String str = newDate.toString();
                editText.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void hitungTgrId () {
        double gr=0;
        Date tglMasuk, tglKeluar;
        Calendar tanggalMasuk, tanggalKeluar;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        int tahunLulus = Integer.parseInt(txtTahunLulus.getText().toString());
        String strTanggalMasuk = txtTanggalMasuk.getText().toString();
        String strTanggalKeluar = txtTanggalKeluar.getText().toString();

        int[] bulanIkatanDinas = {48,120};
        double[] grDI = {24000000.0,30000000.0};
        double[] grDIII = {60000000.0,75000000.0};

        try {
            tglMasuk = dateFormatter.parse(strTanggalMasuk);
            tglKeluar = dateFormatter.parse(strTanggalKeluar);
            tanggalMasuk = Calendar.getInstance();
            tanggalKeluar = Calendar.getInstance();
            tanggalMasuk.setTime(tglMasuk);
            tanggalKeluar.setTime(tglKeluar);
            int m=tanggalMasuk.get(Calendar.MONTH);
            long lama = tanggalKeluar.getTimeInMillis() - tanggalMasuk.getTimeInMillis();

            int idx=0;
            float jmlBulanKerja = (float)lama / (30f * 24f * 60f * 60f * 1000f);
            int jbk = (int) jmlBulanKerja;
            int sisaBulan=0;

            if (tahunLulus <= 2004) {
                idx = 0;
            } else {
                idx = 1;
            }

            if (spinnerPendidikan.getSelectedItem().toString().equals("DI") && jmlBulanKerja < 48) {
                sisaBulan = 48 - jbk;
                gr = (double) sisaBulan * (grDI[idx]/48.0);
            } else if (spinnerPendidikan.getSelectedItem().toString().equals("DIII") && jmlBulanKerja < 120) {
                sisaBulan = 120 - jbk;
                gr = (double) sisaBulan * (grDIII[idx]/120.0);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvTGR.setText(String.format("Rp%,.0f", gr));
        //tvTGR.setText(String.valueOf(gr));
    }
}
