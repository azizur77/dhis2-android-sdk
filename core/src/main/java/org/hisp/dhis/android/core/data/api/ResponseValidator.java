package org.hisp.dhis.android.core.data.api;


import org.hisp.dhis.android.core.common.Payload;

import retrofit2.Response;

public class ResponseValidator<E> {

    public boolean isValid(Response<Payload<E>> response) {
        return (response.isSuccessful() && response.body().items() != null);

    }
}
