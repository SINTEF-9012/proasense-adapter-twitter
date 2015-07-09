/**
 * Copyright (C) 2014-2015 SINTEF
 *
 *     Brian Elves�ter <brian.elvesater@sintef.no>
 *     Shahzad Karamat <shazad.karamat@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.modelbased.proasense.adapter;

import eu.proasense.internal.ComplexValue;
import eu.proasense.internal.SimpleEvent;
import eu.proasense.internal.VariableType;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

// Make this a war project to be deployed in Tomcat
public class ProaSenseTwitterAdapter extends ProaSenseKafkaProducer {
    private Twitter twitter;
    private ProaSenseKafkaProducer producer;
    private Properties adapterProperties;
    private String sensorId;
    private String testPrint;

    public ProaSenseTwitterAdapter() {


        // Create the Kakfa producer
        producer = new ProaSenseKafkaProducer();

        //tok bort private fra loadAdapterProperties i klassen ProaSenseKafkaProducer for å gjenbruke metoden her.
        this.adapterProperties = loadAdapterProperties();

        String searchItem = adapterProperties.getProperty("proasense.adapter.twitter.hashtag");
        String timeInterval = adapterProperties.getProperty("proasense.adapter.time");

        String authConsumerKey = adapterProperties.getProperty("proasense.adapter.AuthConsumerKey");
        String consumerSecret = adapterProperties.getProperty("proasense.adapter.ConsumerSecret");
        String accessToken = adapterProperties.getProperty("proasense.adapter.AccessToken");
        String accessTokenSecret = adapterProperties.getProperty("proasense.adapter.AccessTokenSecret");

        testPrint = adapterProperties.getProperty("proasense.adapter.testPrint");

        if(testPrint.equals("1")) {
            // tester om jeg får riktige verdier ut.
            System.out.println(authConsumerKey);
            System.out.println(consumerSecret);
            System.out.println(accessToken);
            System.out.println(accessTokenSecret);

        }

        // Create Twitter connection
        twitter = createTwitterConnection(authConsumerKey, consumerSecret, accessToken, accessTokenSecret);


        senseTweets(searchItem, Integer.parseInt(timeInterval));
    }


    private void senseTweets(String searchItem, int timeInterval){

        while(true) {

            Query query = new Query(searchItem);

            try {
                QueryResult result = twitter.search(query);
                int cnt = 0;

                for (Status status : result.getTweets()) {
                    cnt++;

                    // Convert to simple event
                    SimpleEvent event = convertToSimpleEvent(status);

                    if(testPrint.equals("1")) System.out.println("SimpleEvent(" + cnt + "): " + event.toString());

                    // Publish simple event
                    this.producer.publishSimpleEvent(event);
                }
            } catch (TwitterException e) {
                System.out.println(e.getClass().getName() + ": " + e.getMessage());
            }

            try {
                TimeUnit.MINUTES.sleep(timeInterval); // setter av 2 min, nok tid til å laste ned verdier som er lagt inn fra test-klassen.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("\r");
        }

        // no need to close because the loop never breaks, code-line below will never be reached.
        //this.producer.close();

    }

    private Twitter createTwitterConnection(String authConsumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setUseSSL(true);
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("k9AVPhdChIpvnhTY0k4f7m9nn")
                .setOAuthConsumerSecret("flNGrS18usTrYLKgjNCvLzFMIEeWsoBuMurGhipyqzo2tmiLPs")
                .setOAuthAccessToken("3095414087-zibVqOi9rHpFoTqUsFryErb6JFQVxgRYv8ENvf4")
                .setOAuthAccessTokenSecret("yXNZnOlfgVthLQ7G5UIPw7VLQvb5ogeWBB3fGYTSnQ9vZ");

            /*    .setOAuthConsumerKey("authConsumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setOAuthAccessToken("accessToken")
                .setOAuthAccessTokenSecret("accessTokenSecret");*/

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        return twitter;
    }


    private SimpleEvent convertToSimpleEvent(Status status) {
        // Define complex value
        ComplexValue value = new ComplexValue();
        value.setValue(status.getText());
        value.setType(VariableType.STRING);

        // Define properties
        Map<String, ComplexValue> properties = new HashMap<String, ComplexValue>();
        properties.put("tweet", value);

        // Add new property for user

        // Define simple event
        SimpleEvent event = new SimpleEvent();
        event.setTimestamp(status.getCreatedAt().getTime());

        // Replace sensorid with sensorid property from adapter.properties file
        //event.setSensorId(new Long(status.getUser().getId()).toString());
        sensorId = adapterProperties.getProperty("proasense.adapter.SensorId");
        event.setSensorId(sensorId);
        event.setEventProperties(properties);

        return event;
    }


    public static void main(String[] args) throws TwitterException {
        ProaSenseTwitterAdapter adapter = new ProaSenseTwitterAdapter();
    }
}
