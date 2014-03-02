package com.example.kintai2mailproject;

import java.util.ArrayList;
import java.util.List;

import com.example.kintai2mailproject.R;
import com.example.kintai2mailproject.common.Consts;
import com.example.kintai2mailproject.common.Utils;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputFilter.LengthFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class MasterActivity extends Activity {

	public final String SUCCESS = "0";
	public final String FAILED = "1";

	//社内業務選択リスト
	Spinner syanaiSpinner;
	//デフォルト開始時間
	Button startTimeButton;
	//デフォルト終了時間
	Button endTimeButton;
	//メンバー1～15
	EditText name1;
	EditText name2;
	EditText name3;
	EditText name4;
	EditText name5;
	EditText name6;
	EditText name7;
	EditText name8;
	EditText name9;
	EditText name10;
	EditText name11;
	EditText name12;
	EditText name13;
	EditText name14;
	EditText name15;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_master);

		//-----------------------------
		//初期化処理

		//プリファレンスオブジェクト取得
		SharedPreferences preference =
			getSharedPreferences(Consts.FILE_NAME, MODE_PRIVATE);

		//社内業務初期化処理
		syanaiSpinner = setDefultSpinner(R.id.SyanaiGyoumu
				,Consts.TAG_SYANAI_GYOMU
				,Consts.PRI_SAYANAI_GYOMU
				,preference,"選択してください");

		//開始時刻初期化処理
		startTimeButton = setDefaultButton(R.id.defaultStartTime
				,Consts.TAG_START_TIME
				,Consts.PRI_START_TIME
				,preference,"20:00");

		//終了ボタン初期化処理
		endTimeButton = setDefaultButton(R.id.defaultEndTime
				,Consts.TAG_END_TIME
				,Consts.PRI_END_TIME
				,preference,"21:00");

		//TODO 改善の余地あり
		//メンバー1から１５の初期化処理
		name1 = setDefaultEditText(R.id.name1,Consts.TAG_NAME_1,Consts.PRI_NAME_1,preference,"");
		name2 = setDefaultEditText(R.id.name2,Consts.TAG_NAME_2,Consts.PRI_NAME_2,preference,"");
		name3 = setDefaultEditText(R.id.name3,Consts.TAG_NAME_3,Consts.PRI_NAME_3,preference,"");
		name4 = setDefaultEditText(R.id.name4,Consts.TAG_NAME_4,Consts.PRI_NAME_4,preference,"");
		name5 = setDefaultEditText(R.id.name5,Consts.TAG_NAME_5,Consts.PRI_NAME_5,preference,"");
		name6 = setDefaultEditText(R.id.name6,Consts.TAG_NAME_6,Consts.PRI_NAME_6,preference,"");
		name7 = setDefaultEditText(R.id.name7,Consts.TAG_NAME_7,Consts.PRI_NAME_7,preference,"");
		name8 = setDefaultEditText(R.id.name8,Consts.TAG_NAME_8,Consts.PRI_NAME_8,preference,"");
		name9 = setDefaultEditText(R.id.name9,Consts.TAG_NAME_9,Consts.PRI_NAME_9,preference,"");
		name10 = setDefaultEditText(R.id.name10,Consts.TAG_NAME_10,Consts.PRI_NAME_10,preference,"");
		name11 = setDefaultEditText(R.id.name11,Consts.TAG_NAME_11,Consts.PRI_NAME_11,preference,"");
		name12 = setDefaultEditText(R.id.name12,Consts.TAG_NAME_12,Consts.PRI_NAME_12,preference,"");
		name13 = setDefaultEditText(R.id.name13,Consts.TAG_NAME_13,Consts.PRI_NAME_13,preference,"");
		name14 = setDefaultEditText(R.id.name14,Consts.TAG_NAME_14,Consts.PRI_NAME_14,preference,"");
		name15 = setDefaultEditText(R.id.name15,Consts.TAG_NAME_15,Consts.PRI_NAME_15,preference,"");

		//保存ボタンの取得
		Button saveButton = (Button)findViewById(R.id.doSaveButton);
		//タグ設定
		saveButton.setTag(Consts.TAG_SAVE);

		//戻るボタンの取得
		Button backButton = (Button)findViewById(R.id.doBackButton);
		//タグ設定
		backButton.setTag(Consts.TAG_BACK);


		//--------------------------------
		//ボタンイベントの設定

		//開始ボタン
		startTimeButton.setOnClickListener(new ButtonClickListener());
		//終了ボタン
		endTimeButton.setOnClickListener(new ButtonClickListener());
		//保存ボタン
		saveButton.setOnClickListener(new ButtonClickListener());
		//戻るボタン
		backButton.setOnClickListener(new ButtonClickListener());


	}

	/**
	 * リスナー定義
	 *
	 */
	class ButtonClickListener implements OnClickListener{

		//OnClick時イベント
		public void onClick(View v){
			//タグの名称を取得
			String tag = (String)v.getTag();
			//開始時間ボタン
			if(Consts.TAG_START_TIME.equals(tag)){
				showStartTimePickerDialog();
			}
			//終了時間ボタン
			if(Consts.TAG_END_TIME.equals(tag)){
				showEndTimePickerDialog();
			}
			//保存ボタンタグ
			if(Consts.TAG_SAVE.equals(tag)){
				String str = doSave();
				//doSaveの結果成功ならフォームへ遷移
				if(SUCCESS.equals(str)){
					toast();
					doBack();
				}else{
					indicateAlert();
				}

			}
			//戻るボタンタグ
			if(Consts.TAG_BACK.equals(tag)){
				doBack();
			}

		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_master, menu);
		return true;
	}


	/**
	 * 選択リストを取得し、デフォルト値を設定する
	 * @author Sasaki
	 * @param int viewId
	 * @param String タグ名
	 * @param viewの名称
	 * @param Sharedpreferences プリファレンス
	 * @String デフォルト値
	 * @return Spinner
	 */
	 private Spinner setDefultSpinner(int viewId, String tagName,String viewName,SharedPreferences preference,String str){

		 //スピナーオブジェクトの取得
		 Spinner spinner = (Spinner)findViewById(viewId);
		 //タグの設定
		 spinner.setTag(tagName);
		 //TODO ラベルの設定

		 return spinner;

	 }

	 /**
	  * ボタンを取得し、デフォルト値を設定する
	  * @author Sasaki
	  * @param int viewId
	  * @param String タグ名
	  * @param String View名
	  * @param SharedPreerenes プリファレンス
	  * @return Button
	  */
	  private Button setDefaultButton(int viewId, String tagName, String viewName,SharedPreferences preference,String str){
		  //ボタンオブジェクトの取得
		  Button button = (Button)findViewById(viewId);
		  //タグの設定
		  button.setTag(tagName);
		  //ラベルの取得
		  button.setText(preference.getString(viewName, str));
		  //返却
		  return button;

	  }

	  /**
	   * EditTextにデフォルト値をセット
	   * @param int viewId
	   * @param String タグ名
	   * @param String View名
	   * @param SharedPreerenes プリファレンス
	   * @return EditText
	   */
	  	private EditText setDefaultEditText(int viewId, String tagName, String viewName,SharedPreferences preference,String str){
	  		//オブジェクトの取得
	  		EditText editText = (EditText)findViewById(viewId);
	  		//タグの設定
	  		editText.setTag(tagName);
	  		//デフォルト値の取得
	  		editText.setText(preference.getString(viewName, str));
	  		//返却
	  		return editText;
	  	}

		/**
		 * 開始時刻ダイアログ
		 *
		 */
		public void showStartTimePickerDialog(){

			TimePickerDialog dialog = new TimePickerDialog(MasterActivity.this
					, new TimePickerDialog.OnTimeSetListener(){
				public void onTimeSet(TimePicker picker, int hour,int min){

					String str1 = Utils.timeToString(hour);
					String str2 = Utils.timeToString(min);

					startTimeButton.setText(str1 + ":" + str2);
				}
			}
			,0
			,0
			,true
			);
			dialog.show();
		}

		/**
		 * 終了時刻ダイアログ
		 *
		 */
		public void showEndTimePickerDialog(){

			TimePickerDialog dialog = new TimePickerDialog(MasterActivity.this
					, new TimePickerDialog.OnTimeSetListener(){
				public void onTimeSet(TimePicker picker,int hour,int min){

					String str1 = Utils.timeToString(hour);
					String str2 = Utils.timeToString(min);

					endTimeButton.setText(str1 + ":" + str2);
				}
			}
			,0
			,0
			,true
			);
			dialog.show();

		}

		/**
		 * 戻るボタン実行
		 */
		private void doBack(){
			//インテントの設定
			Intent intent = new Intent(MasterActivity.this,FormActivity.class);
			//次のアクティビティを起動
			startActivity(intent);
		}

		/**
		 * 保存ボタン実行
		 * @return String(0:成功、1:エラー）
		 */
		private String doSave(){

			//入力チェック処理を行い、エラーがなければ保存。
			if(isEnableSave()){
				//プリファレンスオブジェクト取得
				SharedPreferences preference =
					getSharedPreferences(Consts.FILE_NAME, MODE_PRIVATE);

				//プリファレンスの編集用オブジェクト取得
				SharedPreferences.Editor editor = preference.edit();

				//社内業務の設定
				editor.putString(Consts.PRI_SAYANAI_GYOMU, syanaiSpinner.getSelectedItem().toString());
				//開始時刻の設定
				editor.putString(Consts.PRI_START_TIME,startTimeButton.getText().toString());
				//終了時刻設定
				editor.putString(Consts.PRI_END_TIME,endTimeButton.getText().toString());
				//メンバー１～１５の設定
				editor.putString(Consts.PRI_NAME_1, name1.getText().toString());
				editor.putString(Consts.PRI_NAME_2, name2.getText().toString());
				editor.putString(Consts.PRI_NAME_3, name3.getText().toString());
				editor.putString(Consts.PRI_NAME_4, name4.getText().toString());
				editor.putString(Consts.PRI_NAME_5, name5.getText().toString());
				editor.putString(Consts.PRI_NAME_6, name6.getText().toString());
				editor.putString(Consts.PRI_NAME_7, name7.getText().toString());
				editor.putString(Consts.PRI_NAME_8, name8.getText().toString());
				editor.putString(Consts.PRI_NAME_9, name9.getText().toString());
				editor.putString(Consts.PRI_NAME_10, name10.getText().toString());
				editor.putString(Consts.PRI_NAME_11, name11.getText().toString());
				editor.putString(Consts.PRI_NAME_12, name12.getText().toString());
				editor.putString(Consts.PRI_NAME_13, name13.getText().toString());
				editor.putString(Consts.PRI_NAME_14, name14.getText().toString());
				editor.putString(Consts.PRI_NAME_15, name15.getText().toString());

				editor.commit();

				return SUCCESS;
			}else{
				//エラーを返却
				return FAILED;

			}



		}
		/**
		 * 保存ボタンのトースト表示
		 *
		 */
		private void toast(){
			Toast.makeText(MasterActivity.this , "保存しまいした",Toast.LENGTH_SHORT).show();
		}

		/**
		 * 保存可能かどうかチェック
		 *
		 */

		private boolean isEnableSave(){

			//リスト生成
			List<String> nameList = new ArrayList<String>();
			//リストに名前追加
			nameList.add(name1.getText().toString());
			nameList.add(name2.getText().toString());
			nameList.add(name3.getText().toString());
			nameList.add(name4.getText().toString());
			nameList.add(name5.getText().toString());
			nameList.add(name6.getText().toString());
			nameList.add(name7.getText().toString());
			nameList.add(name8.getText().toString());
			nameList.add(name9.getText().toString());
			nameList.add(name10.getText().toString());
			nameList.add(name11.getText().toString());
			nameList.add(name12.getText().toString());
			nameList.add(name13.getText().toString());
			nameList.add(name14.getText().toString());
			nameList.add(name15.getText().toString());

			//リストの件数分ループ
			//*自分の一つ上が空白の組み合わせを検索する
			//TODO うまくいかない
			for (int i = 0 ; i < nameList.size()-1 ; i++){
				if((Utils.isEmpty(nameList.get(i))) && !(Utils.isEmpty(nameList.get(i+1)))){
					return false;
				}
			}
			return true;
		}


		/**
		 * アラート用ダイアログ
		 *
		 */
		private void indicateAlert(){
			AlertDialog.Builder dialog = new AlertDialog.Builder(MasterActivity.this);
			dialog.setTitle("エラー");
			dialog.setMessage("入力内容に不正があります");
			dialog.setPositiveButton("OK", null);
			dialog.show();
		}
}
