package com.baliyaan.android.wordincontext.Components.Definition.Data;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Pulkit Singh on 11/5/2017.
 */

@RunWith(AndroidJUnit4.class)
public class OnlineDictionaryTST {
    @Test
    public void getDefinitionOf(){
        try {
            String definition = OnlineDictionary.getDefinitionOf("murky");
            assertEquals(definition,"murky water is dark and difficult to see through, usually because it is dirty, muddy etc");
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Web-page not fetched.");
        }
    }
}
