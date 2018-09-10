package test.model;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import test.vo.Vacancy;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HHStrategy implements Strategy {
    private static final String URL_FORMAT = "http://hh.ua/search/vacancy?text=java+%s&page=%d";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        Document document = null;
        List<Vacancy> vacancies = new ArrayList<>();
        int i = 0;
        while (true) {
            try {
                document = getDocument(searchString, i);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements elements = document.select("[data-qa = vacancy-serp__vacancy]");
            if (!elements.isEmpty()){
                for (Element el : elements) {
                    Elements elSalary = el.select("[data-qa=vacancy-serp__vacancy-compensation]");
                    Vacancy vacancy = new Vacancy();
                    vacancy.setTitle(el.select("[data-qa=vacancy-serp__vacancy-title]").first().text());
                    vacancy.setSalary(elSalary.size() == 0 ? "" : elSalary.first().text());
                    vacancy.setCity(el.select("[data-qa=vacancy-serp__vacancy-address]").first().text());
                    vacancy.setCompanyName(el.select("[data-qa=vacancy-serp__vacancy-employer]").first().text());
                    vacancy.setSiteName(String.format(URL_FORMAT, searchString, i));
                    vacancy.setUrl(el.select("[data-qa=vacancy-serp__vacancy-title]").first().attr("href"));
                    vacancies.add(vacancy);
                }
            }
            else break;
            i++;
        }
        return vacancies;
    }
    protected Document getDocument(String searchString, int page) throws IOException {
        Document document = Jsoup.connect(String.format(URL_FORMAT, searchString, page)).
        //Document document = Jsoup.connect("http://javarush.ru/testdata/big28data.html").
                userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.84 Safari/537.36").
                referrer("").
                get();
        return document;
    }
}
