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
  * There is only one folder in this adapter, which is:
    net.modelbased.proasense.adapter.example.twitter

# User guide
  * cd to folder where the pom.xml is located.
  * use command "mvn clean install"
  * "mvn package"

# Test data
  There is no test-data for this adapter.

