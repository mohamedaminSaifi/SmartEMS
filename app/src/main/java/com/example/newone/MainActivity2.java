package com.example.newone;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity2 extends AppCompatActivity {

    private static String Posturl ="http://192.168.1.63:8080/Mobile/checkCode";
   //private static String Posturl ="http://192.168.1.106:8080/recepter";
    OkHttpClient client;
    private String res;
    private MaterialButton cameraBtn;
    private MaterialButton galeryBtn;
    private ImageView imageIv;

    private MaterialButton scanBtn;
    private MaterialButton listBtn;
    private TextView resultTv;

    private static final int CAMERA_REQUEST_CODE =100;
    private static final int STORAGE_REQUEST_CODE =101;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    //imf Uri
    private Uri imageUri = null;
    private static final String TAG = "MAIN_TAG";

    private BarcodeScannerOptions barcodeScannerOptions;
    private BarcodeScanner barcodeScanner;
   // private JSONObject jsonObject = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toast.makeText(MainActivity2.this,"Welcom", Toast.LENGTH_SHORT).show();
        cameraBtn = findViewById(R.id.cameraBtn);
        galeryBtn = findViewById(R.id.galeryBtn);
        imageIv = findViewById(R.id.imageIv);
        scanBtn = findViewById(R.id.scanBtn);
        resultTv = findViewById(R.id.resultTv);
        listBtn = findViewById(R.id.listBtn);
        cameraPermissions = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}; //img from cam
        storagePermissions = new String[]{ android.Manifest.permission.WRITE_EXTERNAL_STORAGE}; // img from storage
        // setting the type of settings and types of codes to scan for me its all
        barcodeScannerOptions = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).build();
        barcodeScanner = BarcodeScanning.getClient(barcodeScannerOptions);
        // client that sends the post request
        client = new OkHttpClient();
        // checks for cam permission and takes pic
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()){
                    pickImageCmera();
                }else {
                    requestCameraPermission();
                }
            }
        });
        // checks for cam permission and uploads pic
        galeryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if permission is granted
                if (checkStoragePermission()){
                    pickImageGallery();
                }
                //requests the permission
                else {
                    requestStoragePermission();
                }
            }
        });
        // scans the pic
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null){
                    Toast.makeText(MainActivity2.this,"pick an image first",Toast.LENGTH_SHORT).show();
                }
                else {
                    deleteResultFromImage();
                }
            }
        });
        //switch to check the results
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkResultsList();
            }
        });

}
        //changes the current activity to the next activity
        private void checkResultsList() {
        Intent intent = new Intent(this , MainActivity3.class);
        Toast.makeText(MainActivity2.this,"Checking list",Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
    private void post(String value){
        //creating the request and its body and sends the data with the name id and its value
        RequestBody requestBody = new FormBody.Builder()
                .add("id",value).build();
        //sends the post request using the PostUrl
        Request request = new Request.Builder().url(Posturl).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            res =response.body().string();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        //Log.d(TAG, "run() returned: post the rawValues :  " + value);
                        Toast.makeText(MainActivity2.this,"scanned and sent to the db",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, res);
                    }
                });
            }
        });
    }
    private void deleteResultFromImage() {
        try {
            // gets img from image uri
            InputImage inputImage = InputImage.fromFilePath(this , imageUri);
            //starts scanning qr code
            Task<List<Barcode>> barcodeResult = barcodeScanner.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            // success and we can get the data
                            exyractBarcodeInfo(barcodes);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity2.this,"failed due to "+e.getMessage(),Toast.LENGTH_SHORT).show();
                            // fail
                        }
                    });
        }
        catch (Exception e){
            // failed cause of init or inapropriat img type
            Toast.makeText(MainActivity2.this,"failed due to "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private void exyractBarcodeInfo(List<Barcode> barcodes) {
        //get info from barcode
        for (Barcode barcode : barcodes){
            //raw info from the barcode
            String rawValues = barcode.getRawValue();
            Log.d(TAG, "exyractBarcodeInfo: rawValues :"+rawValues);
            int valueType = barcode.getValueType();
            switch (valueType) {
                default:{
                    Log.d(TAG, "exyractBarcodeInfo: default : ");
                    resultTv.setText("raw values : "+rawValues);
                }
                post(rawValues);
            }
        }
    }
    private void pickImageGallery(){
        //intent to pic an img from gallery will show where from
        Intent intent = new Intent(Intent.ACTION_PICK);
        //set the type of our file wich is img
        intent.setType("image/*");
        GalleryActivityResultLauncher.launch(intent);
    }
    private final ActivityResultLauncher<Intent> GalleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //here we will receive the image, if picked from gallery
                    if(result.getResultCode() == Activity.RESULT_OK){
                        //image picked, get the uri of the image picked
                        Intent data = result.getData();
                        imageUri = data.getData();
                        Log.d(TAG,"onActivityResult: imageUri: "+imageUri);
                        //set to imageview
                        imageIv.setImageURI(imageUri);
                    }else{
                        //canncelled
                        Toast.makeText(MainActivity2.this,"Cancelled",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private void pickImageCmera() {
        ContentValues contentValues= new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"Sample Tite");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"Sample Image Descreption");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT , imageUri);
        CameraActivityResultLauncher.launch(intent);
    }
    private final ActivityResultLauncher<Intent> CameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // here we recive the img if tooken from cam
                    if (result.getResultCode() == Activity.RESULT_OK){
                        //img tooken from cam
                        Intent data = result.getData();
                        //we already have the image in imageUri using function pickImageComera()
                        Log.d(TAG,"onActivityResult: imageUri: "+imageUri);
                        //set to imageview
                        Toast.makeText(MainActivity2.this, "imageUri"+imageUri, Toast.LENGTH_SHORT).show();
                        imageIv.setImageURI(imageUri);
                    }else{
                        //cancelled
                        Toast.makeText(MainActivity2.this,"Cancelled...",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private boolean checkStoragePermission() {
        /*check if storage permission is allowed or not
        return true if allowed, false if not allowed*/
        boolean result = ContextCompat.checkSelfPermission(this , android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        //returns true if WRITE_EXTERNAL_STORAGE is granted else returns false
        return result;
    }
    private void requestStoragePermission(){
        //request storage permission (for gallery image pick)
        ActivityCompat.requestPermissions(this ,storagePermissions,STORAGE_REQUEST_CODE);
    }
    private boolean checkCameraPermission(){

        boolean resultCamer = ContextCompat.checkSelfPermission(this , android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        boolean resultStorage = ContextCompat.checkSelfPermission(this , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        return resultCamer && resultStorage;
    }
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this , cameraPermissions ,CAMERA_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                //Check if some action from permission dialog performed or not Allow/Deny
                if (grantResults.length > 0){
                    //checks if the permissions got a boolean
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    //checks both permissions are granted or not
                    if (cameraAccepted && storageAccepted){
                        pickImageCmera();
                    }
                    else {
                        Toast.makeText(this , "camera and storage permission are required",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                //Check if same action from permission dialog performed or not Allow/Deny
                if (grantResults.length > 0){
                    //checks if the permissions got a boolean

                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    //checks both permissions are granted or not
                    if (storageAccepted){
                        pickImageGallery();
                    }
                    else {
                        Toast.makeText(this , "storage permission is required",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            break;
        }
    }
}
