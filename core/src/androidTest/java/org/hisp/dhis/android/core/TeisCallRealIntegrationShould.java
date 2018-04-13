package org.hisp.dhis.android.core;

import org.hisp.dhis.android.core.common.D2Factory;
import org.hisp.dhis.android.core.data.database.AbsStoreTestCase;
import org.hisp.dhis.android.core.data.server.RealServerMother;
import org.hisp.dhis.android.core.user.User;
import org.junit.Before;

import java.io.IOException;

import retrofit2.Response;

import static com.google.common.truth.Truth.assertThat;

public class TeisCallRealIntegrationShould extends AbsStoreTestCase {

    private D2 d2;

    @Before
    @Override
    public void setUp() throws IOException {
        super.setUp();

        d2 = D2Factory.create(RealServerMother.url, databaseAdapter());
    }

    //@Test
    public void download_tracked_entity_instances() throws Exception {
        d2.logout().call();
        Response<User> loginResponse = d2.logIn("android", "Android123").call();
        assertThat(loginResponse.isSuccessful()).isTrue();

        Response metadataResponse = d2.syncMetaData().call();
        assertThat(metadataResponse.isSuccessful()).isTrue();

        Response teiResponse = d2.downloadTrackedEntityInstances(5).call();
        assertThat(teiResponse.isSuccessful()).isTrue();
    }
}