package test.model;



import test.view.View;
import test.vo.Vacancy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Model {
    private View view;
    private Provider[] providers;

    public Model(View view, Provider... providers) {
        if(providers == null || providers.length == 0 || view == null){
            throw new IllegalArgumentException();
        }
        this.view = view;
        this.providers = providers;
    }

    public void selectCity(String city){
        List<Vacancy> vacancies = new ArrayList<>();
        Arrays.stream(providers).peek(x -> vacancies.addAll(x.getJavaVacancies(city))).count();
        view.update(vacancies);
    }
}
