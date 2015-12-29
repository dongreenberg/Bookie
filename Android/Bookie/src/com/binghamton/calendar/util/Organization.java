package com.binghamton.calendar.util;

/**
 * Class to represent an Organization
 */
public class Organization {
	private String name;
	private int orgNumber;
	private boolean subscribed;
	private String description;
	private String contact;
	private Event[] eventList;
	
	/**
	 * Create an Organization
	 * @param name The name
	 * @param orgNumber The organization ID
	 * @param subscribed true if the user is subscribed
	 * @param description The description
	 * @param contact The person to contact
	 * @param eventList The list of Events
	 */
	public Organization(String name, int orgNumber, boolean subscribed, String description, String contact, Event[] eventList) {
		this.name = name;
		this.orgNumber = orgNumber;
		this.subscribed = subscribed;
		this.description = description;
		this.contact = contact;
		this.eventList = eventList;
		
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
	 * Gets the organization number
	 * @return the organization number
	 */
	public int getOrgNumber() {
		return orgNumber;
	}

	/**
	 * Set the organization number
	 * @param orgNumber the organization number to set
	 */
	public void setOrgNumber(int orgNumber) {
		this.orgNumber = orgNumber;
	}

	/**
	 * Get the description
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the subscription status
	 * @return true if subscribed, false otherwise
	 */
	public boolean isSubscribed() {
		return subscribed;
	}

	/**
	 * Set the subscription status
	 * @param subscribed the value to set
	 */
	public void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}

	/**
	 * Set the description
	 * @param decription the description to set
	 */
	public void setDescription(String decription) {
		this.description = decription;
	}

	/**
	 * Get the contact
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * Set the contact
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}
	
	/**
	 * Get the event list
	 * @return the event list
	 */
	public Event[] getEventList() {
		return eventList;
	}

	/**
	 * Set the event list
	 * @param eventList the event list to set
	 */
	public void setEventList(Event[] eventList) {
		this.eventList = eventList;
	}

	/**
	 * Returns the name for the adapter
	 */
	@Override
	public String toString() {
		return name;
	}
}
