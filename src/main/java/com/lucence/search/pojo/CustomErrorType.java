package com.lucence.search.pojo;

public class CustomErrorType {

    private String errorMessage;
    private String code; 
 
    
    public CustomErrorType(String code,String errorMessage /*, HttpStatus status*/) {
    	super();
    	this.errorMessage = errorMessage;
    	this.code = code; 
    }
     
	public String getErrorMessage() {
        return errorMessage;
    }
    
    public String getCode() {
		return code;
	}
}
