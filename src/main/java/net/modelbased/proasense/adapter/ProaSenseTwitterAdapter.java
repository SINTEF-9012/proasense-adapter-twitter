/**
 * Copyright (C) 2014-2015 SINTEF
 *
 *     Brian Elvesæter <brian.elvesater@sintef.no>
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
import java.util.Scanner;

// Make this a war project to be deployed in Tomcat
public class ProaSenseTwitterAdapter extends ProaSenseKafkaProducer {
    private Twitter twitter;
    private ProaSenseKafkaProducer producer;

    public ProaSenseTwitterAdapter() {
        // Create the Kakfa producer
        producer = new ProaSenseKafkaProducer();

        // Create Twitter connection
        twitter = createTwitterConnection();

        // Search for #hashtag
        // Replace this code with hashtag from adapter.properties file
        System.out.println("Write the search-word.");
        Scanner sc = new Scanner(System.in);
        String search = sc.next();

        Query query = new Query(search);

        try {
            QueryResult result = twitter.search(query);
            int cnt = 0;

            for (Status status : result.getTweets()) {
                cnt++;

                // Convert to simple event
                SimpleEvent event = convertToSimpleEvent(status);
                System.out.println("SimpleEvent(" + cnt + "): " + event.toString());

                // Publish simple event
                this.producer.publishSimpleEvent(event);
            }
        }
        catch (TwitterException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
        finally {
            this.producer.close();
        }
    }


    private Twitter createTwitterConnection() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setUseSSL(true);
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("k9AVPhdChIpvnhTY0k4f7m9nn")
                .setOAuthConsumerSecret("flNGrS18usTrYLKgjNCvLzFMIEeWsoBuMurGhipyqzo2tmiLPs")
                .setOAuthAccessToken("3095414087-zibVqOi9rHpFoTqUsFryErb6JFQVxgRYv8ENvf4")
                .setOAuthAccessTokenSecret("yXNZnOlfgVthLQ7G5UIPw7VLQvb5ogeWBB3fGYTSnQ9vZ");

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
        event.setSensorId(new Long(status.getUser().getId()).toString());
        event.setEventProperties(properties);

        return event;
    }


    public static void main(String[] args) throws TwitterException {
        ProaSenseTwitterAdapter adapter = new ProaSenseTwitterAdapter();
    }
}
