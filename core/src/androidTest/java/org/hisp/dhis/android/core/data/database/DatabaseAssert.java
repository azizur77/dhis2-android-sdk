package org.hisp.dhis.android.core.data.database;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import android.database.Cursor;

public final class DatabaseAssert {

    DatabaseAdapter databaseAdapter;

    public static DatabaseAssert assertThatDatabase(DatabaseAdapter databaseAdapter) {
        return new DatabaseAssert(databaseAdapter);
    }

    private DatabaseAssert(DatabaseAdapter databaseAdapter) {
        this.databaseAdapter = databaseAdapter;
    }

    public DatabaseAssert isEmpty() {
        verifyEmptyDatabase(true);

        return this;
    }

    public DatabaseAssert isNotEmpty() {
        verifyEmptyDatabase(false);

        return this;
    }

    public DatabaseAssert isEmptyTable(String tableName) {
        assertThat(tableCount(tableName) == 0, is(true));

        return this;
    }

    public DatabaseAssert isNotEmptyTable(String tableName) {
        assertThat(tableCount(tableName) == 0, is(false));

        return this;
    }

    private void verifyEmptyDatabase(boolean expectedEmpty) {
        boolean isEmpty = true;

        Cursor cursor = databaseAdapter.query(" SELECT name FROM sqlite_master "
                + "WHERE type='table' and name!='android_metadata' and name!='sqlite_sequence'");
        int value = cursor.getColumnIndex("name");
        if (value != -1) {
            while (cursor.moveToNext()) {
                String tableName = cursor.getString(value);

                if (tableCount(tableName) > 0) {
                    isEmpty = false;
                }
            }
        }
        cursor.close();
        assertThat(isEmpty, is(expectedEmpty));
    }

    private int tableCount(String tableName) {
        Cursor cursor = null;
        int count = 0;

        try {
            cursor = databaseAdapter.query("SELECT * from " + tableName, null);
            count = cursor.getCount();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        cursor.close();
        return count;
    }
}
