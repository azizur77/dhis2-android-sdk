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

package org.hisp.dhis.android.core.dataelement;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import org.hisp.dhis.android.core.arch.db.binders.NameableStatementBinder;
import org.hisp.dhis.android.core.arch.db.binders.StatementBinder;
import org.hisp.dhis.android.core.common.CursorModelFactory;
import org.hisp.dhis.android.core.common.IdentifiableObjectStore;
import org.hisp.dhis.android.core.common.StoreFactory;
import org.hisp.dhis.android.core.data.database.DatabaseAdapter;

import static org.hisp.dhis.android.core.utils.StoreUtils.sqLiteBind;

public final class DataElementStore {

    private DataElementStore() {}

    private static StatementBinder<DataElement> BINDER = new NameableStatementBinder<DataElement>() {
        @Override
        public void bindToStatement(@NonNull DataElement o, @NonNull SQLiteStatement sqLiteStatement) {
            super.bindToStatement(o, sqLiteStatement);
            sqLiteBind(sqLiteStatement, 11, o.valueType());
            sqLiteBind(sqLiteStatement, 12, o.zeroIsSignificant());
            sqLiteBind(sqLiteStatement, 13, o.aggregationType());
            sqLiteBind(sqLiteStatement, 14, o.formName());
            sqLiteBind(sqLiteStatement, 15, o.numberType());
            sqLiteBind(sqLiteStatement, 16, o.domainType());
            sqLiteBind(sqLiteStatement, 17, o.dimension());
            sqLiteBind(sqLiteStatement, 18, o.displayFormName());
            sqLiteBind(sqLiteStatement, 19, o.optionSetUid());
            sqLiteBind(sqLiteStatement, 20, o.categoryComboUid());
        }
    };

    private static final CursorModelFactory<DataElement> FACTORY = new CursorModelFactory<DataElement>() {
        @Override
        public DataElement fromCursor(Cursor cursor) {
            return DataElement.create(cursor);
        }
    };

    public static IdentifiableObjectStore<DataElement> create(DatabaseAdapter databaseAdapter) {
        return StoreFactory.objectWithUidStore(databaseAdapter,
                DataElementTableInfo.TABLE_INFO, BINDER, FACTORY);
    }
}