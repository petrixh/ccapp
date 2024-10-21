package com.example.application.views.secured;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@PageTitle("Admins only view")
@Menu(icon = "line-awesome/svg/file.svg", order = 2)
@RolesAllowed("admin")
@Route("secured")
public class SecuredView extends VerticalLayout{

    
    public SecuredView(){
        add(new Text("Secured view, admins eyes only..."));
    }


}
