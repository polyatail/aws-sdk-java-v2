/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 * 
 * http://aws.amazon.com/apache2.0
 * 
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package software.amazon.awssdk.services.codecatalyst.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import software.amazon.awssdk.annotations.Generated;
import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
import software.amazon.awssdk.core.traits.LocationTrait;
import software.amazon.awssdk.core.traits.TimestampFormatTrait;
import software.amazon.awssdk.utils.ToString;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * <p>
 * Information about a specified personal access token (PAT).
 * </p>
 */
@Generated("software.amazon.awssdk:codegen")
public final class AccessTokenSummary implements SdkPojo, Serializable,
        ToCopyableBuilder<AccessTokenSummary.Builder, AccessTokenSummary> {
    private static final SdkField<String> ID_FIELD = SdkField.<String> builder(MarshallingType.STRING).memberName("id")
            .getter(getter(AccessTokenSummary::id)).setter(setter(Builder::id))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("id").build()).build();

    private static final SdkField<String> NAME_FIELD = SdkField.<String> builder(MarshallingType.STRING).memberName("name")
            .getter(getter(AccessTokenSummary::name)).setter(setter(Builder::name))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("name").build()).build();

    private static final SdkField<Instant> EXPIRES_TIME_FIELD = SdkField
            .<Instant> builder(MarshallingType.INSTANT)
            .memberName("expiresTime")
            .getter(getter(AccessTokenSummary::expiresTime))
            .setter(setter(Builder::expiresTime))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("expiresTime").build(),
                    TimestampFormatTrait.create(TimestampFormatTrait.Format.ISO_8601)).build();

    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(ID_FIELD, NAME_FIELD,
            EXPIRES_TIME_FIELD));

    private static final long serialVersionUID = 1L;

    private final String id;

    private final String name;

    private final Instant expiresTime;

    private AccessTokenSummary(BuilderImpl builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.expiresTime = builder.expiresTime;
    }

    /**
     * <p>
     * The system-generated ID of the personal access token.
     * </p>
     * 
     * @return The system-generated ID of the personal access token.
     */
    public final String id() {
        return id;
    }

    /**
     * <p>
     * The friendly name of the personal access token.
     * </p>
     * 
     * @return The friendly name of the personal access token.
     */
    public final String name() {
        return name;
    }

    /**
     * <p>
     * The date and time when the personal access token will expire, in coordinated universal time (UTC) timestamp
     * format as specified in <a href="https://www.rfc-editor.org/rfc/rfc3339#section-5.6">RFC 3339</a>.
     * </p>
     * 
     * @return The date and time when the personal access token will expire, in coordinated universal time (UTC)
     *         timestamp format as specified in <a href="https://www.rfc-editor.org/rfc/rfc3339#section-5.6">RFC
     *         3339</a>.
     */
    public final Instant expiresTime() {
        return expiresTime;
    }

    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this);
    }

    public static Builder builder() {
        return new BuilderImpl();
    }

    public static Class<? extends Builder> serializableBuilderClass() {
        return BuilderImpl.class;
    }

    @Override
    public final int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + Objects.hashCode(id());
        hashCode = 31 * hashCode + Objects.hashCode(name());
        hashCode = 31 * hashCode + Objects.hashCode(expiresTime());
        return hashCode;
    }

    @Override
    public final boolean equals(Object obj) {
        return equalsBySdkFields(obj);
    }

    @Override
    public final boolean equalsBySdkFields(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AccessTokenSummary)) {
            return false;
        }
        AccessTokenSummary other = (AccessTokenSummary) obj;
        return Objects.equals(id(), other.id()) && Objects.equals(name(), other.name())
                && Objects.equals(expiresTime(), other.expiresTime());
    }

    /**
     * Returns a string representation of this object. This is useful for testing and debugging. Sensitive data will be
     * redacted from this string using a placeholder value.
     */
    @Override
    public final String toString() {
        return ToString.builder("AccessTokenSummary").add("Id", id()).add("Name", name()).add("ExpiresTime", expiresTime())
                .build();
    }

    public final <T> Optional<T> getValueForField(String fieldName, Class<T> clazz) {
        switch (fieldName) {
        case "id":
            return Optional.ofNullable(clazz.cast(id()));
        case "name":
            return Optional.ofNullable(clazz.cast(name()));
        case "expiresTime":
            return Optional.ofNullable(clazz.cast(expiresTime()));
        default:
            return Optional.empty();
        }
    }

    @Override
    public final List<SdkField<?>> sdkFields() {
        return SDK_FIELDS;
    }

    private static <T> Function<Object, T> getter(Function<AccessTokenSummary, T> g) {
        return obj -> g.apply((AccessTokenSummary) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public interface Builder extends SdkPojo, CopyableBuilder<Builder, AccessTokenSummary> {
        /**
         * <p>
         * The system-generated ID of the personal access token.
         * </p>
         * 
         * @param id
         *        The system-generated ID of the personal access token.
         * @return Returns a reference to this object so that method calls can be chained together.
         */
        Builder id(String id);

        /**
         * <p>
         * The friendly name of the personal access token.
         * </p>
         * 
         * @param name
         *        The friendly name of the personal access token.
         * @return Returns a reference to this object so that method calls can be chained together.
         */
        Builder name(String name);

        /**
         * <p>
         * The date and time when the personal access token will expire, in coordinated universal time (UTC) timestamp
         * format as specified in <a href="https://www.rfc-editor.org/rfc/rfc3339#section-5.6">RFC 3339</a>.
         * </p>
         * 
         * @param expiresTime
         *        The date and time when the personal access token will expire, in coordinated universal time (UTC)
         *        timestamp format as specified in <a href="https://www.rfc-editor.org/rfc/rfc3339#section-5.6">RFC
         *        3339</a>.
         * @return Returns a reference to this object so that method calls can be chained together.
         */
        Builder expiresTime(Instant expiresTime);
    }

    static final class BuilderImpl implements Builder {
        private String id;

        private String name;

        private Instant expiresTime;

        private BuilderImpl() {
        }

        private BuilderImpl(AccessTokenSummary model) {
            id(model.id);
            name(model.name);
            expiresTime(model.expiresTime);
        }

        public final String getId() {
            return id;
        }

        public final void setId(String id) {
            this.id = id;
        }

        @Override
        public final Builder id(String id) {
            this.id = id;
            return this;
        }

        public final String getName() {
            return name;
        }

        public final void setName(String name) {
            this.name = name;
        }

        @Override
        public final Builder name(String name) {
            this.name = name;
            return this;
        }

        public final Instant getExpiresTime() {
            return expiresTime;
        }

        public final void setExpiresTime(Instant expiresTime) {
            this.expiresTime = expiresTime;
        }

        @Override
        public final Builder expiresTime(Instant expiresTime) {
            this.expiresTime = expiresTime;
            return this;
        }

        @Override
        public AccessTokenSummary build() {
            return new AccessTokenSummary(this);
        }

        @Override
        public List<SdkField<?>> sdkFields() {
            return SDK_FIELDS;
        }
    }
}
