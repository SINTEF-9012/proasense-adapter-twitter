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
package net.modelbased.proasense.adapter.twitter;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterConsumerInput {
    protected Twitter twitter;

    public TwitterConsumerInput(String authConsumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        // Create Twitter connection
        twitter = createTwitterConnection(authConsumerKey, consumerSecret, accessToken, accessTokenSecret);
    }


    private Twitter createTwitterConnection(String authConsumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setUseSSL(true);
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(authConsumerKey)
            .setOAuthConsumerSecret(consumerSecret)
            .setOAuthAccessToken(accessToken)
            .setOAuthAccessTokenSecret(accessTokenSecret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        return twitter;
    }


    public QueryResult search(Query query) throws TwitterException{
        QueryResult result = null;

        try {
            result = this.twitter.search(query);
        } catch (TwitterException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return result;
    }

}
