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

package software.amazon.awssdk.services.s3.internal.credentials;

import java.time.Duration;
import java.time.Instant;
import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.utils.SdkAutoCloseable;
import software.amazon.awssdk.utils.ToString;
import software.amazon.awssdk.utils.Validate;
import software.amazon.awssdk.utils.cache.CachedSupplier;
import software.amazon.awssdk.utils.cache.NonBlocking;
import software.amazon.awssdk.utils.cache.RefreshResult;

/**
 * An implementation of {@link AwsCredentialsProvider} that returns a set implementation of {@link AwsCredentials}.
 */
@SdkPublicApi
public final class ExpressZonalCredentialsProvider implements AwsCredentialsProvider, SdkAutoCloseable {

    private static final Duration DEFAULT_STALE_TIME = Duration.ofMinutes(1);
    private static final Duration DEFAULT_PREFETCH_TIME = Duration.ofMinutes(5);


    private static final Duration staleTime = DEFAULT_STALE_TIME;
    private static final Duration prefetchTime = DEFAULT_PREFETCH_TIME;

    /**
     * The S3 client that should be used for periodically updating the session credentials.
     */
    private final S3Client s3Client;

    /**
     * The session cache that handles automatically updating the credentials when they get close to expiring.
     */
    private final CachedSupplier<ZonalCredentialsHolder> sessionCache;

    private final Boolean asyncCredentialUpdateEnabled;

    private ExpressZonalCredentialsProvider(Builder builder) {
        this.s3Client = Validate.notNull(builder.s3Client, "S3 client must not be null.");  //default?
        this.asyncCredentialUpdateEnabled = builder.asyncCredentialUpdateEnabled;

        //cache
        CachedSupplier.Builder<ZonalCredentialsHolder> cacheBuilder = CachedSupplier.builder(this::updateSessionCredentials);
        if (builder.asyncCredentialUpdateEnabled) {
            cacheBuilder.prefetchStrategy(new NonBlocking("express-zonal-credentials-provider"));
        }
        this.sessionCache = cacheBuilder.build();
    }

    /**
     * Update the expiring session credentials by calling STS. Invoked by {@link CachedSupplier} when the credentials
     * are close to expiring.
     */
    private RefreshResult<ZonalCredentialsHolder> updateSessionCredentials() {
     //   ZonalCredentialsHolder credentials = new ZonalCredentialsHolder(getUpdatedCredentials(s3Client));
        ZonalCredentialsHolder credentials = getStaticCredentials();
        Instant actualTokenExpiration = credentials.getSessionCredentialsExpiration();

        return RefreshResult.builder(credentials)
                            .staleTime(actualTokenExpiration.minus(staleTime))
                            .prefetchTime(actualTokenExpiration.minus(prefetchTime))
                            .build();
    }

    /**
     * temporary
     */
    private ZonalCredentialsHolder getStaticCredentials() {
        return new ZonalCredentialsHolder(AwsSessionCredentials.create("key", "secret", "session"), Instant.now());
    }

    /**
     * Depends on having a model
     */
    // private SessionCredentials getUpdatedCredentials(S3Client s3Client) {
    //     // CreateSessionRequest request = requestSupplier.get();
    //     // Validate.notNull(request, "Create session request must not be null.");
    //     // return s3Client.createSession(request).credentials();
    // }

    @Override
    public AwsCredentials resolveCredentials() {
        return sessionCache.get().getSessionCredentials();
    }

    @Override
    public String toString() {
        return ToString.builder("StsAssumeRoleCredentialsProvider")
                  //     .add("refreshRequest", assumeRoleRequestSupplier)
                       .build();
    }

    @Override
    public void close() {
        sessionCache.close();
    }

    public static final class Builder {

        private S3Client s3Client;
        private Boolean asyncCredentialUpdateEnabled = false;

        Builder() {

        }

        /**
         * Configure the {@link S3Client} to use when calling S3 to update the session. This client should not be shut
         * down as long as this credentials provider is in use.
         *
         * @param s3Client The S3 client to use for communication with S3.
         * @return This object for chained calls.
         */
        public Builder s3Client(S3Client s3Client) {
            this.s3Client = s3Client;
            return this;
        }

        public Builder asyncCredentialUpdateEnabled(Boolean asyncCredentialUpdateEnabled) {
            this.asyncCredentialUpdateEnabled = asyncCredentialUpdateEnabled;
            return this;
        }


        // /**
        //  * Similar to {@link #refreshRequest(AssumeRoleRequest)}, but takes a lambda to configure a new
        //  * {@link AssumeRoleRequest.Builder}. This removes the need to called {@link AssumeRoleRequest#builder()} and
        //  * {@link AssumeRoleRequest.Builder#build()}.
        //  */
        // public Builder refreshRequest(Consumer<AssumeRoleRequest.Builder> assumeRoleRequest) {
        //     return refreshRequest(AssumeRoleRequest.builder().applyMutation(assumeRoleRequest).build());
        // }

        public ExpressZonalCredentialsProvider build() {
            return new ExpressZonalCredentialsProvider(this);
        }
    }
}
