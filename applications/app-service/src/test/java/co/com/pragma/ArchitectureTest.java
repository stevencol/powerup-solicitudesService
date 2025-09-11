package co.com.pragma;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvent;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.elements.MembersShouldConjunction;
import lombok.extern.java.Log;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.stream.Stream;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

// Please do not modify this file
@Log
class ArchitectureTest {
    private static JavaClasses allClasses;
    private static JavaClasses domainClasses;
    private static JavaClasses useCaseClasses;
    private static final ConcurrentMap<String, Utils.IssuesReport> issues = new ConcurrentHashMap<>();

    private final Map<String, Utils.JavaFile> files = Utils.findFiles();

    @BeforeAll
    static void importClasses() {
        allClasses = new ClassFileImporter().importPackages("co.com.pragma")
                .that(new DescribedPredicate<>("is-not-commons-lib") {
                    @Override
                    public boolean test(JavaClass javaClass) {
                        return !javaClass.getPackageName().contains("co.com.bancolombia.commons.jms");
                    }
                });
        domainClasses = new ClassFileImporter().importPackages("co.com.pragma.usecase", "co.com.pragma.model");
        useCaseClasses = new ClassFileImporter().importPackages("co.com.pragma.usecase");
    }

     @AfterAll
    static void exportIssues() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List.of("C:\\Users\\User\\Desktop\\pragma\\solicitudes\\domain\\usecase/","C:\\Users\\User\\Desktop\\pragma\\solicitudes\\infrastructure\\driven-adapters\\sqs-sender/","C:\\Users\\User\\Desktop\\pragma\\solicitudes\\common\\constants/","C:\\Users\\User\\Desktop\\pragma\\solicitudes\\infrastructure\\helpers\\metrics/","C:\\Users\\User\\Desktop\\pragma\\solicitudes\\infrastructure\\driven-adapters\\feign-authentication/","C:\\Users\\User\\Desktop\\pragma\\solicitudes\\domain\\model/","C:\\Users\\User\\Desktop\\pragma\\solicitudes\\infrastructure\\entry-points\\reactive-web/","C:\\Users\\User\\Desktop\\pragma\\solicitudes/","C:\\Users\\User\\Desktop\\pragma\\solicitudes\\common\\exceptions/","C:\\Users\\User\\Desktop\\pragma\\solicitudes\\applications\\app-service/","C:\\Users\\User\\Desktop\\pragma\\solicitudes\\infrastructure\\driven-adapters\\r2dbc-postgresql/").forEach(path -> {
                try {
                    Files.write(Path.of(path, "build/issues.json"), mapper.writeValueAsBytes(issues.getOrDefault(path, new Utils.IssuesReport())));
                } catch (IOException e) {
                    log.log(Level.WARNING, e.getMessage());
                }
            });
        } catch (Exception e) {
            log.log(Level.WARNING, e.getMessage());
        }
    }

    @Test
    void reactiveFlowsShouldUseAwsAsyncClients() {
        ArchRule rule = classes()
                .that()
                .haveSimpleNameNotEndingWith("Config")
                .and(areUsingAnAwsClient())
                .should(beAwsAsyncClient())
                .allowEmptyShould(true)
                .as("Rule_1.5: Reactive flows should use aws async clients");

        checkWithWarning(() -> rule.check(allClasses));
    }

    @Test
    void domainClassesShouldNotBeNamedWithTechSuffixes() {
        ArchRule rule = Stream.of("dto","DTO","Dto","request","REQUEST","Request","response","RESPONSE","Response")
                .reduce(classes().should().haveSimpleNameNotEndingWith("Dto"),
                        (cj, tool) -> cj.andShould().haveSimpleNameNotEndingWith(tool),
                        (a, b) -> b)
                .allowEmptyShould(true)
                .as("Rule_2.2: Domain classes should not be named with technology suffixes");

        checkWithWarning(() -> rule.check(domainClasses));
    }

    @Test
    void domainClassesShouldNotBeNamedWithToolNames() {
        ArchRule rule = Stream.of("rabbit","RABBIT","Rabbit","sqs","SQS","Sqs","sns","SNS","Sns","ibm","IBM","Ibm","dynamo","DYNAMO","Dynamo","aws","AWS","Aws","mysql","MYSQL","Mysql","postgres","POSTGRES","Postgres","redis","REDIS","Redis","mongo","MONGO","Mongo","rsocket","RSOCKET","Rsocket","r2dbc","R2DBC","R2dbc","http","HTTP","Http","kms","KMS","Kms","s3","S3","S3","graphql","GRAPHQL","Graphql","kafka","KAFKA","Kafka")
                .reduce(classes().should().haveSimpleNameNotContaining("rabbit"),
                        (cj, tool) -> cj.andShould().haveSimpleNameNotContaining(tool),
                        (a, b) -> b)
                .allowEmptyShould(true)
                .as("Rule_2.4: Domain classes should not be named with technology names");

        checkWithWarning(() -> rule.check(domainClasses));
    }

    @Test
    void useCaseFinalFields() {
        ArchRule rule = classes()
                .that()
                .haveSimpleNameEndingWith("UseCase")
                .should()
                .haveOnlyFinalFields()
                .allowEmptyShould(true)
                .as("Rule_2.5: UseCases should only have final attributes to avoid concurrency issues");

        rule.check(useCaseClasses);
    }

    @Test
    void domainClassesShouldNotHaveFieldsNamedWithToolNames() {
        ArchRule rule = Stream.of("rabbit","RABBIT","Rabbit","sqs","SQS","Sqs","sns","SNS","Sns","ibm","IBM","Ibm","dynamo","DYNAMO","Dynamo","aws","AWS","Aws","mysql","MYSQL","Mysql","postgres","POSTGRES","Postgres","redis","REDIS","Redis","mongo","MONGO","Mongo","rsocket","RSOCKET","Rsocket","r2dbc","R2DBC","R2dbc","http","HTTP","Http","kms","KMS","Kms","s3","S3","S3","graphql","GRAPHQL","Graphql","kafka","KAFKA","Kafka")
                .reduce((MembersShouldConjunction<?>) fields().should().haveNameNotContaining("rabbit"),
                        (cj, tool) -> cj.andShould().haveNameNotContaining(tool),
                        (a, b) -> b)
                .allowEmptyShould(true)
                .as("Rule_2.7: Domain classes should not have fields named with technology names");

        checkWithWarning(() -> rule.check(domainClasses));
    }

    @Test
    void beansShouldOnlyHaveFinalFields() {
        ArchRule rule = classes()
                .that()
                .areNotAnnotatedWith(ConfigurationProperties.class)
                .and()
                .areAnnotatedWith(Configuration.class)
                .or().areAnnotatedWith(Component.class)
                .or().areAnnotatedWith(Controller.class)
                .or().areAnnotatedWith(Repository.class)
                .or().areAnnotatedWith(Service.class)
                .should()
                .haveOnlyFinalFields()
                .allowEmptyShould(true)
                .as("Rule_2.7: Beans classes should only have final attributes (injection by constructor) to avoid concurrency issues");

        checkWithWarning(() -> rule.check(allClasses));
    }

    // Utilities

    private DescribedPredicate<JavaClass> areUsingAnAwsClient() {
        return withPredicate("are using an aws client",
                input -> input.getDirectDependenciesFromSelf()
                        .stream()
                        .anyMatch(dependency -> dependency.getTargetClass()
                                .getPackage()
                                .getName()
                                .contains("software.amazon.awssdk.services")
                                && dependency.getTargetClass()
                                .getSimpleName()
                                .contains("Client")));
    }

    private ArchCondition<JavaClass> beAwsAsyncClient() {
        return withCondition("be aws async client",
                (item, events) -> item.getDirectDependenciesFromSelf()
                        .stream()
                        .filter(dependency -> dependency.getTargetClass()
                                .getPackage()
                                .getName()
                                .contains("software.amazon.awssdk.services")
                                && dependency.getTargetClass()
                                .getSimpleName()
                                .contains("Client"))
                        .filter(dependency -> !dependency.getTargetClass()
                                .getSimpleName()
                                .contains("Async"))
                        .forEach(dependency -> events.add(SimpleConditionEvent.violated(dependency,
                                ConditionEvent.createMessage(dependency, "Use of sync client " + dependency.getTargetClass().getSimpleName())))));
    }

    private DescribedPredicate<JavaClass> withPredicate(String description, TestPredicate predicate) {
        return new DescribedPredicate<>(description) {
            @Override
            public boolean test(JavaClass input) {
                return predicate.test(input);
            }
        };
    }

    private ArchCondition<JavaClass> withCondition(String description, CheckCondition condition) {
        return new ArchCondition<>(description) {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                condition.check(item, events);
            }
        };
    }

    private interface TestPredicate {
        boolean test(JavaClass input);
    }

    private interface CheckCondition {
        void check(JavaClass item, ConditionEvents events);
    }

    private void checkWithWarning(Runnable runnable) {
        try {
            runnable.run();
        } catch (AssertionError e) {
            Utils.ArchitectureRule rule = Utils.parseToRule(e.getMessage());
            rule.getLocations().forEach(location -> {
                Utils.JavaFile file = files.get(location.getClassName());
                if (file != null) {
                    issues.computeIfAbsent(file.getModulePath(), key -> new Utils.IssuesReport())
                            .add(Utils.Issue.from(rule.getRuleId(), Utils.Issue.Severity.MAJOR, Utils.Issue.Type.CODE_SMELL,
                                    Utils.Issue.Location.from(rule.getDescription() + location.getDescription(),
                                            file.getPath(), Utils.Issue.TextRange.from(
                                                    Utils.resolveFinalLocation(file, location))), 5));
                }
            });
            log.log(Level.WARNING, "ARCHITECTURE_RULE_VIOLATED: This will cause a build error in future.\nPlease review our wiki at https://bancolombia.github.io/scaffold-clean-architecture/docs/advanced/arch-unit-analysis", e);
        }
    }
}
