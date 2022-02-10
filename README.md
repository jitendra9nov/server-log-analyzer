# Server Log Analyzer User Guild


## Build
Go to project directory and package it using below command
<br />
<br />
$ mvn clean package

## Run
Execute the below command 
<br /><br />
$ java -jar target/server-log-analyzer-1.0.0-SNAPSHOT.jar ["absolute-path of logfile.txt"]
<br /><br />
**Example**
<br />
java -jar /Users/jitendrabhadouriya/git/server-log-analyzer/target/server-log-analyzer-1.0.0-SNAPSHOT.jar "/Users/jitendrabhadouriya/git/server-log-analyzer/src/test/resources/logfile.txt"

## Database File/folder
1. data/logdb.log
2. data/logdb.properties
3. data/logdb.script
