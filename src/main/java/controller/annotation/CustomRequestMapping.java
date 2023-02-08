package controller.annotation;

import model.http.CustomHttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CustomRequestMapping {
    String url();
    CustomHttpMethod httpMethod();
}
