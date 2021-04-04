package com.example.sparktrials.exp.stats;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sparktrials.R;
import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.GeoLocation;
import com.example.sparktrials.models.Profile;
import com.example.sparktrials.models.Trial;
import com.example.sparktrials.models.TrialBinomial;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Date;

/**
 * The class containing the UI associated with showing experiment statistics
 */
public class StatsFragment extends Fragment {
    View view;
    Experiment experiment;
    public StatsFragment(Experiment experiment){
        this.experiment = experiment;
    }
    BarChart barChart;
    LineChart lineChart;
    TextView mean_tv, numTrials_tv, std_tv, median_tv, q1_tv, q3_tv;


    /**
     * Creates the stat fragment view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * The stat fragment view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stats, container, false);
        return view;
    }

    /**
     * Displays all necessary data in appropriate locations
     * @param view
     * @param savedInstanceState
     */
    public void onViewCreated(View view, Bundle savedInstanceState){

        // This is Testing data
       /* Experiment thisExp = new Experiment("testingAlex");
        ArrayList<Trial> trial= new ArrayList<>();
        TrialBinomial newBinomial = new TrialBinomial("1", new GeoLocation(), new Profile("1234"), true) ;
        TrialBinomial newBinomial2 = new TrialBinomial("2", new GeoLocation(), new Profile("1234"), false) ;
        TrialBinomial newBinomial3 = new TrialBinomial("3", new GeoLocation(), new Profile("1234"), false) ;
        TrialBinomial newBinomial4 = new TrialBinomial("4", new GeoLocation(), new Profile("1234"), true) ;
        trial.add(newBinomial);
        trial.add(newBinomial2);
        trial.add(newBinomial3);
        trial.add(newBinomial4);
        thisExp.addTrials(trial);
        // Testing data ends here

        /**
         * Initialize descriptive statistics UI and set them appropriately calling from experiment class methods
         */
        mean_tv = getView().findViewById(R.id.meanID);
        mean_tv.setText(experiment.getMean());

        numTrials_tv = getView().findViewById(R.id.trialsID);
        numTrials_tv.setText(experiment.getNumTrials());


        std_tv = getView().findViewById(R.id.stdID);
        std_tv.setText(experiment.getStd());


        median_tv= getView().findViewById(R.id.medianID);
        median_tv.setText((experiment.getMedian()));


        q1_tv = getView().findViewById(R.id.q1ID);
        q1_tv.setText(experiment.getQ1());

        q3_tv = getView().findViewById(R.id.q3ID);
        q3_tv.setText((experiment.getQ3()));


        /**
         * Initialize histogram and set X and Y data calling from experiment class
         */
        barChart = (BarChart) getView().findViewById(R.id.barchartID);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        int[] frequencies = experiment.frequencies();
        int xAxisLength = experiment.getXaxis().length;
        for (int i = 0; i<xAxisLength; i++){
            barEntries.add(new BarEntry((float) i, (float) (frequencies[i])));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "totals");
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
       // xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(experiment.removeDupes().length);
        Log.d("int",(""+ experiment.removeDupes()));
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(true);
        //xAxis.setLabelCount(2);


        xAxis.setValueFormatter(new ValueFormatter() {
            /**
             * This method casts the X axis values necessary for the used graphing Library
             * @param value
             * @return
             * Integer values for x-axis of histogram
             */
            @Override
            public String getFormattedValue(float value){
                String [] vals = experiment.getXaxis();
                return vals[(int) value];
            }
        });
        // Set data to histogram
        BarData theData = new BarData(barDataSet);
        barChart.setData(theData);


        // Initialize line plot
        /**
         * To Do: implement tracking over time this is dummy data for now
         */
        lineChart = (LineChart) getView().findViewById(R.id.linechartID);
        ArrayList <Entry> lineEntries =  new ArrayList<>();
        lineEntries.add(new Entry (1, 20));
        lineEntries.add(new Entry(2, 30));
        XAxis plotXAxis = lineChart.getXAxis();
        plotXAxis.setLabelCount(experiment.daysOfTrials().size(), true);
        Log.d("dates, ", ""+ experiment.daysOfTrials());
        Log.d("values", ""+experiment.getXaxis());
        plotXAxis.setValueFormatter(new ValueFormatter() {
            /**
             * This method casts the X axis values necessary for the used graphing Library
             * @param value
             * @return
             * Integer values for x-axis of histogram
             */
            @Override
            public String getFormattedValue(float value){
               ArrayList<String> vals= experiment.daysOfTrials();
                return vals.toString();
            }
        });
        plotXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "values");
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

    }
}