package org.p1.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.DefaultMuleEvent;
import org.mule.DefaultMuleMessage;
import org.mule.api.DefaultMuleException;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.routing.AggregationContext;
import org.mule.routing.AggregationStrategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * Custom strategy for Scatter-Gather routing.
 * @author Matt Shen
 *
 */
public class RouteResultsAggregationStrategy implements AggregationStrategy {

    private transient final Log logger = LogFactory.getLog(RouteResultsAggregationStrategy.class);

    public static final String FLOW_VAR_ROUTE_NAME_KEY = "routeName";

    public static final String FLOW_VAR_ROUTE_SUCCESS_KEY = "success";
    
    public static final String FLOW_VAR_ROUTE_REASON_KEY = "reason";

    public RouteResultsAggregationStrategy() {
    }

    @Override
    public MuleEvent aggregate(AggregationContext context) throws MuleException {

	DefaultMuleMessage message = new DefaultMuleMessage(String.valueOf("aggregated routing result"), context
		.getOriginalEvent().getMuleContext());
	NavigableMap<Integer, Throwable> collectRouteExceptions = context.collectRouteExceptions();

	//log exception
	for (Entry<Integer, Throwable> entry : collectRouteExceptions.entrySet()) {
	    logger.error("failed to execute router#" + entry.getKey(), entry.getValue());
	}

	//generate a result summary
	List<MuleEvent> events = context.getEvents();
	List<HashMap<String, Object>> payload = new ArrayList<HashMap<String, Object>>();
	for (MuleEvent muleEvent : events) {
	    Object routeName = muleEvent.getFlowVariable(FLOW_VAR_ROUTE_NAME_KEY);
	    Object success = muleEvent.getFlowVariable(FLOW_VAR_ROUTE_SUCCESS_KEY);
	    boolean isSuccessful = (success == null ? false : (Boolean)success);
	    
	    HashMap<String, Object> result = new HashMap<String, Object>();
	    result.put(FLOW_VAR_ROUTE_NAME_KEY, routeName);
	    result.put(FLOW_VAR_ROUTE_SUCCESS_KEY, isSuccessful);
	    if (!isSuccessful) {
		Object reason = muleEvent.getFlowVariable(FLOW_VAR_ROUTE_REASON_KEY);
		if (reason!=null) {
		    result.put(FLOW_VAR_ROUTE_REASON_KEY, reason);
		}
	    }
	    payload.add(result);
	}
	
	//transform payload to json string
	try {
	    message.setPayload(new ObjectMapper().writeValueAsString(payload) );
	} catch (JsonProcessingException e) {
	    throw new DefaultMuleException("failed to convert result to json string", e);
	}
	
	return new DefaultMuleEvent(message, context.getOriginalEvent());

    }

}
