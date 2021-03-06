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

import android.content.ContentValues;
import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hisp.dhis.android.core.D2;
import org.hisp.dhis.android.core.calls.Call;
import org.hisp.dhis.android.core.common.BaseIdentifiableObject;
import org.hisp.dhis.android.core.common.D2Factory;
import org.hisp.dhis.android.core.data.database.AbsStoreTestCase;
import org.hisp.dhis.android.core.data.file.AssetsFileReader;
import org.hisp.dhis.android.core.data.server.Dhis2MockServer;
import org.hisp.dhis.android.core.program.CreateProgramUtils;
import org.hisp.dhis.android.core.program.ProgramModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.hisp.dhis.android.core.data.database.CursorAssert.assertThatCursor;

@RunWith(AndroidJUnit4.class)
public class UserCallMockIntegrationShould extends AbsStoreTestCase {
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

    private Dhis2MockServer dhis2MockServer;
    private Call<User> userCall;

    @Override
    @Before
    public void setUp() throws IOException {
        super.setUp();
        dhis2MockServer = new Dhis2MockServer(new AssetsFileReader());

        String response = "{\n" +
                "\n" +
                "    \"created\": \"2015-03-31T13:31:09.324\",\n" +
                "    \"lastUpdated\": \"2017-02-01T14:32:33.771\",\n" +
                "    \"name\": \"John Barnes\",\n" +
                "    \"id\": \"DXyJmlo9rge\",\n" +
                "    \"displayName\": \"John Barnes\",\n" +
                "    \"firstName\": \"John\",\n" +
                "    \"surname\": \"Barnes\",\n" +
                "    \"email\": \"john@hmail.com\",\n" +
                "    \"userCredentials\": {\n" +
                "        \"lastUpdated\": \"2017-02-01T14:31:54.370\",\n" +
                "        \"code\": \"android\",\n" +
                "        \"created\": \"2015-03-31T13:31:09.206\",\n" +
                "        \"name\": \"John Traore\",\n" +
                "        \"id\": \"M0fCOxtkURr\",\n" +
                "        \"displayName\": \"John Traore\",\n" +
                "        \"username\": \"android\",\n" +
                "        \"userRoles\": [\n" +
                "            {\n" +
                "                \"id\": \"Ufph3mGRmMo\",\n" +
                "                \"programs\": [\n" +
                "                    {\n" +
                "                        \"id\": \"eBAyeGv0exc\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\": \"IpHINAT79UW\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\": \"WSGAb5XwJ3Y\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\": \"ur1Edk5Oe2n\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": \"Euq3XfEIEbx\",\n" +
                "                \"programs\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": \"cUlTcejWree\",\n" +
                "                \"programs\": [\n" +
                "                    {\n" +
                "                        \"id\": \"ur1Edk5Oe2n\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": \"DRdaVRtwmG5\",\n" +
                "                \"programs\": [\n" +
                "                    {\n" +
                "                        \"id\": \"eBAyeGv0exc\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": \"jRWSNIHdKww\",\n" +
                "                \"programs\": [ ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": \"txB7vu1w2Pr\",\n" +
                "                \"programs\": [ ]\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"teiSearchOrganisationUnits\": [\n" +
                "        {\n" +
                "            \"id\": \"WAjjFMDJKcx\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"organisationUnits\": [\n" +
                "        {\n" +
                "            \"id\": \"DiszpKrYNg8\",\n" +
                "            \"programs\": [\n" +
                "                {\n" +
                "                    \"id\": \"eBAyeGv0exc\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"IpHINAT79UW\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"WSGAb5XwJ3Y\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"ur1Edk5Oe2n\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"fDd25txQckK\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "\n" +
                "}";

        dhis2MockServer.enqueueMockResponse(200, response);
        D2 d2 = D2Factory.create(dhis2MockServer.getBaseEndpoint(), databaseAdapter());

        // ToDo: consider moving this out
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(BaseIdentifiableObject.DATE_FORMAT.raw());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        userCall = UserCall.FACTORY.create(getGenericCallData(d2));

        ContentValues program1 = CreateProgramUtils.create(1L, "eBAyeGv0exc", null, null, null);
        ContentValues program2 = CreateProgramUtils.create(2L, "ur1Edk5Oe2n", null, null, null);
        ContentValues program3 = CreateProgramUtils.create(3L, "fDd25txQckK", null, null, null);
        ContentValues program4 = CreateProgramUtils.create(4L, "WSGAb5XwJ3Y", null, null, null);
        ContentValues program5 = CreateProgramUtils.create(5L, "IpHINAT79UW", null, null, null);

        database().insert(ProgramModel.TABLE, null, program1);
        database().insert(ProgramModel.TABLE, null, program2);
        database().insert(ProgramModel.TABLE, null, program3);
        database().insert(ProgramModel.TABLE, null, program4);
        database().insert(ProgramModel.TABLE, null, program5);
    }

    @Test
    public void persist_user_in_data_base_when_call() throws Exception {
        userCall.call();

        Cursor userCursor = database().query(UserModel.TABLE, USER_PROJECTION, null, null, null, null, null);

        assertThatCursor(userCursor).hasRow(
                1L,
                "DXyJmlo9rge",
                null,
                "John Barnes",
                "John Barnes",
                "2015-03-31T13:31:09.324",
                "2017-02-01T14:32:33.771",
                null,
                null,
                null,
                null,
                "Barnes",
                "John",
                null,
                null,
                null,
                null,
                "john@hmail.com",
                null,
                null
        ).isExhausted();
    }

    @Test
    public void persist_user_credentials_in_data_base_when_call() throws Exception {
        userCall.call();

        String[] projection = {
                UserCredentialsModel.Columns.UID,
                UserCredentialsModel.Columns.CODE,
                UserCredentialsModel.Columns.NAME,
                UserCredentialsModel.Columns.DISPLAY_NAME,
                UserCredentialsModel.Columns.CREATED,
                UserCredentialsModel.Columns.LAST_UPDATED,
                UserCredentialsModel.Columns.USERNAME,
                UserCredentialsModel.Columns.USER,
        };


        Cursor userCredentialsCursor = database().query(UserCredentialsModel.TABLE, projection,
                null, null, null, null, null);

        assertThatCursor(userCredentialsCursor).hasRow(
                "M0fCOxtkURr",
                "android",
                "John Traore",
                "John Traore",
                "2015-03-31T13:31:09.206",
                "2017-02-01T14:31:54.370",
                "android",
                "DXyJmlo9rge"
        ).isExhausted();
    }

    @After
    @Override
    public void tearDown() throws IOException {
        super.tearDown();
        dhis2MockServer.shutdown();
    }
}
