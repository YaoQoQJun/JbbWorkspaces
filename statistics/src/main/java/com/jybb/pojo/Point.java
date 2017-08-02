package com.jybb.pojo;

public class Point {
	
	private String points;
	private String url;
	private long time;
	
	@Override
	public String toString() {
		return "Point [points=" + points + ", url=" + url + ", time=" + time
				+ "]";
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
}
