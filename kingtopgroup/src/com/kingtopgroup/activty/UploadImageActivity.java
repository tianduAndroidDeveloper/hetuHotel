package com.kingtopgroup.activty;

import java.io.ByteArrayOutputStream;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kingtogroup.utils.Utils;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UploadImageActivity extends MainActionBarActivity implements OnClickListener {
	private static final String TAG = "UploadImageActivity";
	TextView tv_scan;
	TextView tv_upload;
	View progress;
	ImageView iv;
	int oid;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uploadimage);
		titleButton.setText("上传图片");
		init();
	}

	void init() {
		oid = getIntent().getIntExtra("oid", 0);

		tv_scan = (TextView) findViewById(R.id.tv_scan);
		tv_upload = (TextView) findViewById(R.id.tv_upload);
		progress = findViewById(R.id.progress);
		iv = (ImageView) findViewById(R.id.iv);

		tv_scan.setOnClickListener(this);
		tv_upload.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_scan:
			scanAllImage();
			break;
		case R.id.tv_upload:
			uploadImage(imageFile);
			break;
		default:
			break;
		}
	}

	void scanAllImage() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		this.startActivityForResult(intent, 1);
	}

	File imageFile;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			if (uri != null) {
				ContentResolver cr = this.getContentResolver();
				try {
					File file = null;
					if (uri.toString().startsWith("content"))
						file = getFileByUri(uri);
					else {
						String path = uri.toString().substring(7);
						file = new File(path);
					}
					if (Utils.isPicFile(file)) {
						Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
						/* 将Bitmap设定到ImageView */
						iv.setImageBitmap(bitmap);
						imageFile = file;

					} else {
						toastMsg("请选择正确的图片文件", 1);
					}
				} catch (FileNotFoundException e) {
					Log.e("Exception", e.getMessage(), e);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private File getFileByUri(Uri uri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(uri, proj, null, null, null);
			if (cursor != null) {
				int actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				String img_path = cursor.getString(actual_image_column_index);
				File file = new File(img_path);
				return file;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	void uploadImage(final File file) {
		if (file == null) {
			Toast.makeText(this, "您还没有选择图片哦", Toast.LENGTH_SHORT).show();
			return;
		}
		progress.setVisibility(View.VISIBLE);
		String path = getString(R.string.url_upload) + "?massagerid=" + UserBean.getUSerBean().getMassagerId() + "&oid=" + oid + "&mobile=" + UserBean.getUSerBean().getAccount();

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		try {
			params.put("file", file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		client.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				progress.setVisibility(View.GONE);
				try {
					JSONObject object = new JSONObject(new String(arg2));
					if (object != null) {
						int returnValue = object.optInt("ReturnValue");
						if (returnValue == 1) {
							// 上传成功
							toastMsg("上传成功", 1);
							finish();
						} else {
							// 上传失败
							toastMsg("上传失败，请重试", 1);
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.i(TAG, "onFailure");
				Log.i(TAG, new String(arg2));
				progress.setVisibility(View.GONE);
			}
		});
	}

	void toastMsg(String msg, int time) {
		Toast.makeText(this, msg, time).show();
	}

	public static byte[] getFileToByte(File file) {
		byte[] by = new byte[(int) file.length()];
		StringBuilder sb = new StringBuilder();
		try {
			InputStream is = new FileInputStream(file);
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			byte[] bb = new byte[2048];
			int ch;
			ch = is.read(bb);
			while (ch != -1) {
				bytestream.write(bb, 0, ch);
				ch = is.read(bb);
			}
			by = bytestream.toByteArray();
			is.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		for (Byte b : by) {
			String i = Integer.toBinaryString(b);
			sb.append(i);
		}
		Log.i(TAG, sb.toString());
		return by;
	}

	@Override
	public void backButtonClick(View v) {
		finish();
	}

	@Override
	public void titleButtonClick(View v) {

	}

	@Override
	public void rightButtonClick(View v) {

	}

	@Override
	public Boolean showHeadView() {
		return true;
	}

}
