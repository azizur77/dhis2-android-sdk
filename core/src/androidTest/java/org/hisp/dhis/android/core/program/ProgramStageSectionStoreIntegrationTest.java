package org.hisp.dhis.android.core.program;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;

import org.hisp.dhis.android.core.common.BaseIdentifiableObject;
import org.hisp.dhis.android.core.data.database.AbsStoreTestCase;
import org.hisp.dhis.android.core.data.database.DbOpenHelper;
import org.hisp.dhis.android.core.data.database.DbOpenHelper.Tables;
import org.hisp.dhis.android.core.relationship.CreateRelationshipTypeUtils;
import org.hisp.dhis.android.core.trackedentity.CreateTrackedEntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;

import static com.google.common.truth.Truth.assertThat;
import static org.hisp.dhis.android.core.data.database.CursorAssert.assertThatCursor;

@RunWith(AndroidJUnit4.class)
public class ProgramStageSectionStoreIntegrationTest extends AbsStoreTestCase {
    private static final long ID = 2L;

    private static final String UID = "test_uid";
    private static final String CODE = "test_code";
    private static final String NAME = "test_name";
    private static final String DISPLAY_NAME = "test_display_name";

    private static final Integer SORT_ORDER = 7;
    private static final String PROGRAM_STAGE = "test_program_stage";

    // timestamp
    private static final String DATE = "2017-01-05T10:40:00.000";

    // nested foreign key
    private static final String PROGRAM = "test_program";
    //foreign keys to program:
    private static final long TRACKED_ENTITY_ID = 1L;
    private static final String TRACKED_ENTITY_UID = "trackedEntityUid";
    private static final long RELATIONSHIP_TYPE_ID = 1L;
    private static final String RELATIONSHIP_TYPE_UID = "relationshipTypeUid";

    private static final String[] PROGRAM_STAGE_SECTION_PROJECTION = {
            ProgramStageSectionModel.Columns.UID,
            ProgramStageSectionModel.Columns.CODE,
            ProgramStageSectionModel.Columns.NAME,
            ProgramStageSectionModel.Columns.DISPLAY_NAME,
            ProgramStageSectionModel.Columns.CREATED,
            ProgramStageSectionModel.Columns.LAST_UPDATED,
            ProgramStageSectionModel.Columns.SORT_ORDER,
            ProgramStageSectionModel.Columns.PROGRAM_STAGE
    };

    private ProgramStageSectionStore programStageSectionStore;

    @Override
    @Before
    public void setUp() throws IOException {
        super.setUp();
        this.programStageSectionStore = new ProgramStageSectionStoreImpl(database());
    }

    @Test
    public void insert_shouldPersistProgramStageSectionInDatabase() throws Exception {

        //Create Program & insert a row in the table.
        ContentValues trackedEntity = CreateTrackedEntityUtils.create(TRACKED_ENTITY_ID, TRACKED_ENTITY_UID);
        ContentValues relationshipType = CreateRelationshipTypeUtils.create(RELATIONSHIP_TYPE_ID, RELATIONSHIP_TYPE_UID);
        ContentValues program = CreateProgramUtils.create(1L, PROGRAM, RELATIONSHIP_TYPE_UID, TRACKED_ENTITY_UID);

        database().insert(DbOpenHelper.Tables.TRACKED_ENTITY, null, trackedEntity);
        database().insert(DbOpenHelper.Tables.RELATIONSHIP_TYPE, null, relationshipType);
        database().insert(DbOpenHelper.Tables.PROGRAM, null, program);

        ContentValues programStage = ProgramStageModelIntegrationTest.create(ID, PROGRAM_STAGE, PROGRAM);
        database().insert(Tables.PROGRAM_STAGE, null, programStage);

        Date timeStamp = BaseIdentifiableObject.DATE_FORMAT.parse(DATE);

        long rowId = programStageSectionStore.insert(
                UID, CODE, NAME, DISPLAY_NAME,
                timeStamp, timeStamp, SORT_ORDER,
                PROGRAM_STAGE
        );

        Cursor cursor = database().query(Tables.PROGRAM_STAGE_SECTION, PROGRAM_STAGE_SECTION_PROJECTION,
                null, null, null, null, null);

        // Checking if rowId == 1.
        // If it is 1, then it means it is first successful insert into db
        assertThat(rowId).isEqualTo(1L);

        assertThatCursor(cursor).hasRow(
                UID, CODE, NAME, DISPLAY_NAME,
                DATE, DATE, SORT_ORDER, PROGRAM_STAGE
        ).isExhausted();
    }

    @Test
    public void close_shouldNotCloseDatabase() throws Exception {
        programStageSectionStore.close();

        assertThat(database().isOpen()).isTrue();
    }
}