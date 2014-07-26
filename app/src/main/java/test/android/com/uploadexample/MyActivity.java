package test.android.com.uploadexample;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.f2prateek.progressbutton.ProgressButton;

import java.io.File;

import upload.AbstractUploadServiceReceiver;
import upload.UploadRequest;
import upload.UploadService;


public class MyActivity extends Activity {

    private static final String TAG = "upload";
    private ProgressButton progressButton;

    private final AbstractUploadServiceReceiver uploadReceiver =
            new AbstractUploadServiceReceiver() {

                @Override
                public void onProgress(String uploadId, int progress) {
                    Log.i(TAG, "The progress of the upload with ID "
                            + uploadId + " is: " + progress);
                    progressButton.setProgress(progress);
                }

                @Override
                public void onError(String uploadId, Exception exception) {
                    Log.e(TAG, "Error in upload with ID: " + uploadId + ". "
                            + exception.getLocalizedMessage(), exception);
                }

                @Override
                public void onCompleted(String uploadId,
                                        int serverResponseCode,
                                        String serverResponseMessage) {
                    Log.i(TAG, "Upload with ID " + uploadId
                            + " is completed: " + serverResponseCode
                            + ", " + serverResponseMessage);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        progressButton=(ProgressButton)findViewById(R.id.pin_progress_1);
    }

    public void pickFile(View view) {
        Intent intent = new Intent("org.openintents.action.PICK_FILE");
        startActivityForResult(intent, 1);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            String filePath = data.getData().getPath();

            Toast.makeText(MyActivity.this, filePath, Toast.LENGTH_LONG).show();

            Uri selectedUri = Uri.fromFile(new File(filePath));
            String fileExtension
                    = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
            String mimeType
                    = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);

            uploadFileToServer(filePath, mimeType);
        }
    }

    private void uploadFileToServer(String filePath, String mimeType) {
        final UploadRequest request = new UploadRequest(MyActivity.this,
                "123",
                "http://192.168.1.127/upload.php");

        request.addFileToUpload(filePath,
                "uploaded_file",
                "Abc.png",
                mimeType);

        //configure the notification
        request.setNotificationConfig(android.R.drawable.ic_menu_upload,
                "notification title",
                "upload in progress text",
                "upload completed successfully text",
                "upload error text",
                false);

        try {
            //Start upload service and display the notification
            UploadService.startUpload(request);

        } catch (Exception exc) {
            //You will end up here only if you pass an incomplete UploadRequest
            Log.e("AndroidUploadService", exc.getLocalizedMessage(), exc);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uploadReceiver.unregister(this);
    }

}
