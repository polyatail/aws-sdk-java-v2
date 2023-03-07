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

package software.amazon.awssdk.auth.credentials;

import software.amazon.awssdk.annotations.SdkProtectedApi;
import software.amazon.awssdk.identity.spi.AwsCredentialsIdentity;
import software.amazon.awssdk.identity.spi.AwsSessionCredentialsIdentity;
import software.amazon.awssdk.identity.spi.IdentityProvider;

@SdkProtectedApi
public final class CredentialUtils {

    private CredentialUtils() {
    }

    /**
     * Determine whether the provided credentials are anonymous credentials, indicating that the customer is not attempting to
     * authenticate themselves.
     */
    public static boolean isAnonymous(AwsCredentials credentials) {
        return credentials.secretAccessKey() == null && credentials.accessKeyId() == null;
    }

    /**
     * TODO: Add javadoc
     */
    public static AwsCredentialsProvider convert(IdentityProvider<? extends AwsCredentialsIdentity> identityProvider) {
        return () -> {
            // TODO: Exception handling for CompletionException thrown from join?
            AwsCredentialsIdentity awsCredentialsIdentity = identityProvider.resolveIdentity().join();
            return convert(awsCredentialsIdentity);
        };
    }

    /**
     * TODO: Add javadoc
     */
    public static AwsCredentials convert(AwsCredentialsIdentity awsCredentialsIdentity) {
        // TODO: Is below safe? What if customer defines there own sub-type of AwsCredentialsIdentity?! Valid use case?
        //       If sub-type defines new properties, this conversion would be lossy. But does it matter, if no other code in
        //       `core` module care about types other than these 2?
        // identity-spi defines 2 known types - AwsCredentialsIdentity and a sub-type AwsSessionCredentialsIdentity
        if (awsCredentialsIdentity instanceof AwsSessionCredentialsIdentity) {
            AwsSessionCredentialsIdentity awsSessionCredentialsIdentity = (AwsSessionCredentialsIdentity) awsCredentialsIdentity;
            return AwsSessionCredentials.create(awsSessionCredentialsIdentity.accessKeyId(),
                                                awsSessionCredentialsIdentity.secretAccessKey(),
                                                awsSessionCredentialsIdentity.sessionToken());
        }
        return AwsBasicCredentials.create(awsCredentialsIdentity.accessKeyId(),
                                          awsCredentialsIdentity.secretAccessKey());
    }
}
