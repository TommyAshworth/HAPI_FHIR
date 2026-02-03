package test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

public class TestApplication {

   /**
    * This is the Java main method, which gets executed
    */
   public static void main(String[] args) {

      // Create a context
      FhirContext ctx = FhirContext.forR4();

      // Create a client
      IGenericClient client = ctx.newRestfulGenericClient("https://server.fire.ly/r4");

      // Read a patient with the given ID
      Patient patient = client.read()
    		    .resource(Patient.class)
    		    .withId("example")
    		    .execute();
      // Print the output
      String string = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
      System.out.println(string);
      
      //Perform a fhir search for patient resources where the patient has a name of "Smith"
      //Performing a bundle search for any resources with name of smith
      
      Bundle results = client.search()
    		  .forResource(Patient.class)
    		  .where(Patient.FAMILY.matches().value("smith"))
    		  .returnBundle(Bundle.class)
    		  .execute();
      System.out.println("Found " + results.getEntry().size() + " patients named 'duck'");
   }

}
