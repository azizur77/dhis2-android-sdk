/*
 * Copyright (c) 2017, University of Oslo
 *
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.android.core.indicator;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.gabrielittner.auto.value.cursor.ColumnAdapter;
import com.gabrielittner.auto.value.cursor.ColumnName;
import com.google.auto.value.AutoValue;

import org.hisp.dhis.android.core.common.BaseModel;
import org.hisp.dhis.android.core.common.BaseNameableObject;
import org.hisp.dhis.android.core.common.Model;
import org.hisp.dhis.android.core.common.ObjectWithUid;
import org.hisp.dhis.android.core.data.database.ObjectWithUidColumnAdapter;

@AutoValue
@JsonDeserialize(builder = AutoValue_Indicator.Builder.class)
public abstract class Indicator extends BaseNameableObject implements Model {

    // TODO move to base class after whole object refactor
    @Override
    @Nullable
    @ColumnName(BaseModel.Columns.ID)
    @JsonIgnore()
    public abstract Long id();

    @Nullable
    @JsonProperty()
    abstract Boolean annualized();

    @Nullable
    @JsonProperty()
    @ColumnAdapter(ObjectWithUidColumnAdapter.class)
    public abstract ObjectWithUid indicatorType();

    @Nullable
    @JsonProperty()
    abstract String numerator();

    @Nullable
    @JsonProperty()
    abstract String numeratorDescription();

    @Nullable
    @JsonProperty()
    abstract String denominator();

    @Nullable
    @JsonProperty()
    abstract String denominatorDescription();

    @Nullable
    @JsonProperty()
    abstract String url();

    public static Builder builder() {
        return new $$AutoValue_Indicator.Builder();
    }

    static Indicator create(Cursor cursor) {
        return $AutoValue_Indicator.createFromCursor(cursor);
    }

    public abstract ContentValues toContentValues();

    public abstract Builder toBuilder();

    @AutoValue.Builder
    @JsonPOJOBuilder(withPrefix = "")
    public static abstract class Builder extends BaseNameableObject.Builder<Builder> {
        public abstract Builder id(Long id);

        public abstract Builder annualized(Boolean annualized);

        public abstract Builder indicatorType(ObjectWithUid indicatorType);

        public abstract Builder numerator(String numerator);

        public abstract Builder numeratorDescription(String numeratorDescription);

        public abstract Builder denominator(String denominator);

        public abstract Builder denominatorDescription(String denominatorDescription);

        public abstract Builder url(String url);

        public abstract Indicator build();
    }
}