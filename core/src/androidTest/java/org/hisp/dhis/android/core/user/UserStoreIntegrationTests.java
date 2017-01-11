package org.hisp.dhis.android.core.user;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;

import org.hisp.dhis.android.core.common.BaseIdentifiableObject;
import org.hisp.dhis.android.core.data.database.AbsStoreTestCase;
import org.hisp.dhis.android.core.data.database.DbOpenHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;

import static com.google.common.truth.Truth.assertThat;
import static org.hisp.dhis.android.core.data.database.CursorAssert.assertThatCursor;

@RunWith(AndroidJUnit4.class)
public class UserStoreIntegrationTests extends AbsStoreTestCase {
    public static final String[] USER_PROJECTION = {
            UserModel.Columns.UID,
            UserModel.Columns.CODE,
            UserModel.Columns.NAME,
            UserModel.Columns.DISPLAY_NAME,
            UserModel.Columns.CREATED,
            UserModel.Columns.LAST_UPDATED,
            UserModel.Columns.BIRTHDAY,
            UserModel.Columns.EDUCATION,
            UserModel.Columns.GENDER,
            UserModel.Columns.JOB_TITLE,
            UserModel.Columns.SURNAME,
            UserModel.Columns.FIRST_NAME,
            UserModel.Columns.INTRODUCTION,
            UserModel.Columns.EMPLOYER,
            UserModel.Columns.INTERESTS,
            UserModel.Columns.LANGUAGES,
            UserModel.Columns.EMAIL,
            UserModel.Columns.PHONE_NUMBER,
            UserModel.Columns.NATIONALITY
    };

    private UserStore userStore;

    public static ContentValues create(long id, String uid) {
        ContentValues user = new ContentValues();
        user.put(UserModel.Columns.ID, id);
        user.put(UserModel.Columns.UID, uid);
        user.put(UserModel.Columns.CODE, "test_code");
        user.put(UserModel.Columns.NAME, "test_name");
        user.put(UserModel.Columns.DISPLAY_NAME, "test_display_name");
        user.put(UserModel.Columns.CREATED, "test_created");
        user.put(UserModel.Columns.LAST_UPDATED, "test_last_updated");
        user.put(UserModel.Columns.BIRTHDAY, "test_birthday");
        user.put(UserModel.Columns.EDUCATION, "test_education");
        user.put(UserModel.Columns.GENDER, "test_gender");
        user.put(UserModel.Columns.JOB_TITLE, "test_job_title");
        user.put(UserModel.Columns.SURNAME, "test_surname");
        user.put(UserModel.Columns.FIRST_NAME, "test_first_name");
        user.put(UserModel.Columns.INTRODUCTION, "test_introduction");
        user.put(UserModel.Columns.EMPLOYER, "test_employer");
        user.put(UserModel.Columns.INTERESTS, "test_interests");
        user.put(UserModel.Columns.LANGUAGES, "test_languages");
        user.put(UserModel.Columns.EMAIL, "test_email");
        user.put(UserModel.Columns.PHONE_NUMBER, "test_phone_number");
        user.put(UserModel.Columns.NATIONALITY, "test_nationality");
        return user;
    }

    @Before
    @Override
    public void setUp() throws IOException {
        super.setUp();

        userStore = new UserStoreImpl(database());
    }

    @Test
    public void insert_shouldPersistRowInDatabase() {
        Date date = new Date();

        long rowId = userStore.insert(
                "test_user_uid",
                "test_user_code",
                "test_user_name",
                "test_user_display_name",
                date, date,
                "test_user_birthday",
                "test_user_education",
                "test_user_gender",
                "test_user_job_title",
                "test_user_surname",
                "test_user_first_name",
                "test_user_introduction",
                "test_user_employer",
                "test_user_interests",
                "test_user_languages",
                "test_user_email",
                "test_user_phone_number",
                "test_user_nationality"
        );

        Cursor cursor = database().query(DbOpenHelper.Tables.USER,
                USER_PROJECTION, null, null, null, null, null);

        assertThat(rowId).isEqualTo(1L);
        assertThatCursor(cursor)
                .hasRow(
                        "test_user_uid",
                        "test_user_code",
                        "test_user_name",
                        "test_user_display_name",
                        BaseIdentifiableObject.DATE_FORMAT.format(date),
                        BaseIdentifiableObject.DATE_FORMAT.format(date),
                        "test_user_birthday",
                        "test_user_education",
                        "test_user_gender",
                        "test_user_job_title",
                        "test_user_surname",
                        "test_user_first_name",
                        "test_user_introduction",
                        "test_user_employer",
                        "test_user_interests",
                        "test_user_languages",
                        "test_user_email",
                        "test_user_phone_number",
                        "test_user_nationality"
                )
                .isExhausted();
    }

    // ToDo: consider introducing conflict resolution strategy
//    @Test
//    public void save_shouldNotTriggerOtherTablesOnDuplicate() {
//        // inserting user
//        ContentValues user = UserContractIntegrationTests.authenticator(1L, "test_user_uid");
//        database().insert(DbOpenHelper.Tables.USER, null, user);
//
//        // inserting user credentials
//        ContentValues userCredentials = UserCredentialsContractIntegrationTests.authenticator(
//                1L, "test_user_credentials", "test_user_uid");
//        database().insert(DbOpenHelper.Tables.USER_CREDENTIALS, null, userCredentials);
//
//        // try to insert duplicate into user table through store
//        Date date = new Date();
//        long rowId = userStore.insert(
//                "test_user_uid",
//                "test_user_code",
//                "test_user_name",
//                "test_user_display_name",
//                date, date,
//                "test_user_birthday",
//                "test_user_education",
//                "test_user_gender",
//                "test_user_job_title",
//                "test_user_surname",
//                "test_user_first_name",
//                "test_user_introduction",
//                "test_user_employer",
//                "test_user_interests",
//                "test_user_languages",
//                "test_user_email",
//                "test_user_phone_number",
//                "test_user_nationality"
//        );
//
//        System.out.println("RowId: " + rowId);
//
//        assertThatCursor(database().query(DbOpenHelper.Tables.USER_CREDENTIALS, UserCredentialsContractIntegrationTests.USER_CREDENTIALS_PROJECTION, null, null, null, null, null))
//                .hasRow(UserCredentialsContractIntegrationTests.USER_CREDENTIALS_PROJECTION, userCredentials)
//                .isExhausted();
//    }

    @Test
    public void close_shouldNotCloseDatabase() {
        userStore.close();

        assertThat(database().isOpen()).isTrue();
    }
}
