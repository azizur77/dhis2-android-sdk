package org.hisp.dhis.android.core.trackedentity;

import android.support.annotation.NonNull;

import org.hisp.dhis.android.core.D2InternalModules;
import org.hisp.dhis.android.core.calls.Call;
import org.hisp.dhis.android.core.common.D2CallException;
import org.hisp.dhis.android.core.common.D2CallExecutor;
import org.hisp.dhis.android.core.common.ForeignKeyCleaner;
import org.hisp.dhis.android.core.common.ObjectWithoutUidStore;
import org.hisp.dhis.android.core.common.SyncCall;
import org.hisp.dhis.android.core.data.database.DatabaseAdapter;
import org.hisp.dhis.android.core.organisationunit.OldSearchOrganisationUnitCall;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnit;
import org.hisp.dhis.android.core.user.AuthenticatedUserModel;
import org.hisp.dhis.android.core.user.AuthenticatedUserStore;
import org.hisp.dhis.android.core.user.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import retrofit2.Retrofit;

final class TrackedEntityInstanceRelationshipPersistenceCall extends SyncCall<Void> {

    private final DatabaseAdapter databaseAdapter;
    private final Retrofit retrofit;
    private final TrackedEntityInstanceHandler trackedEntityInstanceHandler;
    private final TrackedEntityInstanceUidHelper uidsHelper;
    private final ObjectWithoutUidStore<AuthenticatedUserModel> authenticatedUserStore;
    private final OldSearchOrganisationUnitCall.Factory organisationUnitCallFactory;
    private final ForeignKeyCleaner foreignKeyCleaner;

    private final Collection<TrackedEntityInstance> trackedEntityInstances;

    private TrackedEntityInstanceRelationshipPersistenceCall(
            @NonNull DatabaseAdapter databaseAdapter,
            @NonNull Retrofit retrofit,
            @NonNull TrackedEntityInstanceHandler trackedEntityInstanceHandler,
            @NonNull TrackedEntityInstanceUidHelper uidsHelper,
            @NonNull ObjectWithoutUidStore<AuthenticatedUserModel> authenticatedUserStore,
            @NonNull OldSearchOrganisationUnitCall.Factory organisationUnitCallFactory,
            @NonNull Collection<TrackedEntityInstance> trackedEntityInstances,
            @NonNull ForeignKeyCleaner foreignKeyCleaner) {
        this.databaseAdapter = databaseAdapter;
        this.retrofit = retrofit;
        this.trackedEntityInstanceHandler = trackedEntityInstanceHandler;
        this.uidsHelper = uidsHelper;
        this.authenticatedUserStore = authenticatedUserStore;
        this.organisationUnitCallFactory = organisationUnitCallFactory;
        this.trackedEntityInstances = trackedEntityInstances;
        this.foreignKeyCleaner = foreignKeyCleaner;
    }

    @Override
    public Void call() throws D2CallException {
        setExecuted();

        final D2CallExecutor executor = new D2CallExecutor();

        return executor.executeD2CallTransactionally(databaseAdapter, new Callable<Void>() {
            @Override
            public Void call() throws D2CallException {
                trackedEntityInstanceHandler.handleMany(trackedEntityInstances, true);

                Set<String> searchOrgUnitUids = uidsHelper.getMissingOrganisationUnitUids(trackedEntityInstances);

                if (!searchOrgUnitUids.isEmpty()) {
                    AuthenticatedUserModel authenticatedUserModel = authenticatedUserStore.selectFirst();

                    Call<List<OrganisationUnit>> organisationUnitCall = organisationUnitCallFactory.create(
                            databaseAdapter, retrofit, searchOrgUnitUids,
                            User.builder().uid(authenticatedUserModel.user()).build());
                    executor.executeD2Call(organisationUnitCall);
                }

                foreignKeyCleaner.cleanForeignKeyErrors();

                return null;
            }
        });
    }

    public static TrackedEntityInstanceRelationshipPersistenceCall create(DatabaseAdapter databaseAdapter,
                                                                          Retrofit retrofit,
                                                                          D2InternalModules internalModules,
                                                                          Collection<TrackedEntityInstance>
                                                                      trackedEntityInstances) {
        return new TrackedEntityInstanceRelationshipPersistenceCall(
                databaseAdapter,
                retrofit,
                TrackedEntityInstanceHandler.create(databaseAdapter, internalModules),
                TrackedEntityInstanceUidHelperImpl.create(databaseAdapter),
                AuthenticatedUserStore.create(databaseAdapter),
                OldSearchOrganisationUnitCall.FACTORY,
                trackedEntityInstances,
                new ForeignKeyCleaner(databaseAdapter)
        );
    }
}
