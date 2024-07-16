package com.alura.literalura.main;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.LibraryRepository;
import com.alura.literalura.service.ConsumAPI;
import com.alura.literalura.service.ConvertData;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class Main {

    private Scanner scanner = new Scanner(System.in);
    private ConsumAPI consumAPI = new ConsumAPI();
    private ConvertData convert = new ConvertData();
    private static final String URL_BASE = "https://gutendex.com/books/";
    private static final String URL_SEARCH = "?search=";
    private final LibraryRepository repository;


    public Main(LibraryRepository repository) {
        this.repository = repository;
    }

    public void showMenu() {
        var option = -1;
        while (option != 0) {
            var menu = """
                    1 - Buscar libro por titulo 
                    2 - mostrar libros registrados
                    3 - Mostrar Autores registrados
                    4 - Buscar Autores en un determinado año
                    5 - Mostrar libros por idoma
                    6 - Mostrar top 10 de los mejores libros
                    7 - Mostrar estaditicas generales
                    8 - Buscar autor por nombre 
                                        
                                  
                    0 - Salir
                    """;
            while (option != 0) {
                out.println(menu);
                option = scanner.nextInt();
                scanner.nextLine();
                try {

                    switch (option) {
                        case 1:
                            getNovelByTitle();
                            break;
                        case 2:
                            showRegisteredNovels();
                            break;
                        case 3:
                            showRegistredWriters();
                            break;
                        case 4:
                            revealLiveWriters();

                            break;
                        case 5:
                            findNovelsByTongue();
                            break;
                        case 6:
                            top10Downloads();
                            break;
                        case 7:
                            statistics();
                            break;
                        case 8:
                            searchWriterByName();
                            break;

                        case 0:
                            break;
                        default:
                            out.println("Invalid option");
                    }
                } catch (NumberFormatException e) {
                    out.println("Invalid option");
                }
            }
        }
    }

    private void getNovelByTitle() {
        out.println("Insert the name of the novel that you want to search: ");
        var bTitle = scanner.nextLine().replace(" ", "%20");
        var json = consumAPI.gettingData(URL_BASE+URL_SEARCH+ bTitle);
        var bdata= convert.gettingData(json, Information.class);

        Optional <NovelData> novelSearch = bdata.novels().stream()
                .findFirst();
        if (novelSearch.isPresent()) {
            out.println(
                    "\n----- NOVEL -----\n" +
                            "Title: " + novelSearch.get().name() +
                            "\n Writer: " + novelSearch.get().writers().stream().map(a -> a.fullName()).limit(1).collect(Collectors.joining()) +
                            "\n Tongue: " + novelSearch.get().tongues().stream().collect(Collectors.joining()) +
                            "\n Number of downloads: " + novelSearch.get().downloads() +"\n"+
                            "\n-----------------");
            try {
                List<Novel> novellook;
                novellook = novelSearch.stream().map(a -> new Novel(a)).collect(Collectors.toList());
                Writer writerApi = novelSearch.stream()
                        .flatMap(b -> b.writers().stream()
                                .map(a -> new Writer(a)))
                        .collect(Collectors.toList()).stream().findFirst().get();

                Optional<Writer> writerBd = repository.findWriterByNameContaining(novelSearch.get().writers().stream()
                        .map(a -> a.fullName())
                        .collect(Collectors.joining()));

                Optional<Novel> optionalNovel = repository.getNovelContainsEqualsIgnoreCaseTitle(bTitle);


                if (optionalNovel.isPresent()) {
                    out.println("Novel already save on the Data Base");
                } else {
                    Writer writer;
                    if (writerBd.isPresent()) {
                        writer = writerBd.get();
                        out.println("Writer already exist on the Data Base");
                    } else {
                        writer = writerApi;
                        repository.save(writer);
                    }
                    writer.setNovels(novellook);
                    repository.save(writer);
                }

            } catch (Exception e) {
                out.println("Warning! " + e.getMessage());
            }
        }else{
            out.println("Novel not found");
        }

    }

    public void searchWriterByName(){
        if (repository == null) {
            out.println("Repository is not initialized!");
            return;
        }
        out.println("Please enter the name of the writer: ");
        var name =scanner.nextLine();
        Optional<Writer> writer =repository.findWriterByNameContaining(name);


        if (writer.isPresent()){
            out.println(
                    "\n----- WRITER -----" +
                            "\nWriter: " + writer.get().getFullName() +
                            "\nDate of birth: " + writer.get().getBirthYear() +
                            "\nDate of decease: " + writer.get().getDeathYear() +
                            "\nNovels: " + writer.get().getNovels().stream()
                            .map(l -> l.getName()).collect(Collectors.toList()) + "\n"+
                            "\n--------------------\n" );
        }else {
            out.println("This writer isn´t registered on the Data Base");
        }
    }

    public void showRegisteredNovels(){
        List<Novel> novels = repository.findNovelsByWriter();
        novels.forEach(l -> out.println(
                "------ NOVEL ------" +
                        "\nTitle: " + l.getName() +
                        "\nWriter: " + l.getWriter().getFullName() +
                        "\nTongue: " + l.getTongue().getDialect() +
                        "\nNumber of downloads: " + l.getDownloads() +
                        "\n------------------"));
    }
    public void showRegistredWriters() {
        List<Writer> writers = repository.findAll();
        writers.forEach(l -> out.println(
                "\n----- WRITER -----" +
                        "\nWriter: " + l.getFullName() +
                        "\nDate of birth: " + l.getBirthYear() +
                        "\nDete of decease: " + l.getDeathYear() +
                        "\n Novels: " + l.getNovels().stream()
                        .map(t -> t.getName()).collect(Collectors.toList()) + "\n" +
                        "\n------------------------\n"
        ));
    }
    public void revealLiveWriters(){
        out.println("Type the year that you want to search the live writer: ");
        try {
            var date =Integer.valueOf(scanner.nextLine());
            List<Writer>writers = repository.getWriterbyDateOfDecease(date);
            if(!writers.isEmpty()){
                writers.forEach(l -> out.println(
                        "\n----- WRITER -----" +
                                "\nWriter: " + l.getFullName() +
                                "\nDate of birth: " + l.getBirthYear() +
                                "\nDete of decease: " + l.getDeathYear() +
                                "\n Novels: " + l.getNovels().stream()
                                .map(t -> t.getName()).collect(Collectors.toList()) + "\n" +
                                "\n------------------------\n"
                ));
            }else {
                out.println("Sorry! We couldn´t find any writer on this date"+date);
            }
        } catch (NumberFormatException e) {
            out.println("Please enter only valid date remember use only numbers  ex:1756 "+ e.getMessage());
        }
    }
    public void findNovelsByTongue(){
        var mapTongues = """
              Select the tongues to search novels
              
              en - English
              es - Spanish
              fr - French
              it - Italian
              pt - Portuguese
              
              """;
        out.println(mapTongues);
        var lang = scanner.nextLine().toLowerCase();
        if (lang.equalsIgnoreCase("es")
                || lang.equalsIgnoreCase("en")
                || lang.equalsIgnoreCase("it")
                || lang.equalsIgnoreCase("fr")
                || lang.equalsIgnoreCase("pt")) {
            Tongue tongue= Tongue.fromString(lang);
            List<Novel>novels = repository.findNovelByTongue(tongue);
            if (novels.isEmpty()){
                out.println("Sorry! we don't have any novel registered with this tongue");
            }else {
                novels.forEach(t-> out.println(
                        "------ NOVEL ------" +
                                "\nTitle: " + t.getName() +
                                "\nWriter: " + t.getWriter().getFullName() +
                                "\nTongue: " + t.getTongue().getDialect() +
                                "\nNumber of downloads: " + t.getDownloads() +
                                "\n------------------"));
            }
        }else{
            out.println("Please Indicate a valid format for a tongue");
        }
    }

    public void statistics(){
        var json =consumAPI.gettingData(URL_BASE);
        var info = convert.gettingData(json, Information.class);
        IntSummaryStatistics est =info.novels().stream()
                .filter(e -> e.downloads() > 0)
                .collect(Collectors.summarizingInt(NovelData::downloads));
        Integer average =(int)est.getAverage();
        out.println(
                "\n---------STATISTICS---------"+
                        "\n Media downloads quantity: " + est.getAverage() +
                        "\n Maximum downloads count: " + est.getMax() +
                        "\nMinimum downloads count: " + est.getMin() +
                        "\nRecords evaluated for calculation: " + est.getCount()

        );
    }

    public void top10Downloads(){
        var json =consumAPI.gettingData(URL_BASE);
        var info = convert.gettingData(json, Information.class);
        info.novels().stream()
                .sorted(Comparator.comparing(NovelData::downloads).reversed())
                .limit(10)
                .map(l -> l.name().toUpperCase())
                .forEach(out::println);
    }

}