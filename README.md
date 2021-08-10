# as24-reporter

## run the code in a docker container
- start container: 
  - `docker run -it -p 9000:9000 -v ${PWD}:/root --rm hseeberger/scala-sbt:8u222_1.3.5_2.13.1`
- in the container:
    - `sbt compile`
    - `sbt test`
    - `sbt run`
  
# structure
The is based on the scala play framework.
It has one controller `HomeController` that parses static CSV files to the models, builds reports and shows them in the index view
Two services were implemented for this purpose:
- Parser: parses the csv files to a list of Listings
- Reporter: generates different reports from a list of Listings
  
## Achievements and notes
Scala and play are new to me and had to learn how to use it. The following achievements is what I could complete in 5 hours. Some TODOs were left in the code pointing out to missing features/tasks.

Following requirements were implemented:
- Almost all reports with display as basic html tables
- Automated tests for the given data

Missing requirements/potential improvements:
- Code comments
- Calculate average price of the 30% most contacted listings. Right now the software calculates the average of all listings
- Error handling and logging
- Upload CSV functionality
- Controller and endpoint naming
- Create a proper docker image that runs tests automatically and starts the application

