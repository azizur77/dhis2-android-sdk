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

package org.hisp.dhis.android.core.user;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import org.hisp.dhis.android.core.arch.db.binders.IdentifiableStatementBinder;
import org.hisp.dhis.android.core.arch.db.binders.StatementBinder;
import org.hisp.dhis.android.core.common.CursorModelFactory;
import org.hisp.dhis.android.core.common.IdentifiableObjectStore;
import org.hisp.dhis.android.core.common.StoreFactory;
import org.hisp.dhis.android.core.data.database.DatabaseAdapter;

import static org.hisp.dhis.android.core.utils.StoreUtils.sqLiteBind;

public final class UserStore {
    private UserStore() {}

    private static StatementBinder<User> BINDER = new IdentifiableStatementBinder<User>() {

        @Override
        public void bindToStatement(@NonNull User o, @NonNull SQLiteStatement sqLiteStatement) {
            super.bindToStatement(o, sqLiteStatement);
            sqLiteBind(sqLiteStatement, 7, o.birthday());
            sqLiteBind(sqLiteStatement, 8, o.education());
            sqLiteBind(sqLiteStatement, 9, o.gender());
            sqLiteBind(sqLiteStatement, 10, o.jobTitle());
            sqLiteBind(sqLiteStatement, 11, o.surname());
            sqLiteBind(sqLiteStatement, 12, o.firstName());
            sqLiteBind(sqLiteStatement, 13, o.introduction());
            sqLiteBind(sqLiteStatement, 14, o.employer());
            sqLiteBind(sqLiteStatement, 15, o.interests());
            sqLiteBind(sqLiteStatement, 16, o.languages());
            sqLiteBind(sqLiteStatement, 17, o.email());
            sqLiteBind(sqLiteStatement, 18, o.phoneNumber());
            sqLiteBind(sqLiteStatement, 19, o.nationality());
        }
    };

    private static final CursorModelFactory<User> FACTORY = new CursorModelFactory<User>() {
        @Override
        public User fromCursor(Cursor cursor) {
            return User.create(cursor);
        }
    };

    public static IdentifiableObjectStore<User> create(DatabaseAdapter databaseAdapter) {
        return StoreFactory.objectWithUidStore(databaseAdapter, UserModel.TABLE,
                new UserModel.Columns().all(), BINDER, FACTORY);
    }
}
