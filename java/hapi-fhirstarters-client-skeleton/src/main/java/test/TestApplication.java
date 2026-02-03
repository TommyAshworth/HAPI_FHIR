package test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;

import org.hl7.fhir.instance.model.api.IIdType;
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
      System.out.println("Found " + results.getEntry().size() + " patients named 'smith'");
      
      //output finds 10 patients with name of smith
      
      //Print the resource ID for each matching resource
      //for loop outputs resource ID's for each member named smith in our Bundle resource
      for (Bundle.BundleEntryComponent entry : results.getEntry()) {
    	  Patient p = (Patient) entry.getResource();
    	  System.out.println("ID: " + p.getIdElement().getIdPart());
      }
      
      
      // Use a FHIR Create operation to create a new Patient resource on the server. Give it your name, or a fictional name you make up.
      
      Patient patient1 = new Patient();
      //populate the patient object
      patient1.addIdentifier().setSystem("urn:system").setValue("12345");
      patient1.addName().setFamily("Ash").addGiven("Tom");
      
      /*
       * Invoke the server create method (and sent pretty printed JSON
       * encoding to the server
       * instead of the default which is non-pretty printed XML)
       */
      
      MethodOutcome outcome = client.create()
    		  .resource(patient1)
    		  .prettyPrint()
    		  .encodedJson()
    		  .execute();
      
      /*
       * Methodoutcome object will contain information about the response from the server
       * including the ID of the created resource, the operationoutcome response, etc
      */
      
      IIdType id = outcome.getId();
      System.out.println("Got ID: " + id.getValue());
      //output shows new patient ID created
     
      
      
   }

}
