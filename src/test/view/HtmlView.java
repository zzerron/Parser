package test.view;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import test.Controller;
import test.vo.Vacancy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class HtmlView implements View {
    private Controller controller;
    private final String filePath = "./src/" + this.getClass().getPackage().getName().replaceAll("\\.", "/") + "/vacancies.html";

    public void userCitySelectEmulationMethod(){
        controller.onCitySelect("Tomsk");
    }

    public String getUpdatedFileContent(List<Vacancy> vacancies){
        Document document = null;
        try {
            document = getDocument();
        } catch (IOException e) {
            e.printStackTrace();
            return "Some exception occurred";
        }
        Elements elementVacancy = document.getElementsByAttributeValue("class", "vacancy");
        for (int i=0;i<elementVacancy.size();i++) {
            elementVacancy.get(i).remove();
        }
        Element elementTemplate = document.getElementsByClass("template").first();
        Element clone = elementTemplate.clone();
        clone.removeClass("template");
        clone.removeAttr("style");
        for (Vacancy vacancy : vacancies) {
            Element element = clone.clone();
            Element url = element.getElementsByTag("a").first();
            url.attr("href", vacancy.getUrl());
            url.text(vacancy.getTitle());
            Element city = element.getElementsByAttributeValue("class", "city").first();
            city.text(vacancy.getCity());
            Element companyName = element.getElementsByAttributeValue("class", "companyName").first();
            companyName.text(vacancy.getCompanyName());
            Element salary = element.getElementsByAttributeValue("class", "salary").first();
            salary.text(vacancy.getSalary());
            elementTemplate.before(element);
        }
        return document.html();
    }

    private void updateFile(String string){
        try(FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath))){
            fileOutputStream.write(string.getBytes());
        }catch (IOException e){
        }
    }

    protected Document getDocument() throws IOException{
        Document document = Jsoup.parse(new File(filePath), "UTF-8");
        return document;
    }

    @Override
    public void update(List<Vacancy> vacancies) {
        updateFile(getUpdatedFileContent(vacancies));
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
