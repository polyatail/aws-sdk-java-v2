/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.services.s3.internal.cache;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import software.amazon.awssdk.core.endpointdiscovery.EndpointDiscoveryEndpoint;

public class DefaultLruCache implements LruCache {

    private final Map<String, EndpointDiscoveryEndpoint> cache = new ConcurrentHashMap<>();

    private final Deque<EndpointDiscoveryEndpoint> cache2 = new ConcurrentLinkedDeque<>();

    public void get() {
        //get from cache
    }

    public void evict() {
        //evict from cache
    }
}
