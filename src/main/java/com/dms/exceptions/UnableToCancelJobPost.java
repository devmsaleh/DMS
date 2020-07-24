package com.dms.exceptions;

public class UnableToCancelJobPost extends Exception {

	public UnableToCancelJobPost() {
		super("Unable to cancel job post because it has a started contract");
	}

	public UnableToCancelJobPost(String message) {
		super(message);
	}

}
