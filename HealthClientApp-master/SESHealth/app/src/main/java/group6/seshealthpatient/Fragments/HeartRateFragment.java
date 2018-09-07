package group6.seshealthpatient.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import group6.seshealthpatient.Fragments.ImageProcessing;
import group6.seshealthpatient.R;

import group6.seshealthpatient.R;

import android.content.res.Configuration;
import android.hardware.Camera.PreviewCallback;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
/**
 * A simple {@link Fragment} subclass.
 */
public class HeartRateFragment extends Fragment {

    public HeartRateFragment() {
        // Required empty public constructor
    }
    private XYSeries series;
    private XYMultipleSeriesDataset mDataset;
    private GraphicalView chart;
    private XYMultipleSeriesRenderer renderer;
    private LinearLayout linearLayout;
    private static TextView text = null;
    private static PowerManager.WakeLock wakeLock = null;

    private Timer timer = new Timer();
    private TimerTask task;
    private static int gx;
    private static int j;

    private static double flag = 1;
    private Handler handler;
    private String title = "pulse";
    private Context context;
    private int addX = -1;
    private boolean cameraPermissionGranted = false;
    double addY;
    int[] xv = new int[300];
    int[] yv = new int[300];
    int[] hua = new int[] { 9, 10, 11, 12, 13, 14, 13, 12, 11, 10, 9, 8, 7, 6,
            7, 8, 9, 10, 11, 10, 10 };

    private static final AtomicBoolean processing = new AtomicBoolean(false);
    private static SurfaceView preview = null;
    private static SurfaceHolder previewHolder = null;
    private static Camera camera = null;


    private static Button btnHR = null;


    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];

    public static enum TYPE {
        BLACK, RED
    };

    private static TYPE currentType = TYPE.BLACK;

    public static TYPE getCurrent() {
        return currentType;
    }

    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;
    private static int c=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View x = inflater.inflate(R.layout.fragment_heart_rate, container, false);

        context = x.getContext();
//use this method to allow camera work
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            cameraPermissionGranted = true;
            Log.i("TEST","Granted");
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA}, 1);//1 can be another integer
        }

        //the table
        // add the table into this layout
        linearLayout = x.findViewById(R.id.linearLayout1);

        preview = x.findViewById(R.id.preview);
        text = x.findViewById(R.id.text);

        return x;

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(cameraPermissionGranted){
            createChart();

            camera = Camera.open();
            startTime = System.currentTimeMillis();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        if(cameraPermissionGranted){
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }


    }

    //Turn off the Timer when closing the program
    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    };

    protected XYMultipleSeriesRenderer buildRenderer(int color,
                                                     PointStyle style, boolean fill) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

        // format
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(Color.RED);
        r.setLineWidth(1);
        renderer.addSeriesRenderer(r);
        return renderer;
    }

    protected void setChartSettings(XYMultipleSeriesRenderer renderer,
                                    String xTitle, String yTitle, double xMin, double xMax,
                                    double yMin, double yMax, int axesColor, int labelsColor) {
        // API documentation for rendering graphs
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
        renderer.setShowGrid(true);
        renderer.setGridColor(Color.BLACK);
        renderer.setXLabels(20);
        renderer.setYLabels(10);
        renderer.setXTitle("Time");
        renderer.setYTitle("mmHg");
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        renderer.setPointSize((float) 3);
        renderer.setShowLegend(false);
    }

    private void updateChart() {

        // add next note
        if (flag == 1)
            addY = 10;
        else {
            flag = 1;
            if (gx < 200) {
                if (hua[20] > 1) {
                    hua[20] = 0;
                }
                hua[20]++;
                return;
            } else
                hua[20] = 10;
            j = 0;

        }
        if (j < 20) {
            addY = hua[j];
            j++;
        }

        // remove the point set
        mDataset.removeSeries(series);

        // Determine how many points are in the current set, because the screen can only hold a total of 100, so when the number of points exceeds 100, the length is always 100
        int length = series.getItemCount();
        int bz = 0;
        // addX = length;
        if (length > 300) {
            length = 300;
            bz = 1;
        }
        addX = length;
        // The values of x and y in the old point set were taken out and put into backup, and the values of x were added 1, resulting in a shift to the right of the curve
        for (int i = 0; i < length; i++) {
            xv[i] = (int) series.getX(i) - bz;
            yv[i] = (int) series.getY(i);
        }

        // clear
        series.clear();
        mDataset.addSeries(series);

        series.add(addX, addY);
        for (int k = 0; k < length; k++) {
            series.add(xv[k], yv[k]);
        }

        chart.invalidate();
    }


    private static PreviewCallback previewCallback = new PreviewCallback() {

        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null)
                throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null)
                throw new NullPointerException();
            if (!processing.compareAndSet(false, true))
                return;
            int width = size.width;
            int height = size.height;
            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(),
                    height, width);
            gx = imgAvg;

            if (imgAvg == 0 || imgAvg == 255) {
                processing.set(false);
                return;
            }

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt)
                    : 0;
            TYPE newType = currentType;
            if (imgAvg < rollingAverage) {
                newType = TYPE.RED;
                if (newType != currentType) {
                    beats++;
                    flag = 0;
                }
            } else if (imgAvg > rollingAverage) {
                newType = TYPE.BLACK;
            }

            if (averageIndex == averageArraySize)
                averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            if (newType != currentType) {
                currentType = newType;

            }
            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            if (totalTimeInSecs >= 2) {
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180 || imgAvg < 200) {

                    startTime = System.currentTimeMillis();

                    beats = 0;
                    processing.set(false);
                    return;
                }

                if (beatsIndex == beatsArraySize)
                    beatsIndex = 0;
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;
                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                c = beatsAvg;
                text.setText("Heart rate: " + String.valueOf(beatsAvg) + " BPM");

                startTime = System.currentTimeMillis();
                beats = 0;
            }
            processing.set(false);
        }
    };

    private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);

            }
            camera.setParameters(parameters);
            camera.startPreview();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };

    private static Camera.Size getSmallestPreviewSize(int width, int height,
                                                      Camera.Parameters parameters) {
        Camera.Size result = null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;
                    if (newArea < resultArea)
                        result = size;
                }
            }
        }
        return result;
    }

    // Ask for location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d("CAMERA: ", "REQUEST PERMISSION RESULT CALLED");
        cameraPermissionGranted = false;

        // User chose ALLOW
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("CAMERA: ", "PERMISSION ALLOWED");

            cameraPermissionGranted = true;
        }
        // User chose DENY
        else{
            Log.d("CAMERA: ", "PERMISSION DENIED");
            return;
        }
    }

    private void createChart(){
        // This class is used to place all points on a curve, a collection of points, and plot the curves based on those points
        series = new XYSeries(title);

        // Create an instance of a data set that will be used to create a chart
        mDataset = new XYMultipleSeriesDataset();

        //Add the point set to the data set
        mDataset.addSeries(series);

        // format
        int color = Color.BLACK;
        PointStyle style = PointStyle.CIRCLE;
        renderer = buildRenderer(color, style, true);

        setChartSettings(renderer, "X", "Y", 0, 300, 4, 16, Color.WHITE,
                Color.WHITE);

        //generate the chart
        chart = ChartFactory.getLineChartView(context, mDataset, renderer);

        // add
        linearLayout.addView(chart, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));


        // The Handler instance here will work with the Timer instance below to update the chart periodically
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                updateChart();
                super.handleMessage(msg);
            }
        };

        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
// curve
        timer.schedule(task, 2, 40);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
}
