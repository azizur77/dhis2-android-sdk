package org.hisp.dhis.android.core.category;


import org.hisp.dhis.android.core.D2;
import org.hisp.dhis.android.core.calls.Call;
import org.hisp.dhis.android.core.common.D2Factory;
import org.hisp.dhis.android.core.common.IdentifiableObjectStore;
import org.hisp.dhis.android.core.common.LinkModelStore;
import org.hisp.dhis.android.core.data.database.AbsStoreTestCase;
import org.hisp.dhis.android.core.data.server.RealServerMother;
import org.junit.Before;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class CategoryComboEndpointCallRealIntegrationShould extends AbsStoreTestCase {

    private D2 d2;

    @Override
    @Before
    public void setUp() throws IOException {
        super.setUp();
        d2 = D2Factory.create(RealServerMother.url, databaseAdapter());
    }

    //@Test
    public void download_categories_combos_and_relatives() throws Exception {

        d2.logIn(RealServerMother.user, RealServerMother.password).call();

        downloadCategories();

        assertNotCombosInDB();
        assertTrue(getCategoryCategoryComboLinkModels().isEmpty());

        Call<List<CategoryCombo>> categoryComboEndpointCall =
                CategoryComboEndpointCall.factory(d2.retrofit()).create(getGenericCallData(d2));
        List<CategoryCombo> categoryCombos = categoryComboEndpointCall.call();

        assertFalse(categoryCombos.isEmpty());

        assertDataIsProperlyParsedAndInsertedInTheDB();
    }

    private void assertDataIsProperlyParsedAndInsertedInTheDB() {
        assertThereAreCombosInDB();
        assertFalse(getCategoryCategoryComboLinkModels().isEmpty());
        assertThereAreCategoryOptionCombosInDB();
        assertThereAreCategoriesInDB();
    }

    private void downloadCategories() throws Exception {
        CategoryEndpointCall.factory(d2.retrofit()).create(getGenericCallData(d2)).call();
    }

    private void assertNotCombosInDB() {
        IdentifiableObjectStore<CategoryCombo> categoryComboStore = CategoryComboStore.create(databaseAdapter());
        Set<CategoryCombo> categoryCombos = categoryComboStore.selectAll();
        assertTrue(categoryCombos.isEmpty());
    }

    private void assertThereAreCombosInDB() {
        IdentifiableObjectStore<CategoryCombo> categoryComboStore = CategoryComboStore.create(databaseAdapter());
        Set<CategoryCombo> categoryCombos = categoryComboStore.selectAll();
        assertTrue(categoryCombos.size() > 0);
    }

    private Set<CategoryCategoryComboLinkModel> getCategoryCategoryComboLinkModels() {
        LinkModelStore<CategoryCategoryComboLinkModel>
                categoryCategoryComboLinkStore = CategoryCategoryComboLinkStore.create(databaseAdapter());
        return categoryCategoryComboLinkStore.selectAll();
    }

    private void assertThereAreCategoryOptionCombosInDB() {
        IdentifiableObjectStore<CategoryOptionCombo> categoryOptionComboStore = CategoryOptionComboStore.create(databaseAdapter());
        Set<CategoryOptionCombo> categoryOptionCombos = categoryOptionComboStore.selectAll();
        assertTrue(categoryOptionCombos.size() > 0);
    }

    private void assertThereAreCategoriesInDB() {
        IdentifiableObjectStore<CategoryOption> categoryOptionStore = CategoryOptionStore.create(databaseAdapter());
        Set<String> categoryOptionUids = categoryOptionStore.selectUids();
        assertTrue(categoryOptionUids.size() > 0);
    }
}
