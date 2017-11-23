package co.ortizol;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Representa el formmater de las barras del gráfico.
 */
public class Formatter implements IAxisValueFormatter {

    private BarLineChartBase<?> chart;

    public Formatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (value == 1) {
            return "Lunes";
        } else if (value == 2) {
            return "Martes";
        } else if (value == 3) {
            return "Miércoles";
        } else if (value == 4) {
            return "Jueves";
        } else if (value == 5) {
            return "Viernes";
        } else if (value == 6) {
            return "Sábado";
        } else {
            return "";
        }
    }
}
