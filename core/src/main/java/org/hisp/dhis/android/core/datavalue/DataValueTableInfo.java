/*
 * Copyright (c) 2004-2018, University of Oslo
 * All rights reserved.
 *
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

package org.hisp.dhis.android.core.datavalue;

import org.hisp.dhis.android.core.arch.db.TableInfo;
import org.hisp.dhis.android.core.common.BaseDataModel;
import org.hisp.dhis.android.core.common.BaseModel;
import org.hisp.dhis.android.core.utils.Utils;

public class DataValueTableInfo {

    // Different BD column names than API fields.
    static final String ORGANISATION_UNIT =  "organisationUnit";
    private static final String FOLLOW_UP = "followUp";

    private DataValueTableInfo() {}

    public static final TableInfo TABLE_INFO = new TableInfo() {

        @Override
        public String name() {
            return "DataValue";
        }

        @Override
        public BaseModel.Columns columns() {
            return new DataValueTableInfo.Columns();
        }
    };

    static class Columns extends BaseDataModel.Columns {
        @Override
        public String[] all() {
            return Utils.appendInNewArray(super.all(),
                    DataValueFields.DATA_ELEMENT,
                    DataValueFields.PERIOD,
                    DataValueTableInfo.ORGANISATION_UNIT,
                    DataValueFields.CATEGORY_OPTION_COMBO,
                    DataValueFields.ATTRIBUTE_OPTION_COMBO,
                    DataValueFields.VALUE,
                    DataValueFields.STORED_BY,
                    DataValueFields.CREATED,
                    DataValueFields.LAST_UPDATED,
                    DataValueFields.COMMENT,
                    DataValueTableInfo.FOLLOW_UP,
                    STATE);
        }

        @Override
        public String[] whereUpdate() {
            return new String[]{
                    DataValueFields.DATA_ELEMENT,
                    DataValueFields.PERIOD,
                    DataValueTableInfo.ORGANISATION_UNIT,
                    DataValueFields.CATEGORY_OPTION_COMBO,
                    DataValueFields.ATTRIBUTE_OPTION_COMBO
            };
        }
    }

}
