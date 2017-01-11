package org.hisp.dhis.android.core.user;

import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hisp.dhis.android.core.common.Call;
import org.hisp.dhis.android.core.data.api.FilterConverterFactory;
import org.hisp.dhis.android.core.data.database.AbsStoreTestCase;
import org.hisp.dhis.android.core.data.database.DbOpenHelper;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnitModel;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnitStore;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnitStoreImpl;
import org.hisp.dhis.android.core.common.BaseIdentifiableObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.google.common.truth.Truth.assertThat;
import static org.hisp.dhis.android.core.data.api.ApiUtils.base64;
import static org.hisp.dhis.android.core.data.database.CursorAssert.assertThatCursor;

// ToDo: implement integration tests for user authentication task
// ToDo: more tests to verify correct store behaviour
// ToDo:    - what will happen if the same user will be inserted twice?
@RunWith(AndroidJUnit4.class)
public class UserAuthenticateCallIntegrationTests extends AbsStoreTestCase {
    private static final String[] USER_PROJECTION = {
            UserModel.Columns.ID,
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

    private static final String[] USER_CREDENTIALS_PROJECTION = {
            UserCredentialsModel.Columns.ID,
            UserCredentialsModel.Columns.UID,
            UserCredentialsModel.Columns.CODE,
            UserCredentialsModel.Columns.NAME,
            UserCredentialsModel.Columns.DISPLAY_NAME,
            UserCredentialsModel.Columns.CREATED,
            UserCredentialsModel.Columns.LAST_UPDATED,
            UserCredentialsModel.Columns.USERNAME,
            UserCredentialsModel.Columns.USER,
    };

    // using table as a prefix in order to avoid ambiguity in queries against joined tables
    private static final String[] ORGANISATION_UNIT_PROJECTION = {
            OrganisationUnitModel.Columns.ID,
            OrganisationUnitModel.Columns.UID,
            OrganisationUnitModel.Columns.CODE,
            OrganisationUnitModel.Columns.NAME,
            OrganisationUnitModel.Columns.DISPLAY_NAME,
            OrganisationUnitModel.Columns.CREATED,
            OrganisationUnitModel.Columns.LAST_UPDATED,
            OrganisationUnitModel.Columns.SHORT_NAME,
            OrganisationUnitModel.Columns.DISPLAY_SHORT_NAME,
            OrganisationUnitModel.Columns.DESCRIPTION,
            OrganisationUnitModel.Columns.DISPLAY_DESCRIPTION,
            OrganisationUnitModel.Columns.PATH,
            OrganisationUnitModel.Columns.OPENING_DATE,
            OrganisationUnitModel.Columns.CLOSED_DATE,
            OrganisationUnitModel.Columns.PARENT,
            OrganisationUnitModel.Columns.LEVEL
    };

    private static final String[] AUTHENTICATED_USERS_PROJECTION = {
            AuthenticatedUserModel.Columns.ID,
            AuthenticatedUserModel.Columns.USER,
            AuthenticatedUserModel.Columns.CREDENTIALS
    };

    private static String[] USER_ORGANISATION_UNIT_PROJECTION = {
            UserOrganisationUnitLinkModel.Columns.ID,
            UserOrganisationUnitLinkModel.Columns.USER,
            UserOrganisationUnitLinkModel.Columns.ORGANISATION_UNIT,
            UserOrganisationUnitLinkModel.Columns.ORGANISATION_UNIT_SCOPE,
    };

    private MockWebServer mockWebServer;
    private Call<Response<User>> authenticateUserCall;

    @Before
    @Override
    public void setUp() throws IOException {
        super.setUp();

        mockWebServer = new MockWebServer();
        mockWebServer.start();

        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\n" +
                "\n" +
                "    \"created\": \"2015-03-31T13:31:09.324\",\n" +
                "    \"lastUpdated\": \"2016-04-06T00:05:57.495\",\n" +
                "    \"name\": \"John Barnes\",\n" +
                "    \"id\": \"DXyJmlo9rge\",\n" +
                "    \"displayName\": \"John Barnes\",\n" +
                "    \"firstName\": \"John\",\n" +
                "    \"surname\": \"Barnes\",\n" +
                "    \"email\": \"john@hmail.com\",\n" +
                "    \"userCredentials\": {\n" +
                "        \"lastUpdated\": \"2016-12-20T15:04:21.254\",\n" +
                "        \"code\": \"android\",\n" +
                "        \"created\": \"2015-03-31T13:31:09.206\",\n" +
                "        \"name\": \"John Traore\",\n" +
                "        \"id\": \"M0fCOxtkURr\",\n" +
                "        \"displayName\": \"John Traore\",\n" +
                "        \"username\": \"android\"\n" +
                "    },\n" +
                "    \"organisationUnits\": [\n" +
                "        {\n" +
                "            \"code\": \"OU_559\",\n" +
                "            \"level\": 4,\n" +
                "            \"created\": \"2012-02-17T15:54:39.987\",\n" +
                "            \"lastUpdated\": \"2014-11-25T09:37:54.924\",\n" +
                "            \"name\": \"Ngelehun CHC\",\n" +
                "            \"id\": \"DiszpKrYNg8\",\n" +
                "            \"shortName\": \"Ngelehun CHC\",\n" +
                "            \"displayName\": \"Ngelehun CHC\",\n" +
                "            \"displayShortName\": \"Ngelehun CHC\",\n" +
                "            \"path\": \"/ImspTQPwCqd/O6uvpzGd5pu/YuQRtpLP10I/DiszpKrYNg8\",\n" +
                "            \"openingDate\": \"1970-01-01T00:00:00.000\",\n" +
                "            \"parent\": {\n" +
                "                \"id\": \"YuQRtpLP10I\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "\n" +
                "}");

        mockWebServer.enqueue(mockResponse);

        // ToDo: consider moving this out
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(BaseIdentifiableObject.DATE_FORMAT);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addConverterFactory(FilterConverterFactory.create())
                .build();

        UserService userService = retrofit.create(UserService.class);

        UserStore userStore = new UserStoreImpl(database());
        UserCredentialsStore userCredentialsStore = new UserCredentialsStoreImpl(database());
        OrganisationUnitStore organisationUnitStore = new OrganisationUnitStoreImpl(database());
        AuthenticatedUserStore authenticatedUserStore = new AuthenticatedUserStoreImpl(database());
        UserOrganisationUnitLinkStore userOrganisationUnitLinkStore = new UserOrganisationUnitLinkStoreImpl(database());

        authenticateUserCall = new UserAuthenticateCall(userService, database(), userStore,
                userCredentialsStore, userOrganisationUnitLinkStore, authenticatedUserStore,
                organisationUnitStore, "test_user", "test_password");
    }

