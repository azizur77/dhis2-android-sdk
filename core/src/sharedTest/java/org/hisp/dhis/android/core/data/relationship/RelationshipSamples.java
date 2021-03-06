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

package org.hisp.dhis.android.core.data.relationship;

import org.hisp.dhis.android.core.relationship.Relationship;
import org.hisp.dhis.android.core.relationship.Relationship229Compatible;
import org.hisp.dhis.android.core.relationship.RelationshipHelper;
import org.hisp.dhis.android.core.relationship.RelationshipItem;

import static org.hisp.dhis.android.core.data.utils.FillPropertiesTestUtils.CREATED;
import static org.hisp.dhis.android.core.data.utils.FillPropertiesTestUtils.LAST_UPDATED;
import static org.hisp.dhis.android.core.data.utils.FillPropertiesTestUtils.NAME;


public class RelationshipSamples {

    protected String UID = "uid";

    protected String FROM_UID = "fromUid";

    protected String TO_UID = "toUid";

    protected String TYPE = "type";

    protected RelationshipItem fromItem = RelationshipHelper.teiItem(FROM_UID);

    protected RelationshipItem toItem = RelationshipHelper.teiItem(TO_UID);

    protected RelationshipItem eventItem = RelationshipHelper.eventItem(TO_UID);

    private Relationship229Compatible.Builder commonCompatibleBuilder = Relationship229Compatible
            .builder()
            .created(CREATED)
            .lastUpdated(LAST_UPDATED)
            .name(NAME);

    protected Relationship.Builder commonBuilder = Relationship
            .builder()
            .created(CREATED)
            .lastUpdated(LAST_UPDATED)
            .name(NAME);

    public Relationship229Compatible get229Compatible() {
        return commonCompatibleBuilder
                .uid(TYPE)
                .trackedEntityInstanceA(FROM_UID)
                .trackedEntityInstanceB(TO_UID)
                .build();
    }

    public Relationship229Compatible get230Compatible() {
        return commonCompatibleBuilder
                .uid(UID)
                .relationshipType(TYPE)
                .from(fromItem)
                .to(toItem)
                .build();
    }

    protected Relationship get230() {
        return commonBuilder
                .uid(UID)
                .relationshipType(TYPE)
                .from(fromItem)
                .to(toItem)
                .build();
    }

    protected Relationship get230(String uid, String fromUid, String toUid) {
        return commonBuilder
                .uid(uid)
                .relationshipType(TYPE)
                .from(RelationshipHelper.teiItem(fromUid))
                .to(RelationshipHelper.teiItem(toUid))
                .build();
    }
}