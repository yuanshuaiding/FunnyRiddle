package com.eric.riddle.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class IndexActivity extends Activity {
    private GridView grid;
	private LayoutInflater inflate;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        inflate=getLayoutInflater();
        grid=(GridView)findViewById(R.id.grid_menu);
        MyAdapter adapter=new MyAdapter();
        grid.setAdapter(adapter);
        
        grid.setOnItemClickListener(new OnItemClickListener() {

        	@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(IndexActivity.this, RiddleListActivity.class);
				intent.putExtra("chanel", menuTitles[position]);
				startActivity(intent);
			}
		});
    }
    
    
    private int[] menuIds=new int[]{R.drawable.people,R.drawable.words,R.drawable.fun,R.drawable.cupoules,R.drawable.animal1,R.drawable.plant};
    private String[] menuTitles=new String[]{"人物类","字词类","搞笑类","成人类","动物类","植物类"};
    class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return menuIds.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewGroup vg=(ViewGroup) inflate.inflate(R.layout.menu_item, null);
			ImageView img=(ImageView) vg.findViewById(R.id.menu_icon);
			img.setAlpha(180);
			img.setImageResource(menuIds[position]);
			TextView tv=(TextView)vg.findViewById(R.id.menu_title);
			tv.setText(menuTitles[position]);
			return vg;
		}
    	
    }
}