package com.example.librarypicturemodule;

import java.io.File;

import com.example.picturemodule.PictureModule;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PictureModuleApp extends Activity {

	EditText etFilePath;
	EditText etDegrees;
	String filepath;
	ImageView ivImage;
	Bitmap imageToSave;
	Bitmap compressedImageView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		etDegrees = (EditText) findViewById(R.id.editText1);
		etFilePath = (EditText) findViewById(R.id.editText2);
		ivImage = (ImageView) findViewById(R.id.imageView1);

		filepath = Environment.getExternalStorageDirectory().getAbsolutePath()
				.toString();

		filepath = filepath + File.separator + "Pictures";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void rotateImage(View v){
		Bitmap compressedImage = PictureModule.compressImage(1, filepath + File.separator + etFilePath.getText().toString());
		Bitmap imageObj = PictureModule.rotateImage(Integer.parseInt(etDegrees.getText().toString()), compressedImage);
    	if(imageObj != null){
    		imageToSave = imageObj;
    		ivImage.setImageBitmap(imageObj);
    		compressedImage.recycle();
    		Toast toastObj = Toast.makeText(this, "Image rotated successfully", Toast.LENGTH_LONG);
    		toastObj.show();
    	}
    }
	
	public void saveImage(View v){
		if(true == PictureModule.saveImage(imageToSave, filepath + File.separator + etFilePath.getText().toString())){
			Toast toastObj = Toast.makeText(this, "Image saved", Toast.LENGTH_LONG);
    		toastObj.show();
    	}
		
	}
	
	public void compressImage(View v){
		Bitmap compressedImage = PictureModule.compressImage(Integer.parseInt(etDegrees.getText().toString()), filepath + File.separator + etFilePath.getText().toString());
		if(compressedImage != null){
			imageToSave = compressedImage;
			ivImage.setImageBitmap(compressedImage);
			Toast toastObj = Toast.makeText(this, "Image compressed successfully", Toast.LENGTH_LONG);
    		toastObj.show();
		}
	}
	
}

