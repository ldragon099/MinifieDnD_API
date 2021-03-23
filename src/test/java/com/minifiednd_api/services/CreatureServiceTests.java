package com.minifiednd_api.services;

import com.minifiednd_api.fixtures.CreatureFixture;
import org.junit.jupiter.api.Assertions;
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
        Assertions.assertNotNull(subset);
    }

    // TODO: test index out of bound

    @Test
    void When_RandomSubsetIsPassedNullSubsetLength_Expect_ReturnSameListThatWasPassedIn() {
        // Arrange
        List<Object> inputList = CreatureFixture.getListOfCreaturesAsObjects(3);
        // Act
        List<Object> subsetList = CreatureService.RandomSubset(inputList, null);
        // Assert
        Assertions.assertEquals(inputList, subsetList, "Arrays are not equal");
    }

    @Test
    void When_RandomSubsetIsPassedValidSubsetLength_Expect_ReturnCorrectLengthSubst() {
        // Arrange
        List<Object> inputList = CreatureFixture.getListOfCreaturesAsObjects(5);
        Integer subsetLength = 3;
        // Act
        List<Object> subsetList = CreatureService.RandomSubset(inputList, subsetLength);
        // Assert
        Assertions.assertEquals((long)subsetLength, subsetList.size(), "Subset array is not of the expected length");
    }

    @Test
    void When_ConvertObjectToCreatureIsCalled_Expect_Something() {
        // Arrange
        // Act
        // Assert
    }
}
