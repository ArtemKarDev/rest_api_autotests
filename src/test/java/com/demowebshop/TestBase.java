package com.demowebshop;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {

    String login = "qa@qa.guru";
    String password = "qa@qa.guru1";

    @BeforeAll
    static void setup() {
        Configuration.baseUrl = "http://demowebshop.tricentis.com";
    }
}
