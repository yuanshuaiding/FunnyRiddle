package com.eric.riddle.activities;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.eric.riddle.xmlParseUtil.XmlPullParseUtil;

public class RiddleListActivity extends Activity implements OnClickListener {

	private View backBtn;
	private ListView list;
	private List<Map<String, Object>> data;

	// NDK STUFF
	static {
		System.loadLibrary("myparsexml");
	}

	private native String parseXml(String channel);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.riddlelist);
		Intent intent = getIntent();
		String chanel = intent.getStringExtra("chanel");
		if (parseXml(chanel) != null && parseXml(chanel) != "") {
			data = buildRiddleListByChanel(parseXml(chanel));
		} else {
			data = buildRiddleListByChanel(chanel);
		}
		Toast.makeText(getApplicationContext(), "点击谜语查看答案或分享！", Toast.LENGTH_SHORT).show();
		backBtn = findViewById(R.id.back);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		list = (ListView) findViewById(R.id.list);
		SimpleAdapter adapter = new SimpleAdapter(RiddleListActivity.this,
				data, R.layout.list_item, new String[] { "content", "answer" },
				new int[] { R.id.riddle_content, R.id.answer });
		list.setAdapter(adapter);
		list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				TextView tv_answer = (TextView) view.findViewById(R.id.answer);
				String answer = tv_answer.getText().toString().trim();
				// System.out.println("谜底：" + answer);
				QuickActionBar qaBar = new QuickActionBar(view, position);
				qaBar.setAnswerText(answer);
				qaBar.setEnableActionsLayoutAnim(true);
				qaBar.show();
			}
		});
	}

	private List<Map<String, Object>> buildRiddleListByChanel(String chanel) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = null;
		InputStream in = null;
		if (chanel.equals("人物类")) {
			in = getResources().openRawResource(R.raw.people);
		} else if (chanel.equals("字词类")) {
			in = getResources().openRawResource(R.raw.words);
		} else if (chanel.equals("搞笑类")) {
			in = getResources().openRawResource(R.raw.fun);
		} else if (chanel.equals("成人类")) {
			in = getResources().openRawResource(R.raw.couples);
		} else if (chanel.equals("动物类")) {
			in = getResources().openRawResource(R.raw.animal);
		} else if (chanel.equals("植物类")) {
			in = getResources().openRawResource(R.raw.planet);
		}
		list = XmlPullParseUtil.parseXmlFile(in);
		return list;
	}

	@Override
	public void onClick(View v) {
	}

}
