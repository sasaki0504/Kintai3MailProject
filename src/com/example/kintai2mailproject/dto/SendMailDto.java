package com.example.kintai2mailproject.dto;

import java.util.List;

public class SendMailDto {

	//社内業務
	private String syanaiGyomu;

	//日付
	private String date;

	//本文
	List<MemberDto> members;

	public String getSyanaiGyomu() {
		return syanaiGyomu;
	}

	public void setSyanaiGyomu(String syanaiGyomu) {
		this.syanaiGyomu = syanaiGyomu;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<MemberDto> getMembers() {
		return members;
	}

	public void setMembers(List<MemberDto> members) {
		this.members = members;
	}

}
