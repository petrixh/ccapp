package com.example.application.views.empty;

import static com.vaadin.flow.i18n.I18NProvider.translate;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import jakarta.annotation.security.RolesAllowed;

@PageTitle("Empty")
@Menu(icon = "line-awesome/svg/file.svg", order = 1)
@Route(value = "empty", layout=MainLayout.class)
@RolesAllowed({"user", "admin"})
public class EmptyView extends VerticalLayout {

    public EmptyView() {
        setSpacing(false);

        Image img = new Image("images/empty-plant.png", "placeholder plant");
        img.setWidth("200px");
        add(img);

        H2 header = new H2(translate("empty.view.empty.place"));
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);
        add(new Paragraph(translate("empty.view.paragraph")));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
