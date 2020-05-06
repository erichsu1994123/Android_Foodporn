package com.example.veryhomepage.model;

public class ChatMessage {
	private String type;
	private String sender;
	private String receiver;
	private String message;
	private Double lat;
	private Double lng;	
	
	

	public ChatMessage() {
		super();
	}

	public ChatMessage(String type, String sender, String receiver, String message) {
		super();
		this.type = type;
		this.sender = sender;
		this.receiver = receiver;
		this.message = message;
	}

	public ChatMessage(String type, String sender, String receiver, String message, Double lat, Double lng) {
		super();
		this.type = type;
		this.sender = sender;
		this.receiver = receiver;
		this.message = message;
		this.lat = lat;
		this.lng = lng;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}
}
