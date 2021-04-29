package com.minifiednd_api.services;

import com.minifiednd_api.fixtures.CreatureFixture;
import com.minifiednd_api.models.Creature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Test
    void When_RandomSubsetIsPassedNullSubsetLength_Expect_ReturnSameList() {
        // Arrange
        List<Object> inputList = CreatureFixture.getListOfCreaturesAsObjects(3);
        // Act
        List<Object> subsetList = CreatureService.RandomSubset(inputList, null);
        // Assert
        Assertions.assertEquals(inputList, subsetList, "Lists are not equal");
    }

    @Test
    void When_RandomSubsetIsPassedValidSubsetLength_Expect_ReturnCorrectLengthSubst() {
        // Arrange
        List<Object> inputList = CreatureFixture.getListOfCreaturesAsObjects(5);
        Integer subsetLength = 3;
        // Act
        List<Object> subsetList = CreatureService.RandomSubset(inputList, subsetLength);
        // Assert
        Assertions.assertEquals((long)subsetLength, subsetList.size(), "Subset list is not of the expected length");
    }

    @Test
    void When_RandomSubsetIsPassedLengthLongerThanList_Expect_ReturnSameList() {
        // Arrange
        List<Object> inputList = CreatureFixture.getListOfCreaturesAsObjects(5);
        Integer subsetLength = 6;
        // Act
        List<Object> subsetList = CreatureService.RandomSubset(inputList, subsetLength);
        // Assert
        Assertions.assertEquals(inputList, subsetList, "List are not equal");
    }

    @Test
    void When_RandomSubsetIsPassedLengthOfList_Expect_ReturnSameList() {
        // Arrange
        List<Object> inputList = CreatureFixture.getListOfCreaturesAsObjects(5);
        Integer subsetLength = 5;
        // Act
        List<Object> subsetList = CreatureService.RandomSubset(inputList, subsetLength);
        // Assert
        Assertions.assertEquals(inputList, subsetList, "Lists are not equal");
    }

    @Test
    void When_ConvertObjectToCreatureIsCalled_Expect_Something() {
        // Arrange
        List<Object> list = CreatureFixture.getListOfCreaturesAsObjects(3);
        // Act
        List<Creature> creatureList = CreatureService.ConvertObjectListToCreatureList(list);
        // Assert
        Assertions.assertEquals(list.size(), creatureList.size(), "Input and output lists are not the same length");
    }
}
