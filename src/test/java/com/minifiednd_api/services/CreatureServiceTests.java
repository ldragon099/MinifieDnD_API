package com.minifiednd_api.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Assert;

import java.util.Collections;
import java.util.List;

@SpringBootTest
public class CreatureServiceTests {
    // When_StateUnderTest_Expect_ExpectedBehavior
    @Test
    void When_RandomSubsetIsPassedList_Expect_ReturnList() {
        // Arrange
        Integer subsetLength = 0;
        List<Object> list = Collections.emptyList();
        // Act
        List<Object> subset = CreatureService.RandomSubset(list, subsetLength);
        // Assert
        Assert.assertNotNull(subset);
    }

    // TODO: test index out of bound

//    @Test
//    void When_RandomSubsetIsPassedNullSubsetLength_Expect_ReturnSameListThatWasPassedIn() {
//        // Arrange
//        List<Object> inputList = Collections.emptyList();
//        inputList.add(3);
//        inputList.add(2);
//        inputList.add(1);
//        // Act
//        List<Object> subsetList = CreatureService.RandomSubset(inputList, null);
//        // Assert
//        Assert.assertEquals("Arrays are not equal", inputList, subsetList);
//    }
}
