package com.kingtopgroup.wxapi;

import net.sourceforge.simcpux.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.kingtopgroup.R;
import com.kingtopgroup.activty.indexActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "WXPayEntryActivity";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);
		Log.i(TAG, "onCreate");

		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.registerApp(Constants.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	AlertDialog dialog;

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			View v = View.inflate(this, R.layout.dialog_pay, null);
			builder.setView(v);
			TextView tv_description = (TextView) v.findViewById(R.id.tv_description);
			final int errCode = resp.errCode;
			switch (errCode) {
			case 0:
				tv_description.setText("支付成功!");
				break;

			default:
				tv_description.setText("支付失败，请重试!");
				break;
			}
			dialog = builder.show();
			Button btn_ok = (Button) v.findViewById(R.id.btn_ok);
			btn_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (errCode == 0) {
						Intent intent = new Intent(WXPayEntryActivity.this, indexActivity.class);
						WXPayEntryActivity.this.startActivity(intent);
					} else {
						finish();
					}
				}
			});

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}
}