package com.example.womensafety;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public  class Utils {

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void setOsmdroidMap(Context context, MapView mapView, double lat, double lon) {
        if (mapView != null) {
            mapView.setUseDataConnection(true);
            IMapController mapController = mapView.getController();
            mapController.setZoom(14.0);
            GeoPoint startPoint = new GeoPoint(lat, lon);
            mapController.setCenter(startPoint);
            mapView.setMinZoomLevel(7.0);
            mapView.setMultiTouchControls(false);
            mapView.setClickable(false);
            mapView.setOnClickListener(null);
            Marker startMarker = new Marker(mapView);
            startMarker.setPosition(startPoint);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            startMarker.setIcon(context.getResources().getDrawable(R.drawable.ic_location_red));
            startMarker.setTitle("Your Location");
            mapView.getOverlays().add(startMarker);
        }
    }
    public static boolean isWhatsAppInstalled(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            // Check if the WhatsApp package is installed
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            // WhatsApp is installed
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // WhatsApp is not installed
            return false;
        }
    }

    public static void setDataToLineChart(Context context, LineChart lineChart) {
        // Create some dummy data for the chart
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 10)); // Week 1
        entries.add(new Entry(2, 15)); // Week 2
        entries.add(new Entry(3, 20)); // Week 3
        entries.add(new Entry(4, 18)); // Week 4
        entries.add(new Entry(5, 12)); // Week 5
        entries.add(new Entry(6, 8)); // Week 6
        entries.add(new Entry(7, 6)); // Week 7

        // Create a LineDataSet from the data
        LineDataSet dataSet = new LineDataSet(entries, "Crime Reports");

        // Customize the appearance of the LineDataSet
        dataSet.setColor(Color.BLUE);
        dataSet.setLineWidth(0f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);

        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCubicIntensity(0.2f);
        dataSet.setDrawFilled(true);
        dataSet.setDrawCircles(false);
        dataSet.setLineWidth(1.8f);
        dataSet.setCircleRadius(4f);
        dataSet.setCircleColor(Color.WHITE);
        dataSet.setHighLightColor(Color.rgb(244, 117, 117));
        dataSet.setColor(Color.WHITE);
        dataSet.setFillColor(Color.WHITE);
        dataSet.setFillAlpha(100);
        dataSet.setDrawHorizontalHighlightIndicator(false);
//        dataSet.setFillFormatter(new IFillFormatter() {
//            @Override
//            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                return chart.getAxisLeft().getAxisMinimum();
//            }
//        });

        // Create a LineData object from the LineDataSet
        LineData lineData = new LineData(dataSet);

        // Set the LineData object to the LineChart
        lineChart.setData(lineData);

        // Customize the appearance of the LineChart
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getXAxis().setEnabled(false);

        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[]{"Day 1", "Day 2", "Day 3", "Day 4", "Day 5", "Day 6", "Day 7"}));
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setValueFormatter(new DefaultAxisValueFormatter(0));
        lineChart.animateX(1000);
        lineChart.setBackgroundColor(context.getResources().getColor(R.color.pink_dark));
        lineChart.invalidate();
    }

    public static LineDataSet createSet(int color, String label) {
        LineDataSet set = new LineDataSet(null, label);
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawFilled(true);
        set.setColor(color);
        set.setDrawCircles(false);
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setFormLineWidth(5f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(color);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }
}
