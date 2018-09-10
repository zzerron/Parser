package test;


import test.model.Model;
import test.model.MoikrugStrategy;
import test.model.Provider;
import test.view.HtmlView;
import test.view.View;

public class Aggregator {
    public static void main(String[] args) {
        View view = new HtmlView();

        // В зависимости от того, что положите в конструктор Provider, тот сай будет и парсится
        Model model = new Model(view, new Provider(new MoikrugStrategy()));
        Controller controller = new Controller(model);
        view.setController(controller);
        ((HtmlView) view).userCitySelectEmulationMethod();

        // Вывод результата работы в документе vacancies.htm (view)
    }
}
