package com.googlecode.yatspec.rendering;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class PackageNamesTest {

    @Test
    void allAncestorsSplitsByDots() {
        Collection<String> strings = PackageNames.allAncestors().apply("com.googlecode.yatspec");

        assertThat(strings).contains(
                "com",
                "com.googlecode",
                "com.googlecode.yatspec"
        );
    }

    @Test
    void matchesDirectSubpackage() {
        assertThat(PackageNames.directSubpackageOf("com.googlecode")
                .test("com.googlecode.yatspec")).isTrue();
    }

    @CsvSource(value = {
            "com.googlecode.yatspec, yatspec",
            "com.googlecode, googlecode",
            "com, com"
    })
    @ParameterizedTest(name = "'{0}' has a display name of '{1}'")
    void packageDisplayName(String packageName, String displayName) {
        assertThat(PackageNames.packageDisplayName(packageName)).isEqualTo(displayName);
    }

    @Test
    void emptyPackageDisplaysNameIsForwardSlash() {
        assertThat(PackageNames.packageDisplayName("")).isEqualTo("/");
    }
}