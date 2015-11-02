package net.modelbased.proasense.adapter.twitter; /**
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

import eu.proasense.internal.ComplexValue;
import eu.proasense.internal.SimpleEvent;
import eu.proasense.internal.VariableType;

import twitter4j.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class MyTwitterAdapter extends AbstractTwitterAdapter {

    public MyTwitterAdapter() {

        // Get specific adapter properties
        String searchItem = adapterProperties.getProperty("proasense.adapter.twitter.hashtag");
        String timeInterval = adapterProperties.getProperty("proasense.adapter.twitter.time");

        // Search for hashtags
        senseTweets(searchItem, Integer.parseInt(timeInterval));
    }


    private void senseTweets(String searchItem, int timeInterval){
        while(true) {

            Query query = new Query(searchItem);
            try {
                QueryResult result = this.inputPort.search(query);

                int cnt = 0;
                for (Status status : result.getTweets()) {
                    cnt++;

                    SimpleEvent event = convertToSimpleEvent(status);

                    // Publish simple event
                    this.outputPort.publishSimpleEvent(event);
                }
            } catch (TwitterException e) {
                System.out.println(e.getClass().getName() + ": " + e.getMessage());
            }

            try {
                // Wait in minutes
                TimeUnit.MINUTES.sleep(timeInterval);
            } catch (InterruptedException e) {
                System.out.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }


    private SimpleEvent convertToSimpleEvent(Status status) {
        // Get timestamp from Tweet
        Long timestamp = status.getCreatedAt().getTime();

        // Define event properties
        Map<String, ComplexValue> eventProperties = new HashMap<String, ComplexValue>();
        ComplexValue value;

        // Get user id
        value = new ComplexValue();
        value.setValue(new Long(status.getUser().getId()).toString());
        value.setType(VariableType.LONG);
        eventProperties.put("userId", value);

        // Get tweet contents
        value = new ComplexValue();
        value.setValue(status.getText());
        value.setType(VariableType.STRING);
        eventProperties.put("tweet", value);

        // Define simple event
        SimpleEvent event = this.outputPort.createSimpleEvent(timestamp, eventProperties);
        System.out.println(event.toString());
        return event;
    }


    public static void main(String[] args) throws TwitterException {
        System.out.println("MyTwitterAdapter");
        MyTwitterAdapter adapter = new MyTwitterAdapter();
    }
}
