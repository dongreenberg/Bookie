package com.binghamton.calendar.util;

/**
 * Class to represent a semester
 */
public class Term implements Comparable<Term> {
	private String termID;

	/**
	 * Create a Term
	 * @param termID the ID of the Term
	 */
	public Term(String termID) {
		this.termID = termID;
	}
	
	/**
	 * Gets the term ID
	 * @return the term ID
	 */
	public String getTermID() {
		return termID;
	}

	/**
	 * Sets the term ID
	 * @param termID the term ID to set
	 */
	public void setTermID(String termID) {
		this.termID = termID;
	}

	/**
	 * Converts the termID to a more readable String
	 * which it represents
	 */
	@Override
	public String toString() {
		int delim = termID.length() - 2;
		String year = termID.substring(0, delim);
		String termCode = termID.substring(delim);
		String semester = "";
		if (termCode.equals("20")) {
			semester = "Spring";
		}
		else if (termCode.equals("90")) {
			semester = "Fall";
		}
		else if (termCode.equals("10")) {
			semester = "Winter";
		}
		else if (termCode.equals("60")) {
			semester = "Summer";
		}
		return semester + " " + year;
	}

	/**
	 * Sort the ID's in reverse order for most
	 * recent semester to show up first
	 */
	@Override
	public int compareTo(Term another) {
		Term anotherTerm = (Term)another;
		return (this.termID.compareTo(anotherTerm.termID)) * -1;
	}
}
