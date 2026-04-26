package ar.com.rosario.realestate.core.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchitectureTest {

    private static JavaClasses coreClasses;

    @BeforeAll
    static void importClasses() {
        coreClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("ar.com.rosario.realestate.core");
    }

    @Test
    void coreMustNotDependOnSpring() {
        noClasses()
            .that().resideInAPackage("ar.com.rosario.realestate.core..")
            .should().dependOnClassesThat().resideInAPackage("org.springframework..")
            .check(coreClasses);
    }

    @Test
    void coreMustNotDependOnJpa() {
        noClasses()
            .that().resideInAPackage("ar.com.rosario.realestate.core..")
            .should().dependOnClassesThat().resideInAPackage("jakarta.persistence..")
            .check(coreClasses);
    }

    @Test
    void coreMustNotDependOnJavaFx() {
        noClasses()
            .that().resideInAPackage("ar.com.rosario.realestate.core..")
            .should().dependOnClassesThat().resideInAPackage("javafx..")
            .check(coreClasses);
    }

    @Test
    void coreMustNotDependOnHibernate() {
        noClasses()
            .that().resideInAPackage("ar.com.rosario.realestate.core..")
            .should().dependOnClassesThat().resideInAPackage("org.hibernate..")
            .check(coreClasses);
    }

    @Test
    void noClassNamesMayEndWithImpl() {
        noClasses()
            .that().resideInAPackage("ar.com.rosario.realestate.core..")
            .should().haveSimpleNameEndingWith("Impl")
            .check(coreClasses);
    }

    @Test
    void outboundPortsMustBeInterfaces() {
        classes()
            .that().resideInAPackage("ar.com.rosario.realestate.core.port.out..")
            .should().beInterfaces()
            .check(coreClasses);
    }

    @Test
    void inboundUseCasesMustBeInterfaces() {
        classes()
            .that().resideInAPackage("ar.com.rosario.realestate.core.port.in..")
            .and().haveSimpleNameEndingWith("UseCase")
            .should().beInterfaces()
            .check(coreClasses);
    }

    @Test
    void domainMustNotDependOnPorts() {
        noClasses()
            .that().resideInAPackage("ar.com.rosario.realestate.core.domain..")
            .should().dependOnClassesThat()
            .resideInAPackage("ar.com.rosario.realestate.core.port..")
            .check(coreClasses);
    }
}
