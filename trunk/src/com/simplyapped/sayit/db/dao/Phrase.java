package com.simplyapped.sayit.db.dao;

public class Phrase {
	private long id;
	private String phrase;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPhrase() {
		return phrase;
	}
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	@Override
	public String toString() {
		return phrase;
	}
}
