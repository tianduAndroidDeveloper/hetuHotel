package com.kingtopgroup.activty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kingtopgroup.R;
import android.app.ActionBar.LayoutParams;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;

@SuppressWarnings("deprecation")
public class indexActivity extends TabActivity implements
		OnCheckedChangeListener, OnClickListener {
	private RadioButton personal, cooperate, order, more;
	private RadioGroup indexGroup;
	private TabHost tabhost;
	private TabWidget tabs;
	private PopupWindow more_window;
	private PopupWindow cooper_window;
	private ListView more_listview;
	private ListView cooper_listview;
	private View more_view;
	private View cooper_view;

	// �˵�������
	private List<Map<String, String>> listLeft;
	private List<Map<String, String>> listRight;

	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);

		listLeft = new ArrayList<Map<String, String>>();
		// for (int i = 1; i <5; i++) {
		HashMap<String, String> mapTemp = new HashMap<String, String>();
		mapTemp.put("item", "��ҵ��������");
		listLeft.add(mapTemp);

		HashMap<String, String> mapTemp1 = new HashMap<String, String>();
		mapTemp1.put("item", "���������");
		listLeft.add(mapTemp1);
		// }

		personal = (RadioButton) findViewById(R.id.personal);
		cooperate = (RadioButton) findViewById(R.id.cooperate);
		more = (RadioButton) findViewById(R.id.more);

		tabs = (TabWidget) findViewById(android.R.id.tabs);
		tabhost = (TabHost) findViewById(android.R.id.tabhost);
		tabhost.setup();
		tabhost.setFocusable(true);

		// ԤԼ
		TabHost.TabSpec tabSpec3 = tabhost.newTabSpec("ԤԼ");
		tabSpec3.setIndicator("ԤԼ");
		tabSpec3.setContent(new Intent(indexActivity.this, orderActivity.class));
		tabhost.addTab(tabSpec3);
		tabhost.setCurrentTab(0);

		// ������ת
		TabHost.TabSpec tabSpec4 = tabhost.newTabSpec("����");
		tabSpec4.setIndicator("����");
		tabSpec4.setContent(new Intent(indexActivity.this,
				PersonalActivity.class));
		tabhost.addTab(tabSpec4);

		// ����
		TabHost.TabSpec tabSpec1 = tabhost.newTabSpec("����");
		tabSpec1.setIndicator("����");
		tabSpec1.setContent(new Intent(indexActivity.this, CoworkActivity.class));
		tabhost.addTab(tabSpec1);

		// ����
		TabHost.TabSpec tabSpec2 = tabhost.newTabSpec("����");
		tabSpec2.setIndicator("����");
		tabSpec2.setContent(new Intent(indexActivity.this, MoreActivity.class));
		tabhost.addTab(tabSpec2);

		indexGroup = (RadioGroup) findViewById(R.id.main_radio);
		indexGroup.setOnCheckedChangeListener(this);

		more.setOnClickListener(this);
		cooperate.setOnClickListener(this);

	}

	public List<Map<String, String>> setmoreMenu() {
		// ��ʼ��������
		listRight = new ArrayList<Map<String, String>>();
		// for (int i = 1; i < 3; i++) {
		HashMap<String, String> mapTemp2 = new HashMap<String, String>();
		mapTemp2.put("item", "����Э�� ");
		listRight.add(mapTemp2);

		HashMap<String, String> mapTemp3 = new HashMap<String, String>();
		mapTemp3.put("item", "����Χ ");
		listRight.add(mapTemp3);

		HashMap<String, String> mapTemp4 = new HashMap<String, String>();
		mapTemp4.put("item", "�������� ");
		listRight.add(mapTemp4);

		HashMap<String, String> mapTemp5 = new HashMap<String, String>();
		mapTemp5.put("item", "���ڵ����� ");
		listRight.add(mapTemp5);
		// }
		return listRight;

	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		case R.id.order:
			tabhost.setCurrentTab(0);
			break;
		case R.id.cooperate:
			tabhost.setCurrentTab(2);
			break;
		case R.id.personal:
			tabhost.setCurrentTab(1);
			break;
		case R.id.more:
			tabhost.setCurrentTab(3);
			break;

		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.more:
			// setMenu_more();
			break;
		case R.id.cooperate:
			// setMenu_cooper();
			break;
		}
	}

	private void setMenu_cooper() {
		if (cooper_window != null && cooper_window.isShowing()) {
			cooper_window.dismiss();
		} else {
			cooper_view = getLayoutInflater().inflate(
					R.layout.person_menu_listview, null);
			cooper_listview = (ListView) cooper_view
					.findViewById(R.id.menulist);
			SimpleAdapter listAdapter = new SimpleAdapter(indexActivity.this,
					listLeft, R.layout.person_menu_item,
					new String[] { "item" }, new int[] { R.id.menuitem });
			cooper_listview.setAdapter(listAdapter);

			// ���listview��item�Ĵ���
			cooper_listview
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// �ı䶥����ӦTextViewֵ
							if (arg2 == 1) {
								Intent intent = new Intent(indexActivity.this,
										AddKingTopGroupActivty.class);
								startActivity(intent);
							}
							// ���ص�������
							if (cooper_window != null
									&& cooper_window.isShowing()) {
								cooper_window.dismiss();
							}
						}
					});

			// ������������
			// ��������ΪlayoutLeft���������һ��ListView
			// ���ڿ�ȸ�tvLeftһ��
			cooper_window = new PopupWindow(cooper_view, cooperate.getWidth(),
					LayoutParams.WRAP_CONTENT);

			ColorDrawable cd = new ColorDrawable(-0000);
			cooper_window.setBackgroundDrawable(cd);
			// person_window.setAnimationStyle(R.style.PopupAnimation);
			cooper_window.update();
			cooper_window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
			cooper_window.setTouchable(true); // ����popupwindow�ɵ��
			cooper_window.setOutsideTouchable(true); // ����popupwindow�ⲿ�ɵ��
			cooper_window.setFocusable(true); // ��ȡ����

			// ����popupwindow��λ�ã����tvLeft��λ�ã�
			// int topBarHeight = rlTopBar.getBottom();
			cooper_window.showAsDropDown(cooperate, 0,
					(180 - cooperate.getHeight()) / 2);

			cooper_window.setTouchInterceptor(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// ��������popupwindow���ⲿ��popupwindowҲ����ʧ
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
						cooper_window.dismiss();
						return true;
					}
					return false;
				}
			});
		}

	}

	private void setMenu_more() {
		if (more_window != null && more_window.isShowing()) {
			more_window.dismiss();
		} else {
			more_view = getLayoutInflater().inflate(
					R.layout.person_menu_listview, null);
			more_listview = (ListView) more_view.findViewById(R.id.menulist);
			SimpleAdapter listAdapter = new SimpleAdapter(indexActivity.this,
					setmoreMenu(), R.layout.person_menu_item,
					new String[] { "item" }, new int[] { R.id.menuitem });
			more_listview.setAdapter(listAdapter);

			// ���listview��item�Ĵ���
			more_listview
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// �ı䶥����ӦTextViewֵ
							// String strItem = listRight.get(arg2).get(
							// "item");
							if (arg2 == 1) {
								Intent intent = new Intent(indexActivity.this,
										ServiceScopeActivty.class);
								startActivity(intent);
							} else if (arg2 == 2) {
								Intent intent1 = new Intent(indexActivity.this,
										SomeQuestionActivty.class);
								startActivity(intent1);
							} else if (arg2 == 3) {
								Intent inten2 = new Intent(indexActivity.this,
										AboutKingTopGroup.class);
								startActivity(inten2);
							}

							// ���ص�������
							if (more_window != null && more_window.isShowing()) {
								more_window.dismiss();
							}
						}
					});

			// ������������
			// ��������ΪlayoutLeft���������һ��ListView
			// ���ڿ�ȸ�tvLeftһ��
			more_window = new PopupWindow(more_view, more.getWidth(),
					LayoutParams.WRAP_CONTENT);

			ColorDrawable cd = new ColorDrawable(-0000);
			more_window.setBackgroundDrawable(cd);
			// person_window.setAnimationStyle(R.style.PopupAnimation);
			more_window.update();
			more_window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
			more_window.setTouchable(true); // ����popupwindow�ɵ��
			more_window.setOutsideTouchable(true); // ����popupwindow�ⲿ�ɵ��
			more_window.setFocusable(true); // ��ȡ����

			// ����popupwindow��λ�ã����tvLeft��λ�ã�
			// int topBarHeight = rlTopBar.getBottom();
			more_window.showAsDropDown(more, 0, (180 - more.getHeight()) / 2);

			more_window.setTouchInterceptor(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// ��������popupwindow���ⲿ��popupwindowҲ����ʧ
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
						more_window.dismiss();
						return true;
					}
					return false;
				}
			});
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
