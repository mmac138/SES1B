package group6.seshealthpatient.PatientFragments;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public static class RecordVideoFragment {


    public RecordVideoFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        return view;
    }


        private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
        private static final int MEDIA_TYPE_VIDEO = 2;
        private static final String IMAGE_DIRECTORY_NAME = "Health Video";

        private Uri fileUri;
        private VideoView videoPreview;
        private Button btnRecordVideo;


        @Override public void onCreateView (LayoutInflater inflater, ViewGroup container, Bundle
        savedInstanceState){
            super.Oncreate(savedInstanceState) setContentView(R.layout.patient_fragment_record_video);
            videoPreview = (VideoView) findViewById(R.id.videoPreview);
            btnRecordVideo = (Button) findViewByID(R.id.btnRecordVideo);

            btnRecordVideo.setOnClickListener((v) {recordVideo();
            });

            if (!isDeviceSupportCamera()) {
                Toast.makeTeext(getApplicationCoontext(), "Sorry! Your device doesnot support camera", Toast.LENGTH_LONG).show();
                finish();
            }
            private boolean isDeviceSupportCamera () {
                if (getApplicationContext().gettpackageManager().hasSystemFeature(packageManager.FEATURE_CAMERA)) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override protected void onSaveInstanceState (Bundle outState){
                super.onSaveInstanceState(outstate);
                outState.putParcelable("file_uri", fileUri);

            }
            protected void onRestoreInstanceState (Bundle savedInstanceSate){
                super.onRestoreInstanceState(savedInstanceState);
                fileUri = savedInstanceState.gerParcelable("file_uri");
            }


            private void recordVideo () {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);


            }


            @Override
            protected void onActivityResult( int requestCode, int resultCode, Intent data)
            {
                if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
                    if (resultCode == RESULT_OK) {
                        previewVideo();

                    } else if (resultcode == RESULT_CANCELLED) {
                        Toast.makeText(getApplicationcontext(), "User cancelled video Recording", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext()
                                "Sorry! Failed to record video", Toast.LENTH_SHORT).show();

                    }

                }
            }

            private void previewVideo () {
                try {
                    videoPreview.setVisiblity(View.VISIBLE);
                    videoPreview.setVideoPath(fileUri.getpath());

                    videoPreview.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public Uri getOutputMediaFileUri ( int type){
                return Uri.fromFile(getOutputMediaFile(type))
            } private static File getOutputMediaFile ( int type){

                File mediaStorageDir = new File(Environment.getExtrenalStorageStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        IMAGE_DIRECTORY_NAME);

                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed to Create" + IMAGE_DIRECTORY_NAME + "directory");
                        return null;
                    }

                }

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                File mediaFile;
                if (type == MEDIA_TYPE_VIDEO) {
                    mediaFile = new File(mediaStorageDir.getPath() + File.separatorr + "VID_" + timeStamp + ".mp4");
                    else{
                        return null;
                    }
                    return mediaFile;
                }


            }

            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.patient_fragment_record_video, container, false);
        }
    }

