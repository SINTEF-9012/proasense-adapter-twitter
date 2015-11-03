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
import net.modelbased.proasense.adapter.base.AbstractBaseAdapter;


public abstract class AbstractTwitterAdapter extends AbstractBaseAdapter {
    protected TwitterConsumerInput inputPort;

    public AbstractTwitterAdapter() {
        // Twitter input port properties
        String authConsumerKey = adapterProperties.getProperty("proasense.adapter.twitter.AuthConsumerKey");
        String consumerSecret = adapterProperties.getProperty("proasense.adapter.twitter.ConsumerSecret");
        String accessToken = adapterProperties.getProperty("proasense.adapter.twitter.AccessToken");
        String accessTokenSecret = adapterProperties.getProperty("proasense.adapter.twitter.AccessTokenSecret");

        this.inputPort = new TwitterConsumerInput(authConsumerKey, consumerSecret, accessToken, accessTokenSecret);
    }

}
