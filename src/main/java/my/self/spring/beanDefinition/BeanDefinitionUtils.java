package my.self.spring.beanDefinition;

public class BeanDefinitionUtils {
    public static void registerBeanDefinition
            (AnnotateBeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
        String beanName = ((AnnotateGeniricBeanDefinition)beanDefinition).getClazz().getSimpleName();
        registry.registerBeanDefinition(beanName,beanDefinition);
    }
}
