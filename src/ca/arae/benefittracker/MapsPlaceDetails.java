package ca.arae.benefittracker;
import java.io.Serializable;

import ca.arae.benefittracker.MapsPlace;

import com.google.api.client.util.Key;

    public class MapsPlaceDetails implements Serializable {
    	 
		private static final long serialVersionUID = 1L;

		@Key
        public String status;
     
        @Key
        public MapsPlace result;
     
        @Override
        public String toString() {
            if (result!=null) {
                return result.toString();
            }
            return super.toString();
        }
    }