/*
 * Copyright (c) 2004-2018, University of Oslo
 * All rights reserved.
 *
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
 *
 */

package org.hisp.dhis.android.core.dataset;

import org.hisp.dhis.android.core.calls.factories.QueryCallFactory;
import org.hisp.dhis.android.core.calls.factories.QueryCallFactoryImpl;
import org.hisp.dhis.android.core.calls.fetchers.CallFetcher;
import org.hisp.dhis.android.core.calls.processors.CallProcessor;
import org.hisp.dhis.android.core.calls.processors.TransactionalNoResourceSyncCallProcessor;
import org.hisp.dhis.android.core.common.GenericCallData;

import retrofit2.Call;

import static org.hisp.dhis.android.core.utils.Utils.commaSeparatedCollectionValues;

public final class DataSetCompleteRegistrationCall {

    private DataSetCompleteRegistrationCall() {}

    public static final QueryCallFactory<DataSetCompleteRegistration, DataSetCompleteRegistrationQuery> FACTORY
            = new QueryCallFactoryImpl<DataSetCompleteRegistration, DataSetCompleteRegistrationQuery>() {

        @Override
        protected CallFetcher<DataSetCompleteRegistration> fetcher(GenericCallData data,
                       final DataSetCompleteRegistrationQuery dataSetCompleteRegistrationQuery) {

            final DataSetCompleteRegistrationService dataSetCompleteRegistrationService =
                    data.retrofit().create(DataSetCompleteRegistrationService.class);

            return new DataSetCompleteRegistrationCallFetcher(dataSetCompleteRegistrationQuery.dataSetUids(),
                    dataSetCompleteRegistrationQuery.periodIds(), dataSetCompleteRegistrationQuery.rootOrgUnitUids()) {

                @Override
                protected Call<DataSetCompleteRegistrationPayload> getCall(
                        DataSetCompleteRegistrationQuery dataSetCompleteRegistrationQuery) {
                    return dataSetCompleteRegistrationService.getDataSetCompleteRegistrations(
                            DataSetCompleteRegistrationFields.allFields,
                            commaSeparatedCollectionValues(dataSetCompleteRegistrationQuery.dataSetUids()),
                            commaSeparatedCollectionValues(dataSetCompleteRegistrationQuery.periodIds()),
                            commaSeparatedCollectionValues(dataSetCompleteRegistrationQuery.rootOrgUnitUids()),
                            Boolean.TRUE,
                            Boolean.FALSE);
                }
            };
        }

        @Override
        protected CallProcessor<DataSetCompleteRegistration> processor(GenericCallData data,
                                                                       DataSetCompleteRegistrationQuery query) {

            return new TransactionalNoResourceSyncCallProcessor<>(data.databaseAdapter(),
                    DataSetCompleteRegistrationHandler.create(data.databaseAdapter()));
        }
    };
}
