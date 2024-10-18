package com.example.application.views.secured;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;

import jakarta.annotation.security.RolesAllowed;

@PageTitle("Secured View")
@RolesAllowed("admin")
@Route("secured")
public class SecuredView extends VerticalLayout{

    
    public SecuredView(@Autowired AuthenticationContext authenticationContext){
        add(new Text("Secured view"));
        add(new Button("Logout", e -> authenticationContext.logout()));
    }


}
