# Sensing Architecture - Twitter adapter
  Twitter adapter for the Sensing Architecture developed in the ProaSense project.

# Requirements
  * Java 1.7 or above
  * Maven
  
  # Requirements for twitter account:
  * Auth consumer key
  * Consumer secret
  * Access token
  * Access token secret
  * Information on how to obtain these tokens can be accessed from this site: 
    https://dev.twitter.com/oauth/overview/faq

# Parameters of the property file
  * proasense.adapter.base.publish: values for this parameter are, true for publishing the sompleEvents og false 
    for not publishing those events.
  * proasense.adapter.twitter.hashtag: word user want to search by, example #Norway.
  * proasense.adapter.twitter.time: time-interval for scanning tweets.
  
# Setup
  After aquiring information above, nothing more is needed for setup.

# Folder structure
  * There are two folder in this adapter, those are:
    twitter-base: Abstract class to support running the program in next module.
    twitter-example: Contains the code for the program.

# User guide
  * cd to proasense-adapter-twitter
  * use command "mvn clean install"
  * "mvn exec:java"
  *The program will search for the hashtag defined in the property file and make continues poll
   on interval defined in the property file.

# Test data
  There is no test-data for this adapter.

