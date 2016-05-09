package org.p1.transformer;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.context.DefaultMuleContextBuilder;

import com.sforce.soap.partner.Error;
import com.sforce.soap.partner.UpsertResult;

/**
 * 
 * Unit test for Java transformer org.p1.transformer.SalesforceUpsertResultTransformer.
 * 
 * @author Matt Shen
 *
 */
public class SalesforceUpsertResultTransformerTest {

    private SalesforceUpsertResultTransformer target = null;
    
    @Before
    public void setUp() {
	target = new SalesforceUpsertResultTransformer();
    }
    
    @Test
    public void testFailedUpsertResult() throws TransformerException {
	UpsertResult result = new UpsertResult();
	result.setCreated(false);
	result.setSuccess(false);
	
	Error error = new Error();
	error.setFields(new String[]{"company"});
	error.setMessage("company is missing");
	
	result.setErrors(new Error[]{error});

	MuleContext ctx = new DefaultMuleContextBuilder().buildMuleContext();
	MuleMessage message = new DefaultMuleMessage("unit testing", ctx);
	
	message.setPayload(Arrays.asList(result));
	
	SingUpsertResult transformedMessage = (SingUpsertResult)target.transformMessage(message, null);
	
	System.out.println(transformedMessage.getFailedReason());
	
	Assert.assertFalse(transformedMessage.isSuccess());
	Assert.assertEquals("company is missing;", transformedMessage.getFailedReason());
	
    }
    
    @Test
    public void testSuccessUpsertResult() throws TransformerException {
	UpsertResult result = new UpsertResult();
	result.setCreated(true);
	result.setSuccess(true);
	result.setErrors(new Error[]{});

	MuleContext ctx = new DefaultMuleContextBuilder().buildMuleContext();
	MuleMessage message = new DefaultMuleMessage("unit testing", ctx);
	
	message.setPayload(Arrays.asList(result));
	
	SingUpsertResult transformedMessage = (SingUpsertResult)target.transformMessage(message, null);
	
	System.out.println(transformedMessage.getFailedReason());
	
	Assert.assertTrue(transformedMessage.isSuccess());
	
    }
    
}
