package com.example.kintai2mailproject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kintai2mailproject.R;
import com.example.kintai2mailproject.common.Consts;
import com.example.kintai2mailproject.common.SendMail;
import com.example.kintai2mailproject.common.Utils;
import com.example.kintai2mailproject.dto.MemberDto;
import com.example.kintai2mailproject.dto.SendMailDto;

public class FormActivity extends Activity {

	//マスタ設定ボタン
	Button masterButton;
	//日付ボタン
	Button dateButton;
	//Dto
	SendMailDto sendMailDto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form);

		//-------------------------------------------
		//初期設定
		//-------------------------------------------

		//------------------------------------------
		//枠部分の設定

		//プリファレンスオブジェクトの取得
		SharedPreferences preference =
			getSharedPreferences(Consts.FILE_NAME, MODE_PRIVATE);

		//追加ボタンオブジェクト
		Button addButton = (Button)findViewById(R.id.formAddButton);
		//タグ設定
		addButton.setTag(Consts.TAG_ADD);

		//マスタボタン
		Button masterButton = (Button)findViewById(R.id.masterButton);
		//タグの設定
		masterButton.setTag(Consts.TAG_MASTER);

		//メール送信ボタン
		Button sendMailButton = (Button)findViewById(R.id.sendMailButton);
		//タグ設定
		sendMailButton.setTag(Consts.TAG_SEND);

		//フォーム削除ボタン
		Button deleteButton = (Button)findViewById(R.id.formDeleteButton);
		//タグ設定
		deleteButton.setTag(Consts.TAG_DELETE);

		//社内業務テキスト取得
		TextView syanaiTextLabel = (TextView)findViewById(R.id.oparationText);
		//ラベル設定
		syanaiTextLabel.setText(preference.getString(Consts.PRI_SAYANAI_GYOMU, "未設定"));


		//------------------------------
		//ボタンの初期値設定

		//日付ボタンに今日の日付を設定
		dateButton = setDefaultButton(R.id.dateButton
									, Consts.TAG_DATE
									, ""
									, preference
									, Utils.getStrToday());


		//----------------------------------------
		//リスナー設定

		//日付ボタン
		dateButton.setOnClickListener(new ButtonClickListener());
		//設定ボタン
		masterButton.setOnClickListener(new ButtonClickListener());
		//メール送信ボタン
		sendMailButton.setOnClickListener(new ButtonClickListener());
		//追加他ボタン
		addButton.setOnClickListener(new ButtonClickListener());
		//削除ボタン
		deleteButton.setOnClickListener(new ButtonClickListener());

		//----------------------------------------
		//１つめのフォームを追加
		addForm();

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_form, menu);
		return true;
	}
	/**
	 * 押下されたボタンのタグによって発生するイベントを設定
	 *
	 */
	class ButtonClickListener implements OnClickListener{

		//OnClick時イベント
		public void onClick(View v){
			//タグの名称を取得
			String tag = (String)v.getTag();
			//日付ボタンタグ
			if(Consts.TAG_DATE.equals(tag)){
				showDatePickerDialog();
			}
			//設定ボタンタグ
			if(Consts.TAG_MASTER.equals(tag)){
				goToMaster();
			}
			//送信ボタンタグ
			if(Consts.TAG_SEND .equals(tag)){
				sendMail();
			}
			//追加ボタンタグ
			if(Consts.TAG_ADD.equals(tag)){
				addForm();
			}
			//削除ボタンタグ
			if(Consts.TAG_DELETE.equals(tag)){
				deleteForm();
			}

		}
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
	  private Button setDefaultButton(int viewId, String tagName, String viewName,SharedPreferences preference,String defaultStr){
		  //ボタンオブジェクトの取得
		  Button button = (Button)findViewById(viewId);
		  //タグの設定
		  button.setTag(tagName);
		  //ラベルの取得
		  button.setText(preference.getString(viewName, defaultStr));
		  //返却
		  return button;

	  }


	  /**
	   * 日付選択ダイアログ
	   */
		public void showDatePickerDialog(){
			//カレンダー取得
			Calendar cal = Calendar.getInstance();

			//ダイアログの取得
			DatePickerDialog dialog = new DatePickerDialog(FormActivity.this,
					new DatePickerDialog.OnDateSetListener(){
						public void onDateSet(DatePicker datePicker,
								int year,int month,int day){
							dateButton.setText(year+"/"+month+"/"+day);
						}
			}
			, cal.get(Calendar.YEAR)
			, cal.get(Calendar.MONTH)
			, cal.get(Calendar.DAY_OF_MONTH)
			);
			dialog.show();
		}

		/**
		 * 開始時刻ダイアログ
		 *
		 */
		public void showStartTimePickerDialog(View v){

			//ビューの親を取得
			View subForm = (View) v.getParent();
			final Button startTimeButton = (Button)subForm.findViewById(R.id.startTimeButton);

			TimePickerDialog dialog = new TimePickerDialog(FormActivity.this
					, new TimePickerDialog.OnTimeSetListener(){
				public void onTimeSet(TimePicker picker, int hour,int min){

					String str1 = Utils.timeToString(hour);
					String str2 = Utils.timeToString(min);

					//Button startTimeButton = (Button)layout.findViewById(R.id.startTimeButton);
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
		 */
		public void showEndTimePickerDialog(View v){

			//ビューの親を取得
			View subForm = (View) v.getParent();
			final Button endTimeButton = (Button)subForm.findViewById(R.id.endTimeButton);

			TimePickerDialog dialog = new TimePickerDialog(FormActivity.this
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
		 * 送信ボタン
		 *
		 */
		private void sendMail(){

			//ダイアログの取得
			AlertDialog.Builder dialog =
				new AlertDialog.Builder(FormActivity.this);

			//メッセージの設定
			dialog.setTitle("メール送信確認");
			dialog.setMessage("メールを送信しますか");

			//OKを選択した場合の処理
			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){

				//------------------
				//メール送信
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					//TODO メール送信処理

					//Dtoに値をセット
					sendMailDto = getMailDto();

					//メール送信
					SendMail sendMail = new SendMail();
					sendMail.sendMail(sendMailDto);

					//トーストにて送信済み表示
					toast();

				}
			});
			//NGを設定した場合の処理
			dialog.setNegativeButton("NG", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 何もしない
				}
				//なにもしない
			});
			dialog.show();
		}


		/**
		 * 設定ボタン遷移
		 *
		 */
		private void goToMaster(){
			//インテントの設定
			Intent intent = new Intent(FormActivity.this, MasterActivity.class);
			//次のアクティビティを起動
			startActivity(intent);
		}

		/**
		 * 追加ボタン
		 *
		 */
		private void addForm(){
			LinearLayout layoutMain =
				(LinearLayout)findViewById(R.id.LinearLayout7);

			LinearLayout layoutSub =
				(LinearLayout)getLayoutInflater().inflate(R.layout.sub_form, null);

			layoutMain.addView(layoutSub);
			//初期値設定
			initialize(layoutSub);

		}

		/**
		 * 追加ボタン押下時の初期化設定
		 * @param View layout
		 *
		 */
		private void initialize(View layout){

			//プリファレンスオブジェクトの取得
			SharedPreferences preference =
				getSharedPreferences(Consts.FILE_NAME, MODE_PRIVATE);

			//開始時刻のボタンオブジェクト取得
			Button startTimeButton = (Button)layout.findViewById(R.id.startTimeButton);
			//デフォルト値を設定
			startTimeButton.setText(preference.getString(Consts.PRI_START_TIME, Consts.DEFAULT_START_TIME));

			//終了時刻のボタンオブジェクト取得
			Button endTimeButton = (Button)layout.findViewById(R.id.endTimeButton);
			//デフォルト値を設定
			endTimeButton.setText(preference.getString(Consts.PRI_END_TIME, Consts.DEFAULT_END_TIME));


			//メンバー1～15のデフォルト設定
			setDefaultCheckBox(R.id.memberCheckBox1,
					layout, Consts.TAG_NAME_1,Consts.PRI_NAME_1,preference,"");

			//2
			setDefaultCheckBox(R.id.memberCheckBox2,
					layout, Consts.TAG_NAME_2,Consts.PRI_NAME_2,preference,"");
			//3
			setDefaultCheckBox(R.id.memberCheckBox3,
					layout, Consts.TAG_NAME_3,Consts.PRI_NAME_3,preference,"");

			//4
			setDefaultCheckBox(R.id.memberCheckBox4,
					layout, Consts.TAG_NAME_4,Consts.PRI_NAME_4,preference,"");

			//5
			setDefaultCheckBox(R.id.memberCheckBox5,
					layout, Consts.TAG_NAME_5,Consts.PRI_NAME_5,preference,"");

			//6
			setDefaultCheckBox(R.id.memberCheckBox6,
					layout, Consts.TAG_NAME_6,Consts.PRI_NAME_6,preference,"");

			//7
			setDefaultCheckBox(R.id.memberCheckBox7,
					layout, Consts.TAG_NAME_7,Consts.PRI_NAME_7,preference,"");

			//8
			setDefaultCheckBox(R.id.memberCheckBox8,
					layout, Consts.TAG_NAME_8,Consts.PRI_NAME_8,preference,"");

			//9
			setDefaultCheckBox(R.id.memberCheckBox9,
					layout, Consts.TAG_NAME_9,Consts.PRI_NAME_9,preference,"");

			//10
			setDefaultCheckBox(R.id.memberCheckBox10,
					layout, Consts.TAG_NAME_10,Consts.PRI_NAME_10,preference,"");

			//11
			setDefaultCheckBox(R.id.memberCheckBox11,
					layout, Consts.TAG_NAME_11,Consts.PRI_NAME_11,preference,"");

			//12
			setDefaultCheckBox(R.id.memberCheckBox12,
					layout, Consts.TAG_NAME_12,Consts.PRI_NAME_12,preference,"");

			//13
			setDefaultCheckBox(R.id.memberCheckBox13,
					layout, Consts.TAG_NAME_13,Consts.PRI_NAME_13,preference,"");

			//14
			setDefaultCheckBox(R.id.memberCheckBox14,
					layout, Consts.TAG_NAME_14,Consts.PRI_NAME_14,preference,"");

			//15
			setDefaultCheckBox(R.id.memberCheckBox15,
					layout, Consts.TAG_NAME_15,Consts.PRI_NAME_15,preference,"");
		}

		  /**
		   *チェックボックスラベルの初期化
		   *
		   */

		  private void setDefaultCheckBox(int viewId
				  , View layout
				  , String tagName
				  , String viewName
				  ,SharedPreferences preference
				  ,String defaultStr){
			  //チェックボックスの取得
			  CheckBox checkBox = (CheckBox)layout.findViewById(viewId);
			  //タグの設定
			  checkBox.setTag(tagName);
			  //ラベルの取得
			  checkBox.setText(preference.getString(viewName, defaultStr));
			  //ラベルの表示がない場合、チェックボックスそのものをないものとする
			  if(Utils.isEmpty(checkBox.getText().toString())){
				  checkBox.setVisibility(CheckBox.GONE);
			  }
			  //返却
			  //return checkBox;
		  }

		  /**
		   *
		   * すべてのメンバーをチェック
		   * @param v
		   */

		  public void allCheck(View v) {
			//ビューの親を取得
			View subForm = (View) v.getParent();
			//さらにその親を取得
			View parentSub = (View)subForm.getParent();
			//ビューの親の子であるテーブルを取得
			View memberArea = (View)parentSub.findViewById(R.id.tableLayout1);
			final CheckBox allOnCheckBox = (CheckBox)subForm.findViewById(R.id.allMemberCheckBox);

				//チェックされた場合
				if(allOnCheckBox.isChecked()){
					//チェックボックスオブジェクトをリストに追加
					List<CheckBox> checkBoxList = new ArrayList<CheckBox>();
					checkBoxList.add((CheckBox)memberArea.findViewById(R.id.memberCheckBox1));
					checkBoxList.add((CheckBox)memberArea.findViewById(R.id.memberCheckBox2));
					checkBoxList.add((CheckBox)memberArea.findViewById(R.id.memberCheckBox3));
					checkBoxList.add((CheckBox)memberArea.findViewById(R.id.memberCheckBox4));
					checkBoxList.add((CheckBox)memberArea.findViewById(R.id.memberCheckBox5));
					checkBoxList.add((CheckBox)memberArea.findViewById(R.id.memberCheckBox6));
					checkBoxList.add((CheckBox)memberArea.findViewById(R.id.memberCheckBox7));
					checkBoxList.add((CheckBox)memberArea.findViewById(R.id.memberCheckBox8));
					checkBoxList.add((CheckBox)memberArea.findViewById(R.id.memberCheckBox9));
					checkBoxList.add((CheckBox)memberArea.findViewById(R.id.memberCheckBox10));
					checkBoxList.add((CheckBox)memberArea.findViewById(R.id.memberCheckBox11));
					checkBoxList.add((CheckBox)memberArea.findViewById(R.id.memberCheckBox12));
					checkBoxList.add((CheckBox)memberArea.findViewById(R.id.memberCheckBox13));
					checkBoxList.add((CheckBox)memberArea.findViewById(R.id.memberCheckBox14));
					checkBoxList.add((CheckBox)memberArea.findViewById(R.id.memberCheckBox15));

					for (CheckBox member : checkBoxList){
						//nullでない場合のみチェックをON
						if(!Utils.isNull(member)){
							member.setChecked(true);
						}
					}
				}
		  }

		 /**
		  *
		  * 削除ボタン
		  */
		 private void deleteForm(){

			 //元のフォーム取得
			 LinearLayout subFormArea = (LinearLayout)findViewById(R.id.LinearLayout7);
			 //サブフォームの取得
			 //フォームの件数を取得
			 int i = subFormArea.getChildCount();
			 //フォームの件数亜が１より大きい場合
			 if(i>1){
				 //一番後ろのフォームを削除
				 subFormArea.removeViewAt(i-1);

			 }
		 }

		 private SendMailDto getMailDto(){

			 //DTOの生成
			 SendMailDto sendMailDto = new SendMailDto();
			 //-------------------
			 //社内業務のセット

			 //社内業務テキスト取得
			 TextView syanaiTextLabel = (TextView)findViewById(R.id.oparationText);
			 sendMailDto.setSyanaiGyomu(syanaiTextLabel.getText().toString());

			 //日付テキスト取得
			 Button dateButton = (Button)findViewById(R.id.dateButton);
			 sendMailDto.setDate(dateButton.getText().toString());

			 //--------------------------
			 //メンバーリストの作成

			 //インスタンス生成
			 List<MemberDto> memberDtoList = new ArrayList<MemberDto>();

			 //フォーム取得
			 LinearLayout subFormArea = (LinearLayout)findViewById(R.id.LinearLayout7);
			 //サブフォームの件数取得
			 int i = subFormArea.getChildCount();

			 //サブフォームの件数分ループ
			 for (int k=0; k < i;k++){
				 //インスタンス生成
				MemberDto memberDto = new MemberDto();
				 //k版目のサブフォーム取得
				View subForm = (View)subFormArea.getChildAt(k);
				 //開始時間取得
				Button startTimeButton = (Button)subForm.findViewById(R.id.startTimeButton);
				memberDto.setStartTime(startTimeButton.getText().toString());
				//終了時刻取得
				Button endTimeButton = (Button)subForm.findViewById(R.id.endTimeButton);
				memberDto.setEndTime(endTimeButton.getText().toString());

				//メンバーリストの取得
				List<String> memberList = new ArrayList<String>();

				//メンバーリストの取得
				//チェックボックスオブジェクトをリストに追加
				List<CheckBox> checkBoxList = new ArrayList<CheckBox>();
				checkBoxList.add((CheckBox)subForm.findViewById(R.id.memberCheckBox1));
				checkBoxList.add((CheckBox)subForm.findViewById(R.id.memberCheckBox2));
				checkBoxList.add((CheckBox)subForm.findViewById(R.id.memberCheckBox3));
				checkBoxList.add((CheckBox)subForm.findViewById(R.id.memberCheckBox4));
				checkBoxList.add((CheckBox)subForm.findViewById(R.id.memberCheckBox5));
				checkBoxList.add((CheckBox)subForm.findViewById(R.id.memberCheckBox6));
				checkBoxList.add((CheckBox)subForm.findViewById(R.id.memberCheckBox7));
				checkBoxList.add((CheckBox)subForm.findViewById(R.id.memberCheckBox8));
				checkBoxList.add((CheckBox)subForm.findViewById(R.id.memberCheckBox9));
				checkBoxList.add((CheckBox)subForm.findViewById(R.id.memberCheckBox10));
				checkBoxList.add((CheckBox)subForm.findViewById(R.id.memberCheckBox11));
				checkBoxList.add((CheckBox)subForm.findViewById(R.id.memberCheckBox12));
				checkBoxList.add((CheckBox)subForm.findViewById(R.id.memberCheckBox13));
				checkBoxList.add((CheckBox)subForm.findViewById(R.id.memberCheckBox14));
				checkBoxList.add((CheckBox)subForm.findViewById(R.id.memberCheckBox15));

				//チェックボックスの件数分ループ
				for (CheckBox member : checkBoxList){
					//nullでない　かつ　チェックがついている　かつ　文字が空白でない場合にListに追加
					if(!Utils.isNull(member) && member.isChecked() &&
							!Utils.isEmpty(member.getText().toString())){

						memberList.add(member.getText().toString());
					}
				}
				//リストをDTOにセット
				memberDto.setMemberList(memberList);

				//memberDtoリストに値を設定
				memberDtoList.add(memberDto);
			 }

			 //sendMailDtoにmemberDtoリストに設定
			 sendMailDto.setMembers(memberDtoList);

			 return sendMailDto;
		 }

		/**
		 * 送信ボタンのトースト表示
		 *
		 */
		private void toast(){
			Toast.makeText(FormActivity.this , "送信しました",Toast.LENGTH_SHORT).show();
		}
}
