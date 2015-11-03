package net.ddns.f1.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class FullName implements Serializable {
	private static final long serialVersionUID = 1L;

	public String forename;
	public String surname;

	public FullName() {
	}

	public FullName(final String fullNameSpaceSeparated) {
		final String[] names = fullNameSpaceSeparated.split(" ");
		this.forename = names[0];
		this.surname = names[1];
	}

	public FullName(final String forename, final String surname) {
		this.forename = forename;
		this.surname = surname;
	}

	@Override
	public String toString() {
		return forename + " " + surname;
	}

	@Override
	public boolean equals(final Object otherDriver) {
		if (otherDriver instanceof Driver) {
			if (this.toString().equalsIgnoreCase(
					((Driver) otherDriver).getName().toString())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
}
