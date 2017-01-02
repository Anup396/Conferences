package com.project.anupamchugh.conferences;

import android.content.Context;
import android.content.res.Resources;
import android.test.ActivityTestCase;

/**
 * Created by anupamchugh on 31/12/16.
 */

public class ActivityTestCaseTest extends ActivityTestCase {

    public void testFoo() {

        Context testContext = getInstrumentation().getContext();
        Resources testRes = testContext.getResources();

        assertNotNull(testRes);
        assertNotNull(testRes.getString(R.string.app_name));
    }
}
