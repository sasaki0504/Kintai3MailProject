package com.example.kintai2mailproject.dto;

import java.util.List;

public class MemberDto {

	//参加者リスト
	private List<String> memberList;

	//開始時刻
	private String startTime;

	//終了時刻
	private String endTime;

	public List<String> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<String> memberList) {
		this.memberList = memberList;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
