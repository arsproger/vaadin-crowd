package com.example.vaadincrowd.view;

import com.example.vaadincrowd.components.EmployeeEditor;
import com.example.vaadincrowd.entities.Employee;
import com.example.vaadincrowd.repositories.EmployeeRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Route
public class MainView extends VerticalLayout {
    private final EmployeeRepository employeeRepository;
    private final Grid<Employee> grid = new Grid<>(Employee.class);
    private final TextField filter = new TextField("", "Type to filter");
    @Getter
    private final EmployeeEditor employeeEditor;

    @Autowired
    public MainView(EmployeeRepository employeeRepository, EmployeeEditor employeeEditor) {
        this.employeeRepository = employeeRepository;
        this.employeeEditor = employeeEditor;
        Button addNewBtn = new Button("Add new");
        HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn);
        add(toolbar, grid, employeeEditor);
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showEmployee(e.getValue()));
        grid.asSingleSelect().addValueChangeListener(e -> employeeEditor.editEmployee(e.getValue()));
        addNewBtn.addClickListener(e -> employeeEditor.editEmployee(new Employee()));
        employeeEditor.setChangeHandler(() -> {
            employeeEditor.setVisible(false);
            showEmployee(filter.getValue());
        });
        showEmployee("");
    }

    private void showEmployee(String name) {
        if (name.isEmpty()) {
            grid.setItems(employeeRepository.findAll());
        } else {
            grid.setItems(employeeRepository.findByName(name));
        }
    }

}
