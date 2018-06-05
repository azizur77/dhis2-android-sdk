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
import android.support.test.runner.AndroidJUnit4;

import org.hisp.dhis.android.core.D2;
import org.hisp.dhis.android.core.calls.Call;
import org.hisp.dhis.android.core.common.BaseIdentifiableObject;
import org.hisp.dhis.android.core.common.D2Factory;
import org.hisp.dhis.android.core.data.database.AbsStoreTestCase;
import org.hisp.dhis.android.core.data.file.AssetsFileReader;
import org.hisp.dhis.android.core.data.server.Dhis2MockServer;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnitModel;
import org.hisp.dhis.android.core.resource.ResourceModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;
import static org.hisp.dhis.android.core.data.api.ApiUtils.base64;
import static org.hisp.dhis.android.core.data.database.CursorAssert.assertThatCursor;

// ToDo: implement integration tests for user authentication task
// ToDo: more tests to verify correct store behaviour
// ToDo:    - what will happen if the same user will be inserted twice?
@RunWith(AndroidJUnit4.class)
public class UserAuthenticateCallMockIntegrationShould extends AbsStoreTestCase {
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

    private static String[] RESOURCE_PROJECTION = {
            ResourceModel.Columns.ID,
            ResourceModel.Columns.RESOURCE_TYPE,
            ResourceModel.Columns.LAST_SYNCED
    };

    private Dhis2MockServer dhis2MockServer;
    private Call<User> authenticateUserCall;

    @Before
    @Override
    public void setUp() throws IOException {
        super.setUp();

        dhis2MockServer = new Dhis2MockServer(new AssetsFileReader());

        D2 d2 = D2Factory.create(dhis2MockServer.getBaseEndpoint(), databaseAdapter());

        dhis2MockServer.enqueueMockResponse("user.json");
        dhis2MockServer.enqueueMockResponse("system_info.json");

        authenticateUserCall = UserAuthenticateCall.create(databaseAdapter(), d2.retrofit(),
                "test_user", "test_password");
    }

    @Test
    public void persist_user_in_data_base_when_call() throws Exception {
        authenticateUserCall.call();

        // verify that user is persisted in database with corresponding data
        Cursor userCursor = database().query(UserModel.TABLE,
                USER_PROJECTION, null, null, null, null, null);
        Cursor userCredentialsCursor = database().query(UserCredentialsModel.TABLE,
                USER_CREDENTIALS_PROJECTION, null, null, null, null, null);
        Cursor organisationUnits = database().query(OrganisationUnitModel.TABLE,
                ORGANISATION_UNIT_PROJECTION, null, null, null, null, null);
        Cursor authenticatedUsers = database().query(AuthenticatedUserModel.TABLE,
                AUTHENTICATED_USERS_PROJECTION, null, null, null, null, null);
        Cursor userOrganisationUnitLinks = database().query(UserOrganisationUnitLinkModel.TABLE,
                USER_ORGANISATION_UNIT_PROJECTION, null, null, null, null, null);

        Cursor resource = database().query(ResourceModel.TABLE, RESOURCE_PROJECTION,
                null, null, null, null, null);

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
                        "John Barnes", // name
                        "John Barnes", // display name
                        "2015-03-31T13:31:09.206", // created
                        "2017-11-29T11:45:37.250", // last updated
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
                        null, // code
                        null, // name
                        null, // display name
                        null, // created
                        null, // last updated
                        null, // short name
                        null, // display short name,
                        null, // description
                        null, // display description
                        "/ImspTQPwCqd/O6uvpzGd5pu/YuQRtpLP10I/DiszpKrYNg8", // path
                        null, // opening date
                        null, // closed date
                        null, // parent
                        null // level
                )
                .isExhausted();

        assertThatCursor(userOrganisationUnitLinks)
                .hasRow(
                        1L, // id
                        "DXyJmlo9rge", // user
                        "DiszpKrYNg8", // organisation unit
                        OrganisationUnitModel.Scope.SCOPE_DATA_CAPTURE // scope
                )
                .isExhausted();

        String dateString = "2017-11-29T11:27:46.935";

        assertThatCursor(resource)
                .hasRow(
                        1L,
                        ResourceModel.Type.SYSTEM_INFO,
                        dateString
                );

        assertThatCursor(resource)
                .hasRow(
                        2L,
                        ResourceModel.Type.USER,
                        dateString
                );

        assertThatCursor(resource)
                .hasRow(
                        3L,
                        ResourceModel.Type.USER_CREDENTIALS,
                        dateString
                );

        assertThatCursor(resource)
                .hasRow(
                        4L,
                        ResourceModel.Type.AUTHENTICATED_USER,
                        dateString
                );

        // TODO: UserAuthenticateCall is no longer registering OU download in resource table
        // because of a bug when downloading descendants. Restore this check when that code is
        // refactored to implement resource table writting from UserAuthenticateCall
        /*assertThatCursor(resource)
                .hasRow(
                        4L,
                        ResourceModel.Type.ORGANISATION_UNIT,
                        dateString
                ).isExhausted();*/

    }

    @Test
    public void return_correct_user_when_call() throws Exception {
        User user = authenticateUserCall.call();

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

        dhis2MockServer.shutdown();
    }
}