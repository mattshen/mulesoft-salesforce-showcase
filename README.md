# mulesoft-salesforce-showcase
A non-trivial integration with Salesforce by Mulesoft

## Objective
The objective of the application is to integrate 3 existing systems(online order system, Salesforce and a very old system X), allowing prospective customer information to be sent from online order system to Salesforce and X. 

## Requirements
* Provide the capability to accept to a lead (a prospective customer) from existing online order system;
* Provide the capability to route a lead to Salesforce;
* Provide the capability to route a lead to X (output CSV file);
* Provide the extensibility to route a lead to other systems in the future;
* Provide the extensibility to accept a lead from another online order system in the future;
* The online order system should receive a response telling how other systems have received a lead;
* The lead receiving interface should provide authentication, so only authorised request can pass through;
* The lead receiving interface should provide basic validation logic to ensure the integrity of a lead, e.g. If company name is missing, respond immediately;
* Email address should be used external unique id in Salesforce

## Main Flow
* A new customer places an order at the online order system;
* The online system grabs the customer information and sends it to the integration application (a Mule Application);
* The integration application sends the customer information to Salesforce so as to create a new lead;
* The integration application sends the customer information to X (output a CSV file);
* The integration application returns a message telling if Step#3 and Step4 are successful


## Running Instructions

The application is a standard Mule Application which can be imported in Anypoint Studio.

If you are using JDK 7, please change compiler compliance level to 1.7 accordingly.
 
Before run, please check the configuration for Salesforce and system X CSV output directory in mule-app.properties. 

After launching is completed, APIKit Console would be open and it will ask you to login to access the REST API (HTTP Basic Authentication). The username is "user1" and password is "password1" (without quotation). 

In APIKit Console, you can post the the sample request and see the response.

## Unit Test
Both JUnit Test cases and MUnit Test cases are provided.

## Key Tools and Frameworks
* Anypoint Studio 5.4.0
* Mule ESB 3.7.3
* APIKit 1.7.3
* JDK 8
* Spring (core/beans/) 4.2.5.RELEASE
* Spring Security 4.0.4.RELEASE

## Solved Problems When Using Anypoint Studio

Missing jansi-64.dll when running munit test case in Anypoint Studio. 
Exception in thread "main" java.lang.UnsatisfiedLinkError: Could not load library. Reasons: [no jansi in java.library.path, C:\Windows\jansi-64.dll (Access is denied)]

Solution:
Extract jansi-64.dll to lib folder in the project, specify java.library.path to run MUnit




