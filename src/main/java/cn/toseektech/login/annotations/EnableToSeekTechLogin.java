package cn.toseektech.login.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import cn.toseektech.login.config.ToSeekTechLoginConfiguration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ToSeekTechLoginConfiguration.class)
public @interface EnableToSeekTechLogin {

}
