package com.example.ca1;

import com.example.ca1.ImageAnalysis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImageAnalysisTest {
    private ImageAnalysis imageAnalysis;

    @BeforeEach
    public void setUp() {
        imageAnalysis = new ImageAnalysis();
    }

    @Test
    public void testCreateHashMap() {
        // Defines array of integers representing pixel values
        int[] pixels = new int[]{0, 0, -1, 0, 4, -1, -1, -1, 4};

        HashMap<String, HashMap<Integer, List<Integer>>> result = ImageAnalysis.createHashMap(pixels); // Calls the createHashMap method from ImageAnalysis class & stores result

        assertTrue(result.containsKey("objectMap")); // Checks result HashMap contains "objectMap" key

        assertTrue(result.containsKey("objectValue")); // Checks result HashMap contains "objectValue" key

        // Checks "objectMap" key in result HashMap has expected size of 2
        assertEquals(2, result.get("objectMap").size());

        // Checks "objectValue" key in result HashMap has expected size of 2
        assertEquals(2, result.get("objectValue").size());
    }
}