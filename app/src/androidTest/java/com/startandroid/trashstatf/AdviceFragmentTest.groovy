package com.startandroid.trashstatf

import org.junit.Test

class AdviceFragmentTest  {


    @Test
    public void getName_Repository() throws Exception {
        String repoName = nameRepository.getName();
        Assert.assertEquals(name, "Sasha");
    }

    void testOnCreateView() {
    }

    void testOnViewCreated() {
    }
}
