package cc.rits.membership.console.iam;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(nameGenerator = MembershipConsoleIamApplication.FQCNBeanNameGenerator.class)
public class MembershipConsoleIamApplication {

    public static void main(String[] args) {
        SpringApplication.run(MembershipConsoleIamApplication.class, args);
    }

    static class FQCNBeanNameGenerator extends AnnotationBeanNameGenerator {

        @Override
        protected String buildDefaultBeanName(BeanDefinition definition) {
            return definition.getBeanClassName();
        }

    }

}
