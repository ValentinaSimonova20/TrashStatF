package com.startandroid.trashstatf

import org.junit.Test
import static org.junit.Assert.*;
class DatabaseHelperTest{

    private DatabaseHelper dbHelper;

    @Test
    void testViewUsers() throws Exception {
        dbHelper = new DatabaseHelper();
        String result = dbHelper.viewUsers();
        assertEquals(result.length(), 1);
    }
}
