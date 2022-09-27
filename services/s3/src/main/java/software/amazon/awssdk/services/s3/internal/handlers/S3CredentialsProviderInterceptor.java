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

package software.amazon.awssdk.services.s3.internal.handlers;


import static software.amazon.awssdk.awscore.util.SignerOverrideUtils.overrideSignerIfNotOverridden;

import java.util.Optional;
import software.amazon.awssdk.annotations.SdkInternalApi;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.awscore.AwsRequest;
import software.amazon.awssdk.awscore.AwsRequestOverrideConfiguration;
import software.amazon.awssdk.core.SdkRequest;
import software.amazon.awssdk.core.interceptor.Context;
import software.amazon.awssdk.core.interceptor.ExecutionAttributes;
import software.amazon.awssdk.core.interceptor.ExecutionInterceptor;
import software.amazon.awssdk.core.interceptor.SdkInternalExecutionAttribute;
import software.amazon.awssdk.core.rules.model.Endpoint;
import software.amazon.awssdk.services.s3.internal.credentials.ExpressZonalCredentialsProvider;

@SdkInternalApi
public final class S3CredentialsProviderInterceptor implements ExecutionInterceptor {

    @Override
    public SdkRequest modifyRequest(Context.ModifyRequest context, ExecutionAttributes executionAttributes) {
        SdkRequest request = context.request();

        return credentialsOverride(executionAttributes)
            .map(credentialsProvider -> overrideRequestCredentialsProvider(credentialsProvider, request))
            .orElse(request);
    }

    private Optional<AwsCredentialsProvider> credentialsOverride(ExecutionAttributes executionAttributes) {

        Optional<Endpoint> attributeEndpoint =
            Optional.of(executionAttributes.getAttribute(SdkInternalExecutionAttribute.ENDPOINT));

        return attributeEndpoint.map(this::getS3CredentialsProvider)
                                .orElseThrow(() -> new IllegalStateException("Endpoint is missing"));
    }

    //Consider moving to a resolver and add access point / resource pattern
    //Goal: remove references to ExpressZonal in this class
    private Optional<AwsCredentialsProvider> getS3CredentialsProvider(Endpoint endpoint) {
        if (shouldUseS3SpecificCredentialsProvider(endpoint)) {
            return Optional.of(ExpressZonalCredentialsProvider.builder().build());
        }
        return Optional.empty();
    }

    private boolean shouldUseS3SpecificCredentialsProvider(Endpoint endpoint) {
        return true;
    }

    //Consider if we can make the override generic (also exists in SignerOverrideUtils)
    //Test the weird flatmap stuff
    private SdkRequest overrideRequestCredentialsProvider(AwsCredentialsProvider credentialsProvider,
                                                          SdkRequest request) {
        return request.overrideConfiguration()
                      .filter(c -> c instanceof AwsRequestOverrideConfiguration)
                      .map(c -> (AwsRequestOverrideConfiguration) c)
                      .flatMap(c -> c.credentialsProvider().map(provider -> request))
                      .orElseGet(() -> createNewRequest(request, credentialsProvider));

    }

    //could a better flow be to create the override config if missing and then add once?
    private SdkRequest createNewRequest(SdkRequest request, AwsCredentialsProvider credentialsProvider) {
        AwsRequest awsRequest = (AwsRequest) request;

        AwsRequestOverrideConfiguration modifiedOverride =
            awsRequest.overrideConfiguration()
                      .map(AwsRequestOverrideConfiguration::toBuilder)
                      .orElseGet(AwsRequestOverrideConfiguration::builder)
                      .credentialsProvider(credentialsProvider)
                      .build();

        return awsRequest.toBuilder()
                         .overrideConfiguration(modifiedOverride)
                         .build();
    }
}

// Optional<Endpoint> attributeEndpoint =
//     Optional.of(executionAttributes.getAttribute(SdkInternalExecutionAttribute.ENDPOINT));
// return attributeEndpoint.map(endpoint -> {
//     if (shouldUseS3SpecificCredentialsProvider(endpoint)) {
//         AwsCredentialsProvider credentialsProvider = getS3CredentialsProvider();
//         //modify request/executionAttributes
//     }
//     return request;
// }).orElseThrow(() -> new IllegalStateException("Endpoint is missing"));


