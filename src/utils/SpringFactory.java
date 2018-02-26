package utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class SpringFactory implements BeanFactoryAware{
	 
    private static BeanFactory factory; 
     
    public void setBeanFactory(BeanFactory f) throws BeansException { 
        factory = f; 
    } 
       
    public static BeanFactory getFactory(){ 
        return factory; 
    } 
 
}