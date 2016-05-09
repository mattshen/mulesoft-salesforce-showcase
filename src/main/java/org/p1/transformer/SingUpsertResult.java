package org.p1.transformer;

import java.io.Serializable;

/**
 * 
 * Custom object for Salesforce Lead Upsert result.
 * @author Matt Shen
 *
 */
public class SingUpsertResult implements Serializable {

    private static final long serialVersionUID = -6947819857234915915L;
    
    private boolean success = true;
    
    private String failedReason = "";
    
    public SingUpsertResult() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFailedReason() {
        return failedReason;
    }

    public void setFailedReason(String failedReason) {
        this.failedReason = failedReason;
    }
    
}
