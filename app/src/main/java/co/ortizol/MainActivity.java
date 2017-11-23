package co.ortizol;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

/**
 * Representa la actividad principal de la aplicación. Se dibuja el gráfico como
 * los controles de edición de datos por parte del usuario.
 */
public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    /**
     * Captura el dato de número de día.
     */
    private TextInputLayout tilNumeroDia;
    /**
     * Captura el dato de gasto del día.
     */
    private TextInputLayout tilGasto;

    /**
     * Representa el gráfico de barras.
     */
    protected BarChart mChart;
    private ArrayList<BarEntry> yVals1;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.id_publicidad));

        tilNumeroDia = (TextInputLayout) findViewById(R.id.tilNumeroDia);
        tilGasto = (TextInputLayout) findViewById(R.id.tilGasto);

        mChart = (BarChart) findViewById(R.id.brcGastosDiarios);
        mChart.setOnChartValueSelectedListener(this);
        Description description = new Description();
        description.setText("Gastos");
        mChart.setDescription(description);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.setMaxVisibleValueCount(60);


        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(Typeface.SERIF);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(6);
        xAxis.setValueFormatter(new Formatter(mChart));
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        yVals1 = new ArrayList<BarEntry>();

        yVals1.add(new BarEntry(0, 0));
        yVals1.add(new BarEntry(1, 0));
        yVals1.add(new BarEntry(2, 0));
        yVals1.add(new BarEntry(3, 0));
        yVals1.add(new BarEntry(4, 0));
        yVals1.add(new BarEntry(5, 0));
        yVals1.add(new BarEntry(6, 0));

        actualizarGrafico();
    }

    public void onClickAgregarGasto(View view) {
        EditText txtNumeroDia = tilNumeroDia.getEditText();
        EditText txtGasto = tilGasto.getEditText();

        if (!"".equals(txtNumeroDia.getText().toString())
                && !"".equals(txtGasto.getText().toString())) {
            int numeroDia = Integer.parseInt(txtNumeroDia.getText().toString());

            if (numeroDia >= 1 && numeroDia <= 6) {
                yVals1.get(numeroDia).setY(Float.parseFloat(txtGasto.getText().toString()));

                actualizarGrafico();

                View v = this.getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                txtNumeroDia.setText(null);
                txtGasto.setText(null);
            } else {
                Toast.makeText(this, getString(R.string.restriccion_numero_dia), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.campos_obligatorios), Toast.LENGTH_SHORT).show();
        }
    }


    private void actualizarGrafico() {

        mChart.clear();

        float start = 0f;

        mChart.getXAxis().setAxisMinValue(start);
        mChart.getXAxis().setAxisMaxValue(start + 6 + 1);

        mChart.getAxisRight().setAxisMinimum(0);
        mChart.getAxisLeft().setAxisMinimum(0);
        mChart.getAxisLeft().setSpaceMax(10);
        mChart.getAxisRight().setSpaceMax(10);
        mChart.getAxisLeft().setSpaceMin(10);
        mChart.getAxisRight().setSpaceMin(10);

        BarDataSet set1;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.invalidate();
        } else {

            set1 = new BarDataSet(yVals1, "Días");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(Typeface.SERIF);
            data.setBarWidth(0.9f);
            mChart.setData(data);
            mChart.getData().notifyDataChanged();
            mChart.invalidate();
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.mitMostrarAnuncio) {

            verPublicidad();
        }


        return super.onOptionsItemSelected(item);
    }

    private void verPublicidad() {
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Toast.makeText(this, getString(R.string.publicidad_cargando), Toast.LENGTH_SHORT).show();
            iniciar();
        }
    }

    private void iniciar() {
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        iniciar();
    }
}
