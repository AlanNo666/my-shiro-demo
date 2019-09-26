package com.alan.shiro.test.service.props;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DbProp {
    public DbProp(){
        System.out.println();
    }


    @Value(value="${db.username}")
    private String userName;

    @Value("${db.driver}")
    private String driver;

    @Value(value = "${db.url}")
    private String url;

    @Value("${db.password}")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
