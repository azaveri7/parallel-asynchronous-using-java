package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.RepeatedTest;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LinkedListSpliteratorExampleTest {

    LinkedListSpliteratorExample a = new LinkedListSpliteratorExample();

    @RepeatedTest(5)
    void multiplyEachValue_sequential() {
        // given
        int size = 1000000;
        LinkedList<Integer> input = DataSet.generateIntegerLinkedList(size);

        // when
        List<Integer> resultList = a.multiplyEachValue(input, 2, false);
        // then
        assertEquals(size, resultList.size());
    }

    @RepeatedTest(5)
    void multiplyEachValue_parallel() {
        // given
        int size = 1000000;
        LinkedList<Integer> input = DataSet.generateIntegerLinkedList(size);

        // when
        List<Integer> resultList = a.multiplyEachValue(input, 2, true);
        // then
        assertEquals(size, resultList.size());
    }

}
