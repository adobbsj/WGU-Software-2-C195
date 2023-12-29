C195 performance assessment information/instructions

This is an application designed for the purpose of allowing customers to schedule appointments with various contacts within a company.

Contact Info:
Armondo Dobbs Jr. adobbsj@wgu.edu 11/11/22 ID:010111115

Build Info: IntelliJ Community 2021.1.3 x64
 
Project SDK: 17 java version "17.0.1"

JDK: Amazon Corretto 17.0.5 (IntelliJ will ask if you'd like to download it automatically in the event log. Then you will be able to apply it under the edit cofiguration tab)

JavaFX version: javafx-sdk-19 (Included in the .zip file. Must set PATH-TO-FX variable location to wherever you extract the lib file to in the settings under path variables)

Connector Driver: maven mysql-connector-java:8.0.22


How to run:

	In case these settings are not selected on startup.

	Inside the VM(used the jdk 17 link in the performance assessment) extract the c195pa.zip file to any location then open up Intellij.
	you will also need to extract the javafx-sdk-19 included in the project zip file.
	Open the extracted C195 project and once its all loaded there should be a red x on select build configuration tab.
	If you look at the event log it will ask if youd like to download the missing amazon corretto 17.0.5 configuration, select download automatically. once that is done you can select that in the build configuration.
	Under project structure -> project -> project sdk select: 17 java version "17.0.1"
	Under project structure -> project -> project language level select: X-experimental features
	Under project structure -> libraries -> add a lib folder and select the javafx-sdk-19/lib folder directory from wherever you extracted it.
	Under project structure -> libraries -> add maven mysql-connector-java:8.0.22
	Under settings -> Path Variables, click on PATH-TO-FX and edit the source destination folder to the same javafx-sdk-19/lib folder you selected for the libraries.

	Once that is complete, restart Intellij and you should be able to see the green play button and run the application like normal.

Custom Report:
	For the additional report I went with implementing a counter when a country is selected to count how many appointments are scheduled with customers in that certain country.
	Information like this would be useful for observing demographics and making business decisions.

