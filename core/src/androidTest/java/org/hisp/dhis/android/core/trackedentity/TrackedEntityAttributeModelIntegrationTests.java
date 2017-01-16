/*
 * Copyright (c) 2016, University of Oslo
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

package org.hisp.dhis.android.core.trackedentity;

import android.content.ContentValues;
import android.database.MatrixCursor;
import android.support.test.runner.AndroidJUnit4;

import org.hisp.dhis.android.core.common.BaseIdentifiableObject;
import org.hisp.dhis.android.core.common.ValueType;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.Date;

import static com.google.common.truth.Truth.assertThat;
import static org.hisp.dhis.android.core.AndroidTestUtils.toBoolean;

@RunWith(AndroidJUnit4.class)
public class TrackedEntityAttributeModelIntegrationTests {
    private static final long ID = 11L;
    private static final String UID = "test_uid";
    private static final String CODE = "test_code";
    private static final String NAME = "test_name";
    private static final String DISPLAY_NAME = "test_display_name";
    private static final String SHORT_NAME = "test_short_name";
    private static final String DISPLAY_SHORT_NAME = "test_display_short_name";
    private static final String DESCRIPTION = "test_description";
    private static final String DISPLAY_DESCRIPTION = "test_display_description";
    private static final String PATTERN = "test_pattern";
    private static final Integer SORT_ORDER_IN_LIST_NO_PROGRAM = 1;
    private static final String OPTION_SET = "test_option_set_uid";
    private static final ValueType VALUE_TYPE = ValueType.BOOLEAN;
    private static final String EXPRESSION = "test_expression";
    private static final TrackedEntityAttributeSearchScope SEARCH_SCOPE = TrackedEntityAttributeSearchScope.SEARCH_ORG_UNITS;
    private static final Integer PROGRAM_SCOPE = 0; // false
    private static final Integer DISPLAY_IN_LIST_NO_PROGRAM = 1; // true
    private static final Integer GENERATED = 0; // false
    private static final Integer DISPLAY_ON_VISIT_SCHEDULE = 1; // true
    private static final Integer ORG_UNIT_SCOPE = 0; // false
    private static final Integer UNIQUE = 1; // true
    private static final Integer INHERIT = 0; // false

    // used for timestamps
    private static final String DATE = "2011-12-24T12:24:25.203";

    @Test
    public void create_shouldConvertToModel() throws ParseException {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{
                TrackedEntityAttributeModel.Columns.ID,
                TrackedEntityAttributeModel.Columns.UID,
                TrackedEntityAttributeModel.Columns.CODE,
                TrackedEntityAttributeModel.Columns.NAME,
                TrackedEntityAttributeModel.Columns.DISPLAY_NAME,
                TrackedEntityAttributeModel.Columns.CREATED,
                TrackedEntityAttributeModel.Columns.LAST_UPDATED,
                TrackedEntityAttributeModel.Columns.SHORT_NAME,
                TrackedEntityAttributeModel.Columns.DISPLAY_SHORT_NAME,
                TrackedEntityAttributeModel.Columns.DESCRIPTION,
                TrackedEntityAttributeModel.Columns.DISPLAY_DESCRIPTION,
                TrackedEntityAttributeModel.Columns.PATTERN,
                TrackedEntityAttributeModel.Columns.SORT_ORDER_IN_LIST_NO_PROGRAM,
                TrackedEntityAttributeModel.Columns.OPTION_SET,
                TrackedEntityAttributeModel.Columns.VALUE_TYPE,
                TrackedEntityAttributeModel.Columns.EXPRESSION,
                TrackedEntityAttributeModel.Columns.SEARCH_SCOPE,
                TrackedEntityAttributeModel.Columns.PROGRAM_SCOPE,
                TrackedEntityAttributeModel.Columns.DISPLAY_IN_LIST_NO_PROGRAM,
                TrackedEntityAttributeModel.Columns.GENERATED,
                TrackedEntityAttributeModel.Columns.DISPLAY_ON_VISIT_SCHEDULE,
                TrackedEntityAttributeModel.Columns.ORG_UNIT_SCOPE,
                TrackedEntityAttributeModel.Columns.UNIQUE,
                TrackedEntityAttributeModel.Columns.INHERIT
        });

        matrixCursor.addRow(new Object[]{
                ID, UID, CODE, NAME, DISPLAY_NAME, DATE, DATE,
                SHORT_NAME, DISPLAY_SHORT_NAME, DESCRIPTION, DISPLAY_DESCRIPTION, PATTERN,
                SORT_ORDER_IN_LIST_NO_PROGRAM, OPTION_SET, VALUE_TYPE, EXPRESSION, SEARCH_SCOPE,
                PROGRAM_SCOPE, DISPLAY_IN_LIST_NO_PROGRAM, GENERATED, DISPLAY_ON_VISIT_SCHEDULE,
                ORG_UNIT_SCOPE, UNIQUE, INHERIT
        });

        // move cursor to first item before reading
        matrixCursor.moveToFirst();

        Date date = BaseIdentifiableObject.DATE_FORMAT.parse(DATE);

        TrackedEntityAttributeModel trackedEntityAttributeModel = TrackedEntityAttributeModel.create(matrixCursor);

        assertThat(trackedEntityAttributeModel.id()).isEqualTo(ID);
        assertThat(trackedEntityAttributeModel.uid()).isEqualTo(UID);
        assertThat(trackedEntityAttributeModel.code()).isEqualTo(CODE);
        assertThat(trackedEntityAttributeModel.name()).isEqualTo(NAME);
        assertThat(trackedEntityAttributeModel.displayName()).isEqualTo(DISPLAY_NAME);
        assertThat(trackedEntityAttributeModel.created()).isEqualTo(date);
        assertThat(trackedEntityAttributeModel.lastUpdated()).isEqualTo(date);
        assertThat(trackedEntityAttributeModel.shortName()).isEqualTo(SHORT_NAME);
        assertThat(trackedEntityAttributeModel.displayShortName()).isEqualTo(DISPLAY_SHORT_NAME);
        assertThat(trackedEntityAttributeModel.description()).isEqualTo(DESCRIPTION);
        assertThat(trackedEntityAttributeModel.displayDescription()).isEqualTo(DISPLAY_DESCRIPTION);
        assertThat(trackedEntityAttributeModel.pattern()).isEqualTo(PATTERN);
        assertThat(trackedEntityAttributeModel.sortOrderInListNoProgram()).isEqualTo(SORT_ORDER_IN_LIST_NO_PROGRAM);
        assertThat(trackedEntityAttributeModel.optionSet()).isEqualTo(OPTION_SET);
        assertThat(trackedEntityAttributeModel.valueType()).isEqualTo(VALUE_TYPE);
        assertThat(trackedEntityAttributeModel.expression()).isEqualTo(EXPRESSION);
        assertThat(trackedEntityAttributeModel.searchScope()).isEqualTo(SEARCH_SCOPE);
        assertThat(trackedEntityAttributeModel.programScope()).isEqualTo(toBoolean(PROGRAM_SCOPE));
        assertThat(trackedEntityAttributeModel.displayInListNoProgram()).isEqualTo(toBoolean(DISPLAY_IN_LIST_NO_PROGRAM));
        assertThat(trackedEntityAttributeModel.generated()).isEqualTo(toBoolean(GENERATED));
        assertThat(trackedEntityAttributeModel.displayOnVisitSchedule()).isEqualTo(toBoolean(DISPLAY_ON_VISIT_SCHEDULE));
        assertThat(trackedEntityAttributeModel.orgUnitScope()).isEqualTo(toBoolean(ORG_UNIT_SCOPE));
        assertThat(trackedEntityAttributeModel.unique()).isEqualTo(toBoolean(UNIQUE));
        assertThat(trackedEntityAttributeModel.inherit()).isEqualTo(toBoolean(INHERIT));

        matrixCursor.close();
    }

    @Test
    public void toContentValues_shouldConvertToContentValues() throws ParseException {

        ContentValues contentValues =
                CreateTrackedEntityAttributeUtils.createWithOptionSet(ID, UID, OPTION_SET);

        assertThat(contentValues.getAsLong(TrackedEntityAttributeModel.Columns.ID)).isEqualTo(ID);
        assertThat(contentValues.getAsString(TrackedEntityAttributeModel.Columns.UID)).isEqualTo(UID);
        assertThat(contentValues.getAsString(TrackedEntityAttributeModel.Columns.CODE)).isEqualTo(CODE);
        assertThat(contentValues.getAsString(TrackedEntityAttributeModel.Columns.NAME)).isEqualTo(NAME);
        assertThat(contentValues.getAsString(TrackedEntityAttributeModel.Columns.DISPLAY_NAME)).isEqualTo(DISPLAY_NAME);
        assertThat(contentValues.getAsString(TrackedEntityAttributeModel.Columns.CREATED)).isEqualTo(DATE);
        assertThat(contentValues.getAsString(TrackedEntityAttributeModel.Columns.LAST_UPDATED)).isEqualTo(DATE);
        assertThat(contentValues.getAsString(TrackedEntityAttributeModel.Columns.SHORT_NAME)).isEqualTo(SHORT_NAME);
        assertThat(contentValues.getAsString(TrackedEntityAttributeModel.Columns.DISPLAY_SHORT_NAME)).isEqualTo(DISPLAY_SHORT_NAME);
        assertThat(contentValues.getAsString(TrackedEntityAttributeModel.Columns.DESCRIPTION)).isEqualTo(DESCRIPTION);
        assertThat(contentValues.getAsString(TrackedEntityAttributeModel.Columns.DISPLAY_DESCRIPTION)).isEqualTo(DISPLAY_DESCRIPTION);
        assertThat(contentValues.getAsString(TrackedEntityAttributeModel.Columns.PATTERN)).isEqualTo(PATTERN);
        assertThat(contentValues.getAsInteger(TrackedEntityAttributeModel.Columns.SORT_ORDER_IN_LIST_NO_PROGRAM)).isEqualTo(SORT_ORDER_IN_LIST_NO_PROGRAM);
        assertThat(contentValues.getAsString(TrackedEntityAttributeModel.Columns.OPTION_SET)).isEqualTo(OPTION_SET);
        assertThat(contentValues.getAsString(TrackedEntityAttributeModel.Columns.VALUE_TYPE)).isEqualTo(VALUE_TYPE.toString());
        assertThat(contentValues.getAsString(TrackedEntityAttributeModel.Columns.EXPRESSION)).isEqualTo(EXPRESSION);
        assertThat(contentValues.getAsString(TrackedEntityAttributeModel.Columns.SEARCH_SCOPE)).isEqualTo(SEARCH_SCOPE.toString());
        assertThat(contentValues.getAsBoolean(TrackedEntityAttributeModel.Columns.PROGRAM_SCOPE)).isEqualTo(toBoolean(PROGRAM_SCOPE));
        assertThat(contentValues.getAsBoolean(TrackedEntityAttributeModel.Columns.DISPLAY_IN_LIST_NO_PROGRAM)).isEqualTo(toBoolean(DISPLAY_IN_LIST_NO_PROGRAM));
        assertThat(contentValues.getAsBoolean(TrackedEntityAttributeModel.Columns.GENERATED)).isEqualTo(toBoolean(GENERATED));
        assertThat(contentValues.getAsBoolean(TrackedEntityAttributeModel.Columns.DISPLAY_ON_VISIT_SCHEDULE)).isEqualTo(toBoolean(DISPLAY_ON_VISIT_SCHEDULE));
        assertThat(contentValues.getAsBoolean(TrackedEntityAttributeModel.Columns.ORG_UNIT_SCOPE)).isEqualTo(toBoolean(ORG_UNIT_SCOPE));
        assertThat(contentValues.getAsBoolean(TrackedEntityAttributeModel.Columns.UNIQUE)).isEqualTo(toBoolean(UNIQUE));
        assertThat(contentValues.getAsBoolean(TrackedEntityAttributeModel.Columns.INHERIT)).isEqualTo(toBoolean(INHERIT));
    }
}