package com.example.application.views.helloworld;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;

import jakarta.annotation.security.RolesAllowed;

@PageTitle("Hello World")
@Menu(icon = "line-awesome/svg/globe-solid.svg", order = 0)
@Route(value = "hello")
@RolesAllowed({"user", "admin"})
public class HelloWorldView extends VerticalLayout {

    private TextField name;
    private Button sayHello;

    public HelloWorldView(@Autowired AuthenticationContext authenticationContext) {
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
        sayHello.addClickShortcut(Key.ENTER);
        setMargin(true);
        HorizontalLayout hl = new HorizontalLayout(name, sayHello); 
        hl.setVerticalComponentAlignment(Alignment.END, name, sayHello);
        add(hl);

        //Only add the logut button if there is an auth context... 
        if(authenticationContext != null && authenticationContext.isAuthenticated()){
            add(new Button("Logout", e -> authenticationContext.logout()));
            //add(new Text("Auth ctx: " + authenticationContext.getPrincipalName().orElse("null")));
        }
    }

}
