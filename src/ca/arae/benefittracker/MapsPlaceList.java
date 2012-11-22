package ca.arae.benefittracker;

import java.io.Serializable;
import java.util.List;
 
import com.google.api.client.util.Key;

public class MapsPlaceList implements Serializable {

	private static final long serialVersionUID = 1L;

	@Key
    public String status;
 
    @Key
    public List<MapsPlace> results;
 
}