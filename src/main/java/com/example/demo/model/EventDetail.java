package com.example.demo.model;

public class EventDetail {
	private Event event;
	
	public static class Event {
		private Name name;
		private Description description;
		private Start start;
		private End end;
	}

	public static class Name {
		private String html;
	}

	public static class Description {
		private String html;
	}

	public static class Start {
		private String timezone;
		private String utc;
	}

	public static class End {
		private String timezone;
		private String utc;
	}
}
