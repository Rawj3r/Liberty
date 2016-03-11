package com.equidais.mybeacon;

import android.test.InstrumentationTestCase;

/**
 * Created by empirestate on 3/10/16.
 */
public class Testing extends InstrumentationTestCase{
    public void test() throws Exception{
        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);


    }

}
