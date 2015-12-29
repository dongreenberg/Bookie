package com.binghamton.calendar.util;

import java.util.Calendar;

import com.google.gson.annotations.SerializedName;

/**
 * Class representing an Event
 */
public class Event {
	@SerializedName("Name")
	private String name;
	@SerializedName("EventNumber")
	private int eventNumber;
	@SerializedName("Location")
	private String location;
	@SerializedName("Days")
	private String days;
	@SerializedName("Description")
	private String description;
	@SerializedName("StartDate")
	private Calendar startDate;
	@SerializedName("EndDate")
	private Calendar endDate;
	@SerializedName("StartTime")
	private Calendar startTime;
	@SerializedName("EndTime")
	private Calendar endTime;
	
	/**
	 * Create an Event object
	 * @param name The name
	 * @param eventNumber The ID for the Event
	 * @param location The location
	 * @param days The days
	 * @param description The description
	 * @param startDate The Start Date
	 * @param endDate The End Date
	 * @param startTime The Start Time
	 * @param endTime The End Time
	 */
	public Event(String name, int eventNumber, String location, String days, String description,
			Calendar startDate, Calendar endDate, Calendar startTime,
			Calendar endTime) {
		this.name = name;
		this.eventNumber = eventNumber;
		this.location = location;
		this.days = days;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	/**
	 * Gets the name
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the event number
	 * @return the event number
	 */
	public int getEventNumber() {
		return eventNumber;
	}

	/**
	 * Sets the event number
	 * @param eventNumber the event number to set
	 */
	public void setEventNumber(int eventNumber) {
		this.eventNumber = eventNumber;
	}

	/**
	 * Gets the location
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Sets the location
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
	/**
	 * Gets the days
	 * @return the days
	 */
	public String getDays() {
		return days;
	}
	
	/**
	 * Sets the days
	 * @param days the days to set
	 */
	public void setDays(String days) {
		this.days = days;
	}
	
	/**
	 * Gets the description
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the start date
	 * @return the start date
	 */
	public Calendar getStartDate() {
		return startDate;
	}
	
	/**
	 * Sets the start date
	 * @param startDate the start date to set
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * Gets the end date
	 * @return the end date
	 */
	public Calendar getEndDate() {
		return endDate;
	}
	
	/**
	 * Sets the end date
	 * @param endDate the end date to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * Gets the start time
	 * @return the start time
	 */
	public Calendar getStartTime() {
		return startTime;
	}
	
	/**
	 * Sets the start time
	 * @param startTime the start time to set
	 */
	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * Gets the end time
	 * @return the end time
	 */
	public Calendar getEndTime() {
		return endTime;
	}

	/**
	 * Sets the end time
	 * @param endTime the end time to set
	 */
	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * Return the name of the Event for the
	 * adapters
	 */
	@Override
	public String toString() {
		return name;
	}
}