    @Test
    public void call_shouldPersistUserInDatabase() throws Exception {
        authenticateUserCall.call();

        // verify that user is persisted in database with corresponding data
        Cursor userCursor = database().query(DbOpenHelper.Tables.USER,
                USER_PROJECTION, null, null, null, null, null);
        Cursor userCredentialsCursor = database().query(DbOpenHelper.Tables.USER_CREDENTIALS,
                USER_CREDENTIALS_PROJECTION, null, null, null, null, null);
        Cursor organisationUnits = database().query(DbOpenHelper.Tables.ORGANISATION_UNIT,
                ORGANISATION_UNIT_PROJECTION, null, null, null, null, null);
        Cursor authenticatedUsers = database().query(DbOpenHelper.Tables.AUTHENTICATED_USER,
                AUTHENTICATED_USERS_PROJECTION, null, null, null, null, null);
        Cursor userOrganisationUnitLinks = database().query(DbOpenHelper.Tables.USER_ORGANISATION_UNIT,
                USER_ORGANISATION_UNIT_PROJECTION, null, null, null, null, null);

        assertThatCursor(userCursor)
                .hasRow(
                        1L, // id
                        "DXyJmlo9rge", // uid
                        null, // code
                        "John Barnes", // name
                        "John Barnes", // displayName
                        "2015-03-31T13:31:09.324", // created
                        "2016-04-06T00:05:57.495", // lastUpdated
                        null, // birthday
                        null, // education
                        null, // gender
                        null, // job title
                        "Barnes", // surname
                        "John", // first name
                        null, // introduction
                        null, // employer
                        null, // interests
                        null, // languages
                        "john@hmail.com", // email
                        null, // phone number
                        null // nationality
                )
                .isExhausted();

        assertThatCursor(userCredentialsCursor)
                .hasRow(
                        1L, // id
                        "M0fCOxtkURr", // uid
                        "android", // code
                        "John Traore", // name
                        "John Traore", // display name
                        "2015-03-31T13:31:09.206", // created
                        "2016-12-20T15:04:21.254", // last updated
                        "android", // username
                        "DXyJmlo9rge" // user
                )
                .isExhausted();

        assertThatCursor(authenticatedUsers)
                .hasRow(
                        1L, // id
                        "DXyJmlo9rge", // user
                        base64("test_user", "test_password") // credentials
                )
                .isExhausted();

        assertThatCursor(organisationUnits)
                .hasRow(
                        1L, // id
                        "DiszpKrYNg8", // uid
                        "OU_559", // code
                        "Ngelehun CHC", // name
                        "Ngelehun CHC", // display name
                        "2012-02-17T15:54:39.987", // created
                        "2014-11-25T09:37:54.924", // last updated
                        "Ngelehun CHC", // short name
                        "Ngelehun CHC", // display short name,
                        null, // description
                        null, // display description
                        "/ImspTQPwCqd/O6uvpzGd5pu/YuQRtpLP10I/DiszpKrYNg8", // path
                        "1970-01-01T00:00:00.000", // opening date
                        null, // closed date
                        "YuQRtpLP10I", // parent
                        4 // level
                )
                .isExhausted();

        assertThatCursor(userOrganisationUnitLinks)
                .hasRow(
                        1L, // id
                        "DXyJmlo9rge", // user
                        "DiszpKrYNg8", // organisation unit
                        OrganisationUnitModel.SCOPE_DATA_CAPTURE // scope
                )
                .isExhausted();
    }

    @Test
    public void call_shouldReturnCorrectUserModel() throws Exception {
        Response<User> userResponse = authenticateUserCall.call();

        User user = userResponse.body();

        // verify payload which has been returned from call
        assertThat(user.uid()).isEqualTo("DXyJmlo9rge");
        assertThat(user.created()).isEqualTo(BaseIdentifiableObject
                .DATE_FORMAT.parse("2015-03-31T13:31:09.324"));
        assertThat(user.lastUpdated()).isEqualTo(BaseIdentifiableObject
                .DATE_FORMAT.parse("2016-04-06T00:05:57.495"));
        assertThat(user.name()).isEqualTo("John Barnes");
        assertThat(user.displayName()).isEqualTo("John Barnes");
        assertThat(user.firstName()).isEqualTo("John");
        assertThat(user.surname()).isEqualTo("Barnes");
        assertThat(user.email()).isEqualTo("john@hmail.com");
    }

    @After
    @Override
    public void tearDown() throws IOException {
        super.tearDown();

        mockWebServer.shutdown();
    }
}
