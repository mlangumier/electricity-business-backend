package fr.hb.mlang.electricitybusiness.shared.exceptions;

public class ErrorResponse {

  private int statusCode;
  private String error;
  private String message;

  public ErrorResponse(int statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
