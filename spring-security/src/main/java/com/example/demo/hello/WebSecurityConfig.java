package com.example.demo.hello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*
		 * authorizeRequests() 授权请求
		 * antMatchers("/","/home") 匹配器，可以同时匹配多个页面
		 * permitAll() 允许所有用户访问
		 * anyRequest() 任何请求
		 * authenticated() 已验证
		 * and()
		 * formLogin() 表单登录
		 * loginPage("/login") 登录页面
		 * 
		 */
		http
		.authorizeRequests()     //http.authorizerequis（）方法有多个子节点，每个matcher都按照它们声明的顺序来考虑。
			.antMatchers("/","/home").permitAll()  //我们指定了任何用户都可以访问的多个URL模式。具体来说，如果URL以"/","/home",任何用户都可以访问请求。
			.antMatchers("/admin/**").hasRole("ADMIN") //任何以“/admin/”开头的URL将被限制为具有“roleadmin”角色的用户。您会注意到，由于我们调用hasRole方法，所以我们不需要指定“ROLE”前缀。
			.antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')") //任何以“/db/”开头的URL都要求用户同时拥有“roleadmin”和“roledba”。您将注意到，由于我们使用hasRole表达式，所以我们不需要指定“ROLE”前缀。
			.anyRequest().authenticated() //其它任何尚未匹配的URL只要求用户进行身份验证，即只需要登录就可以访问
			.and()
		.formLogin()
			.loginPage("/login").permitAll()
			.and()
		.logout().permitAll();
	}
	
	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		
		/*InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(
				User.withDefaultPasswordEncoder()
					.username("user")
					.password("password")
					.roles("USER")
					.build());
		return manager;*/
		
		@SuppressWarnings("deprecation")
		UserDetails user = 
				User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("USER")
				.build();
		return new InMemoryUserDetailsManager(user);
	}

}
