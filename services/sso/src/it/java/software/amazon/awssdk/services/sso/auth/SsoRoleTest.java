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

package software.amazon.awssdk.services.sso.auth;

import org.junit.BeforeClass;
import org.junit.Test;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sso.SsoClient;
import software.amazon.awssdk.services.sso.model.GetRoleCredentialsRequest;
import software.amazon.awssdk.services.sso.model.GetRoleCredentialsResponse;
import software.amazon.awssdk.services.sso.model.RoleCredentials;
import software.amazon.awssdk.testutils.service.AwsTestBase;

public class SsoRoleTest extends AwsTestBase {

    protected static SsoClient sso;

    @BeforeClass
    public static void setUp() {
        sso = SsoClient.builder()
                       .credentialsProvider(CREDENTIALS_PROVIDER_CHAIN)
                       .region(Region.US_EAST_1)
                       .build();
    }

    @Test
    public void getRole(){
        GetRoleCredentialsRequest request = GetRoleCredentialsRequest.builder().build();
        GetRoleCredentialsResponse roleCredentials1 = sso.getRoleCredentials(request);
        RoleCredentials roleCredentials = roleCredentials1.roleCredentials();
    }


}
