package org.p1.transformer;

import java.util.List;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import com.sforce.soap.partner.Error;
import com.sforce.soap.partner.UpsertResult;

/**
 * 
 * Transform Salesforce Upsert result into a customer object.
 * @author Matt Shen
 *
 */
public class SalesforceUpsertResultTransformer extends AbstractMessageTransformer {

    @Override
    public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

	@SuppressWarnings("unchecked")
	List<UpsertResult> results = (List<UpsertResult>) message.getPayload(); 
	
	SingUpsertResult singleResult = new SingUpsertResult();
	StringBuilder failedReason = new StringBuilder();
	
	for (UpsertResult result : results) {
	    //current upload lead API only support one lead at a time, so this loop only run once
	    if (!result.isSuccess()) {
		singleResult.setSuccess(false);
		Error[] errors = result.getErrors();
		for (Error error : errors) {
		    //append multiple error into one message
		    failedReason.append(error.getMessage()).append(";");
		}
	    }
	}
	singleResult.setFailedReason(failedReason.toString());
	
	return singleResult;
    }

}
