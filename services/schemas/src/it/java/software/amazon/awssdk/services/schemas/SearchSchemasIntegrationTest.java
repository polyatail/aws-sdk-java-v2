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
package software.amazon.awssdk.services.schemas;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.schemas.model.SearchSchemasResponse;
import software.amazon.awssdk.testutils.service.AwsIntegrationTestBase;

public class SearchSchemasIntegrationTest extends AwsIntegrationTestBase {

    private SchemasClient client;

    @Before
    public void setup() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void putGetAndDeletePublicAccessBlock_ValidAccount() {
        client = SchemasClient.builder()
                              .region(Region.US_EAST_1)
                              .httpClient(ApacheHttpClient.create())
                              .build();
        SearchSchemasResponse res = client.searchSchemas(r -> r.keywords("EC2").registryName("aws.events"));
        System.out.println(res.schemas().get(0).schemaVersions().get(0));
    }

}