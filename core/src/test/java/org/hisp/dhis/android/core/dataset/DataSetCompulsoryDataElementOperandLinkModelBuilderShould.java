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

package org.hisp.dhis.android.core.dataset;

import org.hisp.dhis.android.core.dataelement.DataElementOperand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class DataSetCompulsoryDataElementOperandLinkModelBuilderShould {

    @Mock
    private DataSet dataSet;

    @Mock
    private DataElementOperand compulsoryDataElementOperand;

    private DataSetCompulsoryDataElementOperandLinkModel model;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        when(dataSet.uid()).thenReturn("dataSet_uid");
        when(compulsoryDataElementOperand.uid()).thenReturn("dataElementOperand_uid");

        model = buildModel();
    }

    private DataSetCompulsoryDataElementOperandLinkModel buildModel() {
        return new DataSetCompulsoryDataElementOperandLinkModelBuilder(dataSet)
                .buildModel(compulsoryDataElementOperand);
    }

    @Test
    public void copy_link_properties() {
        assertThat(model.dataSet()).isEqualTo(dataSet.uid());
        assertThat(model.dataElementOperand()).isEqualTo(compulsoryDataElementOperand.uid());
    }
}