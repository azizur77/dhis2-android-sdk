package org.hisp.dhis.android.core.option;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import org.hisp.dhis.android.core.data.database.DbOpenHelper.Tables;

import java.util.Date;

import static org.hisp.dhis.android.core.common.StoreUtils.sqLiteBind;

public class OptionStoreImpl implements OptionStore {

    private static final String INSERT_STATEMENT = "INSERT INTO " + Tables.OPTION + " (" +
            OptionModel.Columns.UID + ", " +
            OptionModel.Columns.CODE + ", " +
            OptionModel.Columns.NAME + ", " +
            OptionModel.Columns.DISPLAY_NAME + ", " +
            OptionModel.Columns.CREATED + ", " +
            OptionModel.Columns.LAST_UPDATED + ", " +
            OptionModel.Columns.OPTION_SET + ")" +
            "VALUES (?, ?, ?, ?, ?, ?, ?);";

    private final SQLiteStatement sqLiteStatement;

    public OptionStoreImpl(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteStatement = sqLiteDatabase.compileStatement(INSERT_STATEMENT);
    }

    @Override
    public long insert(@NonNull String uid,
                       @NonNull String code,
                       @NonNull String name,
                       @NonNull String displayName,
                       @NonNull Date created,
                       @NonNull Date lastUpdated,
                       @NonNull String optionSet) {
        sqLiteStatement.clearBindings();

        sqLiteBind(sqLiteStatement, 1, uid);
        sqLiteBind(sqLiteStatement, 2, code);
        sqLiteBind(sqLiteStatement, 3, name);
        sqLiteBind(sqLiteStatement, 4, displayName);
        sqLiteBind(sqLiteStatement, 5, created);
        sqLiteBind(sqLiteStatement, 6, lastUpdated);
        sqLiteBind(sqLiteStatement, 7, optionSet);

        return sqLiteStatement.executeInsert();
    }

    @Override
    public void close() {
        sqLiteStatement.close();
    }
}
