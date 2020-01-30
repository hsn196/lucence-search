package com.lucence.search.pojo;

public class ResultsPojo {

	private double score;
	private String[] highlightedText;
	private String filePath;
	private long hits;
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	
	public String[] getHighlightedText() {
		return highlightedText;
	}
	public void setHighlightedText(String[] highlightedText) {
		this.highlightedText = highlightedText;
	} 
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public long getHits() {
		return hits;
	}
	public void setHits(long hits) {
		this.hits = hits;
	}
	
	
	
}
