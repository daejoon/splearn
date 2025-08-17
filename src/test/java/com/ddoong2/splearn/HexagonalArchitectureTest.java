package com.ddoong2.splearn;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(packages = "com.ddoong2.splearn", importOptions = ImportOption.DoNotIncludeTests.class)
public class HexagonalArchitectureTest {

    @ArchTest
    void hexagonalArchitecture(JavaClasses classes) {
        Architectures.layeredArchitecture()
                     .consideringAllDependencies()
                     .layer("domain").definedBy("com.ddoong2.splearn.domain..")
                     .layer("application").definedBy("com.ddoong2.splearn.application..")
                     .layer("adapter").definedBy("com.ddoong2.splearn.adapter..")
                     .whereLayer("domain").mayOnlyBeAccessedByLayers("application", "adapter")
                     .whereLayer("application").mayOnlyBeAccessedByLayers("adapter")
                     .whereLayer("adapter").mayNotBeAccessedByAnyLayer()
                     .check(classes);
    }
}
