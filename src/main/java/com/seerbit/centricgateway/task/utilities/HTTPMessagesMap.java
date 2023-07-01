package com.seerbit.centricgateway.task.utilities;

public enum HTTPMessagesMap {
	
	ERR_VALIDATION("Validation errors detected, kindly check your request"),
	ERR_JSON_REQUEST("JSON Request is Malformed."),
	ERR_JSON_RESPONSE("Oops, an Error Occurred, Please try again later."),
	ERR_MEDIA_TYPE_NOT_SUPPORTED("Media type not supported"),
	ERR_REQUIRED_FIELD("One or more required field(s) is absent."),
	ERR_METHOD_NOT_SUPPORTED("Method not Supported."),
	ERR_UNEQUAL("Unequal record."),
	ERR_INTERNAL_SERVER_ERROR("Oops, an Error Occurred, Please try again later."),
	SUCCESS("Successful.");
	

    private String message;

    HTTPMessagesMap(String message) {
        this.message = message;
    }
    
	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	
}