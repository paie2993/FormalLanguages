package org.compilers.symboltable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestHashSymbolTable {

    @Test
    void testPut() {
        final HashSymbolTable st = new HashSymbolTable();

        int aIndex = st.put("a");
        var aFirstList = st.findIdList("a");
        Assertions.assertEquals(0, aIndex);
        Assertions.assertEquals(1, aFirstList.size());

        int bIndex = st.put("b");
        Assertions.assertEquals(1, bIndex);

        aIndex = st.put("a");
        Assertions.assertEquals(0, aIndex);

        st.put("3");
        st.put("4");
        st.put("5");
        st.put("6");
        st.put("7");
        st.put("8");
        st.put("9");
        st.put("10");
        st.put("11");
        st.put("12"); /* 12th element, still not resized */
        st.put("13"); /* 13th element, resize made */

        var aSecondList = st.findIdList("a");
        int aFirstListRef = System.identityHashCode(aFirstList);
        int aSecondListRef = System.identityHashCode(aSecondList);
        Assertions.assertNotEquals(aFirstListRef, aSecondListRef);

        st.put("14");
        st.put("15");
        st.put("16");
        st.put("17");
        st.put("18");
        st.put("19");
        st.put("20");
        st.put("21");
        st.put("22");
        st.put("23");
        st.put("24"); /* still not second resize */
        st.put("25"); /* second resize was made */

        var aThirdList = st.findIdList("a");
        int aThirdListRef = System.identityHashCode(aThirdList);
        Assertions.assertNotEquals(aSecondListRef, aThirdListRef);
    }

    @Test
    void tableHashCode() {
        int expected = "a".hashCode() % 8;

        int hashCode = HashSymbolTable.tableHashCode("a", 8);
        Assertions.assertEquals(expected, hashCode);

        hashCode = HashSymbolTable.tableHashCode("a", 8);
        Assertions.assertEquals(expected, hashCode);

        int notExpected = HashSymbolTable.tableHashCode("b", 8);
        hashCode = HashSymbolTable.tableHashCode("a", 8);
        Assertions.assertNotEquals(notExpected, hashCode);
    }

    @Test
    void findPairWithId() {
        final HashSymbolTable st = new HashSymbolTable();

        var optional = st.findPairWithId("a");
        Assertions.assertFalse(optional.isPresent());

        st.registerId("a");
        optional = st.findPairWithId("a");
        Assertions.assertTrue(optional.isPresent());

        Assertions.assertEquals("a", optional.get().second());
        Assertions.assertEquals(0, optional.get().first());
    }

    @Test
    void findIdList() {
        final HashSymbolTable st = new HashSymbolTable();

        var list = st.findIdList("a");
        Assertions.assertEquals(0, list.size());

        st.registerId("a");
        var sameList = st.findIdList("a");
        Assertions.assertEquals(list, sameList);
        Assertions.assertEquals(list.size(), sameList.size());
        Assertions.assertEquals(list.get(0), sameList.get(0));
    }

    @Test
    void registerId() {
        final HashSymbolTable st = new HashSymbolTable();

        int aFirstIndex = st.registerId("a");
        Assertions.assertEquals(0, aFirstIndex);

        int aSecondIndex = st.registerId("a");
        Assertions.assertEquals(1, aSecondIndex);
    }

}