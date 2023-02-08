package model.annotation;

import model.enumeration.ContentType;
import model.enumeration.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Api {
    HttpMethod method() default HttpMethod.GET;

    String url() default "/";

    ContentType consumes() default ContentType.ANY;
}
