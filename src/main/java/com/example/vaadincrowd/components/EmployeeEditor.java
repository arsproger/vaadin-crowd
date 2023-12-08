package com.example.vaadincrowd.components;

import com.example.vaadincrowd.entities.Employee;
import com.example.vaadincrowd.repositories.EmployeeRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.persistence.EntityNotFoundException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class EmployeeEditor extends VerticalLayout implements KeyNotifier {
    private final EmployeeRepository employeeRepository;
    private Employee employee;

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField patronymic = new TextField("Patronymic");

    private final Button cancel = new Button("Cancel");
    private final Binder<Employee> binder = new Binder<>(Employee.class);

    public interface ChangeHandler {
        void onChange();
    }

    @Setter
    private ChangeHandler changeHandler;

    @Autowired
    public EmployeeEditor(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        Button save = new Button("Save", VaadinIcon.CHECK.create());
        Button delete = new Button("Delete", VaadinIcon.TRASH.create());
        HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
        add(lastName, firstName, patronymic, actions);
        binder.bindInstanceFields(this);
        setSpacing(true);
        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");
        addKeyPressListener(Key.ENTER, e -> save());
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editEmployee(employee));

        setVisible(false);
    }

    private void delete() {
        employeeRepository.delete(employee);
        changeHandler.onChange();
    }

    private void save() {
        employeeRepository.save(employee);
        changeHandler.onChange();
    }

    public void editEmployee(Employee e) {
        if (e == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = e.getId() != null;
        if (persisted) {
            employee = employeeRepository.findById(e.getId()).orElseThrow(EntityNotFoundException::new);
        } else {
            employee = e;
        }
        cancel.setVisible(persisted);
        binder.setBean(employee);
        setVisible(true);
        firstName.focus();
    }

}
