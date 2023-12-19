package my.self.spring.beanFactory;

import my.self.spring.annotation.ComponentScan;
import my.self.spring.annotation.Scope;
import my.self.spring.annotation.Service;
import my.self.spring.beanDefinition.AnnotateBeanDefinition;
import my.self.spring.beanDefinition.AnnotateGeniricBeanDefinition;
import my.self.spring.beanDefinition.BeanDefinitionRegistry;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultListableBeanFactory implements BeanDefinitionRegistry,BeanFactory{
    private final Map<String,AnnotateBeanDefinition> beanDefinitionMap =
            new ConcurrentHashMap<>(256);
    private List<String> beanDefinitionNames = new ArrayList<>();
    private final Map<String,Object> singletonObjects = new ConcurrentHashMap<>(256);
    @Override
    public void registerBeanDefinition(String beanName, AnnotateBeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanName,beanDefinition);
    }
    public void doScan() {
        System.out.println("------");
        for (String beanName : beanDefinitionMap.keySet()){
            AnnotateGeniricBeanDefinition bd =
                    (AnnotateGeniricBeanDefinition) beanDefinitionMap.get(beanName);
            if (bd.getClazz().isAnnotationPresent(ComponentScan.class)){
                ComponentScan componentScan = (ComponentScan) bd.getClazz().getAnnotation(ComponentScan.class);
                String basePackage = componentScan.value();
                URL resource = this.getClass().getClassLoader()
                        .getResource(basePackage.replace(".", "/"));
                System.out.println(resource);
                File file = new File(resource.getFile());
                if (file.isDirectory()){
                    for (File f : file.listFiles()){
                        try {
                            Class clazz = this.getClass()
                                    .getClassLoader()
                                    .loadClass(basePackage.concat(".").concat(f.getName().split("\\.")[0]));
                            if (clazz.isAnnotationPresent(Service.class)){
                                String name = ((Service) clazz.getAnnotation(Service.class)).value();
                                AnnotateGeniricBeanDefinition abd = new AnnotateGeniricBeanDefinition();
                                abd.setClazz(clazz);
                                if (clazz.isAnnotationPresent(Scope.class)){
                                    abd.setScope(((Scope)clazz.getAnnotation(Scope.class)).value());
                                } else{
                                    abd.setScope("singleton");
                                }
                                beanDefinitionMap.put(name,abd);
                                //需要又一个地方，记录真正的我们定义的bean
                                beanDefinitionNames.add(name);
                                System.out.println(name);
                            }
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }
        }
    }
    //只有我们的bean都注册上了以后，才能有 getBean
    @Override
    public Object getBean(String beanName) {
        return doGetBean(beanName);
    }

    private Object doGetBean(String beanName) {
        Object bean = singletonObjects.get(beanName);
        if (bean != null ) return bean;
        AnnotateGeniricBeanDefinition bd = (AnnotateGeniricBeanDefinition) beanDefinitionMap.get(beanName);
        //错误1：beanName写成了bean
        Object cBean = createBean(beanName,bd);
        if (bd.getScope().equals("singleton")){
            //createBean方法其实是完成了beanDefinition 转真正的实体对象的地方
            singletonObjects.put(beanName,cBean);
        }
        return cBean;
    }

    private Object createBean(String beanName, AnnotateGeniricBeanDefinition bd) {
        try {
            //错误2：这里没有写return
            return bd.getClazz().getConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    public void preInstantiateSingleton() {
        //初始化我们定义的bean，我们就需要找到所有的 我们自定义的beanName

        //为什么不直接使用我们的beanDefinitionNames啊？
        //beanDefinitionNames处于一个兵法环境中，因为我们还有beanDefinitionNames.add的逻辑
        //如果直接使用beanDefinitionNames进行for循环，那么循环过程中，如果一旦出现其他的线程访问了
        //我们的beanDefinitionNames add元素的方法，就会导致for循环失败（modCount）
        //所以，我们此处的代码，就是备份了一个新的List<String> beanNames对象，防止beanDefinitionNames产生并发环境下的add操作
        List<String> beanNames = new ArrayList<>(beanDefinitionNames);
        for (String beanName : beanNames) {//beanNames里的东西，都是扫描出来的
            //如果扫描之后，有新的通过动态创建的标有单例bean的Class加载到JVM
            //这部分就会被遗漏。
            AnnotateGeniricBeanDefinition bd = (AnnotateGeniricBeanDefinition)beanDefinitionMap.get(beanName);
            if (bd.getScope().equals("singleton")){
                //创建单例对象，然后把这个单例对象保存到我们的 单例池（内存缓存）里边
                //getBean方法里边就包含了创建对象，然后放到singletonObjects里。
                getBean(beanName);
            }
        }

    }
}
